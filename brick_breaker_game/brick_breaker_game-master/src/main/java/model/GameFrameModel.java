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
package model;

import view.GameBoardView;
import view.HomeMenuView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;


/**
 * GameFrameModel is the class responsible for the implementation of the Game Frame window.
 */
public class GameFrameModel extends JFrame implements WindowFocusListener {

    private static final String DEF_TITLE = "Brick Destroyer";//game title

    private GameBoardView gameBoard;
    private HomeMenuView homeMenu;

    private boolean gaming;

    /**
     * GameFrameModel is a Default Constructor that adds the HomeMenu page to the Game frame.
     */
    public GameFrameModel(){
        super();

        gaming = false;

        this.setLayout(new BorderLayout());

        gameBoard = new GameBoardView(this);

        homeMenu = new HomeMenuView(this,new Dimension(600,450));//from (450,300) . make size of HomeMenu same as size of GameBoard

        this.add(homeMenu,BorderLayout.CENTER);

        this.setUndecorated(true);


    }

    /**
     * initialize is a Method to initialize the in-game frame.
     * Sets the title for the game frame window.
     * Handles the window's closing options.
     */
    public void initialize(){
        this.setTitle(DEF_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.autoLocate();
        this.setVisible(true);
    }

    /**
     * enableGameBoard is a Method that makes the transition from the HomeMenu to the GameBoard screen.
     * It removes the HomeMenu page and adds the GameBoard page.
     * It also sets the frame's features and properties such as Frame decorations and Window Focus Listeners.
     */
    public void enableGameBoard(){
        this.dispose();
        this.remove(homeMenu);
        this.add(gameBoard,BorderLayout.CENTER);
        this.setUndecorated(false);
        initialize();
        /*to avoid problems with graphics focus controller is added here*/
        this.addWindowFocusListener(this);

    }

    /**
     * autoLocate is a Method that sets the location of the Game Frame depending on the device's screen size.
     */
    private void autoLocate(){
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (size.width - this.getWidth()) / 2;
        int y = (size.height - this.getHeight()) / 2;
        this.setLocation(x,y);
    }


    /**
     * windowGainedFocus implements the method in WindowListener.
     * Handles implementation for when Game Board is back in focus.
     * @param windowEvent       to indicate if a key action has occurred or not.
     */
    @Override
    public void windowGainedFocus(WindowEvent windowEvent) {
        /*
            the first time the frame loses focus is because
            it has been disposed to install the GameBoard,
            so when it regains the focus it's ready to play.
            of course calling a method such as 'onLostFocus'
            is useful only if the GameBoard has been displayed
            at least once
         */
        gaming = true;
    }

    /**
     * windowLostFocus implements the method in WindowListener.
     * Handles implementation when Game Board has lost focus.
     * Calls the onLostFocus method from the GameBoardView class.
     * @param windowEvent   to indicate if a key action has occurred or not.
     */
    @Override
    public void windowLostFocus(WindowEvent windowEvent) {
        if(gaming)
            gameBoard.onLostFocus();

    }
}
