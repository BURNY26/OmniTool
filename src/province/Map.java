/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool.pp;

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
import java.util.HashMap;
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
    int mapbreedte;
    int maplengte;

    public Map(File f) {
        try {
            klaar = false;
            map = ImageIO.read(f);
            mapbreedte = map.getWidth();
            maplengte = map.getHeight();
            basicMap = new Color[mapbreedte][maplengte];
            levelMap = new Color[mapbreedte][maplengte];
            readBasicMap(map);
        } catch (IOException ex) {
            System.out.println("map kan niet gelezen worden in de map-constructor");
        }
    }

    public void readBasicMap(BufferedImage map) {
        for (int i = 0; i < mapbreedte; i++) {
            for (int j = 0; j < maplengte; j++) {
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
        BufferedImage bi = new BufferedImage(mapbreedte, maplengte, TYPE_INT_RGB);
        for (int i = 0; i < mapbreedte; i++) {
            for (int j = 0; j < maplengte; j++) {
                bi.setRGB(i, j, c[i][j].getRGB());
            }
        }
        return bi;
    }

    public WritableImage convertBIToImage(BufferedImage bi) {
        WritableImage wr = null;
        if (bi != null) {
            wr = new WritableImage(mapbreedte, maplengte);
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < mapbreedte; x++) {
                for (int y = 0; y < maplengte; y++) {
                    pw.setArgb(x, y, bi.getRGB(x, y));
                }
            }
        }
        return wr;
    }

    //init levelMap op countyniveau
    public void setCountyMap(HashMap<Color, County> hm) {
        for (int i = 0; i < basicMap.length; i++) {
            for (int j = 0; j < basicMap[i].length; j++) {
                Color c = basicMap[i][j];
                if (hm.get(c) != null) {
                    levelMap[i][j] = hm.get(c).getRGB();
                }
                else{
                    levelMap[i][j] = Color.black;
                }
            }
        }
    }

    public void setDuchyMap(HashMap<Color, County> hm) {
        for (int i = 0; i < basicMap.length; i++) {
            for (int j = 0; j < basicMap[i].length; j++) {
                Color c = basicMap[i][j];
                if (hm.get(c) != null) {
                    levelMap[i][j] = hm.get(c).getbRGB();
                }
                else{
                    levelMap[i][j] = Color.black;
                }
            }
        }
    }

    public void setKingdomMap(HashMap<Color, County> hm) {
       for (int i = 0; i < basicMap.length; i++) {
            for (int j = 0; j < basicMap[i].length; j++) {
                Color c = basicMap[i][j];
                if (hm.get(c) != null) {
                    levelMap[i][j] = hm.get(c).getbbRGB();
                }
                else{
                    levelMap[i][j] = Color.black;
                }
            }
        }
    }

    public void setEmpireMap(HashMap<Color, County> hm) {
       for (int i = 0; i < basicMap.length; i++) {
            for (int j = 0; j < basicMap[i].length; j++) {
                Color c = basicMap[i][j];
                if (hm.get(c) != null) {
                    levelMap[i][j] = hm.get(c).getbbbRGB();
                }
                else{
                    levelMap[i][j] = Color.black;
                }
            }
        }
    }


    public void displayLevelMap() {
        for (int i = 0; i < mapbreedte; i++) {
            for (int j = 0; j < maplengte; j++) {
                System.out.print(basicMap[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Color[][] getBasicMap() {
        return basicMap;
    }

    public Color[][] getLevelMap() {
        return levelMap;
    }
}
