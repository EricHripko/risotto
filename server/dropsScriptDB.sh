#!/bin/bash
# Drop tables from the database
sqlite3 restaurantdatabase.db "DROP TABLE IF EXISTS RestaurantTable"
sqlite3 restaurantdatabase.db "DROP TABLE IF EXISTS Booking"
sqlite3 restaurantdatabase.db "DROP TABLE IF EXISTS Meal"
sqlite3 restaurantdatabase.db "DROP TABLE IF EXISTS RestaurantOrder"
