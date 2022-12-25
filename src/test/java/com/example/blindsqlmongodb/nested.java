package com.example.blindsqlmongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.Set;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class nested {

  @Autowired MongoTemplate mongoTemplate;

  @Test
  void connectTest() {

    if (!mongoTemplate.getCollectionNames().contains("users")) {
      mongoTemplate.createCollection("users");
    }
    Set<String> collections = mongoTemplate.getCollectionNames();
    collections.forEach(System.out::println);
  }

  @Test
  void test() {
    // Connect to the MongoDB server
    MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/uoc");
    MongoClient mongoClient = new MongoClient(uri);

    // Get the database
    MongoDatabase database = mongoClient.getDatabase("uoc");

    MongoCollection<Document> result = database.getCollection("users");

    // Perform some operations on the database
    MongoCursor<Document> cursor = result.find().iterator();

    try {
      // Iterate over the cursor
      while (cursor.hasNext()) {
        // Get the next document
        Document doc = cursor.next();

        // Print out some information from the document
        System.out.println("_id: " + doc.get("_id"));
        System.out.println("email: " + doc.get("email"));
        System.out.println("name: " + doc.get("name"));
        System.out.println("password: " + doc.get("password"));
        System.out.println("accountId: " + doc.get("accountId"));
      }
    } finally {
      // Close the cursor to release resources
      cursor.close();
    }

    // Close the connection
    mongoClient.close();
  }

  @Test
  void simulation() {
    // Arrange
    String email = "juan@gmail.com";
    String password = "123123";
    String injection= "12345' || '1'=='1";

    // Connect to the MongoDB server
    MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/uoc");
    MongoClient mongoClient = new MongoClient(uri);

    // Get the database
    MongoDatabase database = mongoClient.getDatabase("uoc");
    MongoCollection<Document> collection = database.getCollection("users");

    // run a command
    // Create the filter document with the $where operator and JavaScript function
    String functionSQL = String.format("function() { return (this.email == '%s' && this.password == '%s') }",email,password) ;
    Document filter =
        new Document(
            "$where",
            functionSQL);


    // Find the documents that match the filter
    MongoCursor<Document> cursor = collection.find(filter).iterator();
    try {
      // Iterate over the cursor and print the documents
      while (cursor.hasNext()) {
        Document doc = cursor.next();
        System.out.println(doc.toJson());
      }
    } finally {
      // Close the cursor to release resources
      cursor.close();
    }
  }
//
//  @Test
//  void solution(){
//    // Connect to the database and get a handle to the collection
//    MongoClient mongoClient = new MongoClient("localhost", 27017);
//    MongoDatabase database = mongoClient.getDatabase("mydb");
//    MongoCollection<Document> collection = database.getCollection("users");
//
//// Get the username and password from the login form
//    String username = request.getParameter("username");
//    String password = request.getParameter("password");
//
//// Use a parameterized query to find the user with the given username and password
//    Document filter = new Document("username", username).append("password", password);
//    Document user = collection.find(filter).first();
//
//// If the user was found, log them in
//    if (user != null) {
//      session.setAttribute("username", username);
//      response.sendRedirect("/welcome");
//    } else {
//      // Otherwise, show an error message
//      response.sendRedirect("/login?error=1");
//    }
//  }

  @Test
  void x(){

    String email = "juan@gmail.com";
    String password = "123456' || '1'=='1";

    System.out.println(String.format("function() { return (this.email == '%s' && this.password == '%s') }",email,password));
    System.out.println("function() { return (this.email == 'juan@gmail.com' && this.password == '123456' || 1==1) }");
    }
}
