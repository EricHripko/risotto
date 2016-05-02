<div align="center">
    <img src="client/Icon.jpg">
</div>
-----------------------------

Risotto is a restaurant management system whose aim is to simplify the job of
the employees.
It provides a client, a server and a database that can be used to take bookings,
orders, manage menu and tables of the restaurant, take payments and analyze
the revenues.
The server can be executed everywhere thanks to its Java implementation and the
client can be used by everyone easily thanks to its beutiful UI.

## Installation
To clone the repository from GitLab type:
```
git clone git@gitlab.com:comp2541/bison.git
```

To create a .jar file of the server type from `bison/server`:
```
ant distribute
```

## Execution
To execute the server run from `bison/server/bin`:
```
java -jar RestaurantServer.jar
```