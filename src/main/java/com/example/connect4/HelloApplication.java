package com.example.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    //This is to Create the method for controller
    private HelloController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game.fxml"));
        GridPane rootGridPane = fxmlLoader.load();

        //this is to connect the controller
        controller=fxmlLoader.getController();

        //this is to create the play ground, and it is call from the controller class
        controller.createPlayGround();

        //this is to call the create menu
        MenuBar menuBar=createMenu();

        //this is to adjust and bind the width property of the menu bar
        menuBar.prefWidthProperty().bind(stage.widthProperty());

        //this is to insert the menu to the menu pane
        Pane menuPane=(Pane)rootGridPane.getChildren().get(0);

        //this is to menu bar to the menu pane
        menuPane.getChildren().add(menuBar);

        Scene scene=new Scene(rootGridPane);

        stage.setScene(scene);
        stage.setTitle("Connect 4 Game");
        stage.show();
    }

    //this method is to create the menu inside the menu bar
    private MenuBar createMenu(){

        //File Menu
        Menu FileMenu=new Menu("File");

        //this is to create menu items inside the file menu and perform some event handler inside the menu items
        //In both the cases new game and reset game we are going to reset the game so create and call the method resetGame inside the both
        MenuItem newGame=new MenuItem("New Game");
        //Event handler
        newGame.setOnAction(event -> controller.resetGame());

        MenuItem resetGame=new MenuItem("Reset Game");
        //Event handler
        resetGame.setOnAction(event -> controller.resetGame());

        //this is to separate a line between the menu items
        SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();

        //In the exit game menu the game wants to exit
        //For that create and call the method exitGame inside the event handler of exitGame
        MenuItem exitGame=new MenuItem("Exit Game");
        //Event handler
        exitGame.setOnAction(event -> exitGame());

        //this is to add all the menu items to the file menu
        FileMenu.getItems().addAll(newGame,resetGame,separatorMenuItem,exitGame);


        //Help Menu
        Menu helpMenu=new Menu("Help");

        //this is to create menu items inside the help menu and perform some event handler inside the menu items
        MenuItem AboutApp=new MenuItem("About Connect 4");
        //This is to perform event handler and call the method about app inside it
        AboutApp.setOnAction(event -> aboutApp());

        //This is to separate a line between the menu items
        SeparatorMenuItem separatorMenuItem1=new SeparatorMenuItem();
        MenuItem AboutMe=new MenuItem("About Me");
        //this is to perform event handler and call the method about me inside it
        AboutMe.setOnAction(event -> aboutMe());

        //this is to add all the menu items to the help menu
        helpMenu.getItems().addAll(AboutApp,separatorMenuItem1,AboutMe);

        //this is to add all the menu to the menu bar
        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().addAll(FileMenu,helpMenu);

        //this is to return the menu bar and change the method type void to menu bar
        return menuBar;

    }

    //this method is to describe about the developer (i.e) me
    private void aboutMe() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the Developer");
        alert.setHeaderText("Aadityaa B S");
        alert.setContentText("I love to play around with the code and create games. " +
                "Connect 4 is one of the them. " +
                "In free time.I like to spend " +
                "time with my friends,neighbours and with my family members...");

        alert.show();
    }

    //this method is used to describe about the app
    private void aboutApp() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four");
        alert.setHeaderText("Instructions to play");
        alert.setContentText("Connect Four is a two-player connection game in which the " +
                "players first choose a color and then take turns dropping colored discs " +
                "from the top into a seven-column, six-row vertically suspended grid. " +
                        "The pieces fall straight down, occupying the next available space within the column " +
                        "The objective of the game is to be the first to form a horizontal,vertical,diagonal line " +
                        "of four of one's own discs.Connect four is a solved game. " +
                        "The first player can always win by playing the right moves");

        alert.show();
    }

    //this method is to shut Down the current application and to shut Down the java virtual machine
    private void exitGame() {
        Platform.exit();//to shut down the current application
        System.exit(0);//to shut down the java virtual machine
    }

    private void resetGame() {
    }

    public static void main(String[] args) {
        launch();
    }
}