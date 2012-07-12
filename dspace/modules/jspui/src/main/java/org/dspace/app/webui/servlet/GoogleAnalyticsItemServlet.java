package org.dspace.app.webui.servlet;

import org.apache.log4j.Logger;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DSpaceObject;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.dspace.googlestats.GoogleAnalyticsAccount;
import org.dspace.handle.HandleManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * University of Edinburgh
 *
 * @author cknowles
 * @date 12-Apr-2011
 */
public class GoogleAnalyticsItemServlet extends DSpaceServlet
{

    /**
     * log4j logger
     */
    private static Logger log = Logger.getLogger(GoogleAnalyticsItemServlet.class);


    protected void doDSGet(Context context, HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        //get from google analytics account object
        boolean privatereport = ConfigurationManager.getBooleanProperty("googleanalytics.item.authorization.admin");
         // is the user a member of the Administrator (1) group?
        boolean admin = Group.isMember(context, 1);
        if (!privatereport || admin)
        {
            displayStatistics(context, request, response);
        }
        else
        {
            throw new AuthorizeException();
        }
    }

     protected void doDSPost(Context context, HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        // is the statistics data publically viewable?
        boolean privatereport = ConfigurationManager.getBooleanProperty("googleanalytics.authorization.admin");
         // is the user a member of the Administrator (1) group?
        boolean admin = Group.isMember(context, 1);
        if (!privatereport || admin)
        {
            displayStatistics(context, request, response);
        }
        else
        {
            throw new AuthorizeException();
        }
    }

    protected void displayStatistics(Context context, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, AuthorizeException
    {

        //add to headmetadata ?
        String headMetadata = "";

        String handle = request.getParameter("handle");
        DSpaceObject dso = HandleManager.resolveToObject(context, handle);
        try
        {
            //todo  get dates from the calendar
            Calendar gregCal = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String endDate = sdf.format(gregCal.getTime());
            gregCal.roll(Calendar.YEAR, -1);
            String startDate = sdf.format(gregCal.getTime());

            request.setAttribute("dso", dso);
            request.setAttribute("dspace.layout.head", headMetadata);
            request.setAttribute("siteId", GoogleAnalyticsAccount.getInstance().getSiteId());
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate",endDate);

            String gaStart = GoogleAnalyticsAccount.getInstance().getStartDate();
            request.setAttribute("gaStart", gaStart);

            JSPManager.showJSP(request, response, "/gastatistics/display-stats.jsp");

        }
        catch (Exception e)
        {
            log.error("displayStatistics error : " + e);
            e.printStackTrace();
            throw new ServletException(e);
        }

    }
}

