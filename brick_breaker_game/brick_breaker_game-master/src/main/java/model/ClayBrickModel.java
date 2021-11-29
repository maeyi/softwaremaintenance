package model;

import controller.BrickController;

import java.awt.*;


/**
 * ClayBrickModel class is SubClass/Child Class of the BrickController class.
 * It extends the BrickController class. Inheritance
 * Responsible for all the implementations regarding the Clay Brick.
 */
public class ClayBrickModel extends BrickController {

    private static final String NAME = "Clay Brick";
    private static final Color DEF_INNER = new Color(255, 255, 0).brighter();//clay brick color as yellow
    private static final Color DEF_BORDER = Color.BLACK;//border color as black colour
    private static final int CLAY_STRENGTH = 1;


    /**
     * ClayBrickModel is a Parameterized Constructor that runs the Parent Class's, BrickController class constructor.
     * @param point     brick position/location.
     * @param size      size of the brick.
     */
    public ClayBrickModel(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CLAY_STRENGTH);
    }

    /**
     * makeBrickFace overrides the makeBrickFace in the parent class.
     * This method creates the clay brick.
     * @param pos   the position/location of the brick.
     * @param size  size of the brick.
     * @return      returns the brick.
     */
    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    }

    /**
     * getBrick implements the Abstract method from the parent class.
     * @return      returns the Clay Brick.
     */
    @Override
    public Shape getBrick() {
        return super.brickFace;
    }


}
