package org.dspace.googlestats;

import com.google.gdata.client.GoogleAuthTokenFactory;
import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.data.analytics.AccountFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.apache.log4j.Logger;
import org.dspace.core.ConfigurationManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * University of Edinburgh
 *
 * @author Claire Knowles
 */
public class GoogleAnalyticsConnector
{

    private static final Logger log = Logger.getLogger(GoogleAnalyticsConnector.class);

    public static final String ACCOUNTS_URL = "https://www.google.com/analytics/feeds/data";


    /**
     * Returns a data feed containing the accounts that the user logged in to the
     * given AnalyticsService has access to.
     *
     * @param myService The AnalyticsService to request accounts from
     * @return An AccountFeed containing an entry for each profile the logged-in
     *         user has access to
     * @throws java.io.IOException If an error occurs while trying to communicate with
     *                             the Analytics server
     * @throws ServiceException    If the API cannot fulfill the user request for
     *                             any reason
     */



    /**
     * Get google analytics config and wrap it up for queries
     *
     * @return account information populated from config
     * @throws MalformedURLException if URL is incorrect
     */
    public static GoogleAnalyticsAccount getAccountDetails() throws MalformedURLException
    {

        String siteid = ConfigurationManager.getProperty("google-analytics", "siteid");
        String handle = ConfigurationManager.getProperty("google-analytics", "handleprefix");
        String filename = ConfigurationManager.getProperty("dspace.dir") +
                System.getProperty("file.separator") +
                ConfigurationManager.getProperty("google-analytics", "filename");

        String startDate = ConfigurationManager.getProperty("google-analytics", "startdate");
        String url = ConfigurationManager.getProperty("google-analytics", "tracker.uri");
        String dataurl = ConfigurationManager.getProperty("google-analytics", "dataurl");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String endDate = dateFormat.format(GregorianCalendar.getInstance().getTime());

        GoogleAnalyticsAccount gaa = new GoogleAnalyticsAccount(url, siteid, startDate, endDate, handle, filename, dataurl);
        log.info(gaa.toString());
        return gaa;


    }

    public static AnalyticsService getConnection()
            throws AuthenticationException
    {
        // Set up the variables
        String username = ConfigurationManager.getProperty("google-analytics", "username");
        String password = ConfigurationManager.getProperty("google-analytics", "password");
        AnalyticsService as = new AnalyticsService("UniEd_ERA_v1.0");

// Login to Google
        try
        {
            log.info("username " + username + " password " + password);
            as.setUserCredentials(username, password);
            GoogleAuthTokenFactory.UserToken auth_token = (GoogleAuthTokenFactory.UserToken) as.getAuthTokenFactory().getAuthToken();
            //todo use token instead of authenticating for every query
            String token = auth_token.getValue(); // token is '12345abcde'
            log.info("token" + token);
            return as;
        }
        catch (AuthenticationException e)
        {
            log.error("Authentication failed logging into GA: " + e.getMessage());
            //e.printStackTrace();
            throw e;
        }
    }
}