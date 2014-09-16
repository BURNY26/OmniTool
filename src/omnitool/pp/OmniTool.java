/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool.pp;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.ToolTipManager;

/**
 *
 * @author Bernard
 */
public class OmniTool extends Application {

    PointerInfo pointer;
    Point point;
    Robot robot;
    Color color;

    // 1.oorspronkelijke csv en landed moeten nog geconverteerd worden naar utf-8 encoding
    // 2.blijkbaar is er in localisation\text1.csv een stuk met de link tussen provincenumber en de naam getoond op
    // de kaart
    // 3. lijst opstellen met conventies die opgevolgd moeten worden indien er gebruik wordt gemaakt van dit programma 
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, UnsupportedEncodingException, AWTException {

        final ProvincesPanel p = new ProvincesPanel(false, false);
        final Map m = new Map(new File("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Crusader Kings II\\map\\provinces.bmp"));
        m.setCountyMap(p.getGrail());
        BufferedImage bi = m.convertColorArrayToBufIm(m.getLevelMap());
        Button btn = new Button();
        btn.setText("OmniTool!");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("OmniTool!");
            }
        });

        final ScrollPane scroll = new ScrollPane();
        ImageView iv = new ImageView(m.convertBIToImage(bi));
        scroll.setContent(iv);
        
        robot = new Robot();
        pointer = MouseInfo.getPointerInfo();
        point = pointer.getLocation();
        color = robot.getPixelColor((int) point.getX(), (int) point.getY());
        ToolTipManager.sharedInstance().setInitialDelay(1);
        final Tooltip tooltip = new Tooltip();
        tooltip.setText(" " + color);
        scroll.setTooltip(tooltip);
        scroll.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                pointer = MouseInfo.getPointerInfo();
                point = pointer.getLocation();
                color = robot.getPixelColor((int) point.getX(), (int) point.getY());
                tooltip.setText(" " + color);
                System.out.println("Color at: " + point.getX() + "," + point.getY() + " is: " + color);
            }
        });

        final ComboBox cb = new ComboBox();
        cb.getItems().addAll("county", "duchy", "kingdom", "empire");
        cb.setValue("county");
        cb.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (t1.equals("county")) {
                    m.setCountyMap(p.getGrail());
                    BufferedImage bi = m.convertColorArrayToBufIm(m.getLevelMap());
                    scroll.setContent(new ImageView(m.convertBIToImage(bi)));
                } else if (t1.equals("duchy")) {
                    m.setDuchyMap(p.getGrail());
                    BufferedImage bi = m.convertColorArrayToBufIm(m.getLevelMap());
                    scroll.setContent(new ImageView(m.convertBIToImage(bi)));
                } else if (t1.equals("kingdom")) {
                    m.setKingdomMap(p.getGrail());
                    BufferedImage bi = m.convertColorArrayToBufIm(m.getLevelMap());
                    scroll.setContent(new ImageView(m.convertBIToImage(bi)));
                } else if (t1.equals("empire")) {
                    m.setEmpireMap(p.getGrail());
                    BufferedImage bi = m.convertColorArrayToBufIm(m.getLevelMap());
                    scroll.setContent(new ImageView(m.convertBIToImage(bi)));
                }
            }

        });

        scroll.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                pointer = MouseInfo.getPointerInfo();
                point = pointer.getLocation();
                color = robot.getPixelColor((int) point.getX(), (int) point.getY());
                Stage st = new Stage();
                if (cb.valueProperty().getValue().toString().equals("county") && !color.equals(Color.BLACK)) {
                    County c = p.checkPresenceCountybyRGB(color);
                    st.setTitle("County of "+c.getName());
                    st.setScene(new CountyWindow(new StackPane(), 400, 200));
                    st.show();
                } else if (cb.valueProperty().getValue().toString().equals("duchy") && !color.equals(Color.BLACK)) {
                    Duchy d = p.checkPresenceDuchybyRGB(color);
                    st.setTitle("Duchy of "+d.getName());
                    st.setScene(new CountyWindow(new StackPane(), 400, 200));
                    st.show();
                } else if (cb.valueProperty().getValue().toString().equals("kingdom") && !color.equals(Color.BLACK)) {
                    Kingdom k =p.checkPresenceKingdombyRGB(color);
                    st.setTitle("Kingdom of "+k.getName());
                    st.setScene(new CountyWindow(new StackPane(), 400, 200));
                    st.show();
                } else if (cb.valueProperty().getValue().toString().equals("empire") && !color.equals(Color.BLACK)) {
                    Empire e = p.checkPresenceEmpirebyRGB(color);
                    st.setTitle("Empire of "+e.getName());
                    st.setScene(new CountyWindow(new StackPane(), 400, 200));
                    st.show();
                }
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(scroll);

        root.getChildren().add(cb);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("OmniTool!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);

    }

}
