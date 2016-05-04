#!/bin/bash
# Purpose: Import data into database's tables.
# Author: Ilyass Taouil.
# Note : The script is run by Java's PB.
# Last updated on : 04-May-2016
# -----------------------------------------------

# File declaration
FILE = restaurantdatabase.db

# Execute
if [ -e "$FILE" ]
then
	# Drop tables from the database
	sqlite3 restaurantdatabase.db ".import tables.txt RestaurantTable"
	sqlite3 restaurantdatabase.db ".import meals.txt Meal"
	sqlite3 restaurantdatabase.db ".import bookings.txt Booking"
fi
