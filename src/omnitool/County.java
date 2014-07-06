/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.image.Image;

/**
 *
 * @author Bernard
 */
public class County extends GeoUnit {

    private int positionLandedTitles, endPositionLandedTitles;

    private GeoUnit superstruct;
    private ArrayList<GeoUnit> substruct;
    private String capital;
    private Color basicRGB;

    public County(int positionLandedTitles, String name, Color RGB, Image COA, ArrayList<GeoUnit> substruct, String capital, GeoUnit superstruct, int endPosition) {
        super(name, RGB, COA);
        this.positionLandedTitles = positionLandedTitles;
        this.endPositionLandedTitles = endPosition;
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
    
    public void setBasicRGB(Color c){
        this.basicRGB=c;
    }

    @Override
    public String toString() {
        String a = "";
        return ("\n\t\t\tName County: " + super.getName() + "\n \t\t\tColor:" + super.getRGB());
    }

    public int getEndPositionLandedTitles() {
        return endPositionLandedTitles;
    }

    public int getPositionLandedTitles() {
        return positionLandedTitles;
    }
    
    public Color getBasicRGB(){
        return basicRGB;
    }

}
