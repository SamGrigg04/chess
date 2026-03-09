"# My Notes ;)"
 - [ ] Wrap calls to get a connection with ```try-with-resources``` blocks
 - [ ] On startup, code should create databases and tables based on config values in ```db.properties```. There is code in ```DatabaseManager``` that you can use
 - [ ] When a password is provided by a user, hash it before storing it (register and login)
``` java
void storeUserPassword(String username, String clearTextPassword) {
   String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());

   // write the hashed password in database along with the user's other information
   writeHashedPasswordToDatabase(username, hashedPassword);
}
```
 - [ ] When a user logs in, hash the password and compare it to the old one they provided
``` java
   boolean verifyUser(String username, String providedClearTextPassword) {
   // read the previously hashed password from the database
   var hashedPassword = readHashedPasswordFromDatabase(username);

   return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
   }
```
### Storing a chess game in MySQL:
 1. Select the game’s state (JSON string) from the database
 2. Deserialize the JSON string to a ChessGame Java object
 3. Update the state of the ChessGame object
 4. Re-serialize the Chess game to a JSON string
 5. Update the game’s JSON string in the database

``` SQL
String createUserTable = """
CREATE TABLE IF NOT EXISTS user (
username VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL,
PRIMARY KEY (username)
)""";
```

