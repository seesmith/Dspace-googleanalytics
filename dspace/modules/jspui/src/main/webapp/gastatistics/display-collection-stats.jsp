<%@ page contentType="text/html;charset=UTF-8" %>
<%--
  - Renders a page containing a statistical summary of the repository usage
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<dspace:layout locbar="link" title="collection statistics">

    <h2>Google Analytics Statistics for ${dso.name}</h2>

    <div id="dataControls">
        <form method="post" action="">
            <input type="hidden" id="handle" value="${dso.handle}"/>
            <input type="hidden" id="context" value='<%= request.getContextPath() %>'/>
        </form>
    </div>
    <div id="garesults">
        <div id="outputTopItemDiv" class="floatResults" style="width:700px;">
            <table class="gaResults">
                <caption>Top Items for the past year</caption>
                <thead>
                <th>Item</th>
                <th>Pageviews</th>
                </thead>
                <tbody>

                <c:forEach items="${itemList}" var="entry" varStatus="counter">
                    <c:choose>
                        <c:when test="${counter.index % 2 == 0}">
                            <c:set var="rowClass" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="rowClass" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${rowClass}">
                        <td>${entry.object.name}<br/>
                            <a href="<%= request.getContextPath() %>${entry.path}">${entry.path}</a></td>
                        <td>${entry.views}</td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
            <h4>Total pageviews for all ${dso.name} items: ${totalViews}</h4>
        </div>
        <div class="clearfloat" style="clear:both; height:0; width:0"></div>
        <div id="outputTopBitstreamDiv" class="floatResults" style="width:700px;">
            <table class="gaResults">
                <caption>Top Downloads for the past year</caption>
                <thead>
                <th>Bitstream</th>
                <th>Downloads</th>
                </thead>
                <tbody>

                <c:forEach items="${downloadsList}" var="entry" varStatus="counter">
                    <c:choose>
                        <c:when test="${counter.index % 2 == 0}">
                            <c:set var="rowClass" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="rowClass" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${rowClass}">
                        <td>${entry.object.name}<br/>
                            <a href="<%= request.getContextPath() %>${entry.path}">${entry.path}</a></td>
                        <td>${entry.views}</td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
            <h4>Total Downloads for all ${dso.name} bitstreams: ${totalDownloads}</h4>
        </div>
    </div>
    <div class="clearfloat" style="clear:both; height:0; width:0"></div>
    <div><fmt:message key="jsp.google.stats.msg">
        <fmt:param>${gaStart}</fmt:param>
    </fmt:message></div>
</dspace:layout>
