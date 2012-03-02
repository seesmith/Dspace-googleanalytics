package org.dspace.app.webui.servlet;

import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.util.common.xml.XmlWriter;
import org.apache.log4j.Logger;
import org.apache.xml.serializer.Method;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DSpaceObject;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.dspace.googlestats.GoogleAnalyticsConnector;
import org.dspace.googlestats.GoogleQueryManager;
import org.dspace.handle.HandleManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * University of Edinburgh
 *
 * @author cknowles
 * @date 12-Apr-2011
 */
public class GoogleAnalyticsItemQueryServlet extends DSpaceServlet
{

    /**
     * log4j logger
     */
    private static Logger log = Logger.getLogger(GoogleAnalyticsItemQueryServlet.class);


    protected void doDSGet(Context context, HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
       // is the statistics data publically viewable?
        boolean privatereport = ConfigurationManager.getBooleanProperty("googleanalytics.item.authorization.admin");
        // is the user a member of the Administrator (1) group?
        boolean admin = Group.isMember(context, 1);
        if (!privatereport || admin)
        {
            process(context, request, response);
        }
        else
        {
            log.info("Admin report");
            throw new AuthorizeException("Unauthorised as not an administrator");
        }

    }

    protected void doDSPost(Context context, HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        // is the statistics data publically viewable?
        boolean privatereport = ConfigurationManager.getBooleanProperty("googleanalytics.item.authorization.admin");
        // is the user a member of the Administrator (1) group?
        boolean admin = Group.isMember(context, 1);
        if (!privatereport || admin)
        {
            process(context, request, response);
        }
        else
        {
           log.info("Admin report");
           throw new AuthorizeException("Unauthorised as not an administrator");
        }

    }

    protected void process(Context context, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, AuthorizeException
    {
        String headMetadata = "";

        try
        {
            //todo get dates from request and if empty revert to calendar
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String action = request.getParameter("action");
            String handle = request.getParameter("handle");
            DSpaceObject dso = HandleManager.resolveToObject(context, handle);
            log.debug("GoogleAnalyticsItemQueryServlet :" + startDate + "," + endDate + "," + action + "," + handle + ".");

            if (startDate.length() == 0 || endDate.length() == 0)
            {

                Calendar gregCal = new GregorianCalendar();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                endDate = sdf.format(gregCal.getTime());
                gregCal.roll(Calendar.YEAR, -1);
                startDate = sdf.format(gregCal.getTime());

            }

            String gaStart = ConfigurationManager.getProperty("googleanalytics.startdate");
            request.setAttribute("gaStart", gaStart);

            request.setAttribute("dspace.layout.head", headMetadata);
            GoogleQueryManager query = new GoogleQueryManager();


            response.setContentType("text/xml; charset=\"utf-8\"");
            Properties props = OutputPropertiesFactory.getDefaultMethodProperties(Method.XML);
            Serializer ser = SerializerFactory.getSerializer(props);
            PrintWriter writer = response.getWriter();
            ser.setWriter(writer);
            try
            {
                DataFeed feed;
                AnalyticsService as = GoogleAnalyticsConnector.getConnection();
                if (action.equals("YearMonth"))
                {

                    feed = query.queryViewDSpaceMonth(dso, startDate, endDate, as);
                    XmlWriter xmlW = new XmlWriter(writer);
                    writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                    feed.generateAtom(xmlW, as.getExtensionProfile());
                }
                else if (action.equals("BitstreamYearMonth"))
                {
                    feed = query.queryBitstreamViewMonth(dso, startDate, endDate);
                    XmlWriter xmlW = new XmlWriter(writer);
                    writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                    feed.generateAtom(xmlW, as.getExtensionProfile());
                }
                else
                {
                    writer.append("Statistics cannot be return for this selection");
                }

                writer.flush();
            }
            catch (Exception e)
            {
                log.error("Exception error : " + e);
                throw new Exception(e.toString());

            }
            finally
            {
                ser.reset();
            }
        }
        catch (Exception e)
        {
            log.error("error : " + e);
            throw new ServletException(e);
        }

    }
}