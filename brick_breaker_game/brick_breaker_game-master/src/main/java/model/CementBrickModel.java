package model;

import controller.BrickController;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;


/**
 * CementBrickModel class is SubClass/Child Class of the BrickController class.
 * It extends the BrickController class. Inheritance
 * Responsible for all the implementations regarding the Cement Brick.
 */
public class CementBrickModel extends BrickController {


    private static final String NAME = "Cement Brick";
    private static final Color DEF_INNER = new Color(147, 147, 147);
    private static final Color DEF_BORDER = new Color(217, 199, 175);
    private static final int CEMENT_STRENGTH = 2;

    private Crack crack;
    private Shape brickFace;


    /**
     * CementBrickModel is a Parameterized Constructor that runs the Parent Class's, BrickController class constructor.
     * Runs the Crack class constructor as well.
     * Gets the brickface from the Parent class.
     * @param point     brick position/location.
     * @param size      size of the brick.
     */
    public CementBrickModel(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CEMENT_STRENGTH);
        crack = new Crack(DEF_CRACK_DEPTH,DEF_STEPS);
        brickFace = super.brickFace;
    }

    /**
     * makeBrickFace overrides the makeBrickFace in the parent class.
     * This method creates the cement brick.
     * @param pos  the position/location of the brick.
     * @param size size of the brick.
     * @return     returns the brick.
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    }

    /**
     * setImpact is an overridden method from the parent class.
     * Responsible for knowing if an impact has occurred or not.
     * Calls the BrickController's impact() method.
     * @param point point of impact.
     * @param dir   direction of impact.
     * @return      returns a boolean value stating if an impact has occurred or not.
     */
    @Override
    public boolean setImpact(Point2D point, int dir) {
        if(super.isBroken())
            return false;
        super.impact();
        if(!super.isBroken()){
            crack.makeCrack(point,dir);
            updateBrick();
            return false;
        }
        return true;
    }


    /**
     * getBrick implements the Abstract method from the parent class.
     * @return      returns the Cement Brick
     */
    @Override
    public Shape getBrick() {
        return brickFace;
    }

    /**
     * updateBrick is a Private Method that is responsible for updating the Cement brick if not broken.
     */
    private void updateBrick(){
        if(!super.isBroken()){
            GeneralPath gp = crack.draw();
            gp.append(super.brickFace,false);
            brickFace = gp;
        }
    }

    /**
     * repair Method is responsible for repairing the Cement brick.
     * Calls parent class repair() method.
     * Resets the cracks on the cement bricks. Calls the reset() method.
     */
    public void repair(){
        super.repair();
        crack.reset();
        brickFace = super.brickFace;
    }
}
