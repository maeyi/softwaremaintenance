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

import controller.BrickController;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;


/**
 * SteelBrickModel class is SubClass/Child Class of the BrickController class.
 * It extends the BrickController class. Inheritance
 * Responsible for all the implementations regarding the Steel Brick.
 */
public class SteelBrickModel extends BrickController {

    private static final String NAME = "Steel Brick";
    private static final Color DEF_INNER = new Color(203, 203, 201);
    private static final Color DEF_BORDER = Color.BLACK;
    private static final int STEEL_STRENGTH = 1;
    private static final double STEEL_PROBABILITY = 0.4;

    private Random rnd;
    private Shape brickFace;

    /**
     * SteelBrickModel is a Parameterized Constructor that runs the Parent Class's, BrickController class constructor.
     * @param point     brick position/location.
     * @param size      size of the brick.
     */
    public SteelBrickModel(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,STEEL_STRENGTH);
        rnd = new Random();
        brickFace = super.brickFace;
    }


    /**
     * makeBrickFace overrides the makeBrickFace in the parent class.
     * This method creates the clay brick.
     * @param pos  the position/location of the brick.
     * @param size size of the brick.
     * @return      returns the brick.
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    }

    /**
     * getBrick implements the Abstract method from the parent class.
     * @return      returns the Steel Brick.
     */
    @Override
    public Shape getBrick() {
        return brickFace;
    }

    /**
     * setImpact is an Overridden Method from the parent class.
     * Responsible for knowing if an impact has occurred or not.
     * Calls the BrickController's impact() and isBroken() method.
     * @param point point of impact.
     * @param dir   direction of impact.
     * @return      returns a boolean value stating if an impact has occurred or not.
     */
    public  boolean setImpact(Point2D point , int dir){
        if(super.isBroken())
            return false;
        impact();
        return  super.isBroken();
    }

    /**
     * impact is an Overrides the Method in the parent class, BrickController class.
     * Responsible for deducting the strength of a brick when an impact has occurred.
     */
    public void impact(){
        if(rnd.nextDouble() < STEEL_PROBABILITY){
            super.impact();
        }
    }

}
