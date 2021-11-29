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

import controller.BallController;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * RubberBallModel class is SubClass/Child Class of the BallController class.
 * It extends the BallController class. Inheritance
 * Responsible for creating the Rubber Ball.
 */
public class RubberBallModel extends BallController {


    private static final int DEF_RADIUS = 12;// make ball bigger
    private static final Color DEF_INNER_COLOR = new Color(255, 0, 0);//change colour of ball to red
    private static final Color DEF_BORDER_COLOR = DEF_INNER_COLOR.darker().darker();


    /**
     * RubberBallModel is a Parameterized Constructor that runs the Parent Class's, BallController class constructor.
     * Passes in the radius of the ball and the inner and border colors of the ball.
     * @param center    the initial location of the ball.
     */
    public RubberBallModel(Point2D center){
        super(center,DEF_RADIUS,DEF_RADIUS,DEF_INNER_COLOR,DEF_BORDER_COLOR);
    }


    /**
     * makeBall implements the Abstract Method from the Ball Controller class.
     * This method creates the ball.
     * @param center    the initial location of the ball.
     * @param radiusA   the width of the ball.
     * @param radiusB   the height of the ball.
     * @return          returns the shape and size of the ball.
     */
    @Override
    protected Shape makeBall(Point2D center, int radiusA, int radiusB) {

        double x = center.getX() - (radiusA / 2);
        double y = center.getY() - (radiusB / 2);

        return new Ellipse2D.Double(x,y,radiusA,radiusB);
    }
}
