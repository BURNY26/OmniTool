/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool.pp;

import database.Database;
import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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

// TO DO
// nadenken over hoe previous installpaths geimplem dient te worden (evt met Topmenu of via locatie OmniTool.exe)
public class OmniTool extends Application {

    private Database database;
    private Boolean mainInstallFound = false;
    private Boolean modInstallFound = false;
    private Label lblMain;
    private Label lblMod;
    private TextField tfNameMod;

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
        HBox hbSave = new HBox();
        tfNameMod = new TextField();
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
        cb.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == true) {
                    loadInstallData();
                } else {
                    lblMain.setText("");
                    lblMod.setText("");
                    tfNameMod.setText("");
                }
            }
        });

        lblMain = new Label("Path to the maininstallation");
        lblMain.setPrefWidth(350);
        lblMain.getStyleClass().add("pad");
        lblMain.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!lblMain.getText().isEmpty()) {
                    Tooltip tp = new Tooltip(lblMain.getText());
                    lblMain.setTooltip(tp);
                }
            }
        });

        lblMain.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!lblMain.getText().isEmpty()) {
                    mainInstallFound = true;
                } else {
                    mainInstallFound = false;
                }
            }
        });
        final Button btnMain = new Button("Browse");
        btnMain.setPrefSize(100, 20);
        btnMain.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setTitle("Path to the maininstallation");
                File f = dc.showDialog(primaryStage);
                lblMain.setText(f.toString());
                database.setMainInstallFolder(f.toString());
            }
        });

        lblMod = new Label("Path to modsfolder");
        lblMod.setPrefWidth(350);
        lblMod.getStyleClass().add("pad");
        lblMod.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!lblMod.getText().isEmpty()) {
                    Tooltip tp = new Tooltip(lblMod.getText());
                    lblMod.setTooltip(tp);
                }
            }
        });
        lblMod.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!lblMod.getText().isEmpty()) {
                    modInstallFound = true;
                } else {
                    modInstallFound = false;
                }
            }
        });
        final Button btnMod = new Button("Browse");
        btnMod.setPrefSize(100, 20);
        btnMod.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setTitle("Path to modsfolder");
                File f = dc.showDialog(primaryStage);
                lblMod.setText(f.toString());
                lblMod.setContentDisplay(ContentDisplay.RIGHT);
                database.setModInstallFolder(f.toString());
            }
        });

        // controls for the name of the mod added to hbox
        tfNameMod.setPrefSize(300, 20);
        tfNameMod.setText("Name of your mod");
        tfNameMod.addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                database.setNameMod(tfNameMod.getText());
            }

        });
        HBox.setHgrow(tfNameMod, Priority.ALWAYS);
        hbNameMod.getChildren().addAll(tfNameMod);

        //controls for previous installsettings
        hbPrevInstall.getChildren().addAll(cb);
        hbPrevInstall.setSpacing(20);
        hbPrevInstall.setPadding(new Insets(15, 20, 15, 20));

        // controls for maininstallsearch added to hbox
        hbMain.getChildren().addAll(lblMain, btnMain);
        hbMain.setSpacing(20);
        hbMain.setPadding(new Insets(15, 20, 15, 20));
        hbMain.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(lblMain, Priority.ALWAYS);

        // controls for modinstallsearch added to hbox
        hbMod.getChildren().addAll(lblMod, btnMod);
        hbMod.setSpacing(20);
        hbMod.setPadding(new Insets(15, 20, 15, 20));
        hbMod.setAlignment(Pos.CENTER_RIGHT);

        //controls for savebutton
        btnSave = new Button("Save");
        btnSave.setAlignment(Pos.BOTTOM_RIGHT);
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File save = new File(database.getModInstallFolder() + "\\Omnitool.txt");
                saveInstallData(save);
            }
        });
        btnSave.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Tooltip tp = new Tooltip("This will save your paths to a seperate file. \n So next time you only need to check the checkbox to go on with the last used installinfo.");
                btnSave.setTooltip(tp);
            }
        });
        hbSave.getChildren().add(btnSave);
        hbSave.setAlignment(Pos.BOTTOM_RIGHT);

        // hboxes are added to one vbox
        vbPaths.getChildren().addAll(hbPrevInstall, hbNameMod, hbMain, hbMod, hbSave);
        vbPaths.setSpacing(0);
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
                if (selector.contains("music") && mainInstallFound == true && (!tfNameMod.getText().isEmpty() && !tfNameMod.getText().contains("Name of your mod")) && modInstallFound == true) {
                    MusicPanel mp = new MusicPanel(database);
                    mp.setTitle("OmniTool - Music");
                    mp.initModality(Modality.APPLICATION_MODAL);
                    mp.show();
                } else if (selector.contains("provinces") && mainInstallFound == true && (!tfNameMod.getText().isEmpty() && !tfNameMod.getText().contains("Name of your mod")) && modInstallFound == true) {
                    ProvincesPanel pp = new ProvincesPanel(false, false);
                    pp.setTitle("OmniTool - Regionediting");
                    pp.initModality(Modality.APPLICATION_MODAL);
                    pp.show();
                } else {
                    Warning w = new Warning("The maininstall ,modinstall and/or modname haven't been filled in yet. Or you haven't made a choice yet on what you wish to edit.");
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
        scene.getStylesheets().add("/stijl/song.css");

        primaryStage.setTitle("OmniTool!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void saveInstallData(File save) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(save));
            bw.write(database.getMainInstallFolder(), 0, database.getMainInstallFolder().length());
            bw.newLine();
            bw.write(database.getModInstallFolder(), 0, database.getModInstallFolder().length());
            bw.newLine();
            bw.write(database.getNameMod(), 0, database.getNameMod().length());
            bw.flush();
            bw.close();
        } catch (IOException io) {
            System.out.println("Problem writing to installsave");
        }
    }

    public void loadInstallData() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Bernard\\Documents\\Paradox Interactive\\Crusader Kings II\\mod\\OmniTool.txt"));
            database.setMainInstallFolder(br.readLine());
            database.setModInstallFolder(br.readLine());
            database.setNameMod(br.readLine());
            lblMain.setText(database.getMainInstallFolder());
            lblMod.setText(database.getModInstallFolder());
            tfNameMod.setText(database.getNameMod());
        } catch (FileNotFoundException ex) {
            System.out.println("OmniTool.txt niet gevonden bij het zoeken van previnstall");
        } catch (IOException ex) {
            System.out.println("Probleem met het lezen van OmniTool.txt");
        }

    }
}