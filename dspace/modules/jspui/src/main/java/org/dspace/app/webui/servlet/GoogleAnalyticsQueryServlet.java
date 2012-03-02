package org.dspace.app.webui.servlet;

import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.util.common.xml.XmlWriter;
import org.apache.log4j.Logger;
import org.apache.xml.serializer.Method;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.dspace.googlestats.GoogleQueryManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * University of Edinburgh
 *
 * @author cknowles
 * @date 12-Apr-2011
 */
public class GoogleAnalyticsQueryServlet extends DSpaceServlet
{

    /**
     * log4j logger
     */
    private static Logger log = Logger.getLogger(GoogleAnalyticsQueryServlet.class);


    protected void doDSGet(Context context, HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        // is the statistics data publically viewable?
        boolean privatereport = ConfigurationManager.getBooleanProperty("googleanalytics.authorization.admin");
        // is the user a member of the Administrator (1) group?
        boolean admin = Group.isMember(context, 1);
        if (!privatereport || admin)
        {
            process(context, request, response);
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
            process(context, request, response);
        }
        else
        {
            throw new AuthorizeException();
        }

    }

    protected void process(Context context, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, AuthorizeException
    {

        String headMetadata = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            //todo get dates from request and if empty revert to calendar
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String action = request.getParameter("action");

            log.debug("GoogleAnalyticsQueryServlet action " + action);
            log.debug("GoogleAnalyticsQueryServlet startDate " + startDate);
            log.debug("GoogleAnalyticsQueryServlet endDate " + endDate);

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
                        throw new ServletException("Start Date is not valid");
                    }
                }
                catch (ParseException pe)
                {
                    throw new ServletException("Start Date is not valid");
                }

                try
                {
                    Date testDate = sdf.parse(endDate);
                    if (!sdf.format(testDate).equals(endDate))
                    {
                        throw new ServletException("End Date is not valid");
                    }
                }
                catch (ParseException pe)
                {
                    throw new ServletException("End Date is not valid");
                }
            }
            //todo check dates are actual dates


            String gaStart = ConfigurationManager.getProperty("googleanalytics.startdate");
            request.setAttribute("gaStart", gaStart);

            request.setAttribute("dspace.layout.head", headMetadata);
            GoogleQueryManager query = new GoogleQueryManager();

            response.setContentType("text/xml; charset=\"utf-8\"");
            Properties props = OutputPropertiesFactory.getDefaultMethodProperties(Method.XML);
            Serializer ser = SerializerFactory.getSerializer(props);
            Writer writer = response.getWriter();
            ser.setWriter(writer);
            try
            {
                DataFeed feed;
                if (action.equals("YearMonth"))
                {
                    feed = query.queryViewDSpaceMonth(startDate, endDate);
                    XmlWriter xmlW = new XmlWriter(writer);
                    writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                    feed.generateAtom(xmlW, new ExtensionProfile());
                }
                else if (action.equals("BitstreamYearMonth"))
                {
                    feed = query.queryBitstreamViewMonth(startDate, endDate);
                    XmlWriter xmlW = new XmlWriter(writer);
                    writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                    feed.generateAtom(xmlW, new ExtensionProfile());
                }
                else if (action.equals("TopPages"))
                {
                    feed = query.queryTopPages(startDate, endDate);
                    XmlWriter xmlW = new XmlWriter(writer);
                    writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                    feed.generateAtom(xmlW, new ExtensionProfile());
                }
                else if (action.equals("TopItems"))
                {
                    feed = query.queryTopItems(startDate, endDate, context);
                    XmlWriter xmlW = new XmlWriter(writer);
                    writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                    feed.generateAtom(xmlW, new ExtensionProfile());
                }
                else if (action.equals("TopDownloads"))
                {
                    feed = query.queryTopDownloads(startDate, endDate);
                    writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                    XmlWriter xmlW = new XmlWriter(writer);
                    feed.generateAtom(xmlW, new ExtensionProfile());
                }
                else if (action.equals("TopCountries"))
                {
                    feed = query.queryTopCountries(startDate, endDate);                   
                    XmlWriter xmlW = new XmlWriter(writer);
                    writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                    feed.generateAtom(xmlW, new ExtensionProfile());
                }
                else
                {
                    writer.append("Statistics cannot be return for this selection");
                }

                writer.flush();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                log.error("IOException error : " + e);
                throw new IOException(e.toString());

            }
            finally
            {
                ser.reset();
            }
        }
        catch (Exception e)
        {
            log.error("error : " + e);
            //e.printStackTrace();
            throw new ServletException(e);
        }

    }
}