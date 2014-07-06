/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool;

import java.awt.Color;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author Bernard
 */
public class Kingdom extends GeoUnit {

    private int positionLandedTitles, endPositionLandedTitles;
    private GeoUnit superstruct;
    private ArrayList<GeoUnit> substruct;
    private String capital;

    public Kingdom(int positionLandedTitles, String name, Color RGB, Image COA, ArrayList<GeoUnit> substruct, String capital, GeoUnit superstruct, int endPositionLandedTitles) {
        super(name, RGB, COA);
        this.positionLandedTitles = positionLandedTitles;
        this.endPositionLandedTitles = endPositionLandedTitles;
        this.substruct = substruct;
        this.capital = capital;
        this.superstruct = superstruct;
    }

    public void setPositionLandedTitles(int pos) {
        positionLandedTitles = pos;
    }

    public void setSubstruct(ArrayList<GeoUnit> substruct) {
        this.substruct = substruct;
    }

    public void setCapital(String cap) {
        capital = cap;
    }

    public void setSuperstruct(GeoUnit superstruct) {
        this.superstruct = superstruct;
    }

    public void setEndPositionLandedTitles(int endPosition) {
        this.endPositionLandedTitles = endPosition;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setRGB(Color k) {
        super.setRGB(k);
    }

    @Override
    public void setCOA(Image COA) {
        super.setCOA(COA);
    }

    @Override
    public String toString() {
        String a = "";
        for (GeoUnit g : substruct) {
            a += g.toString() + "\n";
        }
        return ("\tName Kingdom: " + super.getName() + "\n \tColor:" + super.getRGB() + "\n \tCapital: " + capital + "\n \tSubstruct: " + a);
    }

    public int getEndPositionLandedTitles() {
        return endPositionLandedTitles;
    }

    public int getPositionLandedTitles() {
        return positionLandedTitles;
    }
}
