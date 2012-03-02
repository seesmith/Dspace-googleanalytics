// Load the Google data JavaScript client library.
google.load('gdata', '1.x', {packages: ['analytics']});
google.load('visualization', '1', {'packages': ['barchart','geomap','linechart']});


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

    //<dxp:dimension name="ga:year" value="2011"/>
    //<dxp:dimension name="ga:month" value="08"/>
    //<dxp:metric confidenceInterval="0.0" name="ga:pageviews" type="integer" value="31130"/>

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
}


// callback method to be invoked when getDataFeed() returns data
function handleTopDataFeed(feedXML)
{
    var context = document.getElementById('context').value;
    var entries = feedXML.getElementsByTagName("entry");
    if (entries.length == 0)
    {
        entries = feedXML.getElementsByTagName("atom:entry");
    }
    // create an HTML Table using an array of elements
    var outputTable = ['<table class="gaResults">'];
    outputTable.push('<caption>Top Pages</caption><thead><tr><th>Page</th><th>Pageviews</th></tr></thead>');

    var data1 = [];
    var data2 = [];

    // Iterate through the feed entries and add the data as table rows
    for (var i = 0, entry; entry = entries[i]; ++i)
    {
        var cname = "odd";
        if (i % 2 == 0)
        {
            cname = "even";
        }

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

        var diName = "";
        var diValue = "";
        for (var j = 0, di; di = dimensions[j]; ++j)
        {
            diName += di.getAttribute("name").replace("ga:", "");
            if (di.getAttribute("name") == "ga:pagePath")
            {
                diValue += "<br/><a href='" + context + di.getAttribute("value").replace("ga:", "") + "'>" + di.getAttribute("value") + "</a>";
            }
            else
            {
                diValue += di.getAttribute("value");
            }
        }
        var metName = "";
        var metValue = "";
        for (var k = 0, met; met = metrics[k]; ++k)
        {
            metName += met.getAttribute("name").replace("ga:", "");
            metValue += met.getAttribute("value");
        }
        diName = diName.substring(0, diName.length - 1);
        metName = metName.substring(0, metName.length - 1);
        diValue = diValue.substring(0, diValue.length - 1);
        metValue = metValue.substring(0, metValue.length);

        var row = [diValue,metValue].join('</td><td>');
        outputTable.push('<tr class="' + cname + '"><td>', row, '</td></tr>');

        data1[i] = diValue;
        data2[i] = parseInt(metValue);
    }
    outputTable.push('</table>');

    // print the generated table
    document.getElementById('outputTopDiv').innerHTML = outputTable.join('');
}


// callback method to be invoked when getDataFeed() returns data
function handletopItemDataFeed(feedXML)
{
    var context = document.getElementById('context').value;
    var entries = feedXML.getElementsByTagName("entry");
    if (entries.length == 0)
    {
        entries = feedXML.getElementsByTagName("atom:entry");
    }
    // create an HTML Table using an array of elements
    var outputItemTable = ['<table class="gaResults">'];
    outputItemTable.push('<caption>Top Items Viewed</caption><thead><tr><th>Page</th><th>Pageviews</th></tr></thead>');

    var outputCollTable = ['<table class="gaResults">'];
    outputCollTable.push('<caption>Top Collections Viewed</caption><thead><tr><th>Page</th><th>Pageviews</th></tr></thead>');

    var outputCommTable = ['<table class="gaResults">'];
    outputCommTable.push('<caption>Top Communities Viewed</caption><thead><tr><th>Page</th><th>Pageviews</th></tr></thead>');

    var data1 = [];
    var data2 = [];
    var noIt = 0;
    var noColl = 0;
    var noComm = 0;

    // Iterate through the feed entries and add the data as table rows
    for (var i = 0, entry; entry = entries[i]; ++i)
    {
        var item = false;
        var coll = false;
        var comm = false;
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

        var diName = "";
        var diValue = "";
        for (var j = 0, di; di = dimensions[j]; ++j)
        {

            if (di.getAttribute("name") == "ga:pagePath")
            {
                diName += di.getAttribute("name").replace("ga:", "");
                diValue += "<br/><a href='" + context + di.getAttribute("value").replace("ga:", "") + "'>" + di.getAttribute("value") + "</a>";
            }
            else if (di.getAttribute("name") == "resourceType")
            {
                if (di.getAttribute("value") == "Collection")
                {
                    coll = true;
                }
                else if (di.getAttribute("value") == "Community")
                {
                    comm = true;
                }
                else
                {
                    item = true;
                }
            }
            else
            {
                diName += di.getAttribute("name").replace("ga:", "");
                diValue += di.getAttribute("value");
            }
        }
        var metName = "";
        var metValue = "";
        for (var k = 0, met; met = metrics[k]; ++k)
        {
            metName += met.getAttribute("name").replace("ga:", "");
            metValue += met.getAttribute("value");
        }
        diName = diName.substring(0, diName.length - 1);
        metName = metName.substring(0, metName.length - 1);
        diValue = diValue.substring(0, diValue.length - 1);
        metValue = metValue.substring(0, metValue.length);

        var cname = "odd";
        var row = [diValue,metValue].join('</td><td>');
        if (item)
        {
            if (noIt < 10)
            {
                if (noIt % 2 == 0)
                {
                    cname = "even";
                }
                noIt++;
                outputItemTable.push('<tr class="' + cname + '"><td>', row, '</td></tr>');
            }
        }
        else if (coll)
        {
            if (noColl < 10)
            {
                if (noColl % 2 == 0)
                {
                    cname = "even";
                }
                noColl++;
                outputCollTable.push('<tr class="' + cname + '"><td>', row, '</td></tr>');
            }
        }
        else if (comm)
        {
            if (noComm < 10)
            {
                if (noComm % 2 == 0)
                {
                    cname = "even";
                }
                noComm++;
                outputCommTable.push('<tr class="' + cname + '"><td>', row, '</td></tr>');
            }
        }

        data1[i] = diValue;
        data2[i] = parseInt(metValue);
    }
    outputItemTable.push('</table>');
    outputCollTable.push('</table>');
    outputCommTable.push('</table>');

    // print the generated table
    document.getElementById('outputTopItemDiv').innerHTML = outputItemTable.join('');
    document.getElementById('outputCollDiv').innerHTML = outputCollTable.join('');
    document.getElementById('outputCommDiv').innerHTML = outputCommTable.join('');
}


