package org.lazaro.rt5e.logic.player;

/**
 * @author Lazaro
 */
public class PlayerDefinition {
    private static final long serialVersionUID = 238268914180215319L;
    private int coordX;
    private int coordY;
    private int coordZ;

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public int getCoordZ() {
        return coordZ;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public void setCoordZ(int corodZ) {
        this.coordZ = corodZ;
    }
}
