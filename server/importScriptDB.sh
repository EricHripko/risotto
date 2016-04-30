#!/bin/bash
# Drop tables from the database
sqlite3 restaurantdatabase.db ".import tables.txt RestaurantTable"
sqlite3 restaurantdatabase.db ".import meals.txt Meal"
sqlite3 restaurantdatabase.db ".import bookings.txt Booking"
