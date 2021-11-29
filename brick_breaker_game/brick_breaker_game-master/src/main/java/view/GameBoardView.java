/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package view;

import controller.BallController;
import controller.BrickController;
import model.PlayerModel;
import model.WallModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;


/**
 * GameBoardView class displays the in-game screen and handles the in-game implementation.
 */
public class GameBoardView extends JComponent implements KeyListener,MouseListener,MouseMotionListener {

    private static final String CONTINUE = "Continue";
    private static final String RESTART = "Restart";
    private static final String EXIT = "Exit";
    private static final String PAUSE = "Pause Menu";
    private static final int TEXT_SIZE = 30;
    private static final Color MENU_COLOR = new Color(255,255,0); // change text colour to yellow


    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;

    private static final Color BG_COLOR = Color.BLACK; // change background colour to black

    private Timer gameTimer;

    private WallModel wall;

    private String message;

    private boolean showPauseMenu;

    private Font menuFont;

    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;
    private int strLen;

    private DebugConsoleView debugConsole;


    /**
     * GameBoardView is a parameterized constructor that displays the In-Game screen. Handles the in-game implementation.
     * Deals with the Wall implementation, the Debug Console implementation.
     * Sets the Game Timer.
     * @param owner     reads the owner of the system the game runs on.
     */
    public GameBoardView(JFrame owner){
        super();

        strLen = 0;
        showPauseMenu = false;



        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE);


        this.initialize();
        message = "";
        wall = new WallModel(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2,new Point(300,430));

        debugConsole = new DebugConsoleView(owner,wall,this);
        //initialize the first level
        wall.nextLevel();

