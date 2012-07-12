package org.dspace.googlestats;

import com.google.gdata.client.GoogleAuthTokenFactory;
import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.data.analytics.AccountFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;


/**
 * University of Edinburgh
 *
 * @author Claire Knowles
 */
public class GoogleAnalyticsConnector
{

    private static final Logger log = Logger.getLogger(GoogleAnalyticsConnector.class);


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
    protected static AccountFeed getAvailableAccounts(AnalyticsService myService) throws IOException, ServiceException
    {
        URL feedUrl = new URL(GoogleAnalyticsAccount.getInstance().getDataUrl());
        return myService.getFeed(feedUrl, AccountFeed.class);

    }


    public static AnalyticsService getConnection() throws AuthenticationException
    {
        // Set up the variables

        AnalyticsService as = new AnalyticsService("SDLC_v2.0");

// Login to Google
        try
        {
            as.setUserCredentials(GoogleAnalyticsAccount.getInstance().getUsername(), GoogleAnalyticsAccount.getInstance().getPassword());
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