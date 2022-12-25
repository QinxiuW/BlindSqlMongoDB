package com.example.blindsqlmongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {


  @GetMapping("/index")
  public String main(Model model){

    model.addAttribute("loginForm", new LoginParamDto());
    return "login.html";
  }


  @PostMapping("/login")
  public String login(LoginParamDto paramDto, Model model){
    // Connect to the MongoDB server
    MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/uoc");
    MongoClient mongoClient = new MongoClient(uri);

    // Get the database
    MongoDatabase database = mongoClient.getDatabase("uoc");
    MongoCollection<Document> collection = database.getCollection("users");

    // run a command
    // Create the filter document with the $where operator and JavaScript function
    String functionSQL = String.format("function() { return (this.email == '%s' && this.password == '%s') }",paramDto.getEmail(),paramDto.getPassword()) ;
    Document filter =
        new Document(
            "$where",
            functionSQL);


    // Find the documents that match the filter
    MongoCursor<Document> cursor = collection.find(filter).iterator();
    try {

      if (cursor.hasNext()) {
        return "welcome";
      }
    } finally {
      // Close the cursor to release resources
      cursor.close();
    }
    return  "error";
  }
}
