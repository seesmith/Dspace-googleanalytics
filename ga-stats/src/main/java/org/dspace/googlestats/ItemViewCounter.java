package org.dspace.googlestats;

import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.data.analytics.DataEntry;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.data.analytics.Metric;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.apache.log4j.Logger;
import org.dspace.core.ConfigurationManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


public class ItemViewCounter
{

    /**
     * log4j category
     */
    private static final Logger log = Logger.getLogger(ItemViewCounter.class);

    /**
     * Hit counter
     */
    private static Properties counts;

    /**
     * When the counter last loaded?
     */
    private static Date lastloaded;

    /**
     * The filename of the counter file
     */
    private static String filename;

    /**
     * Initalise the system
     */
    private static void init()
    {
// Load the properties file
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        lastloaded = yesterday.getTime();
        filename = ConfigurationManager.getProperty("dspace.dir") +
                System.getProperty("file.separator") +
                ConfigurationManager.getProperty("google-analytics","filename");
        counts = new Properties();
        loadCounter();
    }

    /**
     * Get the count for a particular page (e.g. /handle/123/456)
     *
     * @param page The page path
     * @return The count. Empty String if unknown
     */
    public static String getPageCount(String page)
    {
// Check we're initialised
        if (lastloaded == null)
        {
            init();
        }

// Reload the hits
        loadCounter();

// Get the value
        if (page == null)
        {
            page = "";
        }
        String count = counts.getProperty(page);

// Return the value
        if (count != null)
        {
            return count;
        }
        return "";
    }

    /**
     * (Re)load the counter. It is reloaded every hour.
     */
    private static void loadCounter()
    {
// Do we need to load it?
        Calendar hourago = Calendar.getInstance();
        hourago.add(Calendar.HOUR, -1);
        if (lastloaded.before(hourago.getTime()))
        {
            try
            {
                counts.load(new FileInputStream(new File(filename)));
                lastloaded = Calendar.getInstance().getTime();
            }
            catch (Exception e)
            {
                log.warn("Unable to load google hit counter from " + filename);
            }
        }
    }

    public static void main(String[] args)
    {
        //todo needs to be able to see the dspace.cfg file
        populateCounter();
    }

    /**
     * Command line method to collect the statistics from Google Analytics.
     */
    private static void populateCounter()
    {
        try
        {
            String siteid = ConfigurationManager.getProperty("google-analytics","siteid");
            String handle = ConfigurationManager.getProperty("google-analytics","handleprefix");
            String filename = ConfigurationManager.getProperty("dspace.dir") +
                    System.getProperty("file.separator") +
                    ConfigurationManager.getProperty("google-analytics","filename");

            AnalyticsService as = GoogleAnalyticsConnector.getConnection();


// The results
            Properties counts = new Properties();

// Keep requesting pages of results from Google until a blank page is found
// pages of 1,000 results at a time
            URL queryUrl;
            int i = 1;
            boolean found = true;
            int total = 0;
            String baseUrl = "https://www.google.com/analytics/feeds/";
            // Get stats up until yesterday
            Calendar today = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String enddate = format.format(today.getTime());
            today.roll(Calendar.YEAR, -1);
            String startdate = format.format(today.getTime());

            while (found)
            {
                found = false;
                try
                {
                    String q = baseUrl +
                            "data?start-index=" + i +
                            "&ids=" + siteid +
                            "&start-date=" + startdate +
                            "&end-date=" + enddate +
                            "&metrics=ga:pageviews" +
                            "&dimensions=ga:pagePath" +
                            "&sort=-ga:pageViews" +
                            "&filters=ga:pagePath%3D~/handle/" + handle;
                    queryUrl = new URL(q);
                }
                catch (MalformedURLException e)
                {
                    System.err.println("Malformed URL: " + baseUrl);
                    return;
                }

// Send our request to the Analytics API and wait for the results to come back
                DataFeed dataFeed;
                try
                {
                    dataFeed = as.getFeed(queryUrl, DataFeed.class);
                }
                catch (IOException e)
                {
                    System.err.println("Network error trying to retrieve feed: " + e.getMessage());
                    return;
                }
                catch (ServiceException e)
                {
                    System.err.println("Analytics API responded with an error message: " + e.getMessage());
                    return;
                }

                for (DataEntry entry : dataFeed.getEntries())
                {
                    String id = entry.stringValueOf("ga:pagePath");
                    for (Metric metric : entry.getMetrics())
                    {    //
                        counts.put(id, metric.getValue());
                        System.out.println(metric.getValue() + " = id '" + id + "'");
                        total = total + Integer.parseInt(metric.getValue());
                        found = true;
                    }
                }
                i = i + 1000;
            }

// Save the properties file
            counts.put("total", "" + total);

            counts.store(new FileOutputStream(filename), null);
            System.out.println("Saved " + total + " total hits in " + filename);
        }
        catch (AuthenticationException e)
        {
            System.err.println("Error authenticating " + e);

        }
        catch (IOException e)
        {
            System.err.println("Error saving results to file: " + filename);

        }
        catch (Exception e)
        {
            System.err.println("Error: " + e);

        }
    }


}