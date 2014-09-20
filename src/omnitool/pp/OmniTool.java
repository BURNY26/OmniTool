/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool.pp;

import database.Database;
import java.awt.AWTException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import music.MusicPanel;
import music.Warning;
import province.ProvincesPanel;

public class OmniTool extends Application {

    private Database database;
    private Boolean mainInstallFound = false;
    private Boolean modInstallFound = false;

    // 1.oorspronkelijke csv en landed moeten nog geconverteerd worden naar utf-8 encoding
    // 2.blijkbaar is er in localisation\text1.csv een stuk met de link tussen provincenumber en de naam getoond op
    // de kaart
    // 3. lijst opstellen met conventies die opgevolgd moeten worden indien er gebruik wordt gemaakt van dit programma 
    @Override
    public void start(final Stage primaryStage) throws FileNotFoundException, UnsupportedEncodingException, AWTException {
        //init of all hboxes, vboxes and panel
        GridPane gp = new GridPane();
        VBox vb = new VBox();
        VBox vbPaths = new VBox();
        HBox hbNameMod = new HBox();
        HBox hbMain = new HBox();
        HBox hbMod = new HBox();
        HBox hbPrevInstall = new HBox();
        final TextField tfNameMod = new TextField();
        final Button btnEdit;
        final Button btnHistory;
        final Button btnSave;
        final Button info;

        btnEdit = new Button("Edit");
        btnEdit.setPrefSize(100, 20);
        btnHistory = new Button("Changelog");
        btnHistory.setPrefSize(100, 20);
        final Button btnInfo = new Button("Info");
        btnInfo.setPrefSize(100, 20);

        Database db = new Database();
        database = db;
        database.setPrimaryStage(primaryStage);

        // decl+init of the controls that help with the installpaths 
        CheckBox cb = new CheckBox("Use previous installsettings");
        CheckBox cb2 = new CheckBox("Save current installsetting");
        cb2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                File save = new File(database.getModInstallFolder() + "\\Omnitool.txt");
                System.out.println(database.getNameMod());
                saveInstallData(save);
            }
        });

        final Label lblMain = new Label("give the path to the maininstallationpath");
        final Button btnMain = new Button("Browse");
        btnMain.setPrefSize(100, 20);
        btnMain.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setTitle("Open path to main install of CK2");
                File f = dc.showDialog(primaryStage);
                lblMain.setText(f.toString());
                database.setMainInstallFolder(f.toString());
                mainInstallFound = true;
            }
        });

        final Label lblMod = new Label("give the path to the modfolder");
        final Button btnMod = new Button("Browse");
        btnMod.setPrefSize(100, 20);
        btnMod.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setTitle("Open path to modfolder of CK2");
                File f = dc.showDialog(primaryStage);
                lblMod.setText(f.toString());
                database.setModInstallFolder(f.toString());
                modInstallFound = true;
            }
        });

        // controls for the name of the mod added to hbox
        Label lblNameMod = new Label("What is going to be the name of your mod?");
        tfNameMod.setPrefSize(300, 20);
        tfNameMod.addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>(){

            @Override
            public void handle(KeyEvent event) {
                database.setNameMod(tfNameMod.getText());
            }
            
        });
        HBox.setHgrow(tfNameMod, Priority.ALWAYS);
        hbNameMod.getChildren().addAll(lblNameMod, tfNameMod);

        //controls for previous installsettings
        hbPrevInstall.getChildren().addAll(cb, cb2);
        hbPrevInstall.setSpacing(20);
        hbPrevInstall.setPadding(new Insets(15, 20, 15, 20));

        // controls for maininstallsearch added to hbox
        hbMain.getChildren().addAll(lblMain, btnMain);
        hbMain.setSpacing(20);
        hbMain.setPadding(new Insets(15, 20, 15, 20));
        HBox.setHgrow(btnMain, Priority.ALWAYS);

        // controls for modinstallsearch added to hbox
        HBox.setHgrow(btnMod, Priority.ALWAYS);
        hbMod.getChildren().addAll(lblMod, btnMod);
        hbMod.setSpacing(20);
        hbMod.setPadding(new Insets(15, 20, 15, 20));

        // hboxes are added to one vbox
        vbPaths.getChildren().addAll(hbPrevInstall, hbNameMod, hbMain, hbMod);
        vbPaths.setSpacing(10);
        vbPaths.setPadding(new Insets(15, 20, 15, 20));

        gp.add(vbPaths, 1, 1, 8, 2);

        //list with modoptions
        ObservableList<String> options = FXCollections.observableArrayList("music", "provinces", "culture", "events");
        final ListView<String> lijst = new ListView<>(options);
        StackPane lijstStack = new StackPane();
        lijstStack.getChildren().add(lijst);
        lijstStack.setPadding(new Insets(15, 20, 15, 20));

        gp.add(lijstStack, 1, 4, 4, 9);

        //setting functionality of interactivitybuttons used by each topic + graph. properties
        btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                String selector = lijst.getSelectionModel().getSelectedItems().toString();
                if (selector.contains("music") && mainInstallFound == true && (!tfNameMod.getText().isEmpty()) && modInstallFound == true) {
                    blockPaths(tfNameMod, btnMain, btnMod);
                    MusicPanel mp = new MusicPanel(database);
                    mp.setTitle("OmniTool - Music");
                    mp.initModality(Modality.APPLICATION_MODAL);
                    mp.show();
                } else if (selector.contains("provinces") && mainInstallFound == true && (!tfNameMod.getText().isEmpty()) && modInstallFound == true) {
                    blockPaths(tfNameMod, btnMain, btnMod);
                    ProvincesPanel pp = new ProvincesPanel(false, false);
                    pp.setTitle("OmniTool - Regionediting");
                    pp.initModality(Modality.APPLICATION_MODAL);
                    pp.show();
                } else {
                    Warning w = new Warning();
                    w.show();
                }
            }

        });

        // placing interactivity buttons in a vbox
        vb.getChildren().addAll(btnEdit, btnHistory, btnInfo);
        vb.setSpacing(20);
        vb.setPadding(new Insets(20));

        gp.add(vb, 6, 4, 8, 9);

        Scene scene = new Scene(gp, 500, 600);

        primaryStage.setTitle("OmniTool!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void blockPaths(TextField tfNameMod, Button btnMain, Button btnMod) {
        database.setNameMod(tfNameMod.getText());
        tfNameMod.setEditable(false);
        btnMain.setDisable(true);
        btnMod.setDisable(true);
    }

    public void saveInstallData(File save) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(save));
            bw.write(database.getMainInstallFolder(), 0, database.getMainInstallFolder().length());
            bw.newLine();
            bw.write(database.getModInstallFolder(), 0, database.getModInstallFolder().length());
            bw.newLine();
            bw.write(database.getNameMod(),0,database.getNameMod().length());
            bw.flush();
            bw.close();
        } catch (IOException io){
            System.out.println("Problem writing to installsave");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
