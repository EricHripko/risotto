// Viewmodel for the report screen
var vmReportScreen = {};
vmReportScreen.weeks = [];

function month(index) {
    return ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"][index];
}

function dow(dayOfWeek) {
    return (dayOfWeek + 6) % 7;
}

/**
 * Create a report for a given month.
 * @param date Date.
 */
function reportMonth(date) {
    var i;
    vmReportScreen.date = date;
    vmReportScreen.month = month(date.getMonth());
    var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
    var firstTime = Math.ceil(firstDay.getTime() / 1000);
    var lastTime = Math.ceil(lastDay.getTime() / 1000);

    server.bookings.retrieve(undefined, {startingTime: firstTime, endingTime: lastTime}).then(function (result) {
        // Load all the bookings
        var bookings = result.bookings;
        // Load all the orders
        var promises = bookings.map(function (booking) {
            return server.orders.retrieve(null, {id: booking.referenceNumber});
        });
        Promise.all(promises).then(function (orders) {
            for (var i = 0; i < orders.length; i++) {
                bookings[i].order = orders[i];
                orders[i].total = orders[i].meals.reduce(function (total, meal) {
                    return total + meal.price;
                }, 0);
            }

            // Create the first week
            var firstDow = dow(firstDay.getDay());
            var week = [];
            vmReportScreen.weeks = [week];
            vmReportScreen.total = 0;
            for (i = 0; i < firstDow; i++) {
                week.push({
                    day: "",
                    total: "",
                    empty: true
                });
            }

            // Create following weeks
            for (i = 1; i <= lastDay.getDate(); i++) {
                // Identify unix time for start and end of the day
                var startTime = Math.ceil(new Date(lastDay.getFullYear(), lastDay.getMonth(), i, 0, 1).getTime() / 1000);
                var endTime = Math.ceil(new Date(lastDay.getFullYear(), lastDay.getMonth(), i, 23, 59).getTime() / 1000);
                // Calculate total takings of the day
                var total = bookings
                    .filter(function (booking) {
                        return booking.date > startTime && booking.date < endTime
                    })
                    .reduce(function (total, booking) {
                        return total + booking.order.total
                    }, 0);
                vmReportScreen.total += total;

                // Record day and total taking
                week.push({
                    day: i,
                    total: total,
                    empty: total == 0
                });

                // Switch to the next week upon reaching Sunday
                if (dow(new Date(date.getFullYear(), date.getMonth(), i).getDay()) == 6) {
                    week = [];
                    vmReportScreen.weeks.push(week);
                }
            }

            // Create the last week
            var lastDow = dow(lastDay.getDay());
            for (i = lastDow; i < 6; i++) {
                week.push({
                    day: "",
                    total: "",
                    empty: true
                });
            }
        });
    });
}

/**
 * Open a report screen.
 * @param e Event.
 */
function reportOpen(e) {
    document.getElementById("reportScreen").classList.add("show");
    reportMonth(new Date());
}

/**
 * Update the date on the report screen to the previous month.
 * @param e Event.
 */
function reportBefore(e) {
    vmReportScreen.date.setMonth(vmReportScreen.date.getMonth() - 1);
    reportMonth(vmReportScreen.date);
}

/**
 * Update the date on the report screen to the next month.
 * @param e Event.
 */
function reportAfter(e) {
    vmReportScreen.date.setMonth(vmReportScreen.date.getMonth() + 1);
    reportMonth(vmReportScreen.date);
}

/**
 * Close a report screen.
 * @param e Event.
 */
function reportClose(e) {
    document.getElementById("reportScreen").classList.remove("show");
}

// Perform data binding
document.addEventListener("DOMContentLoaded", function() {
    // Setup view model
    rivets.bind(document.getElementById("reportScreen"), vmReportScreen);
});