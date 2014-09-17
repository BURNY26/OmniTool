/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool.pp;

import java.awt.AWTException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import music.MusicPanel;
import province.ProvincesPanel;

public class OmniTool extends Application {

    // 1.oorspronkelijke csv en landed moeten nog geconverteerd worden naar utf-8 encoding
    // 2.blijkbaar is er in localisation\text1.csv een stuk met de link tussen provincenumber en de naam getoond op
    // de kaart
    // 3. lijst opstellen met conventies die opgevolgd moeten worden indien er gebruik wordt gemaakt van dit programma 
    @Override
    public void start(final Stage primaryStage) throws FileNotFoundException, UnsupportedEncodingException, AWTException {

        BorderPane root = new BorderPane();
        VBox vb = new VBox();
        
        final Label lblMain = new Label("give the path to the maininstallationpath");
        Button btnMain = new Button("Browse");
        btnMain.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setTitle("Open path to main install of CK2");
                File f = dc.showDialog(primaryStage);
                lblMain.setText(f.toString());
            }
        });
        HBox hb = new HBox();
        hb.getChildren().add(lblMain);
        hb.getChildren().add(btnMain);
        
        final Label lblMod = new Label("give the path to the modfolder");
        Button btnMod = new Button("Browse");
        btnMod.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent t) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setTitle("Open path to main install of CK2");
                File f = dc.showDialog(primaryStage);
                lblMain.setText(f.toString());
            }
            
        });
        HBox hb0 = new HBox();
        hb0.getChildren().add(lblMod);
        hb0.getChildren().add(btnMod);
        
        Button btnMusic = new Button("Music");
        btnMusic.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                MusicPanel mp = new MusicPanel();
                mp.show();
            }
        });
        Button btnProvinces = new Button("Provinces");
        btnProvinces.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                ProvincesPanel pp;
                pp = new ProvincesPanel(false,false);
                pp.show();
            }
        });
        
        
        vb.getChildren().add(btnMusic);
        vb.getChildren().add(btnProvinces);
        vb.setSpacing(5);
        HBox hb1 = new HBox();
        HBox hb2 = new HBox();
        VBox vb2 = new VBox();
        vb2.getChildren().addAll(hb,hb0);

        BorderPane.setAlignment(vb, Pos.CENTER);
        root.setTop(vb2);
        root.setCenter(vb);
        root.setLeft(hb1);
        root.setRight(hb2);
        
        Scene scene = new Scene(root, 500, 600);

        primaryStage.setTitle("OmniTool!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
