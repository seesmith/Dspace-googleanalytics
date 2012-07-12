package org.dspace.googlestats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * University of Edinburgh
 *
 * @author cknowles
 * @date 21/03/12
 */
public class GoogleAnalyticsUtils
{

    public static List testDate(String startDate, String endDate, String gaStartDate)
    {
        List<String> dateErrors = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            Date end = sdf.parse(endDate);
            Date gaStart = sdf.parse(gaStartDate);

            if (end.before(gaStart))
            {
                dateErrors.add("End date is prior to statistics start date.");
            }
        }
        catch (ParseException e)
        {
            dateErrors.add("Google Analytics start date is not valid");
        }

        if (startDate.length() == 0 || endDate.length() == 0)
        {
            Calendar gregCal = new GregorianCalendar();

            endDate = sdf.format(gregCal.getTime());
            gregCal.roll(Calendar.YEAR, -1);
            startDate = sdf.format(gregCal.getTime());
        }
        else
        {
            try
            {
                Date testDate = sdf.parse(startDate);
                if (!sdf.format(testDate).equals(startDate))
                {
                    dateErrors.add("Start Date is not valid");
                }
            }
            catch (ParseException pe)
            {
                dateErrors.add("Start Date is not valid");
            }

            try
            {
                Date testDate = sdf.parse(endDate);
                if (!sdf.format(testDate).equals(endDate))
                {
                    dateErrors.add("End Date is not valid");
                }
            }
            catch (ParseException pe)
            {
                dateErrors.add("End Date is not valid");
            }
        }
        

        return dateErrors;
    }
}
