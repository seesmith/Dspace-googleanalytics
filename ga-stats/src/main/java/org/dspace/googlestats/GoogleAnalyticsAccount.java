package org.dspace.googlestats;

import org.dspace.core.ConfigurationManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * University of Edinburgh
 *
 * @author cknowles
 *         12-Apr-2011
 */
public class GoogleAnalyticsAccount
{

    private static String siteUrl;
    private static String siteId;
    private static String startDate;
    private static String endDate;
    private static String handlePrefix;
    private static String filename;
    private static String dataUrl;
    private static String gaHandle;
    private static String username;
    private static String password;

    private static final GoogleAnalyticsAccount INSTANCE = new GoogleAnalyticsAccount();

    public String getSiteUrl()
    {
        return siteUrl;
    }

    public String getGaHandle()
    {
        return gaHandle;
    }

    public String getDataUrl()
    {
        return dataUrl;
    }

    public String getFilename()
    {
        return filename;
    }

    public String getHandlePrefix()
    {
        return handlePrefix;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public String getSiteId()
    {
        return siteId;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUsername()
    {
        return username;
    }

    //toDo as a singleton
    private GoogleAnalyticsAccount()
    {

        siteId = ConfigurationManager.getProperty("google-analytics","siteid");
        gaHandle = ConfigurationManager.getProperty("google-analytics","handleprefix");
        filename = ConfigurationManager.getProperty("dspace.dir") +
                System.getProperty("file.separator") +
                ConfigurationManager.getProperty("google-analytics","filename");

        startDate = ConfigurationManager.getProperty("google-analytics","startdate");
        String url = ConfigurationManager.getProperty("google-analytics","tracker.uri");
        dataUrl = ConfigurationManager.getProperty("google-analytics","dataurl");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        endDate = dateFormat.format(GregorianCalendar.getInstance().getTime());
        username = ConfigurationManager.getProperty("google-analytics","username");
        password = ConfigurationManager.getProperty("google-analytics","password");

        try
        {
            URL localURL = new URL(url);
            url = localURL.getPath();
            if (url.endsWith("/"))
            {
                url = url.substring(0, url.length() - 1);
            }
        }
        catch (MalformedURLException e)
        {
            System.err.println("Invalid tracker.uri (" + url + ")");
            //throw e;
        }
        siteUrl = url;
    }

    public static GoogleAnalyticsAccount getInstance()
    {
        return INSTANCE;
    }
}