        gameTimer = new Timer(10,e ->{
            wall.move();
            wall.findImpacts();
            message = String.format("Bricks: %d Balls %d",wall.getBrickCount(),wall.getBallCount());
            if(wall.isBallLost()){
                if(wall.ballEnd()){
                    wall.wallReset();
                    message = "Game over";
                }
                wall.ballReset();
                gameTimer.stop();
            }
            else if(wall.isDone()){
                if(wall.hasLevel()){
                    message = "Go to Next Level";
                    gameTimer.stop();
                    wall.ballReset();
                    wall.wallReset();
                    wall.nextLevel();
                }
                else{
                    message = "ALL WALLS DESTROYED";
                    gameTimer.stop();
                }
            }

            repaint();
        });

    }


    /**
     * initialize is a Private Method to initialize the in-game screen.
     * Sets the in-game screen size, enables game screen to gain focus.
     * Listens and notifies when any keys are pressed, and if there are any mouse clicks or motions.
     */
    private void initialize(){
        this.setPreferredSize(new Dimension(DEF_WIDTH,DEF_HEIGHT));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }


    /**
     * paint is an Overridden Method from the JComponent class.
     * The method paints/draws the game board view.
     * Draws the message on screen.
     * Draws the ball, player and wall on the in-game screen.
     * Draws the Pause screen as well.
     * @param g
     */
    public void paint(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        clear(g2d);

        g2d.setColor(Color.WHITE);//change text colour to white
        g2d.drawString(message,250,225);

        drawBall(wall.ball,g2d);

        for(BrickController b : wall.bricks)
            if(!b.isBroken())
                drawBrick(b,g2d);

        drawPlayer(wall.player,g2d);

        if(showPauseMenu)
            drawMenu(g2d);

        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * clear is a Private Method which is responsible for clearing the in-game screen.
     * @param g2d
     */
    private void clear(Graphics2D g2d){
        Color tmp = g2d.getColor();
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0,0,getWidth(),getHeight());
        g2d.setColor(tmp);
    }

    /**
     * drawBrick is a private Method that draws the brick to the in-game screen.
     * Sets and Draws the brick color, shape and type.
     * @param brick     passing in the Object/Reference variable of the BrickController class. Aggregation relationship.
     * @param g2d
     */
    private void drawBrick(BrickController brick, Graphics2D g2d){
        Color tmp = g2d.getColor();

        g2d.setColor(brick.getInnerColor());
        g2d.fill(brick.getBrick());

        g2d.setColor(brick.getBorderColor());
        g2d.draw(brick.getBrick());


        g2d.setColor(tmp);
    }

    /**
     * drawBall is a private Method that draws the ball components to the in-game screen.
     * Draws the ball features such as color and shape
     * @param ball      passing in the Object/Reference variable of the BallController class. Aggregation relationship.
     * @param g2d
     */
    private void drawBall(BallController ball, Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = ball.getBallFace();

        g2d.setColor(ball.getInnerColor());
        g2d.fill(s);

        g2d.setColor(ball.getBorderColor());
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    /**
     * drawPlayer is a private Method that draws the player components to the in-game screen.
     * Draws the player shape, color.
     * @param p     passing in the Object/Reference variable of the PlayerModel class. Aggregation relationship.
     * @param g2d
     */
    private void drawPlayer(PlayerModel p, Graphics2D g2d){
        Color tmp = g2d.getColor();

        Shape s = p.getPlayerFace();
        g2d.setColor(PlayerModel.INNER_COLOR);
        g2d.fill(s);

        g2d.setColor(PlayerModel.BORDER_COLOR);
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    /**
     * drawMenu calls the obscureGameBoard and drawPauseMenu methods to completely draw the Pause Menu screen.
     * Draws the Pause Menu screen and background.
     * Draws the text and buttons in the Pause Menu.
     * @param g2d
     */
    private void drawMenu(Graphics2D g2d){
        obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }

    /**
     * obscureGameBoard is a private Method that draws the Pause Menu container screen and sets its properties.
     * Draws on top of the existing In-Game Screen.
     * Handles the blending and transparency of the screen.
     * @param g2d
     */
    private void obscureGameBoard(Graphics2D g2d){

        Composite tmp = g2d.getComposite();
        Color tmpColor = g2d.getColor();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f);
        g2d.setComposite(ac);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,DEF_WIDTH,DEF_HEIGHT);

        g2d.setComposite(tmp);
        g2d.setColor(tmpColor);
    }

    /**
     * drawPauseMenu is a private Method that draws and renders the fonts on the Pause Menu Screen.
     * Draws the CONTINUE, RESTART and EXIT buttons.
     * @param g2d
     */
    private void drawPauseMenu(Graphics2D g2d){
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();


        g2d.setFont(menuFont);
        g2d.setColor(MENU_COLOR);

        if(strLen == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = menuFont.getStringBounds(PAUSE,frc).getBounds().width;
        }

        int x = (this.getWidth() - strLen) / 2;
        int y = this.getHeight() / 10;

        g2d.drawString(PAUSE,x,y);

        x = this.getWidth() / 8;
        y = this.getHeight() / 4;


        if(continueButtonRect == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            continueButtonRect = menuFont.getStringBounds(CONTINUE,frc).getBounds();
            continueButtonRect.setLocation(x,y-continueButtonRect.height);
        }

        g2d.drawString(CONTINUE,x,y);

        y *= 2;

        if(restartButtonRect == null){
            restartButtonRect = (Rectangle) continueButtonRect.clone();
            restartButtonRect.setLocation(x,y-restartButtonRect.height);
        }

        g2d.drawString(RESTART,x,y);

        y *= 3.0/2;

        if(exitButtonRect == null){
            exitButtonRect = (Rectangle) continueButtonRect.clone();
            exitButtonRect.setLocation(x,y-exitButtonRect.height);
        }

        g2d.drawString(EXIT,x,y);



        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    /**
     * keyPressed implements the method in KeyListener.
     * Listens and notifies if a key is pressed.
     * Implements the next course of actions if specific keys are pressed, such as:
     * A: move player left, D: move player right, ESC: show pause menu, SPACE: pause game, ALT-SHIFT-F1: display DebugConsole.
     * @param keyEvent      to indicate if a key action has occurred or not.
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch(keyEvent.getKeyCode()){
            case KeyEvent.VK_A:
                wall.player.moveLeft();
                break;
            case KeyEvent.VK_D:
                wall.player.movRight();
                break;
            case KeyEvent.VK_ESCAPE:
                showPauseMenu = !showPauseMenu;
                repaint();
                gameTimer.stop();
                break;
            case KeyEvent.VK_SPACE:
                if(!showPauseMenu)
                    if(gameTimer.isRunning())
                        gameTimer.stop();
                    else
                        gameTimer.start();
                break;
            case KeyEvent.VK_F1:
                if(keyEvent.isAltDown() && keyEvent.isShiftDown())
                    debugConsole.setVisible(true);
            default:
                wall.player.stop();
        }
    }

    /**
     * keyReleased implements the method in KeyListener.
     * Stops the player's movement once key released.
     * @param keyEvent      to indicate if a key action has occurred or not.
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        wall.player.stop();
    }

    /**
     * mouseClicked implements the method in MouseListener.
     * Implements the next course of actions if the CONTINUE, RESTART and EXIT buttons are clicked on.
     * @param mouseEvent    to indicate if a mouse action has occurred or not.
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(!showPauseMenu)
            return;
        if(continueButtonRect.contains(p)){
            showPauseMenu = false;
            repaint();
        }
        else if(restartButtonRect.contains(p)){
            message = "Restarting Game...";
            wall.ballReset();
            wall.wallReset();
            showPauseMenu = false;
            repaint();
        }
        else if(exitButtonRect.contains(p)){
            System.exit(0);
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    /**
     * mouseMoved implements the method in MouseListener.
     * Implements what the cursor should look like when the cursor hovers over the CONTINUE, RESTART and EXIT buttons.
     * Implements what the cursor should look like otherwise.
     * @param mouseEvent       to indicate if a mouse action has occurred or not.
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(exitButtonRect != null && showPauseMenu) {
            if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p))
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else
                this.setCursor(Cursor.getDefaultCursor());
        }
        else{
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * onLostFocus method implements what should occur when focus is lost.
     * Stops the game timer.
     * Prints out the message mentioning the focus is lost.
     * Redraw the screen.
     */
    public void onLostFocus(){
        gameTimer.stop();
        message = "Focus Lost";
        repaint();
    }

}
