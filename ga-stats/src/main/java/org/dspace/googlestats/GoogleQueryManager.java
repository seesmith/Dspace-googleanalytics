package org.dspace.googlestats;

import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.data.analytics.DataEntry;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.data.analytics.Dimension;
import com.google.gdata.util.ServiceException;
import org.apache.log4j.Logger;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class GoogleQueryManager
{

    /**
     * log4j category
     * <p/>
     * 2011-09-20 15:31:22,084 ERROR org.dspace.googlestats.GoogleQueryManager @ Analytics API responded with a service error message:  Error connecting with login URI
     */
    private static Logger log = Logger.getLogger(GoogleQueryManager.class);
    private DataFeed feed;

    public DataFeed queryViewDSpaceMonth(String startDate, String endDate)
    {
        URL queryUrl;
        String baseUrl = GoogleAnalyticsAccount.getInstance().getDataUrl();

        //check start date is not before startDate in gaa 
        try
        {
            String q = baseUrl +
                    "?start-date=" + startDate +
                    "&end-date=" + endDate +
                    "&dimensions=ga:year,ga:month" +
                    "&metrics=ga:pageviews" +
                    "&sort=ga:year,ga:month" +
                    "&max-results=60" +
                    //"&filters=ga:pagePath%3D~/handle/" +
                    //check that handle won't return where item id is like handle + n
                    "&ids=" + GoogleAnalyticsAccount.getInstance().getSiteId();

            queryUrl = new URL(q);
            log.info("queryViewDSpaceMonth query " + q);
            AnalyticsService as = GoogleAnalyticsConnector.getConnection();
            feed = as.getFeed(queryUrl, DataFeed.class);
        }
        catch (MalformedURLException e)
        {
            log.error("Malformed URL: " + baseUrl);
        }
        catch (IOException e)
        {
            log.error("Network error trying to retrieve feed: " + e.getMessage());
        }
        catch (ServiceException e)
        {
            log.error("Analytics API responded with a service error message: " + e.getMessage());
        }
        catch (Exception e)
        {
            log.error("Analytics API responded with an error message: " + e.getMessage());
        }

        return feed;
    }

    public DataFeed queryBitstreamViewMonth(String startDate, String endDate)
    {
        URL queryUrl;
        String baseUrl = GoogleAnalyticsAccount.getInstance().getDataUrl();

        //check start date is not before startDate in gaa
        try
        {
            String q = baseUrl +
                    "?start-date=" + startDate +
                    "&end-date=" + endDate +
                    "&dimensions=ga:year,ga:month" +
                    "&metrics=ga:pageviews" +
                    "&sort=ga:year,ga:month" +
                    "&max-results=60" +
                    "&filters=ga:pagePath%3D~/bitstream/" +
                    "&ids=" + GoogleAnalyticsAccount.getInstance().getSiteId();

            queryUrl = new URL(q);
            log.info("query " + q);
            AnalyticsService as = GoogleAnalyticsConnector.getConnection();
            feed = as.getFeed(queryUrl, DataFeed.class);

        }
        catch (MalformedURLException e)
        {
            log.error("Malformed URL: " + baseUrl);
        }
        catch (IOException e)
        {
            log.error("Network error trying to retrieve feed: " + e.getMessage());
        }
        catch (ServiceException e)
        {
            log.error("Analytics API responded with a service error message: " + e.getMessage());
        }
        catch (Exception e)
        {
            log.error("Analytics API responded with an error message: " + e.getMessage());
        }

        return feed;
    }

    public DataFeed queryViewDSpaceMonth(DSpaceObject dso, String startDate, String endDate, AnalyticsService as)
    {
        URL queryUrl;
        String baseUrl = GoogleAnalyticsAccount.getInstance().getDataUrl();

        //check start date is not before startDate in gaa
        try
        {
            String q = baseUrl +
                    "?start-date=" + startDate +
                    "&end-date=" + endDate +
                    "&dimensions=ga:year,ga:month" +
                    "&metrics=ga:pageviews" +
                    "&sort=ga:year,ga:month" +
                    "&max-results=60" +
                    "&ids=" + GoogleAnalyticsAccount.getInstance().getSiteId() +
                    "&filters=ga:pagePath%3D~/handle/" + dso.getHandle() + "$";
            //check that handle won't return where item id is like handle + n

            queryUrl = new URL(q);
            log.info("queryViewDSpaceMonth query " + q);
            as = GoogleAnalyticsConnector.getConnection();
            feed = as.getFeed(queryUrl, DataFeed.class);
        }
        catch (MalformedURLException e)
        {
            log.error("Malformed URL: " + baseUrl);
        }
        catch (IOException e)
        {
            log.error("Network error trying to retrieve feed: " + e.getMessage());
        }
        catch (ServiceException e)
        {
            log.error("Analytics API responded with a service error message: " + e.getMessage());
        }
        catch (Exception e)
        {
            log.error("Analytics API responded with an error message: " + e.getMessage());
        }

        return feed;
    }

    public DataFeed queryBitstreamViewMonth(DSpaceObject dso, String startDate, String endDate)
    {
        URL queryUrl;
        String baseUrl = GoogleAnalyticsAccount.getInstance().getDataUrl();
        log.info("base url " + baseUrl);

        //check start date is not before startDate in gaa
        try
        {
            String q = baseUrl +
                    "?start-date=" + startDate +
                    "&end-date=" + endDate +
                    "&dimensions=ga:year,ga:month" +
                    "&metrics=ga:pageviews" +
                    "&sort=ga:year,ga:month" +
                    "&max-results=60" +
                    "&filters=ga:pagePath%3D~/bitstream/" + dso.getHandle() +
                    "&ids=" + GoogleAnalyticsAccount.getInstance().getSiteId();

            queryUrl = new URL(q);
            log.info("query " + q);
            AnalyticsService as = GoogleAnalyticsConnector.getConnection();
            feed = as.getFeed(queryUrl, DataFeed.class);
        }
        catch (MalformedURLException e)
        {
            log.error("Malformed URL: " + baseUrl);
        }
        catch (IOException e)
        {
            log.error("Network error trying to retrieve feed: " + e.getMessage());
        }
        catch (ServiceException e)
        {
            log.error("Analytics API responded with a service error message: " + e.getMessage());
        }
        catch (Exception e)
        {
            log.error("Analytics API responded with an error message: " + e.getMessage());
        }

        return feed;
    }

    public DataFeed queryTopPages(String startDate, String endDate)
    {
        URL queryUrl;
        String baseUrl = GoogleAnalyticsAccount.getInstance().getDataUrl();

        //check start date is not before startDate in gaa
        try
        {
            String q = baseUrl +
                    "?start-date=" + startDate +
                    "&end-date=" + endDate +
                    "&dimensions=ga:pageTitle,ga:pagePath" +
                    "&metrics=ga:pageviews" +
                    "&sort=-ga:pageviews" +
                    "&max-results=10" +
                    "&ids=" + GoogleAnalyticsAccount.getInstance().getSiteId();

            queryUrl = new URL(q);
            log.info("query " + q);

            AnalyticsService as = GoogleAnalyticsConnector.getConnection();
            feed = as.getFeed(queryUrl, DataFeed.class);
        }
        catch (MalformedURLException e)
        {
            log.error("Malformed URL: " + baseUrl);
        }
        catch (IOException e)
        {
            log.error("Network error trying to retrieve feed: " + e.getMessage());
        }
        catch (ServiceException e)
        {
            log.error("Analytics API responded with a service error message: " + e.getMessage());
        }
        catch (Exception e)
        {
            log.error("Analytics API responded with an error message: " + e.getMessage());
        }

        return feed;
    }

    

    public DataFeed queryTopItems(String startDate, String endDate, Context context)
    {
        //todo there must be a better way to get just the items
        URL queryUrl;
        String baseUrl = GoogleAnalyticsAccount.getInstance().getDataUrl();;
        try
        {

            String handlePrefix = ConfigurationManager.getProperty("handle.prefix");
            log.info("handlePrefix " + handlePrefix);
            Collection[] colls = Collection.findAll(context);
            Community[] comms = Community.findAll(context);
            LinkedList<String> collHandles = new LinkedList<String>();
            LinkedList<String> commHandles = new LinkedList<String>();
            //n.b. maximum of 128 chars per reg ex in google analytics filters
            for (Collection coll : colls)
            {
                String handle = coll.getHandle();
                log.info("add to colls " + coll.getHandle());
                collHandles.add(handle);
            }

            for (Community comm : comms)
            {
                String handle = comm.getHandle();
                commHandles.add(handle);

            }

            String q = baseUrl +
                    "?start-date=" + startDate +
                    "&end-date=" + endDate +
                    "&dimensions=ga:pageTitle,ga:pagePath" +
                    "&metrics=ga:pageviews" +
                    "&sort=-ga:pageviews" +
                    "&max-results=200" +
                    "&filters=ga:pagePath%3D~/handle/[0-9]*/[0-9]*$" +
                    "&ids=" + GoogleAnalyticsAccount.getInstance().getSiteId();

            queryUrl = new URL(q);
            log.info("query TOP ITEMS =" + q);
            AnalyticsService as = GoogleAnalyticsConnector.getConnection();
            feed = as.getFeed(queryUrl, DataFeed.class);

            //split results into community/collection/item
            List<DataEntry> entries = feed.getEntries();
            for (DataEntry entry : entries)
            {
                // Put all the dimension and metric names into an array.
                Dimension dimension = entry.getDimension("ga:pagePath");
                String handle = dimension.getValue();
                handle = handle.substring(handle.indexOf("/handle/") + 8, handle.length());
                if (collHandles.contains(handle))
                {
                    //collection handle
                    Dimension di = new Dimension("resourceType", "Collection");
                    entry.addDimension(di);
                }
                else if (commHandles.contains(handle))
                {
                    //community handle
                    Dimension di = new Dimension("resourceType", "Community");
                    entry.addDimension(di);
                }
                else
                {
                    //item handle
                    Dimension di = new Dimension("resourceType", "Item");
                    entry.addDimension(di);
                }
            }
        }
        catch (MalformedURLException e)
        {
            log.error("Malformed URL: " + baseUrl);
        }
        catch (IOException e)
        {
            log.error("Network error trying to retrieve feed: " + e.getMessage());
        }
        catch (ServiceException e)
        {
            log.error("Analytics API responded with a service error message: " + e.getMessage());
        }
        catch (SQLException e)
        {
            log.error("SQL error message: " + e.getMessage());
        }
        catch (Exception e)
        {
            log.error("Top Items error message: " + e.getMessage());
            e.printStackTrace();
        }

        return feed;
    }

    public DataFeed queryTopDownloads(String startDate, String endDate)
    {
        URL queryUrl;
        String baseUrl = GoogleAnalyticsAccount.getInstance().getDataUrl();

        //check start date is not before startDate in gaa
        try
        {
            String q = baseUrl +
                    "?start-date=" + startDate +
                    "&end-date=" + endDate +
                    "&dimensions=ga:pageTitle,ga:pagePath" +
                    "&metrics=ga:pageviews" +
                    "&sort=-ga:pageviews" +
                    "&max-results=10" +
                    "&filters=ga:pagePath%3D~/bitstream/[0-9]*/[0-9]*" +
                    "&ids=" + GoogleAnalyticsAccount.getInstance().getSiteId();

            queryUrl = new URL(q);
            log.info("query " + q);
            AnalyticsService as = GoogleAnalyticsConnector.getConnection();
            feed = as.getFeed(queryUrl, DataFeed.class);
        }
        catch (MalformedURLException e)
        {
            log.error("Malformed URL: " + baseUrl);
        }
        catch (IOException e)
        {
            log.error("Network error trying to retrieve feed: " + e.getMessage());
        }
        catch (ServiceException e)
        {
            log.error("Analytics API responded with a service error message: " + e.getMessage());
        }
        catch (Exception e)
        {
            log.error("Top Downloads error message: " + e.getMessage());
        }
        return feed;
    }

    public DataFeed queryTopCountries(String startDate, String endDate)
    {
        URL queryUrl;
        String baseUrl = GoogleAnalyticsAccount.getInstance().getDataUrl();
        try
        {
            //check start date is not before startDate in gaa
            String q = baseUrl +
                    "?start-date=" + startDate +
                    "&end-date=" + endDate +
                    "&dimensions=ga:country" +
                    "&metrics=ga:visits" +
                    "&sort=-ga:visits" +
                    "&max-results=100" +
                    "&ids=" + GoogleAnalyticsAccount.getInstance().getSiteId();

            queryUrl = new URL(q);
            log.info("query TOP countries " + q);
            AnalyticsService as = GoogleAnalyticsConnector.getConnection();
            feed = as.getFeed(queryUrl, DataFeed.class);
        }
        catch (MalformedURLException e)
        {
            log.error("Malformed URL: " + baseUrl);
        }
        catch (IOException e)
        {
            log.error("Network error trying to retrieve feed: " + e.getMessage());
        }
        catch (ServiceException e)
        {
            log.error("Analytics API responded with a service error message: " + e.getMessage());
        }
        catch (Exception e)
        {
            log.error("Analytics API responded with an error message: " + e.getMessage());
        }

        return feed;
    }


}