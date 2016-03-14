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
    },
    {
        id: 4, 
        description: "#5",
        size: 4
    }
];

// Formatters
rivets.formatters.padZeroes = function (value) {
    return value < 10 ? "0" + value : value;
};
rivets.formatters.nth = function (value) {
    if(value > 10 && value < 20)
        return "th";

    value = value % 10;
    var map = ["th", "st", "nd", "rd", "th"];
    return map[Math.min(value, map.length - 1)];
};
rivets.formatters.time = function (value) {
    if(!(value instanceof Date))
        value = new Date(value * 1000);
    return rivets.formatters.padZeroes(value.getHours()) + ":" + rivets.formatters.padZeroes(value.getMinutes());
};
rivets.formatters.shortDate = function (value) {
    if(!(value instanceof Date))
        value = new Date(value * 1000);

    var months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    return months[value.getMonth()] + " " + value.getDate() + rivets.formatters.nth(value.getDate());
};

// Identify the opening and closing UNIX times
var openTime = 12;
var closeTime = 21;
var openDateTime = new Date();
var closeDateTime = new Date();
openDateTime.setHours(openTime - 1);
openDateTime.setMinutes(59);
closeDateTime.setHours(closeTime - 1);
closeDateTime.setMinutes(59);
openDateTime = Math.ceil(openDateTime.getTime() / 1000);
closeDateTime = Math.ceil(closeDateTime.getTime() / 1000);

var vmMainScreen;
// Perform data binding
document.addEventListener("DOMContentLoaded", function () {
    server.bookings.retrieve(undefined, {startingTime: openDateTime, endingTime: closeDateTime}).then(
        function (result) {
            var bookings = result.bookings;

            // Date and time grid
            var granularity = 15;
            var timescale = [];
            var hours = [];
            // Create time intervals
            for(var hour = openTime; hour < closeTime; hour++) {
                // Save all the opening hours
                if(hours.indexOf(hour) == -1)
                    hours.push(hour);

                for (var minutes = 0; minutes < 60; minutes = minutes + 15) {
                    var interval = {hour: hour, mins: minutes, tables: []};
                    // For each interval add a table
                    tables.forEach(function (table) {
                        table = Object.assign({}, table);
                        interval.tables.push(table);
                        // For each table find appropriate bookings
                        table.bookings = bookings.filter(function (booking) {
                            var date = new Date(booking.date * 1000);
                            return booking.table.id == table.id && date.getHours() == hour && date.getMinutes() == minutes;
                        });
                    });
                    timescale.push(interval);
                }
            }

            // Create the model for the view
            vmMainScreen = {
                now: new Date(),
                tables: tables,
                hours: hours,
                granularity: granularity,
                timescale: timescale
            };

            // Update the current time
            setInterval(function () {
                vmMainScreen.now = new Date();
            }, 500);

            rivets.bind(document.getElementById("mainScreen"), vmMainScreen);
        }).catch(
        function (ex) {
            console.log(ex);
            alert("Failed to load bookings:" + JSON.stringify(ex));
        });
});