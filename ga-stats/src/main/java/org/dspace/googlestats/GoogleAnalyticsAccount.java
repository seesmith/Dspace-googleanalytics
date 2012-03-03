package org.dspace.googlestats;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * University of Edinburgh
 * @author cknowles
 * 12-Apr-2011
 */
public class GoogleAnalyticsAccount
{

    private String url;
    private String siteId;
    private String startDate;
    private String endDate;
    private String handlePrefix;
    private String filename;
    private String dataurl;


    public GoogleAnalyticsAccount(String url, String tableId, String startDate, String endDate, String handle, String filename, String dataurl) throws MalformedURLException
    {

        this.url = url;
        try
        {
            URL localURL = new URL(url);
            this.url = localURL.getPath();
            if (this.url.endsWith("/"))
            {
                this.url = this.url.substring(0, this.url.length() - 1);
            }
        }
        catch (MalformedURLException e)
        {
            System.err.println("Invalid dspace.url URL (" + url + ")");
            throw e;
        }

        this.siteId = tableId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.handlePrefix = handle;
        this.filename = filename;
        this.dataurl = dataurl;
    }

    public String getSiteId()
    {
        return siteId;
    }

    @Override
    public String toString()
    {
        return "GoogleAnalyticsAccount{" +
                "url='" + url + '\'' +
                ", siteId='" + siteId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", handlePrefix='" + handlePrefix + '\'' +
                ", filename='" + filename + '\'' +
                ", dataurl='" + dataurl + '\'' +
                '}';
    }
}
