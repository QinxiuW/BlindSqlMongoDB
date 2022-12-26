package com.example.blindsqlmongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

  @GetMapping("/index")
  public String main(Model model) {

    model.addAttribute("loginForm", new LoginParamDto());
    return "login.html";
  }

  @PostMapping("/login")
  public String login(LoginParamDto paramDto, Model model) {
    // Connect to the MongoDB server
    MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/uoc");
    MongoClient mongoClient = new MongoClient(uri);

    // Get the database
    MongoDatabase database = mongoClient.getDatabase("uoc");
    MongoCollection<Document> collection = database.getCollection("users");

    // run a command
    Document filter = new Document()
            .append("email", paramDto.getEmail())
            .append("password", paramDto.getPassword());
    Document user = collection.find(filter).first();

    if (user != null && !user.isEmpty()) {
      return "welcome";
    }
    return "error";
  }
}
