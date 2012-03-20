<%--
  -- footer-home.jsp
  --
  -- Version: $Revision: 1.12 $
  --
  -- Date: $Date: 2005/08/25 17:20:26 $
  --
  -- Copyright (c) 2002, Hewlett-Packard Company and Massachusetts
  -- Institute of Technology.  All rights reserved.
  --
  -- Redistribution and use in source and binary forms, with or without
  -- modification, are permitted provided that the following conditions are
  -- met:
  --
  -- - Redistributions of source code must retain the above copyright
  -- notice, this list of conditions and the following disclaimer.
  --
  -- - Redistributions in binary form must reproduce the above copyright
  -- notice, this list of conditions and the following disclaimer in the
  -- documentation and/or other materials provided with the distribution.
  --
  -- - Neither the name of the Hewlett-Packard Company nor the name of the
  -- Massachusetts Institute of Technology nor the names of their
  -- contributors may be used to endorse or promote products derived from
  -- this software without specific prior written permission.
  --
  -- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
  -- ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
  -- LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
  -- A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
  -- HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
  -- INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
  -- BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
  -- OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
  -- ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
  -- TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
  -- USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
  -- DAMAGE.
  --%>

<%--
  - Footer for home page
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>

<%
    String sidebar = (String) request.getAttribute("dspace.layout.sidebar");
    int overallColSpan = 3;
    if (sidebar == null)
    {
        overallColSpan = 2;
    }
%>
                    <%-- End of page content --%>
                    <p>&nbsp;</p>
                </td>

            <%-- Right-hand side bar if appropriate --%>
<%
    if (sidebar != null)
    {
%>
                <td class="sidebar">
                    <%= sidebar %>
                </td>
<%
    }
%>
            </tr>

            <%-- Page footer --%>
<%
    String fromPage = UIUtil.getOriginalURL(request);
    fromPage = URLEncoder.encode(fromPage);
%>
             <tr class="pageFooterBar">
                <td colspan="<%= overallColSpan %>" class="pageFootnote">
       <%-- Sid: RGU Footer panel --%>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
	     <tr class = "RGUPanel">
                <td colspan="2">&nbsp;</td>
	     </tr>
	     <tr class = "RGUPanelMain">
	       <td align=left>&nbsp;&nbsp;&nbsp;<a href="http://www.rgu.ac.uk/policy/disclaimer.cfm" class = "RGULinkText">Disclaimer</a> | <a class = "RGULinkText" href="http://www.rgu.ac.uk/foi">Freedom of Information</a> | <a class = "RGULinkText">Copyright &copy;2011 </a></td>
       	   <td align=right><a class = "RGULinkText">Robert Gordon University, Schoolhill, Aberdeen, AB10 1FR, Scotland, UK: a Scottish charity, registration No. SCO13781</a> &nbsp;&nbsp;</td>
	     </tr>
	     <tr class = "RGUPanel">
             <td colspan="2">&nbsp;</td>
	     </tr>
	</table>
                </td>
            </tr>
        </table>
    </body>
</html>
