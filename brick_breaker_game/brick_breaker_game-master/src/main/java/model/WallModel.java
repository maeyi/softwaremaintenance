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
import controller.BrickController;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;


/**
 * WallModel class is responsible for all the implementations on the wall,ball and the impacts.
 */
public class WallModel {

    private static final int LEVELS_COUNT = 4;

    private static final int CLAY = 1;
    private static final int STEEL = 2;
    private static final int CEMENT = 3;

    private Random rnd;
    private Rectangle area;

    public BrickController[] bricks;
    public BallController ball;
    public PlayerModel player;

    private BrickController[][] levels;
    private int level;

    private Point startPoint;
    private int brickCount;
    private int ballCount;
    private boolean ballLost;

    /**
     * WallModel is a Parameterized Constructor that hat handles the initial implementation of the wall.
     * @param drawArea
     * @param brickCount
     * @param lineCount
     * @param brickDimensionRatio
     * @param ballPos       the position/location of the ball.
     */
    public WallModel(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio, Point ballPos){

        this.startPoint = new Point(ballPos);

        levels = makeLevels(drawArea,brickCount,lineCount,brickDimensionRatio);
        level = 0;

        ballCount = 3;
        ballLost = false;

        rnd = new Random();

        makeBall(ballPos);
        int speedX,speedY;
        do{
            speedX = rnd.nextInt(5) - 2;
        }while(speedX == 0);
        do{
            speedY = -rnd.nextInt(3);
        }while(speedY == 0);

        ball.setSpeed(speedX,speedY);

        player = new PlayerModel((Point) ballPos.clone(),150,10, drawArea);

        area = drawArea;


    }

