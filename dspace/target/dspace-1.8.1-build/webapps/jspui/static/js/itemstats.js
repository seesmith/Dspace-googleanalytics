// Load the Google data JavaScript client library.
google.load('gdata', '1.x', {packages: ['analytics']});
google.load('visualization', '1', {'packages': ['barchart','geomap','linechart']});
// Set the callback function when the library is ready.
google.setOnLoadCallback(init);

/**
 * This is called once the Google Data JavaScript library has been loaded.
 * It creates a new AnalyticsService object, adds a click handler to the
 * authentication button and updates the button text depending on the status.
 */
function init()
{
}
/**
 * Handle the data returned by the Export API by constructing the
 * inner parts of an HTML table and inserting into the HTML File.
 * @param {object} result Parameter passed back from the feed handler.
 */
function handleDataFeed(feedXML)
{
    var entries = feedXML.getElementsByTagName("entry");
    if (entries.length == 0)
    {
        entries = feedXML.getElementsByTagName("atom:entry");
    }

    // Create an HTML Table using an array of elements.
    var outputTable = ['<table class="gaResults">' +
                       '<caption>No. of Page views</caption>' +
                       '<thead><tr>',
        '<th>Year/Month</th>',
        '<th>Page views</th></tr></thead>'];

    //create 2 arrays
    var data1 = [];
    var data2 = [];
    // Iterate through the feed entries and add the data as table rows.
    for (var i = 0, entry; entry = entries[i]; ++i)
    {
        var dimensions = entry.getElementsByTagName("dimension");
        if (dimensions.length == 0)
        {
            dimensions = entry.getElementsByTagName("dxp:dimension");
        }
        var metrics = entry.getElementsByTagName("metric");
        if (metrics.length == 0)
        {
            metrics = entry.getElementsByTagName("dxp:metric");
        }


        var cname = "odd";
        if (i % 2 == 0)
        {
            cname = "even";
        }

        var diName = "";
        var diValue = "";
        for (var j = 0, di; di = dimensions[j]; ++j)
        {
            diName += di.getAttribute("name").replace("ga:", "") + "/";
            diValue += di.getAttribute("value") + "/";
        }
        var metName = "";
        var metValue = "";
        for (var k = 0, met; met = metrics[k]; ++k)
        {
            metName += met.getAttribute("name").replace("ga:", "") + "/";
            metValue += met.getAttribute("value") + "/";
        }
        diName = diName.substring(0, diName.length - 1);
        metName = metName.substring(0, metName.length - 1);
        diValue = diValue.substring(0, diValue.length - 1);
        metValue = metValue.substring(0, metValue.length - 1);
        // Add a row in the HTML Table array.
        var row = [diValue,metValue].join('</td><td>');
        outputTable.push('<tr class="' + cname + '"><td>', row, '</td></tr>');

        data1[i] = diValue;
        data2[i] = parseInt(metValue);
    }
    var aggregate = feedXML.getElementsByTagName("aggregates");
    if (aggregate.length == 0)
    {
        aggregate = feedXML.getElementsByTagName("dxp:aggregates");
    }
    if (aggregate != null && aggregate.length > 0)
    {
        var aggregates = aggregate[0].getElementsByTagName("metric");
        if (aggregates.length == 0)
        {
            aggregates = aggregate[0].getElementsByTagName("dxp:metric");
        }

        for (var l = 0, metric; metric = aggregates[l]; ++l)
        {
            var row1 = ['Total', metric.getAttribute("value")].join('</td><td>');
            outputTable.push('<tfoot><tr><td>', row1, '</td></tr><tfoot>');
        }
    }
    outputTable.push('</table>');
    // Insert the table into the UI.
    document.getElementById('outputDiv').innerHTML = outputTable.join('');
    drawYearMonthPageViewsChart(data1, data2);
}

function drawYearMonthPageViewsChart(data1, data2)
{
    var chartOptions = {};
    chartOptions['height'] = 300;
    chartOptions['legend'] = 'left';
    chartOptions['legendFontSize'] = '12';
    chartOptions['axisFontSize'] = '12';
    chartOptions['showCategories'] = true;
    chartOptions['title'] = 'No of page views per month';
    var wrapper = new google.visualization.ChartWrapper({
        chartType: 'BarChart',
        dataTable: [ data1, data2],
        options: chartOptions,
        containerId: 'pageViewChartDiv'
    });
    wrapper.draw();
}


