<%@ page contentType="text/html;charset=UTF-8" %>
<%--
  - Renders a page containing a statistical summary of the repository usage
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="org.dspace.content.DSpaceObject" %>
<% DSpaceObject dso = (DSpaceObject) request.getAttribute("dso");%>
<link rel="stylesheet" type="text/css"
      href="<%= request.getContextPath() %>/static/css/smoothness/jquery-ui-1.8.16.custom.css">
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/itemstats.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/jquery.validate.js"></script>

<dspace:layout title='<%= dso.getHandle() +" statistics"%>'>
    <script type="text/javascript">

        $(document).ready(function()
        {
            $("#gaDateForm").validate({
                rules: {
                    startDate: {
                        required: true,
                        dateISO: true
                    },
                    endDate: {
                        required: true,
                        dateISO: true
                    }
                },
                messages: {
                    startDate:
                    {
                        required: " Start Date must be entered",
                        dateISO: " Enter a valid start date yyyy-MM-dd e.g. 2012-12-25"
                    },
                    endDate: {
                        required: " End Date must be entered",
                        dateISO: " Enter a valid end date yyyy-MM-dd e.g. 2012-12-25"
                    }
                },
                submitHandler: function()
                {
                    getQueryResults();
                }
            });
        });

        $(function()
        {
            var dates = $("#startDate, #endDate").datepicker({
                defaultDate: "-1d",
                changeMonth: true,
                numberOfMonths: 1,
                dateFormat: 'yy-mm-dd',
                onSelect: function(selectedDate)
                {
                    var option = this.id == "startDate" ? "minDate" : "maxDate",
                            instance = $(this).data("datepicker"),
                            date = $.datepicker.parseDate(
                                    instance.settings.dateFormat ||
                                    $.datepicker._defaults.dateFormat,
                                    selectedDate, instance.settings);
                    dates.not(this).datepicker("option", option, date);
                }
            });
        });

        $.ajaxSetup({
            cache: false
        });

        function getYearMonthDSpace()
        {
            $.ajax({
                type: "POST",
                data: "startDate=" + document.getElementById('startDate').value
                        + "&endDate=" + document.getElementById('endDate').value
                        + "&handle=" + document.getElementById('handle').value + "&action=YearMonth",
                url: "<%= request.getContextPath() %>/getItemQueryResults?",
                context: document.body,
                dataType: "xml",
                error: function()
                {
                    document.getElementById("message").innerHTML = "Please modify the date to load statistics"
                },
                success: function(dataBack)
                {
                    handleDataFeed(dataBack);
                }
            });
        }

        function getBitstreamYearMonthDSpace()
        {
            $.ajax({
                type: "POST",
                data: "startDate=" + document.getElementById('startDate').value
                        + "&endDate=" + document.getElementById('endDate').value
                        + "&handle=" + document.getElementById('handle').value + "&action=BitstreamYearMonth",
                url: "<%= request.getContextPath() %>/getItemQueryResults?",
                context: document.body,
                dataType: "xml",
                error: function()
                {
                    document.getElementById("message").innerHTML = "Please modify the date to load statistics"
                },
                success: function(dataBack)
                {
                    handleBitstreamDataFeed(dataBack);
                }
            });
        }

        function getQueryResults()
        {
            loading();
            getYearMonthDSpace();
            getBitstreamYearMonthDSpace();
        }

    </script>

    <h2>Statistics for item <a href="<%= request.getContextPath() %>/handle/${dso.handle}">${dso.handle}</a></h2>
    <div id="loading">loading statistics ...</div>
    <div id="dataControls">
        <form action="" id="gaDateForm">
            <input type="hidden" id="siteId" value="${siteId}"/>
            <label name="lstartDate" id="lstartDate" for="startDate">Start Date: </label>
            <input name="startDate" type="text" id="startDate" value="${startDate}"/>
            <label name="lendDate" id="lendDate" for="endDate">End Date: </label>
            <input name="endDate" type="text" id="endDate" value="${endDate}"/>
            <input type="hidden" id="handle" value="${dso.handle}"/>
            <input id="btnSubmit" type="submit" value="Statistics"/>

        </form>
    </div>
    <div id="message"></div>
    <div id="garesults">
        <div id="chromeDiv" class="floatResults" style="width:200px;"></div>
        <div class="clearfloat" style="clear:both; height:0; width:0"></div>
        <div id="outputDiv" class="floatResults" style="width:200px;"></div>
        <div id="pageViewChartDiv" class="floatResults" style="width: 500px; height: 300px;"></div>
        <div class="clearfloat" style="clear:both; height:0; width:0"></div>
        <div id="bitstreamOutputDiv" class="floatResults" style="width:200px;"></div>
        <div id="bitstreamChartDiv" class="floatResults" style="width:500px; height: 300px;"></div>
        <div class="clearfloat" style="clear:both; height:0; width:0"></div>
        <div><fmt:message key="jsp.google.stats.msg">
        <fmt:param>${gaStart}</fmt:param>
    </fmt:message>
        </div>
    </div>

    <script type="text/javascript">window.onLoad = getQueryResults();</script>
</dspace:layout>