    /**
     * makeSingleTypeLevel is a Private Method implements a wall with only 1 type of brick.
     * @param drawArea      a rectangular area for the wall.
     * @param brickCnt      the number of bricks.
     * @param lineCnt       the number of rows of bricks on the wall.
     * @param brickSizeRatio    the brick dimension.
     * @param type          the type of brick.
     * @return          returns a single brick type wall.
     */
    private BrickController[] makeSingleTypeLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int type){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        BrickController[] tmp  = new BrickController[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt);
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / brickOnLine;
            if(line == lineCnt)
                break;
            double x = (i % brickOnLine) * brickLen;
            x =(line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,type);
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = new ClayBrickModel(p,brickSize);
        }
        return tmp;

    }

    /**
     * makeChessBoard level is a Private Method implements a wall with multiple types of brick.
     * Creates a Chessboard like wall.
     * @param drawArea      a rectangular area for the wall.
     * @param brickCnt      the number of bricks.
     * @param lineCnt       the number of rows of bricks on the wall.
     * @param brickSizeRatio    the brick dimension.
     * @param typeA     the type of brick 1.
     * @param typeB     the type of brick 2.
     * @return          returns a Chessboard wall.
     */
    private BrickController[] makeChessboardLevel(Rectangle drawArea, int brickCnt, int lineCnt, double brickSizeRatio, int typeA, int typeB){
        /*
          if brickCount is not divisible by line count,brickCount is adjusted to the biggest
          multiple of lineCount smaller then brickCount
         */
        brickCnt -= brickCnt % lineCnt;

        int brickOnLine = brickCnt / lineCnt;

        int centerLeft = brickOnLine / 2 - 1;
        int centerRight = brickOnLine / 2 + 1;

        double brickLen = drawArea.getWidth() / brickOnLine;
        double brickHgt = brickLen / brickSizeRatio;

        brickCnt += lineCnt / 2;

        BrickController[] tmp  = new BrickController[brickCnt];

        Dimension brickSize = new Dimension((int) brickLen,(int) brickHgt);
        Point p = new Point();

        int i;
        for(i = 0; i < tmp.length; i++){
            int line = i / brickOnLine;
            if(line == lineCnt)
                break;
            int posX = i % brickOnLine;
            double x = posX * brickLen;
            x =(line % 2 == 0) ? x : (x - (brickLen / 2));
            double y = (line) * brickHgt;
            p.setLocation(x,y);

            boolean b = ((line % 2 == 0 && i % 2 == 0) || (line % 2 != 0 && posX > centerLeft && posX <= centerRight));
            tmp[i] = b ?  makeBrick(p,brickSize,typeA) : makeBrick(p,brickSize,typeB);
        }

        for(double y = brickHgt;i < tmp.length;i++, y += 2*brickHgt){
            double x = (brickOnLine * brickLen) - (brickLen / 2);
            p.setLocation(x,y);
            tmp[i] = makeBrick(p,brickSize,typeA);
        }
        return tmp;
    }

    /**
     * makeBall is a Private Method that calls the RubberBall Constructor to make the rubber ball.
     * Composition relationship
     * @param ballPos       the initial location of the ball.
     */
    private void makeBall(Point2D ballPos){
        ball = new RubberBallModel(ballPos);
    }

    /**
     * makeLevels is a Private Method that creates the wall based on the levels.
     * @param drawArea      a rectangular area for the wall.
     * @param brickCount    the number of bricks.
     * @param lineCount     the number of rows of bricks on the wall.
     * @param brickDimensionRatio   the brick dimension.
     * @return      returns a wall. Either Single type wall or Chessboard wall.
     */
    private BrickController[][] makeLevels(Rectangle drawArea, int brickCount, int lineCount, double brickDimensionRatio){
        BrickController[][] tmp = new BrickController[LEVELS_COUNT][];
        tmp[0] = makeSingleTypeLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY);
        tmp[1] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,CEMENT);
        tmp[2] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,CLAY,STEEL);
        tmp[3] = makeChessboardLevel(drawArea,brickCount,lineCount,brickDimensionRatio,STEEL,CEMENT);
        return tmp;
    }

    /**
     * move Method calls the move methods in the Player and BallController classes.
     */
    public void move(){
        player.move();
        ball.move();
    }

    /**
     * findImpacts Method is responsible for implementing all ball and brick properties when impact is made.
     * Implements when ball makes impact with the player.
     * Implements when ball makes impact with the wall. Calls the impactWall() method.
     * Implements when ball makes impact with the game frame/border.
     */
    public void findImpacts(){
        if(player.impact(ball)){
            ball.reverseY();
        }
        else if(impactWall()){
            /*for efficiency reverse is done into method impactWall
            * because for every brick program checks for horizontal and vertical impacts
            */
            brickCount--;
        }
        else if(impactBorder()) {
            ball.reverseX();
        }
        else if(ball.getPosition().getY() < area.getY()){
            ball.reverseY();
        }
        else if(ball.getPosition().getY() > area.getY() + area.getHeight()){
            ballCount--;
            ballLost = true;
        }
    }

    /**
     * impactWall is a Private Method that is responsible for when the ball makes impact with the wall.
     * @return      returns a boolean value to denote if ball made impact with wall or not.
     */
    private boolean impactWall(){
        for(BrickController b : bricks){
            switch(b.findImpact(ball)) {
                //Vertical Impact
                case BrickController.UP_IMPACT:
                    ball.reverseY();
                    return b.setImpact(ball.down, BrickController.Crack.UP);
                case BrickController.DOWN_IMPACT:
                    ball.reverseY();
                    return b.setImpact(ball.up, BrickController.Crack.DOWN);

                //Horizontal Impact
                case BrickController.LEFT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.right, BrickController.Crack.RIGHT);
                case BrickController.RIGHT_IMPACT:
                    ball.reverseX();
                    return b.setImpact(ball.left, BrickController.Crack.LEFT);
            }
        }
        return false;
    }

    /**
     * impactBorder is a Private Method that implements when the ball makes impact with game border.
     * @return      returns a boolean value to denote if ball made impact with the border or not.
     */
    private boolean impactBorder(){
        Point2D p = ball.getPosition();
        return ((p.getX() < area.getX()) ||(p.getX() > (area.getX() + area.getWidth())));
    }

    /**
     * getBrickCount is a Getter Method.
     * @return  returns the number of bricks.
     */
    public int getBrickCount(){
        return brickCount;
    }

    /**
     * getBallCount is a Getter Method.
     * @return  returns the number of balls.
     */
    public int getBallCount(){
        return ballCount;
    }

    /**
     * isBallLost is a Getter Method.
     * @return  returns a boolean value of whether the ball is lost or not.
     */
    public boolean isBallLost(){
        return ballLost;
    }

    /**
     * ballReset Method resets the ball back to the starting position.
     */
    public void ballReset(){
        player.moveTo(startPoint);
        ball.moveTo(startPoint);
        int speedX,speedY;
        do{
            speedX = rnd.nextInt(5) - 2;
        }while(speedX == 0);
        do{
            speedY = -rnd.nextInt(3);
        }while(speedY == 0);

        ball.setSpeed(speedX,speedY);
        ballLost = false;
    }

    /**
     * wallReset Method resets/repairs the wall.
     * Sets number of ball back to 3 (full amount).
     */
    public void wallReset(){
        for(BrickController b : bricks)
            b.repair();
        brickCount = bricks.length;
        ballCount = 3;
    }

    /**
     * ballEnd Method checks if the number of balls in the game has reached 0.
     * @return  returns a boolean value based on the number of balls present.
     *          True if ballCount is 0.
     *          False if ballCount is greater than 0.
     */
    public boolean ballEnd(){
        return ballCount == 0;
    }

    /**
     * isDone Method checks if the number of bricks on the wall is 0.
     * @return  returns a boolean value based on the number of bricks present.
     *          True if brickCount is 0.
     *          False if brickCount is greater than 0.
     */
    public boolean isDone(){
        return brickCount == 0;
    }

    /**
     * nextLevel Method sets the next level.
     */
    public void nextLevel(){
        bricks = levels[level++];
        this.brickCount = bricks.length;
    }

    /**
     * hasLevel Method checks if there is a next level.
     * @return  returns a boolean value.
     */
    public boolean hasLevel(){
        return level < levels.length;
    }

    public void setBallXSpeed(int s){
        ball.setXSpeed(s);
    }

    public void setBallYSpeed(int s){
        ball.setYSpeed(s);
    }

    public void resetBallCount(){
        ballCount = 3;
    }

    /**
     * makeBrick is a Private Method that creates the different types of bricks.
     * @param point     brick position/location.
     * @param size      size of the brick.
     * @param type      the type of brick, CLAY, CEMENT or STEEL.
     * @return          returns the brick.
     */
    private BrickController makeBrick(Point point, Dimension size, int type){
        BrickController out;
        switch(type){
            case CLAY:
                out = new ClayBrickModel(point,size);
                break;
            case STEEL:
                out = new SteelBrickModel(point,size);
                break;
            case CEMENT:
                out = new CementBrickModel(point, size);
                break;
            default:
                throw  new IllegalArgumentException(String.format("Unknown Type:%d\n",type));
        }
        return  out;
    }

}