/**
 * Handle the data returned by the Export API by constructing the
 * inner parts of an HTML table and inserting into the HTML File.
 * @param {object} result Parameter passed back from the feed handler.
 */
function handleBitstreamDataFeed(feedXML)
{
    var entries = feedXML.getElementsByTagName("entry");
    if (entries.length == 0)
    {
        entries = feedXML.getElementsByTagName("atom:entry");
    }

    // Create an HTML Table using an array of elements.
    var outputTable = ['<table class="gaResults">' +
                       '<caption>No. of Downloads</caption>'
            + '<thead><tr>',
        '<th>Year/Month</th>',
        '<th>Downloads</th></tr></thead>'];

    //create 2 arrays
    var data1 = [];
    var data2 = [];
    // Iterate through the feed entries and add the data as table rows.
    for (var i = 0, entry; entry = entries[i]; ++i)
    {
        var dimensions = entry.getElementsByTagName("dimension");
        if (dimensions.length == 0)
        {
            dimensions = entry.getElementsByTagName("dxp:dimension");
        }
        var metrics = entry.getElementsByTagName("metric");
        if (metrics.length == 0)
        {
            metrics = entry.getElementsByTagName("dxp:metric");
        }


        var cname = "odd";
        if (i % 2 == 0)
        {
            cname = "even";
        }

        var diName = "";
        var diValue = "";
        for (var j = 0, di; di = dimensions[j]; ++j)
        {
            diName += di.getAttribute("name").replace("ga:", "") + "/";
            diValue += di.getAttribute("value") + "/";
        }
        var metName = "";
        var metValue = "";
        for (var k = 0, met; met = metrics[k]; ++k)
        {
            metName += met.getAttribute("name").replace("ga:", "") + "/";
            metValue += met.getAttribute("value") + "/";
        }
        diName = diName.substring(0, diName.length - 1);
        metName = metName.substring(0, metName.length - 1);
        diValue = diValue.substring(0, diValue.length - 1);
        metValue = metValue.substring(0, metValue.length - 1);
        // Add a row in the HTML Table array.
        var row = [diValue,metValue].join('</td><td>');
        outputTable.push('<tr class="' + cname + '"><td>', row, '</td></tr>');

        data1[i] = diValue;
        data2[i] = parseInt(metValue);
    }
    var aggregate = feedXML.getElementsByTagName("aggregates");
    if (aggregate.length == 0)
    {
        aggregate = feedXML.getElementsByTagName("dxp:aggregates");
    }
    if (aggregate != null && aggregate.length > 0)
    {
        var aggregates = aggregate[0].getElementsByTagName("metric");
        if (aggregates.length == 0)
        {
            aggregates = aggregate[0].getElementsByTagName("dxp:metric");
        }

        for (var l = 0, metric; metric = aggregates[l]; ++l)
        {
            var row1 = ['Total', metric.getAttribute("value")].join('</td><td>');
            outputTable.push('<tfoot><tr><td>', row1, '</td></tr><tfoot>');
        }
    }

    outputTable.push('</table>');
    // Insert the table into the UI.
    document.getElementById('bitstreamOutputDiv').innerHTML = outputTable.join('');
    drawYearMonthBitstreamChart(data1, data2);
}

function drawYearMonthBitstreamChart(data1, data2)
{
    var chartOptions = {};
    //chartOptions['width']  = getElementWidth(container.parentNode);
    chartOptions['height'] = 300;
    chartOptions['legend'] = 'bottom';
    chartOptions['legendFontSize'] = '12';
    chartOptions['axisFontSize'] = '12';
    chartOptions['showCategories'] = true;
    chartOptions['title'] = 'No of downloads per month';

    var wrapper = new google.visualization.ChartWrapper({
        chartType: 'ColumnChart',
        dataTable: [
            data1,
            data2
        ],
        options: chartOptions,
        containerId: 'bitstreamChartDiv'
    });
    wrapper.draw();
    loaded();
}


function loading()
{
    document.getElementById("loading").style.display = "block";
}
function loaded()
{
    document.getElementById("loading").style.display = "none";
}