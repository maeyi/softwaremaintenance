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
package controller;

import model.WallModel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;


/**
 * DebugPanelController Class handles the implementation of the Debug Panel Window.
 */
public class DebugPanelController extends JPanel {

    private static final Color DEF_BKG = Color.WHITE;


    private JButton skipLevel;
    private JButton resetBalls;

    private JSlider ballXSpeed;
    private JSlider ballYSpeed;

    private WallModel wall;

    /**
     * DebugPanelController is a Parameterized Constructor that initializes the Debug Console window and it's properties.
     * Calls the initialize() method.
     * Creates the 'Skip Level' and 'Reset Balls' buttons.
     * Creates a slider for the ball x-axis speed.
     * Creates a slider for the ball y-axis speed.
     * @param wall      passing in the Object/Reference variable of the WallController class. Aggregation relationship.
     */
    public DebugPanelController(WallModel wall){

        this.wall = wall;

        initialize();

        //create buttons for the SKIP LEVEL and RESET BALLS
        skipLevel = makeButton("Skip Level",e -> wall.nextLevel());
        resetBalls = makeButton("Reset Balls",e -> wall.resetBallCount());

        //creates a slider for the ball's speed in the x and y-axis directions
        ballXSpeed = makeSlider(-4,4,e -> wall.setBallXSpeed(ballXSpeed.getValue()));
        ballYSpeed = makeSlider(-4,4,e -> wall.setBallYSpeed(ballYSpeed.getValue()));

        this.add(skipLevel);
        this.add(resetBalls);

        this.add(ballXSpeed);
        this.add(ballYSpeed);

    }

    /**
     * initialize is a Private Method to initialize the Debug Panel.
     * Sets the layout and background color of the debug panel.
     */
    private void initialize(){
        this.setBackground(DEF_BKG);
        this.setLayout(new GridLayout(2,2));
    }

    /**
     * makeButton is a Private Method which creates a button for the Debug Panel.
     * @param title     title/name of the button
     * @param e         notifies if an action has been performed
     * @return          returns a button.
     */
    private JButton makeButton(String title, ActionListener e){
        JButton out = new JButton(title);
        out.addActionListener(e);
        return  out;
    }

    /**
     * makeSlider is a Private Method which creates a slider in the Debug Panel.
     * @param min       the min value.
     * @param max       the max value.
     * @param e         notifies if any change of event.
     * @return          returns a slider.
     */
    private JSlider makeSlider(int min, int max, ChangeListener e){
        JSlider out = new JSlider(min,max);
        out.setMajorTickSpacing(1);
        out.setSnapToTicks(true);
        out.setPaintTicks(true);
        out.addChangeListener(e);
        return out;
    }

    /**
     * setValues Method sets the values of the ball's speed in x-axis and y-axis. Self-calling method.
     * @param x     speed of ball in x-axis.
     * @param y     speed of ball in y-axis.
     */
    public void setValues(int x,int y){
        ballXSpeed.setValue(x);
        ballYSpeed.setValue(y);
    }

}
