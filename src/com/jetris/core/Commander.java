package com.jetris.core;

import javax.swing.Timer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Commander implements ActionListener{

    Commander(Canvas canvas){
        this.canvas = canvas;
        paintingTimer = new Timer(1000, this);
        paintingTimer.start();
    }
    public void actionPerformed(ActionEvent e){
        Shape character = canvas.getCharacter();
        String[] types = canvas.getTypes();
        Color[] colors = canvas.getColors();

        if(character == null){
            String type = types[random_range(types.length)];
            Color color = colors[random_range(colors.length)];

            character = new Shape(0,(canvas.getCanvasWidth() / Canvas.SQUARE_WIDTH ) / 2 -2,type,color);
            canvas.setCharacter(character);

        }else{
            if(!move("down")){
                if(canvas.blob == null){
                    canvas.blob = character.mergeWith(null);
                }else{
                    character.mergeWith(canvas.blob);
                }
                canvas.setCharacter(null);
                removeFullLines();
            }

        }

        canvas.repaint();
    }
    public void removeFullLines(){
        if(canvas.blob == null){
            return;
        }
        int count = 0;
        Square[][] grid = canvas.blob.getGrid();

        for(int i=0; i<grid.length;i++){
            if(grid[i].length == canvas.getCanvasWidth() / Canvas.SQUARE_WIDTH && !containsNull(grid[i])){

                count++;
            }
        }


        if(count == 0){
            return;
        }

        Square[][] newGrid = new Square[grid.length-count][grid[0].length];

        for(int i=0, ii=0; i<grid.length;i++){
            if(!containsNull(grid[i])){
                continue;
            }
            for(int j=0;j<grid[0].length-1;j++){
                if(grid[i][j] != null){
                    newGrid[ii][j] = grid[i][j].clone();
                }
            }
            ii++;
        }

        canvas.blob.setGrid(newGrid);
        canvas.blob.row += count;
    }
    public void turn(){
        Shape character = canvas.getCharacter();

        character.turn("clockwise");
        if(character.intersects(canvas.getBlob())){
            character.turn("counterclockwise");
        }

        canvas.repaint();

    }
    public boolean move(String direction){
        Shape character = canvas.getCharacter();
        int canvasRows = canvas.getCanvasHeight() / Canvas.SQUARE_WIDTH;
        int canvasColumns = canvas.getCanvasWidth() / Canvas.SQUARE_WIDTH;
        int height = character.getGrid().length;
        int width = character.getGrid()[0].length;
        int Y = character.row;
        int X = character.column;

        if(direction.equals("left")){
            if(character.column == 0 ){
                return false;
            }
            goLeft();
        }
        if(direction.equals("right")){
            if(X + width == canvasColumns){
                return false;
            }
            goRight();
        }
        if(direction.equals("down")){
            if(Y + height == canvasRows){
                return false;
            }
            goDown();
        }
        if(character.intersects(canvas.getBlob())){
            if(direction.equals("left")){
                goRight();
            }
            if(direction.equals("right")){
                goLeft();
            }
            if(direction.equals("down")){
                goUp();
            }
            return false;
        }

        canvas.repaint();
        return true;
    }
    public class KeyAdapter extends java.awt.event.KeyAdapter{
        public void keyReleased(KeyEvent e){

        }
        public void keyPressed(KeyEvent e){
            if(canvas.getCharacter() == null){
                return;
            }
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT){
                move("left");
            }else if(key == KeyEvent.VK_RIGHT){
                move("right");
            }else if(key == KeyEvent.VK_DOWN){
                move("down");
            }else if(key == KeyEvent.VK_SPACE){
                turn();
            }
        }
    }
    public void goLeft(){
        Shape character = canvas.getCharacter();
        character.column -= 1;
    }
    public void goRight(){
        Shape character = canvas.getCharacter();
        character.column += 1;
    }

    public void goDown(){
        Shape character = canvas.getCharacter();
        character.row += 1;
    }
    public void goUp(){
        Shape character = canvas.getCharacter();
        character.row -= 1;
    }
    private Canvas canvas;
    private Timer paintingTimer;

    private int random_range(int range){
        int value = (int)Math.floor(Math.random()*range);
        if(value == range){
            value = range -1;
        }
        return value;
    }
    private boolean containsNull(Square[] squares){
        for(Square square : squares){
            if(square == null){
                return true;
            }
        }
        return false;
    }
}
