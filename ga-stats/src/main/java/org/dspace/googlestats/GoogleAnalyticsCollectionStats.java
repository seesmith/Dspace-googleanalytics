package org.dspace.googlestats;

import org.apache.log4j.Logger;
import org.dspace.content.Bitstream;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.dspace.content.ItemIterator;
import org.dspace.core.Constants;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * University of Edinburgh
 *
 * @author cknowles
 *         20-Oct-2011
 *         <p/>
 *         to obtain stats from the daily files created from google analytics for item views and bitstream downloads
 */
public class GoogleAnalyticsCollectionStats
{

    /**
     * log4j category
     */
    private static final Logger log = Logger.getLogger(GoogleAnalyticsCollectionStats.class);
    private int totalViews;
    private int totalDownloads;
    private final List<FileFeed> topViews;
    private final List<FileFeed> topDownloads;

    public List<FileFeed> getTopDownloads()
    {
        return topDownloads;
    }

    public List<FileFeed> getTopViews()
    {
        return topViews;
    }

    public int getTotalDownloads()
    {
        return totalDownloads;
    }

    public int getTotalViews()
    {
        return totalViews;
    }

    /**
     * Initalise the system
 * @param collection    collection to collect stats for
     */
    public GoogleAnalyticsCollectionStats(Collection collection) throws SQLException
    {
        topViews = new LinkedList<FileFeed>();
        topDownloads = new LinkedList<FileFeed>();
        ItemIterator itemItr = collection.getAllItems();
        while (itemItr.hasNext())
        {
            Item item = itemItr.next();

            String path = "/handle/" + item.getHandle();

            //Views
            String count = ItemViewCounter.getPageCount(path);
            int views = 0;
            if (count.length() > 0)
            {
                views = Integer.parseInt(count);
            }
            totalViews = totalViews + views;
            FileFeed ff = new FileFeed(item, path, views);
            topViews.add(ff);
            Collections.sort(topViews);

            //Downloads

            String bitpath = "/bitstream/"+ item.getHandle() + "/";
            Bitstream[] bitstreams = item.getNonInternalBitstreams();
            for (Bitstream bitstream : bitstreams)
            {
                String encodedName = null;
                try
                {
                    encodedName = encodeBitstreamName(bitstream.getName(), Constants.DEFAULT_ENCODING);
                }
                catch (UnsupportedEncodingException e)
                {
                    log.error(e);
                }
                String bitstreamPath = bitpath + bitstream.getSequenceID() + "/" + encodedName;

                String downloadCount = ItemDownloadCounter.getPageCount(bitstreamPath);
                int downloads = 0;
                if (downloadCount.length() > 0)
                {
                    downloads = Integer.parseInt(downloadCount);
                }
                totalDownloads = totalDownloads + downloads;

                FileFeed bf = new FileFeed(bitstream, path, downloads);
                {
                    topDownloads.add(bf);
                }
            }
            Collections.sort(topDownloads);

        }

    }

     /**
      * Copied method into here as created a recursive maven dependency
      * TODO: should not be in here
         * Encode a bitstream name for inclusion in a URL in an HTML document. This
         * differs from the usual URL-encoding, since we want pathname separators to
         * be passed through verbatim; this is required so that relative paths in
         * bitstream names and HTML references work correctly.
         * <P>
         * If the link to a bitstream is generated with the pathname separators
         * escaped (e.g. "%2F" instead of "/") then the Web user agent perceives it
         * to be one pathname element, and relative URI paths within that document
         * containing ".." elements will be handled incorrectly.
         * <P>
         *
         * @param stringIn
         *            input string to encode
         * @param encoding
         *            character encoding, e.g. UTF-8
         * @return the encoded string
         */
        private static String encodeBitstreamName(String stringIn, String encoding) throws java.io.UnsupportedEncodingException {
            // FIXME: This should be moved elsewhere, as it is used outside the UI
            StringBuilder out = new StringBuilder();

            final String[] pctEncoding = { "%00", "%01", "%02", "%03", "%04",
                    "%05", "%06", "%07", "%08", "%09", "%0a", "%0b", "%0c", "%0d",
                    "%0e", "%0f", "%10", "%11", "%12", "%13", "%14", "%15", "%16",
                    "%17", "%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f",
                    "%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27", "%28",
                    "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f", "%30", "%31",
                    "%32", "%33", "%34", "%35", "%36", "%37", "%38", "%39", "%3a",
                    "%3b", "%3c", "%3d", "%3e", "%3f", "%40", "%41", "%42", "%43",
                    "%44", "%45", "%46", "%47", "%48", "%49", "%4a", "%4b", "%4c",
                    "%4d", "%4e", "%4f", "%50", "%51", "%52", "%53", "%54", "%55",
                    "%56", "%57", "%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e",
                    "%5f", "%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67",
                    "%68", "%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f", "%70",
                    "%71", "%72", "%73", "%74", "%75", "%76", "%77", "%78", "%79",
                    "%7a", "%7b", "%7c", "%7d", "%7e", "%7f", "%80", "%81", "%82",
                    "%83", "%84", "%85", "%86", "%87", "%88", "%89", "%8a", "%8b",
                    "%8c", "%8d", "%8e", "%8f", "%90", "%91", "%92", "%93", "%94",
                    "%95", "%96", "%97", "%98", "%99", "%9a", "%9b", "%9c", "%9d",
                    "%9e", "%9f", "%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6",
                    "%a7", "%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af",
                    "%b0", "%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7", "%b8",
                    "%b9", "%ba", "%bb", "%bc", "%bd", "%be", "%bf", "%c0", "%c1",
                    "%c2", "%c3", "%c4", "%c5", "%c6", "%c7", "%c8", "%c9", "%ca",
                    "%cb", "%cc", "%cd", "%ce", "%cf", "%d0", "%d1", "%d2", "%d3",
                    "%d4", "%d5", "%d6", "%d7", "%d8", "%d9", "%da", "%db", "%dc",
                    "%dd", "%de", "%df", "%e0", "%e1", "%e2", "%e3", "%e4", "%e5",
                    "%e6", "%e7", "%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee",
                    "%ef", "%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7",
                    "%f8", "%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff" };

            byte[] bytes = stringIn.getBytes(encoding);

            for (byte aByte : bytes)
            {
                // Any unreserved char or "/" goes through unencoded
                if ((aByte >= 'A' && aByte <= 'Z')
                        || (aByte >= 'a' && aByte <= 'z')
                        || (aByte >= '0' && aByte <= '9') || aByte == '-'
                        || aByte == '.' || aByte == '_' || aByte == '~'
                        || aByte == '/')
                {
                    out.append((char) aByte);
                }
                else if (aByte >= 0)
                {
                    // encode other chars (byte code < 128)
                    out.append(pctEncoding[aByte]);
                }
                else
                {
                    // encode other chars (byte code > 127, so it appears as
                    // negative in Java signed byte data type)
                    out.append(pctEncoding[256 + aByte]);
                }
            }
            log.debug("encoded \"" + stringIn + "\" to \"" + out.toString() + "\"");

            return out.toString();
        }

}
