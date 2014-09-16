/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool.pp;

import java.awt.Color;
import javafx.scene.image.Image;

/**
 *
 * @author Bernard
 */
public class Holding extends GeoUnit {

    public Holding(String name, Color RGB, Image COA) {
        super(name, RGB, COA);
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
    public String toString(){
        return super.getName();
    }
}
