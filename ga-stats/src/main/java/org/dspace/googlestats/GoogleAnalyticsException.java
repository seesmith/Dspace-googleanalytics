package org.dspace.googlestats;

/**
 * University of Edinburgh
 * @author cknowles
 * 12-Apr-2011
 */
public class GoogleAnalyticsException extends Exception
{

    public GoogleAnalyticsException(String message)
        {
            super(message);
        }

        public GoogleAnalyticsException(Throwable cause)
        {
            super(cause);
        }

        public GoogleAnalyticsException(String message, Throwable cause)
        {
            super(message, cause);
        }


}
