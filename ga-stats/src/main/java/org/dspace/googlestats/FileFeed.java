package org.dspace.googlestats;

import org.dspace.content.DSpaceObject;

public class FileFeed implements Comparable
{


    private final DSpaceObject dso;

    private final String path;

    private final int views;

    public FileFeed(DSpaceObject dso, String path, int views)
    {
        this.dso = dso;
        this.path = path;
        this.views = views;
    }

    public DSpaceObject getObject()
    {
        return dso;
    }

    public int getViews()
    {
        return views;
    }

    public String getPath()
    {
        return path;
    }

    @Override
    public int hashCode()
    {
        int result = dso != null ? dso.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + views;
        return result;
    }

    @Override
    public String toString()
    {
        return "FileFeed{" +
                "dso=" + dso +
                ", path='" + path + '\'' +
                ", views=" + views +
                '}';
    }

    public int compareTo(Object o)
    {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        if (!(o instanceof FileFeed))
        {
            return AFTER;
        }
        else
        {
            FileFeed aThat = (FileFeed) o;
            if (this.views < aThat.views)
            {
                return AFTER;
            }
            else if (this.views > aThat.views)
            {
                return BEFORE;
            }
            else
            {
                return EQUAL;
            }
        }
    }
}