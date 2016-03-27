// Build the viewmodel
var vmPay = {};
vmPay.total = 0;

// Show months
var i;
vmPay.months = [];
for(i = 1; i <= 12; i++)
    vmPay.months.push(i);

// Show years
vmPay.years = [];
var currentYear = new Date().getFullYear();
for(i = currentYear; i < currentYear + 20; i++)
    vmPay.years.push(i);

// Databind the viewmodel
document.addEventListener("DOMContentLoaded", function() {
    rivets.bind(document.getElementById("amount"), vmMenu);
    rivets.bind(document.getElementById("expiryDate"), vmPay);
});

function displayPayment() {
    document.getElementById("payScreen").classList.add("show");
}

function discardPayment() {
    document.getElementById("payScreen").classList.remove("show");
}

function confirmPayment() {
    document.getElementById("payScreen").classList.remove("show");
}