package org.dspace.app.webui.servlet;

import org.apache.log4j.Logger;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Collection;
import org.dspace.content.DSpaceObject;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.dspace.googlestats.GoogleAnalyticsCollectionStats;
import org.dspace.handle.HandleManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * University of Edinburgh
 *
 * @author cknowles
 *         05-Oct-2011
 */
public class GoogleAnalyticsCollectionServlet extends DSpaceServlet
{

    /**
     * log4j logger
     */
    private static Logger log = Logger.getLogger(GoogleAnalyticsCollectionServlet.class);


    protected void doDSGet(Context context, HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        // is the statistics data publically viewable?
        boolean privatereport = ConfigurationManager.getBooleanProperty("googleanalytics.collection.authorization.admin");
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


            request.setAttribute("dso", dso);
            request.setAttribute("dspace.layout.head", headMetadata);
            Collection collection = (Collection) dso;
            GoogleAnalyticsCollectionStats query = new GoogleAnalyticsCollectionStats(collection);
            request.setAttribute("totalViews", query.getTotalViews());
            request.setAttribute("totalDownloads", query.getTotalDownloads());
            List topviews = query.getTopViews();
            List topdownloads = query.getTopDownloads();
            if (!topviews.isEmpty() && topviews.size() > 10)
            {
                request.setAttribute("itemList", topviews.subList(0, 10));
            }
            else
            {
                request.setAttribute("itemList", topviews);
            }

            if (!topdownloads.isEmpty() && topdownloads.size() > 10)
            {
                request.setAttribute("downloadsList", topdownloads.subList(0, 10));
            }
            else
            {
                request.setAttribute("downloadsList", topdownloads);
            }

            String gaStart = ConfigurationManager.getProperty("googleanalytics.startdate");
            request.setAttribute("gaStart", gaStart);

            JSPManager.showJSP(request, response, "/gastatistics/display-collection-stats.jsp");

        }
        catch (Exception e)
        {
            log.error("displayStatistics error : " + e);
            //e.printStackTrace();
            throw new ServletException(e);
        }

    }
}