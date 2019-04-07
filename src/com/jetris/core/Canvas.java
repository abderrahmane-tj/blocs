package com.jetris.core;

import javax.swing.*;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.RenderingHints;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Canvas extends JPanel{
    private int canvasWidth;
    private int canvasHeight;
    public static final int SQUARE_WIDTH = 20;
    private Timer paintingTimer;
    private Graphics2D g2D;
    private Graphics g;
    public Shape character;
    public Shape blob;
    private Commander commander;
    private String[] types = {"T","Z","ZMirrored","L","LMirrored","Square","I"};
    private Color[] colors= {
            Color.decode("#4d90fe"),
            Color.decode("#CC8081"),
            Color.decode("#888888"),
            Color.decode("#0971B2"),
            Color.decode("#9937B2"),
            Color.decode("#FFFEBA"),
            Color.decode("#FFFC19"),
            Color.decode("#00CCFF"),
            Color.decode("#24459A"),
            Color.decode("#faa614"),
            Color.decode("#24459A"),
            Color.decode("#e81123"),
            Color.decode("#52b043"),
            Color.decode("#24459A"),
            Color.decode("#ea3e24"),
            Color.decode("#00188f"),
            Color.decode("#ba141a"),
            Color.decode("#e51400"),
            Color.decode("#68217a"),
            Color.decode("#c1d304"),
    };
    Canvas(int width, int height){
        canvasWidth = width;
        canvasHeight = height;
        setFocusable(true);
        setBackground(ColorConstants.BACKGROUND);
        setDoubleBuffered(true);

        commander = new Commander(this);
        addKeyListener(commander.new KeyAdapter());
    }

    public void painting(){
        drawShape(character);
        drawShape(blob);
//        drawShape(new Shape(27,4,"T",colors[random_range(colors.length)]));
//        drawShape(new Shape(28,6,"L",colors[random_range(colors.length)]));
//        drawShape(new Shape(29,2,"I",colors[random_range(colors.length)]));
//        drawShape(new Shape(28,0,"Square",colors[random_range(colors.length)]));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;
        this.g2D = g2D;
        this.g = g;

        // Smoothing up things so that we get good graphics instead of pixely things
        RenderingHints rh = new RenderingHints( RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put( RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2D.setRenderingHints(rh);


        // Before painting :
        // - in debug mode showing the guide is essential to know what is being done
        drawGuide();

        painting();

        // After painting :
        // - in debug mode Showing some info about current location and frame rates are essential
        // TODO: add debugging stuff.

        Toolkit.getDefaultToolkit().sync();
        g.dispose();

    }

    public void drawShape(Shape shape){
        if(shape == null){
            return;
        }
        Square[][] grid = shape.getGrid();
        for(int row=0; row < grid.length; row++){
            for(int column=0; column < grid[0].length; column++){
                if(grid[row][column] != null)
                    drawSquareAt(shape.getRow()+row, shape.getColumn()+column, grid[row][column].getColor());
            }
        }
    }

    public void drawSquareAt(int row, int column,Color color){
        Color background;
        Color border;

        if(color == null){
            background = ColorConstants.DEFAULT_SQUARE_BACKGROUND;
            border = ColorConstants.DEFAULT_SQUARE_BORDER;
        }else{
            background = color;
            border = color.darker();
        }
        drawSquare(new Rectangle2D.Double(
                column * SQUARE_WIDTH,
                row * SQUARE_WIDTH, SQUARE_WIDTH, SQUARE_WIDTH),
                1,
                background,
                border);
    }

    public void drawSquare(Rectangle2D square, int strokeWidth, Color background, Color border){
        g2D.setColor(background);
        g2D.fill(square);

        int x = (int)square.getX();
        int y = (int)square.getY();

        g2D.setColor(border);
        g2D.drawLine(x,y,x+SQUARE_WIDTH-1,y);
        g2D.drawLine(x,y,x,y+SQUARE_WIDTH-1);
        g2D.drawLine(x+SQUARE_WIDTH-1,y,x+SQUARE_WIDTH-1,y+SQUARE_WIDTH-1);
        g2D.drawLine(x,y+SQUARE_WIDTH-1,x+SQUARE_WIDTH-1,y+SQUARE_WIDTH-1);

    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color, int strokeWidth){

        g2D.setColor(color);
        g2D.drawLine(x1, y1, x2, y2);

    }

    public void drawGuide(){
        for(int i = 0; i < (canvasHeight/SQUARE_WIDTH + 1) ; i++){
            drawLine(0,i*SQUARE_WIDTH,canvasWidth,i*SQUARE_WIDTH,ColorConstants.GUIDE,1);
        }

        for(int i = 0; i < (canvasWidth/SQUARE_WIDTH + 1) ; i++){
            drawLine(i*SQUARE_WIDTH, 0,i*SQUARE_WIDTH, canvasHeight,ColorConstants.GUIDE,1);
        }
    }
    public Shape getCharacter() {
        return character;
    }

    public void setCharacter(Shape character) {
        this.character = character;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public void setCanvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public void setCanvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    public Timer getPaintingTimer() {
        return paintingTimer;
    }

    public void setPaintingTimer(Timer paintingTimer) {
        this.paintingTimer = paintingTimer;
    }

    public Graphics2D getG2D() {
        return g2D;
    }

    public void setG2D(Graphics2D g2D) {
        this.g2D = g2D;
    }

    public Graphics getG() {
        return g;
    }

    public void setG(Graphics g) {
        this.g = g;
    }

    public Shape getBlob() {
        return blob;
    }

    public void setBlob(Shape blob) {
        this.blob = blob;
    }

    public Commander getCommander() {
        return commander;
    }

    public void setCommander(Commander commander) {
        this.commander = commander;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public Color[] getColors() {
        return colors;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
    }
}
