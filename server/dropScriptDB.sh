#!/bin/bash
# Purpose: Drop database tables.
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
	sqlite3 restaurantdatabase.db "DROP TABLE IF EXISTS RestaurantTable"
	sqlite3 restaurantdatabase.db "DROP TABLE IF EXISTS Booking"
	sqlite3 restaurantdatabase.db "DROP TABLE IF EXISTS Meal"
	sqlite3 restaurantdatabase.db "DROP TABLE IF EXISTS RestaurantOrder"
fi
