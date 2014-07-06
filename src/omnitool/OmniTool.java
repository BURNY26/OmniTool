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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Bernard
 */
public class OmniTool extends Application {
    
    // er zijn tekens met trema die problemen geven met het inlezen
    @Override
    public void start(Stage primaryStage) {
        ProvincesPanel p = new ProvincesPanel(false,false);
        try{
            
        BufferedReader br = new BufferedReader (new FileReader("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Crusader Kings II\\common\\landed_titles\\landed_titles.txt"));
        p.readHierarchy(br);
        } catch (IOException e){
            System.out.println("landed_titles.txt niet gevonden in main()");
        }
        p.giveCountyBasicRGB(p.getBasicRGB());
        Map m = new Map(new File("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Crusader Kings II\\map\\provinces.bmp"));
        //m.displayLevelMap();
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

        
        //Label lbl = new Label("test",new ImageView(m.convertBIToImage(bi)));
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(new ImageView(m.convertBIToImage(bi)));
        StackPane root = new StackPane();
        root.getChildren().add(scroll);
        
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
