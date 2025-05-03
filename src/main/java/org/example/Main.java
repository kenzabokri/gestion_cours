package org.example;

import entity.User;
import service.UserService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();

        // Create a new user
       /* User user = new User();
        user.setNom("raeeaeaea");
        user.setPrenom("eaeaeaeae");
        user.setEmail("testrae@gmail.com"); // Unique email
        // Use proper password hashing later, for now just a placeholder
        user.setPassword("rae");
        user.setAdmin(true);
        user.setMember(false);
        user.setArtist(false);

        System.out.println("Creating new user...");
        userService.create(user);
       */ System.out.println("User created successfully!");

        System.out.println("\nListing all users:");
        for(User u : userService.getAll()) {
            System.out.println("ID: " + u.getId() +
                    " | Name: " + u.getNom() + " " + u.getPrenom() +
                    " | Email: " + u.getEmail() +
                    " | Roles: " + u.getRoles());
        }



        System.out.println("\nTesting getById(1):");
        User foundUser = userService.getById(71);
        if(foundUser != null) {
            System.out.println("Found user: " + foundUser);
        } else {
            System.out.println("No user found with ID 1");
        }

        System.out.println("\nTesting update:");
        if(foundUser != null) {
            foundUser.setPrenom(foundUser.getPrenom() + " (Updated)");
            userService.update(foundUser);
            System.out.println("User updated successfully!");
        }

        System.out.println("\nTesting delete:");
        if(foundUser != null) {
            userService.delete(foundUser);
            System.out.println("User deleted successfully!");
        }

    }
}