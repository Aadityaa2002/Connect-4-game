package com.example.connect4;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloController implements Initializable {

    private static final int COLUMNS=7;
    private static final int ROWS=6;
    private static final int DIAMETER=80;
    private static final String disc1="#FF0000";
    private static final String disc2="#FFA500";

    private static String Player_1="Player One";
    private static String Player_2="Player Two";

    private boolean isPlayerOneTurn=true;

    private Disc[][] insertedDiscArray=new Disc[ROWS][COLUMNS];  //For structural changes

    @FXML
    public GridPane OverallGame;

    @FXML
    public Pane DiscPane;

    @FXML
    public Label PlayerName;

    @FXML
    public TextField Player_1Name;

    @FXML
    public TextField Player_2Name;

    @FXML
    public Button setNames;


    private boolean isAllowedToInsert=true;

    //this is to call the all the methods and all the element in the root grid pane
    public void createPlayGround(){
        Shape rectwithholes=GameStructure();
        OverallGame.add(rectwithholes,0,1);

        List<Rectangle> rectList=ClickableColumn();
        for (Rectangle rectangle:rectList) {
            OverallGame.add(rectangle,0,1);
        }
    }

    //this is to create the rectangles
    private Shape GameStructure(){
        Shape rectwithholes=new Rectangle((COLUMNS+1)*DIAMETER,(ROWS+1)*DIAMETER);

        for(int row=0;row<ROWS;row++){
            for(int col=0;col<COLUMNS;col++){
                //this is to create holes inside the rectangle
                Circle circle=new Circle();
                circle.setRadius(DIAMETER/2);
                circle.setCenterX(DIAMETER/2);
                circle.setCenterY(DIAMETER/2);
                circle.setSmooth(true);

                //this is to create holes all over the play ground
                //To create the margins between the holes we want to add 5 with diameter
                //to create the space at the left and the top we want to add Diameter / 4 with the col*Diameter+5 and row*Diamter+5
                circle.setTranslateX(col*(DIAMETER+5) + DIAMETER/4);
                circle.setTranslateY(row*(DIAMETER+5) +DIAMETER/4);

                rectwithholes=Shape.subtract(rectwithholes,circle);

            }
        }

        rectwithholes.setFill(Color.BLUE);
        return rectwithholes;
    }

    //This is to create the rectangle back side of the disc
    private List<Rectangle> ClickableColumn(){
        List<Rectangle> rectList=new ArrayList<>();
        for(int col=0;col<COLUMNS;col++){
            Rectangle rect=new Rectangle(DIAMETER,(ROWS+1)*DIAMETER);
            rect.setFill(Color.TRANSPARENT);
            rect.setTranslateX(col*(DIAMETER+5)+DIAMETER/4);

            //when the mouse is entered in any row then it changes its color
            rect.setOnMouseEntered(event->rect.setFill(Color.valueOf("#eeeeee26")));

            //when the mouse is excited from that row then it changes its color to Transparent
            rect.setOnMouseExited(event->rect.setFill(Color.TRANSPARENT));

            //this is to perform the click event on the rectangle
            final int column=col;
            rect.setOnMouseClicked(event->{
                if(isAllowedToInsert){
                    isAllowedToInsert=false;
                }
                insertDisc(new Disc(isPlayerOneTurn),column);
            });
            rectList.add(rect);
        }
        return rectList;
    }

    private void insertDisc(Disc disc,int column){

        int row=ROWS-1;
        while (row>=0){
            if(getDiscIfPresent(row,column)==null)
                break;
            row--;
        }
        if(row<0) //If it is full,we cannot insert anymore disc
            return;

        insertedDiscArray[row][column]=disc; //For structural changes:For Developers
        DiscPane.getChildren().add(disc);

        //By doing the above code the disc will be placed at the first whole only so to overcome this we use translate animation
        //This is to place disc at clicked row
        disc.setTranslateX(column*(DIAMETER+5)+DIAMETER/4);
        //this is to set the duration of the disc to be dropped from top to bottom
        int CurrentRow=row;
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
        //this is to place disc at bottom of the column
        translateTransition.setToY(row*(DIAMETER+5)+DIAMETER/4);

        //if the player completes his turn the disc color automatically changes other color for that use this code
        translateTransition.setOnFinished(event -> {

            isAllowedToInsert=true;
            if(gameEnded(CurrentRow,column)){
                gameOver();
            }
            isPlayerOneTurn = !isPlayerOneTurn;
            //this is to change the player name label
            PlayerName.setText(isPlayerOneTurn?Player_1:Player_2);
        });

        //to play the animation use the following code
        translateTransition.play();
    }

    private void gameOver() {
        String winner=isPlayerOneTurn?Player_1:Player_2;
        System.out.println("Winner is: " +winner);

        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect 4");
        alert.setHeaderText("The winner is " +winner);
        alert.setContentText("Do you want to play again?");

        ButtonType yesBtn=new ButtonType("Yes");
        ButtonType noBtn=new ButtonType("No,Exit");
        alert.getButtonTypes().setAll(yesBtn,noBtn);

        Platform.runLater(()->{
            Optional<ButtonType> btnClicked=alert.showAndWait();
            if(btnClicked.isPresent() && btnClicked.get()==yesBtn){
                resetGame();
            }else {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void resetGame() {
        DiscPane.getChildren().clear();
        for(int row=0;row<insertedDiscArray.length; row++){
            for (int col=0; col<insertedDiscArray.length;col++){
                insertedDiscArray[row][col]=null;
            }
        }
        isPlayerOneTurn=true;
        PlayerName.setText(Player_1);
        createPlayGround();
    }

    private boolean gameEnded(int row,int column){

        //Vertical Points.A small Example:Player has inserted his last disc at row=2 and column=3

        //index of each element present in column[row][column]:

        //this is for vertical points
        List<Point2D> verticalPoints= IntStream.rangeClosed(row-3,row+3)
                                        .mapToObj(r->new Point2D(r,column))
                                        .collect(Collectors.toList());

        //this is for horizontal points
        List<Point2D> horizontalPoints= IntStream.rangeClosed(column-3,column+3)
                .mapToObj(col->new Point2D(row,col))
                .collect(Collectors.toList());

        //This is for diagonal point 1
        Point2D startPoint1=new Point2D(row-3,column+3);
        List<Point2D> diagonal1Points=IntStream.rangeClosed(0,6)
                .mapToObj(i->startPoint1.add(i,-i))
                .collect(Collectors.toList());

        //this is for diagonal point 2
        Point2D startPoint2=new Point2D(row-3,column-3);
        List<Point2D> diagonal2Points=IntStream.rangeClosed(0,6)
                .mapToObj(i->startPoint2.add(i,i))
                .collect(Collectors.toList());


        boolean isEnded=checkCombinations(verticalPoints) || checkCombinations(horizontalPoints) || checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);
        return isEnded;
    }

    private boolean checkCombinations(List<Point2D> points) {
        int chain=0;
        for (Point2D point:points) {
            int rowIndexForArray=(int)point.getX();
            int columnIndexForArray=(int)point.getY();
            Disc disc=getDiscIfPresent(rowIndexForArray,columnIndexForArray);
            if (disc!=null && disc.isPlayerOneMove==isPlayerOneTurn){
                chain++;
                if(chain==4){
                    return true;
                }
            }else{
                chain=0;
            }
        }
        return false;
    }

    //This is to prevent array indexed out of bounds exception
    private Disc getDiscIfPresent(int row,int column){
        if(row>=ROWS || row<0 || column>=COLUMNS || column<0)
            return null;
        return insertedDiscArray[row][column];

    }

    private static class Disc extends Circle{

        private boolean isPlayerOneMove;

        public Disc(boolean isPlayerOneMove){

            this.isPlayerOneMove=isPlayerOneMove;
            setRadius(DIAMETER/2);
            setFill(isPlayerOneMove?Color.valueOf(disc1):Color.valueOf(disc2));
            setCenterX(DIAMETER/2);
            setCenterY(DIAMETER/2);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setNames.setOnAction(event -> {
            String input1=Player_1Name.getText();
            String input2=Player_2Name.getText();
            Player_1=input1 + " 's";
            Player_2=input2 + " 's";
            if(input1.isEmpty())
                Player_1="Player One's";
            if(input2.isEmpty())
                Player_2="Player Two's";
            PlayerName.setText(isPlayerOneTurn?Player_1:Player_2);
        });
    }
}