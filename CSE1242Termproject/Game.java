//Ahmet Arda Nalbant 150121004
//Umut Bayar 150120043
//This class has the operation mechanism of the game, data retrieval and animation.
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

//This enum is created to find path of the game end.
enum Position{TOP,LEFT,RIGHT,DOWN}

class Game {
	
	//Declares the variables
	public int id;
    public String type;
    public String property;
    public Game[][] board;
    public int levelUnlocked;
    public Path path;
    
    public Game(){
    	path = new Path();
    	levelUnlocked=0;
        board=new Game[4][4];
    }
    
    public Game(int id, String type, String property){
    	//Constructor for cell datas
        this.id = id;
        this.type = type;
        this.property = property;
    }
    
  
    //This method checks the move is legal or not
    public boolean isLegal(int x0,int y0,int x1,int y1){
    	
        if(board[x0][y0].type.equals("Starter")||board[x0][y0].type.equals("End")||board[x0][y0].type.equals("PipeStatic")||board[x0][y0].property.equals("Free")){
            return false;
        }
        else{
            if(board[x1][y1].property.equals("Free")){
                return (Math.abs(x0-x1)==1 && Math.abs(y0-y1)==0)||(Math.abs(y0-y1)==1 && Math.abs(x0-x1)==0);
            }
            else{
                return false;
            }
        }
    }
    //This method is getting information form text files and generates map
    public void generateMapData(String inputFilePath) {
    	
        BufferedReader read = null;
        String line = "";
        try {
        	read = new BufferedReader(new FileReader(inputFilePath));
            int i = 0;
            while ((line = read.readLine()) != null) {
                String[] info = line.split(",");
                try {
                    Game Game = new Game(Integer.parseInt(info[0]), info[1], info[2]);
                    board[i % 4][i / 4] =Game;
                    i++;
                } catch (NumberFormatException e) {
                }
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            
        } catch (IOException e) {
            e.printStackTrace();
            
        } finally {
            if (read != null) {
                try {
                	read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //This method swaps cells's coordinates.
    public void makeMove(int x0,int y0,int x1,int y1){
        Game temp = board[x0][y0];
        board[x0][y0] = board[x1][y1];
        board[x1][y1] = temp;
    }
    
    //This method checks the game is ended or not and finds path with help of enum
    public boolean gameEnded(){
        int x0 = 0 ,y0 = 0;
        int x1,y1;
       
        boolean result=true;
        while(!board[x0][y0].type.equals("Starter")){
            x0++;
            if(x0>=4){
                y0++;
                x0 = 0;
            }
        }
        if(board[x0][y0].property.equals("Vertical")) {  	
            x1 = x0;
            y1 = y0 + 1;
            path.getElements().add(new MoveTo(x0*127+105,y0*127+109.5));
        	path.getElements().add(new LineTo(x1*127+105, y1*127 + 109.5));
        	
        }
        
        else{
            x1 = x0 - 1;
            y1 = y0;
            path.getElements().add(new MoveTo(x0*127+105,y0*127+109.5));
        	path.getElements().add(new LineTo(x1*127+105, y1*127 + 109.5));
        }
        
        while(true) {
        	
            Position enteredPos;
            int diffX = x1 - x0;
            int diffY = y1 - y0;
            if (diffX != 0) {
                if (diffX == 1) {
                    enteredPos = Position.LEFT;
                }
                
                else {
                    enteredPos = Position.RIGHT;
                }
                
            }
            
            else {
                if (diffY == 1) {
                    enteredPos = Position.TOP;
                }
                
                else {
                    enteredPos = Position.DOWN;
                }
            }
            
            Game newCell = board[x1][y1];
            if (newCell.type.equals("Pipe")||newCell.type.equals("PipeStatic")) {
            	
            	if (newCell.property.equals("00")) {
                	
                	if (enteredPos == Position.TOP) {   	
                    	y0 = y1;
                        x0 = x1--;
                        path.getElements().add(new LineTo(x0*127+105-63.5, y0*127 + 109.5));
                    }
                    
                    else {
                    	y0 = y1--;
                        x0 = x1;
                        path.getElements().add(new LineTo(x0*127+105, y0*127 + 109.5-63.5-46));
                    	
                    }
                }
                
                else if (newCell.property.equals("01")) {
                	if (enteredPos == Position.TOP) {
                    	y0 = y1;
                        x0 = x1++;
                        path.getElements().add(new LineTo(x0*127+105+63.5, y0*127 + 109.5));

                    }
                    
                    else {
                    	y0 = y1--;
                        x0 = x1;
                        path.getElements().add(new LineTo(x0*127+105, y0*127 + 109.5-63.5-46));

                    }
                }
                
                else if (newCell.property.equals("10")) {
                	if (enteredPos == Position.LEFT) {
                    	y0 = y1++;
                        x0 = x1;
                        path.getElements().add(new LineTo(x0*127+105, y0*127 + 109.5+63.5));

                    } 
                    else {
                    	y0 = y1;
                        x0 = x1--;
                        path.getElements().add(new LineTo(x0*127+105-63.5-46, y0*127 + 109.5));

                    }
                }
                else if (newCell.property.equals("11")) {
                	if (enteredPos == Position.DOWN) {
                    	y0 = y1;
                        x0 = x1++;
                        path.getElements().add(new LineTo(x0*127+105+63.5+46, y0*127 + 109.5));

                    } 
                    else {
                    	y0 = y1++;
                    	x0 = x1;
                        path.getElements().add(new LineTo(x0*127+105, y0*127 + 109.5+63.5));

                    }
                }
                else if (newCell.property.equals("Horizontal")) {
                	if (enteredPos == Position.LEFT) {
                    	x0 = x1++;
                        path.getElements().add(new LineTo(x1*127+105, y1*127 + 109.5));

                    } else {
                    	x0 = x1--;
                        path.getElements().add(new LineTo(x1*127+105, y1*127 + 109.5));

                    }
                }
                else {
                	if (enteredPos == Position.TOP) {
                    	y0 = y1++;
                        path.getElements().add(new LineTo(x1*127+105, y1*127 + 109.5));

                    } else {
                    	y0 = y1--;
                        path.getElements().add(new LineTo(x1*127+105, y1*127 + 109.5));

                    }
                }
            }
            
            else if(newCell.type.equals("End")){
            	if(enteredPos==Position.LEFT && newCell.property.equals("Horizontal")){
                	break;
                }
                else if(enteredPos==Position.DOWN && newCell.property.equals("Vertical")){
                	break;
                }
         	      else{
         	    	  result=false;
         	    	  break;
         	      }
            }
            else{
            	result=false;
            	break;
            }
        }
 	      return result;
    }
}
