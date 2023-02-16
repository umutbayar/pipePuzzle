//Ahmet Arda Nalbant 150121004
//Umut Bayar 150120043
//The purpose of the game is to connect the pipes to the end to provide that the ball move.
//This class has the property of the game (photos of the tiles, main menu, font etc.) and other necessary algorithms.
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Project extends Application {
	//Declares variables
	public Game game;
	public int level;
	public int numberOfMoves;
	public String[] data;
	public Rectangle[][] boardGrid;
	public GridPane panel;
    public Label gameStatus;
    public Label countMove;
    public Circle ball;
    public Scene welcomeScene;
    public Scene gameScene;
    public PathTransition animation;
    private final ObjectProperty<Point2D> selected = new SimpleObjectProperty<>();

    //Organizes the main menu
    public void start(Stage window) throws Exception{
    	
    	animation = new PathTransition();
        game = new Game();
        boardGrid = new Rectangle[4][4];
        data = new String[]{"level1.txt","level2.txt","level3.txt","level4.txt","level5.txt","level6.txt"};
        loadGame();
        panel = new GridPane();
        panel.setPadding(new Insets(46,46,46,46));
        panel.setVgap(0);
        panel.setHgap(0);
        panel.setBackground(new Background(new BackgroundFill(Color.SILVER, new CornerRadii(0), Insets.EMPTY)));
        for(int i=0;i<16;i++){
            boardGrid[i %4 ][i / 4] = new Rectangle(127,127,Color.WHITE);
            GridPane.setConstraints(boardGrid[i%4][i/4],i%4,i/4);
            panel.add(boardGrid[i%4][i/4],i%4,i/4);
        }
        //Creates the ball
        ball = new Circle(10,new ImagePattern(new Image(new File("ball.png").toURI().toString())));
        ball.setCenterX(105);
        ball.setCenterY(109.5);
        Button nextLevelButton = new Button("Start");
        nextLevelButton.setOnAction(e -> {
            if(game.gameEnded()){
            	
            	//Organizes the end of the level
                if(game.levelUnlocked<5) {
                    PathTransition p = playAnimation();
                    p.setOnFinished(t -> {
                        numberOfMoves = 0;
                        level++;
                        if(level>5){
                        	Image panel ;
                        	panel=new Image(new File("b.png").toURI().toString());
                        }
                        nextLevel(); 
                        gameStatus.setText("Level "+Integer.toString(level+1));
                    });
                }else{
                	
                    PathTransition p = playAnimation();
                    p.setOnFinished(t -> {
                        
                        numberOfMoves = 0; 
                        level++;
                        if(level>5){
                        	panel.getChildren().clear();
                        	ImageView imageView = new ImageView(new Image(new File("b.png").toURI().toString()));
                            imageView.setFitHeight(500);
                            imageView.setFitWidth(500);
                            panel.add(imageView, 0, 0);
                        }
                        if(level<6) {
                        game.generateMapData(data[level]);
                        generateGridPane();      
                        gameStatus.setText("Level "+Integer.toString(level+1));
                        }
                        });
                }
            }else{
                gameStatus.setText("You LOSER! Ýs it too hard for you?  :(");     
                e.consume();
            }
        });
        Button mainMenuButton = new Button("Return to Main Menu");
        mainMenuButton.setOnAction(e -> window.setScene(welcomeScene));
        gameStatus = new Label("Try to solve level-"+Integer.toString(game.levelUnlocked+1));
        countMove = new Label();
        countMove.setText("Number of Moves: "+numberOfMoves);
        HBox gameStatusLayout = new HBox(10);
        VBox gameLayout = new VBox(10);
        Pane gameView = new Pane();
        gameStatusLayout.getChildren().addAll(nextLevelButton,gameStatus,countMove,mainMenuButton);
        gameView.getChildren().addAll(panel,ball);
        gameLayout.getChildren().addAll(gameView,gameStatusLayout);
        gameScene = new Scene(gameLayout,600,600);
        Label title = new Label("\n                      WELCOME TO\n                  PIPEPUZZLE GAME ");
        title.setFont(new Font("Times New Roman",40));
        VBox levelsBox = new VBox(10);
        levelsBox.setMaxWidth(100);
        levelsBox.setMaxHeight(350);
        
        //Creates buttons
        Button[] levelButtons = new Button[6];
        levelButtons[0]=new Button("    Level 1    ");
        levelButtons[1]=new Button("    Locked    ");
        levelButtons[2]=new Button("    Locked    ");
        levelButtons[3]=new Button("    Locked    ");
        levelButtons[4]=new Button("    Locked    ");
        levelButtons[5]=new Button("    Locked    ");
        levelButtons[0].setOnAction(e -> {
            level = 0; 
            numberOfMoves = 0;
            countMove.setText("Moves: "+numberOfMoves);
            game.generateMapData(data[0]);
            generateGridPane();
            gameStatus.setText("Level 1");
            window.setScene(gameScene);
        });
    
        levelButtons[1].setOnAction(e -> {
            if(game.levelUnlocked>0) {
                level = 1;
                numberOfMoves = 0;
                countMove.setText("Moves: "+numberOfMoves);
                game.generateMapData(data[1]);
                generateGridPane();
                gameStatus.setText("Level 2");
                window.setScene(gameScene);
            }
        });
     
        levelButtons[2].setOnAction(e -> {
            if(game.levelUnlocked>1) {
                level = 2;
                numberOfMoves = 0;
                countMove.setText("Moves: "+numberOfMoves);
                game.generateMapData(data[2]); 
                generateGridPane();  
                gameStatus.setText("Level 3");
                window.setScene(gameScene);
                levelButtons[0].setText("Level 1(Completed)");
                levelButtons[1].setText("Level 2(Completed)");
                levelButtons[3].setText("Level 4(Locked)");
                levelButtons[4].setText("Level 5(Locked)");
                levelButtons[5].setText("Level 6(Locked)");

            }
        });
        
        levelButtons[3].setOnAction(e -> {
            if(game.levelUnlocked>2) {
                level = 3;
                numberOfMoves = 0;
                countMove.setText("Moves: "+numberOfMoves);
                game.generateMapData(data[3]);
                generateGridPane();
                gameStatus.setText("Level 4");
                window.setScene(gameScene);
                levelButtons[0].setText("Level 1(Completed)");
                levelButtons[1].setText("Level 2(Completed)");
                levelButtons[2].setText("Level 3(Completed)");
                levelButtons[4].setText("Level 5(locked)");
                levelButtons[5].setText("Level 6(Locked)");

            }
        });
        
        levelButtons[4].setOnAction(e -> {
            if(game.levelUnlocked>3) {
                level = 4;
                numberOfMoves = 0; 
                countMove.setText("Moves: "+numberOfMoves);
                game.generateMapData(data[4]);
                generateGridPane();
                levelButtons[0].setText("Level 1(Completed)");
                levelButtons[1].setText("Level 2(Completed)");
                levelButtons[2].setText("Level 3(Completed)");
                levelButtons[3].setText("Level 4(Completed)");
                levelButtons[5].setText("Level 6(Locked)");

                gameStatus.setText("Level 5");
                window.setScene(gameScene);
            }
        });
        
        levelButtons[5].setOnAction(e -> {
            if(game.levelUnlocked>4) {
                level = 5;
                numberOfMoves = 0; 
                countMove.setText("Moves: "+numberOfMoves);
                game.generateMapData(data[5]);
                generateGridPane();
                levelButtons[0].setText("Level 1(Completed)");
                levelButtons[1].setText("Level 2(Completed)");
                levelButtons[2].setText("Level 3(Completed)");
                levelButtons[3].setText("Level 4(Completed)");
                levelButtons[4].setText("Level 5(Completed)");

                gameStatus.setText("Level 6");
                window.setScene(gameScene);
            }
        });
        
        levelsBox.getChildren().addAll(levelButtons); 
        BorderPane welcomePane = new BorderPane();
        Image backGround ;
        backGround=new Image(new File("a.png").toURI().toString());
        welcomePane.setBackground(new Background(new BackgroundFill(new ImagePattern(backGround), new CornerRadii(0), Insets.EMPTY)));
        welcomePane.setTop(title);
        welcomePane.setCenter(levelsBox);   
        welcomeScene = new Scene(welcomePane,600,600);
        window.setScene(welcomeScene);
        window.setTitle("Pipe Puzzle");
        window.setOnCloseRequest(e -> saveGame());
        window.show();
    }
    //Enters the next level
    private void nextLevel(){
    	animation.stop();
        
    	animation.jumpTo(Duration.ZERO);
        game.levelUnlocked++;
        game.generateMapData(data[level]);
        generateGridPane();
    }
    //Uploads photos to gridpane 
    private void generateGridPane(){
        for(int i=0;i<16;i++){
            String type = game.board[i % 4][i / 4].type;
            String property = game.board[i % 4][i / 4].property;
            Image temp;
            if(type.equals("Pipe")){
                if(property.equals("Vertical")) {
                    temp=new Image(new File("Pipe_Vertical.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                    setMove(boardGrid[i%4][i/4]);
                }
                else if(property.equals("Horizontal")){
                    temp=new Image(new File("Pipe_Horizontal.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                    setMove(boardGrid[i%4][i/4]);
                }
                else if(property.equals("00")){
                    temp=new Image(new File("CurvedPipe_00.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                    setMove(boardGrid[i%4][i/4]);
                }
                else if(property.equals("01")){
                    temp=new Image(new File("CurvedPipe_01.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                    setMove(boardGrid[i%4][i/4]);
                }
                else if(property.equals("10")){
                    temp=new Image(new File("CurvedPipe_10.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                    setMove(boardGrid[i%4][i/4]);
                }
                else{
                    temp=new Image(new File("CurvedPipe_11.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                    setMove(boardGrid[i%4][i/4]);
                }
            }
            else if(type.equals("Empty")){
                if(property.equals("Free") || (property.equals("free"))){
                    temp=new Image(new File("Empty_Free.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                    setPlace(boardGrid[i%4][i/4]);
                }
                else{
                    temp=new Image(new File("Empty.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                    setMove(boardGrid[i%4][i/4]);
                }
            }
            else if(type.equals("PipeStatic")){
                if(property.equals("Horizontal")){
                    temp=new Image(new File("PipeStatic_Horizontal.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                }
                else if(property.equals("00")){
                    temp=new Image(new File("PipeStatic_00.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                }
                else if(property.equals("01")){
                    temp=new Image(new File("PipeStatic_01.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                }
                else if(property.equals("10")){
                    temp=new Image(new File("PipeStatic_10.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                }
                else if(property.equals("11")){
                    temp=new Image(new File("PipeStatic_11.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                }
                else{
                    temp=new Image(new File("PipeStatic_Vertical.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                }
            }           
            else if(type.equals("Starter")){
                if(property.equals("Horizontal")){
                    temp=new Image(new File("Starter_Horizontal.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                }
                else{
                    temp=new Image(new File("Starter_Vertical.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                }
            }
            else if(type.equals("End")){
                if(property.equals("Horizontal")) {
                    temp=new Image(new File("End_Horizontal.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                }
                else{
                    temp=new Image(new File("End_Vertical.png").toURI().toString());
                    (boardGrid[i % 4][i / 4]).setFill(new ImagePattern(temp));
                }
            }
            else {
                System.out.println("Board is broken.");
                System.exit(1);
            }
        }
    }

    //This method enables the rectangle to move
    private void setMove(Rectangle tile){
    	
    	tile.setOnDragDetected(e -> {
            Rectangle temp=(Rectangle) e.getSource();
            int i;
            for(i=0;i<16;i++){
                if(boardGrid[i%4][i/4].equals(temp)){
                    break;
                }
            }
            selected.set(new Point2D(i%4,i/4));
            tile.startFullDrag();
            e.consume();
        });
    }
    //This method makes a place that other moveable elements can be moved into.
    private void setPlace(Rectangle tile){
    	tile.setOnMouseDragExited(e -> {
            Rectangle target = (Rectangle) e.getSource();
            int i;
            for(i=0;i<16;i++){
                if(boardGrid[i%4][i/4].equals(target)){
                    break;
                }
            }
            int x0=(int) selected.get().getX();
            int y0=(int) selected.get().getY();
            int x1=i%4;
            int y1=i/4;
            if(game.isLegal(x0,y0,x1,y1)){
                game.makeMove(x0,y0,x1,y1);
                changeRectangles(x0,y0,x1,y1);
                numberOfMoves++;
                countMove.setText("Moves: "+numberOfMoves);
            }
            e.consume();
        });
    }
    //This method changes rectangles
    private void changeRectangles(int x0,int y0,int x1,int y1){
        panel.getChildren().remove(boardGrid[x0][y0]);
        panel.getChildren().remove(boardGrid[x1][y1]);
        panel.add(boardGrid[x1][y1],x0,y0);
        panel.add(boardGrid[x0][y0],x1,y1);
        Rectangle temp = boardGrid[x0][y0];
        boardGrid[x0][y0] = boardGrid[x1][y1];
        boardGrid[x1][y1] = temp;
    }
  
    //This method plays the animation
    public PathTransition playAnimation(){
        Path path = game.path ;
        animation.setPath(path);
        animation.setNode(ball);
        animation.setDuration(Duration.seconds(3));
        animation.setCycleCount(1);
        animation.play();
        return animation;            
    }
    
    //This method saves the game
    private void saveGame() {
        try {
            File f = new File("saver.dat");
            Formatter fo = new Formatter(f);
            fo.format("%d", game.levelUnlocked);
            fo.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
  //This method loads the game
    private void loadGame(){
        File f = new File("saver.dat");
        if(f.exists()){
            try{
                BufferedReader br = new BufferedReader(new FileReader("save.dat"));
                game.levelUnlocked=br.read() - ((int) '0');
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            game.levelUnlocked=0;
        }
    }
    //Launchs 
    public static void main(String[] args) {
        launch(args);
    }
}