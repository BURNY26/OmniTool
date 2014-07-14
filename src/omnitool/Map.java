/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Bernard
 */
public class Map {

    BufferedImage map;
    private Color[][] basicMap, levelMap;
    private boolean klaar;

    public Map(File f) {
        try {
            klaar = false;
            map = ImageIO.read(f);
            basicMap = new Color[map.getWidth()][map.getHeight()];
            levelMap = new Color[map.getWidth()][map.getHeight()];
            readBasicMap(map);
        } catch (IOException ex) {
            System.out.println("map kan niet gelezen worden in de map-constructor");
        }
    }

    public void readBasicMap(BufferedImage map) {

        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                int rawcolor = map.getRGB(i, j);
                int red = (rawcolor >> 16) & 0x000000FF;
                int green = (rawcolor >> 8) & 0x000000FF;
                int blue = (rawcolor) & 0x000000FF;
                Color c = new Color(red, green, blue);
                basicMap[i][j] = c;
            }
        }
        klaar = true;
        System.out.println("finito " + klaar);
    }

    public BufferedImage convertColorArrayToBufIm(Color[][] c) {
        BufferedImage bi = new BufferedImage(map.getWidth(), map.getHeight(), TYPE_INT_RGB);
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                bi.setRGB(i, j, c[i][j].getRGB());
            }
        }
        return bi;
    }

    public WritableImage convertBIToImage(BufferedImage bi) {
        WritableImage wr = null;
        if (bi != null) {
            wr = new WritableImage(bi.getWidth(), bi.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bi.getWidth(); x++) {
                for (int y = 0; y < bi.getHeight(); y++) {
                    pw.setArgb(x, y, bi.getRGB(x, y));
                }
            }
        }
        return wr;
    }

    public Color[][] getBasicMap() {
        return basicMap;
    }

    public Color[][] getLevelMap() {
        return levelMap;
    }

    //init levelMap op countyniveau
    public void setCountyMap(ArrayList<County> countyList) {
        //for (int i = 0; i < map.getWidth(); i++) {
        //for (int j = 0; j < map.getHeight(); j++) {
        for (int i = 0; i < basicMap.length; i++) {
            for (int j = 0; j < basicMap[i].length; j++) {
                Color c = basicMap[i][j];
                levelMap[i][j] = getColorCounty(c, countyList);
            }
        }
    }

    public Color getColorCounty(Color kleur, ArrayList<County> countyList) {
        for (County c : countyList) {
            if (c.getBasicRGB()!=null && c.getBasicRGB().equals(kleur)) {
                return c.getRGB();
            }
        }
        return (new Color(0, 0, 0));
    }

    public void displayLevelMap() {
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                System.out.print(basicMap[i][j] + " ");
            }
            System.out.println();
        }
    }
}
