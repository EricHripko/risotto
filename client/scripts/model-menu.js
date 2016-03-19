var vmMenu = {};
vmMenu.table = {
    id: 1,
    description: "#1",
    size: 4
};
vmMenu.order = [];
vmMenu.orderTotal = 0;

// Formatters
rivets.formatters.gbp = function (value) {
    return "£" + Math.floor(value / 100) + "." + rivets.formatters.padZeroes(value % 100);
};
// Perform data binding
document.addEventListener("DOMContentLoaded", function() {
    // Load menu
    server.menu.retrieve(undefined, {}).then(
        function (result) {
            // Setup view model
            vmMenu.menu = [];
            for(var category in result)
                vmMenu.menu.push({name: category, meals: result[category]});

            rivets.bind(document.getElementById("menuScreen"), vmMenu);
        }).catch(
        function (ex) {
            ex.errorMessage = ex.errorMessage || "An error occured";
            alert("Failed to load bookings: " + ex.errorMessage);
        });
});

/**
 * Retrieve the meal.
 * @param target Element.
 * @returns Meal Meal object bound to this element.
 */
function getMeal(target) {
    // Identify the category
    var category = target.parentNode.parentNode;

    // Identify the meal
    var mealIndex = Array.prototype.indexOf.call(target.parentNode.children, target);
    var catIndex = Array.prototype.indexOf.call(category.parentNode.children, category);
    return vmMenu.menu[catIndex].meals[mealIndex];
}

/**
 * Order the meal.
 * @param e Event.
 */
function orderMeal(e) {
    // Identify the meal
    var target = e.currentTarget;
    var meal = getMeal(target);

    // Add the meal to the order
    if(vmMenu.order.indexOf(meal) == -1) {
        meal.count = 1;
        vmMenu.order.push(meal);
    }
    else
        meal.count++;

    // Recalculate order total
    recalculateTotal();
}

/**
 * Increase the number of meal ordered.
 * @param e Event.
 */
function increaseCount(e) {
    // Identify the meal
    var target = e.currentTarget;
    var mealIndex = Array.prototype.indexOf.call(target.parentNode.parentNode.parentNode.children, target.parentNode.parentNode);
    var meal = vmMenu.order[mealIndex];

    // Recalculate order total
    meal.count++;
    recalculateTotal();
}

/**
 * Decrease the number of meal ordered.
 * @param e Event.
 */
function decreaseCount(e) {
    // Identify the meal
    var target = e.currentTarget;
    var mealIndex = Array.prototype.indexOf.call(target.parentNode.parentNode.parentNode.children, target.parentNode.parentNode);
    var meal = vmMenu.order[mealIndex];

    // Recalculate order total
    meal.count--;
    if(meal.count == 0)
        vmMenu.order.splice(vmMenu.order.indexOf(meal), 1);
    recalculateTotal();
}

/**
 * Recalculate order total.
 */
function recalculateTotal() {
    var result = 0;
    vmMenu.order.forEach(function(meal) { result += meal.price * meal.count; });
    vmMenu.orderTotal = result;
}

function showOrderScreen(event) {
    // Show order screen
    document.getElementById("menuScreen").classList.add("show");
    event.stopPropagation();

    // Identify the table
    var target = event.currentTarget;
    var tableIndex = Array.prototype.indexOf.call(target.parentNode.parentNode.children, target.parentNode);
    var table = vmMainScreen.tables[tableIndex];
    // Identify the booking
    var id = target.getAttribute("id");
    var booking = vmMainScreen.bookings.filter(function (booking) { return booking.referenceNumber == id; })[0];

    vmMenu.table = table;
    vmMenu.booking = booking;
}

function discardOrder() {
    document.getElementById("menuScreen").classList.remove("show");
}

function confirmOrder() {
    // Identify the items ordered
    var order = [];
    vmMenu.order.forEach(function (meal) {
        for(var i = 0; i < meal.count; i++)
            order.push({booking: vmMenu.booking.referenceNumber, meal: meal.id});
    });

    server.orders.create({orders: order});
}