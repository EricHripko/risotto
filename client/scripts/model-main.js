// List of all tables
var tables = [
    {
        id: 1,
        description: "#1",
        size: 4
    },
    {
        id: 2,
        description: "#2",
        size: 3
    },
    {
        id: 3,
        description: "#3",
        size: 2
    },
    {
        id: 4,
        description: "#4",
        size: 5
    }
];
// List of all bookingss
var bookings = [
    {
        referenceNumber: 15246437,
        customerName: "Joanna Doe",
        phoneNumber: "07483215619",
        emailAddress: "ehripko@redev.co.uk",
        partySize: 3,
        date: 1456304400,
        table: 1
    },
    {
        referenceNumber: 15246437,
        customerName: "John Doe",
        phoneNumber: "07484565622",
        emailAddress: "ehripko@redev.co.uk",
        partySize: 5,
        date: 1456315200,
        table: 2
    },
    {
        referenceNumber: 15246437,
        customerName: "Janice Doe",
        phoneNumber: "07488210633",
        emailAddress: "ehripko@redev.co.uk",
        partySize: 2,
        date: 1456322400,
        table: 4
    }
];

// Formatters
rivets.formatters.padZeroes = function (value) {
    return value < 10 ? "0" + value : value;
};

// Date and time grid
var granularity = 15;
var timescale = [];
var openTime = 9;
var closeTime = 17;
// Create time intervals
for(var hour = openTime; hour < closeTime; hour++)
    for(var minutes = 0; minutes < 60; minutes = minutes + 15) {
        var interval = {hour: hour, mins: minutes, tables: []};
        // For each interval add a table
        tables.forEach(function (table) {
            table = Object.assign({}, table);
            interval.tables.push(table);
            // For each table find appropriate bookings
            table.bookings = bookings.filter(function (booking) {
                var date = new Date(booking.date * 1000);
                return booking.table == table.id && date.getHours() == hour && date.getMinutes() == minutes;
            });
        });
        timescale.push(interval);
    }

// Create the model for the view
var viewModel = {
    tables: tables,
    granularity: granularity,
    timescale: timescale
};

// Perform data binding
document.addEventListener("DOMContentLoaded", function () {
    rivets.bind(document.getElementById("calendar"), viewModel);
});