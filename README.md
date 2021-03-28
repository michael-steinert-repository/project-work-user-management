## Setup

### Application Server:
* The compiled Microservice *User-Management* already contains the Application Server and can be executed directly inside a Container that has Java version 11 installed.

* The running Microservices is available at the URL http://localhost:8081.

### Database:

* The Microservice *User-Management* needs its own Database. Therefore an Instance of the PostgreSQL Database *user_management* is set up on a Container.

### API of Microservices

http://localhost:8081/user-management/

| HTTP-Method | URL | Result |
| --- | --- | --- |
| POST | /register-a-new-user | Registers a new User in the System |
| POST | /authenticate-an-existing-user | Authenticates a existing User in the System and returns an Authentication-Token |
| POST | /update-an-existing-user | Updates an existing User in the System |
| POST | /update-all-existing-users | Updates all existing Users in the System |
| GET | /find-user-by-username/{username} | Returns a User with the corresponding Username |
| GET | /find-all-users | Returns all existing Users |
___

## Einrichtung
### Anwendungsserver:

* Der kompilierte Microservice *User-Management* enthält bereist den Anwendungsserver und kann direkt innerhalb eines Containers ausgeführt werden, welcher die Java-Version 11 installiert hat.

* Der laufende Microservices ist erreichbar unter der URL http://localhost:8081.

### Datenbank:

* Der Microservice *User-Management* benötigt seine eigene Datenbank. Dafür wird auf einem Container eine Instanz der Datenbank PostgreSQL *user_management* eingerichtet.

### Schnittstelle des Microservices

http://localhost:8081/user-management/

| HTTP-Methode | URL | Ergebnis |
| --- | --- | --- |
| POST | /register | Registriert einen neuen Nutzer in dem System |
| POST | /authenticate | Authentifiziert einen existierenden Nutzer in dem System und liefert einen Authentifikations-Token zurück |
| GET | /fetch-user-by-username/{username} | Liefert einen Nutzer mit dem entsprechenden Usernamen zurück |
| GET | /fetch-all-users | Liefert alle existierende Nutzer |