// callback method to be invoked when getDataFeed() returns data
function handleTopBitstreamDataFeed(feedXML)
{
    var context = document.getElementById('context').value;
    var entries = feedXML.getElementsByTagName("entry");
    if (entries.length == 0)
    {
        entries = feedXML.getElementsByTagName("atom:entry");
    }
    // create an HTML Table using an array of elements
    var outputTable = ['<table class="gaResults">'];
    outputTable.push('<caption>Top Downloads</caption><thead><tr><th>Bitstream</th><th>Downloads</th></tr></thead>');

    var data1 = [];
    var data2 = [];

    // Iterate through the feed entries and add the data as table rows
    for (var i = 0, entry; entry = entries[i]; ++i)
    {
        var cname = "odd";
        if (i % 2 == 0)
        {
            cname = "even";
        }

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


        var diName = "";
        var diValue = "";
        for (var j = 0, di; di = dimensions[j]; ++j)
        {
            diName += di.getAttribute("name").replace("ga:", "");
            if (di.getAttribute("name") == "ga:pagePath")
            {
                diValue += "<br/><a href='" + context + di.getAttribute("value").replace("ga:", "") + "'>" + di.getAttribute("value") + "</a>";
            }
            else
            {
                diValue += di.getAttribute("value");
            }
        }
        var metName = "";
        var metValue = "";
        for (var k = 0, met; met = metrics[k]; ++k)
        {
            metName += met.getAttribute("name").replace("ga:", "");
            metValue += met.getAttribute("value");
        }
        diName = diName.substring(0, diName.length - 1);
        metName = metName.substring(0, metName.length - 1);
        diValue = diValue.substring(0, diValue.length - 1);
        metValue = metValue.substring(0, metValue.length);

        var row = [diValue,metValue].join('</td><td>');
        outputTable.push('<tr class="' + cname + '"><td>', row, '</td></tr>');

        data1[i] = diValue;
        data2[i] = parseInt(metValue);
    }
    outputTable.push('</table>');

    // print the generated table
    document.getElementById('outputTopBitstreamDiv').innerHTML = outputTable.join('');
}

// callback method to be invoked when getDataFeed() returns data
function handleTopCountryDataFeed(feedXML)
{
    var context = document.getElementById('context').value;
    var entries = feedXML.getElementsByTagName("entry");
    if (entries.length == 0)
    {
        entries = feedXML.getElementsByTagName("atom:entry");
    }
    // create an HTML Table using an array of elements
    var outputTable = ['<table class="gaResults">'];
    outputTable.push('<caption>Top Country Views</caption><thead><tr><th>Country</th><th>PageViews</th></tr></thead>');

    var data1 = [];
    var data2 = [];
    var mapdata = new google.visualization.DataTable();
    mapdata.addRows(100);
    mapdata.addColumn('string', 'Country');
    mapdata.addColumn('number', 'Views');


    // Iterate through the feed entries and add the data as table rows
    for (var i = 0, entry; entry = entries[i]; ++i)
    {
        var cname = "odd";
        if (i % 2 == 0)
        {
            cname = "even";
        }

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


        var diName = "";
        var diValue = "";
        for (var j = 0, di; di = dimensions[j]; ++j)
        {
            diName += di.getAttribute("name").replace("ga:", "");
            diValue += di.getAttribute("value");
        }
        var metName = "";
        var metValue = "";
        for (var k = 0, met; met = metrics[k]; ++k)
        {
            metName += met.getAttribute("name").replace("ga:", "");
            metValue += met.getAttribute("value");
        }
        diName = diName.substring(0, diName.length);
        metName = metName.substring(0, metName.length);
        diValue = diValue.substring(0, diValue.length);
        metValue = metValue.substring(0, metValue.length);

        var row = [diValue,metValue].join('</td><td>');
        if (i < 20)
        {
            outputTable.push('<tr class="' + cname + '"><td>', row, '</td></tr>');
        }

        data1[i] = diValue;
        metValue = parseInt(metValue);
        data2[i] = metValue;
        mapdata.setValue(i, 0, diValue);
        mapdata.setValue(i, 1, metValue);

    }
    outputTable.push('</table>');

    // print the generated table
    document.getElementById('outputTopCountries').innerHTML = outputTable.join('');
    drawMap(mapdata);
    loaded();
}

function drawMap(mapdata)
{
    var options = {};
    options['dataMode'] = 'regions';
    var container = document.getElementById('countriesMap');
    var geomap = new google.visualization.GeoMap(container);
    geomap.draw(mapdata, options);

}


function loading()
{
    document.getElementById("loading").style.display = "block";
}
function loaded()
{
    document.getElementById("loading").style.display = "none";
}