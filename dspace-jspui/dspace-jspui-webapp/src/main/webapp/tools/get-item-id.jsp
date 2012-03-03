<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Form requesting a Handle or internal item ID for item editing
  -
  - Attributes:
  -     invalid.id  - if this attribute is present, display error msg
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>


<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.ConfigurationManager" %>

<dspace:layout titlekey="jsp.tools.get-item-id.title"
               navbar="admin"
               locbar="link"
               parenttitlekey="jsp.administer"
               parentlink="/dspace-admin">

	<%-- <h1>Edit or Delete Item</h1> --%>
	<h1><fmt:message key="jsp.tools.get-item-id.heading"/></h1>
    
<%
    if (request.getAttribute("invalid.id") != null) { %>
    <%-- <p><strong>The ID you entered isn't a valid item ID.</strong>  If you're trying to
    edit a community or collection, you need to use the --%>
    <%-- <a href="<%= request.getContextPath() %>/dspace-admin/edit-communities">communities/collections admin page.</a></p> --%>
	<p><fmt:message key="jsp.tools.get-item-id.info1">
        <fmt:param><%= request.getContextPath() %>/dspace-admin/edit-communities</fmt:param>
    </fmt:message></p>
<%  } %>

    <%-- <p>Enter the Handle or internal item ID of the item you want to edit or
    delete.  <dspace:popup page="/help/site-admin.html#items">More help...</dspace:popup></p> --%>

	<div><fmt:message key="jsp.tools.get-item-id.info2"/>  <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.site-admin\") + \"#items\"%>"><fmt:message key="jsp.morehelp"/></dspace:popup></div>
    
    <form method="get" action="">
        <center>
            <table class="miscTable">
                <tr class="oddRowEvenCol">
                    <%-- <td class="submitFormLabel">Handle:</td> --%>
					<td class="submitFormLabel"><label for="thandle"><fmt:message key="jsp.tools.get-item-id.handle"/></label></td>
                    <td>
                            <input type="text" name="handle" id="thandle" value="<%= ConfigurationManager.getProperty("handle.prefix") %>/" size="12"/>
                            <%-- <input type="submit" name="submit" value="Find" /> --%>
							<input type="submit" name="submit" value="<fmt:message key="jsp.tools.get-item-id.find.button"/>" />
                    </td>
                </tr>
                <tr><td></td></tr>
                <tr class="oddRowEvenCol">
                    <%-- <td class="submitFormLabel">Internal ID:</td> --%>
					<td class="submitFormLabel"><label for="titem_id"><fmt:message key="jsp.tools.get-item-id.internal"/></label></td>
                    <td>
                            <input type="text" name="item_id" id="titem_id" size="12"/>
                            <%-- <input type="submit" name="submit" value="Find"> --%>
							<input type="submit" name="submit" value="<fmt:message key="jsp.tools.get-item-id.find.button"/>" />
                    </td>
                </tr>
            </table>
        </center>
    </form>
</dspace:layout>
