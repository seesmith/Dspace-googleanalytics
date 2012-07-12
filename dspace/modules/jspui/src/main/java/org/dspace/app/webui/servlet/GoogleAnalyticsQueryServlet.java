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
import org.dspace.googlestats.GoogleAnalyticsAccount;
import org.dspace.googlestats.GoogleAnalyticsUtils;
import org.dspace.googlestats.GoogleQueryManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.List;
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

        //todo get dates from request and if empty revert to calendar
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String action = request.getParameter("action");

        log.debug("GoogleAnalyticsQueryServlet action " + action);

        String gaStart = GoogleAnalyticsAccount.getInstance().getStartDate();
        request.setAttribute("gaStart", gaStart);

        request.setAttribute("dspace.layout.head", headMetadata);
        GoogleQueryManager query = new GoogleQueryManager();

        List<String> dateErrors = GoogleAnalyticsUtils.testDate(startDate, endDate, gaStart);
        Writer writer = response.getWriter();
        Properties props = OutputPropertiesFactory.getDefaultMethodProperties(Method.XML);
        Serializer ser = SerializerFactory.getSerializer(props);
        ser.setWriter(writer);

        response.setContentType("text/xml; charset=\"utf-8\"");

        if (dateErrors.isEmpty())
        {
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
                //e.printStackTrace();
                log.error("GoogleAnalyticsQueryServlet Exception : " + e);
                writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>Error Generating XML " + e);
                writer.flush();

            }
            finally
            {
                ser.reset();
            }

        }
        else
        {
            log.info("Date error " + dateErrors);

            writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            for (String error : dateErrors)
            {
                writer.append("<error message=\""+error + "\"/>");
            }
            writer.flush();
            ser.reset();
        }

    }
}