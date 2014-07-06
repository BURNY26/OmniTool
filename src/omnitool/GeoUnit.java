/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool;

import java.awt.Color;
import javafx.scene.image.Image;

/**
 *
 * @author Bernard
 */
public class GeoUnit {

    private String name;
    private Color RGB;
    private Image COA;

    public GeoUnit(String name, Color RGB, Image COA) {
        this.name = name;
        this.RGB = RGB;
        this.COA = COA;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRGB(Color k) {
        this.RGB = k;
    }

    public void setCOA(Image COA) {
        this.COA = COA;
    }

    public String getName() {
        return name;
    }

    public Color getRGB() {
        return RGB;
    }

    public Image getCOA() {
        return COA;
    }
}
