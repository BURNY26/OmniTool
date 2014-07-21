/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Bernard
 */
public class OmniTool extends Application {

    // 1.oorspronkelijke csv en landed moeten nog geconverteerd worden naar utf-8 encoding
    // 2.blijkbaar is er in localisation\text1.csv een stuk met de link tussen provincenumber en de naam getoond op
    // de kaart
    // 3. lijst opstellen met conventies die opgevolgd moeten worden indien er gebruik wordt gemaakt van dit programma 
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, UnsupportedEncodingException {
        final ProvincesPanel p = new ProvincesPanel(false, false);
        final Map m = new Map(new File("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Crusader Kings II\\map\\provinces.bmp"));
        m.setCountyMap(p.getCountyList());
        BufferedImage bi = m.convertColorArrayToBufIm(m.getLevelMap());
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        final ScrollPane scroll = new ScrollPane();
        scroll.setContent(new ImageView(m.convertBIToImage(bi)));
        ComboBox cb = new ComboBox();
        cb.getItems().addAll("county", "duchy", "kingdom", "empire");
        cb.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (t1.equals("county")) {
                    m.setCountyMap(p.getCountyList());
                    BufferedImage bi = m.convertColorArrayToBufIm(m.getLevelMap());
                    scroll.setContent(new ImageView(m.convertBIToImage(bi)));
                } else if (t1.equals("duchy")) {
                    m.setDuchyMap(p.getCountyList());
                    BufferedImage bi = m.convertColorArrayToBufIm(m.getLevelMap());
                    scroll.setContent(new ImageView(m.convertBIToImage(bi)));
                } else if (t1.equals("kingdom")) {
                    m.setKingdomMap(p.getCountyList());
                    BufferedImage bi = m.convertColorArrayToBufIm(m.getLevelMap());
                    scroll.setContent(new ImageView(m.convertBIToImage(bi)));
                } else if (t1.equals("empire")) {
                    m.setEmpireMap(p.getCountyList());
                    BufferedImage bi = m.convertColorArrayToBufIm(m.getLevelMap());
                    scroll.setContent(new ImageView(m.convertBIToImage(bi)));
                }
            }

        });
        cb.setValue("county");

        StackPane root = new StackPane();
        root.getChildren().add(scroll);

        root.getChildren().add(cb);
        

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
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
