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
        id: 5,
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

var vmMainScreen = {};

function refreshBooking() {
    server.bookings.retrieve(undefined, {startingTime: openDateTime, endingTime: closeDateTime}).then(
        function (result) {
            var bookings = result.bookings;

            // Date and time grid
            var granularity = 15;
            var timescale = [];
            var hours = [];
            // Create time intervals
            for (var hour = openTime; hour < closeTime; hour++) {
                // Save all the opening hours
                if (hours.indexOf(hour) == -1)
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
                            return booking.table == table.id && date.getHours() == hour && date.getMinutes() == minutes;
                        });
                    });
                    timescale.push(interval);
                }
            }

            // Create the model for the view
            vmMainScreen.now = new Date();
            vmMainScreen.tables = tables;
            vmMainScreen.hours = hours;
            vmMainScreen.granularity = granularity;
            vmMainScreen.timescale = timescale;
        }).catch(
        function (ex) {
            console.log(ex);
            alert("Failed to load bookings:" + JSON.stringify(ex));
        });
}

function createBooking(e)
{
    // Identify the grid slots
    var slot = e.target;
    var row = slot.parentNode;

    // Identify the time and date of a booking
    vmMainScreen.activeSlot.hour = row.getAttribute("hours");
    vmMainScreen.activeSlot.mins = row.getAttribute("mins");
    vmMainScreen.activeSlot.table = slot.getAttribute("table");

    // Display booking creation form
    var bounds = slot.getBoundingClientRect();
    var form = document.getElementById("bookingCreation");
    var fBounds = form.getBoundingClientRect();
    if(bounds.top + fBounds.height < window.innerHeight)
        form.style.top = bounds.top + "px";
    else
        form.style.top = (window.innerHeight - fBounds.height) + "px";
    form.style.left = bounds.left + "px";
    form.style.width = bounds.width + "px";
    form.classList.remove("hidden");
}

function cancelBooking(e)
{
    // Hide previously opened form
    var form = document.getElementById("bookingCreation");
    form.classList.add("hidden");
    // Reset the input
    form.reset();
}

function confirmBooking(e) {
    // Retrieve the booking information
    var name = document.getElementById("name").value;
    var phone = document.getElementById("phone").value;
    var party = document.getElementById("party").value;
    var email = document.getElementById("email").value;
    // Create a booking object and send the information off
    party = parseInt(party);
    var today = new Date(openDateTime * 1000);
    var date = new Date(today.getFullYear(), today.getMonth(), today.getDate(), vmMainScreen.activeSlot.hour, vmMainScreen.activeSlot.mins, 0, 0);

    var booking = new Booking(name, phone, email, party, date, vmMainScreen.activeSlot.table);
    BookingService.make(booking).then(
        function (val) {
            alert("Booking created, customer reference is " + val.referenceNumber);
            refreshBooking();
            cancelBooking();
        }).catch(
        function (ex) {
            alert(ex.errorMessage);
            cancelBooking();
        });
}

// Perform data binding
document.addEventListener("DOMContentLoaded", function() {
    // Setup view model
    refreshBooking();
    vmMainScreen.activeSlot = {
        hour: 0,
        mins: 0
    };
    rivets.bind(document.getElementById("mainScreen"), vmMainScreen);

    // Periodically refresh the model
    setInterval(refreshBooking, 1000);
    setInterval(function () {
        vmMainScreen.now = new Date();
    }, 500);
});