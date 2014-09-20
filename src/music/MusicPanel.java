/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music;

import database.Database;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Bernard
 */
public class MusicPanel extends Stage {

    private ArrayList<Track> musicList;

    public MusicPanel(final Database d) {
        musicList = new ArrayList<>();
        GridPane gp = new GridPane();

        final ObservableList<String> nummers = FXCollections.observableArrayList();
        final ListView<String> lijst = new ListView<>(nummers);
        gp.add(lijst, 1, 1, 4, 4);

        VBox vb = new VBox();
        Button btnAdd = new Button("Add song");

        btnAdd.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                File dir = new File(d.getModInstallFolder() + "\\" + d.getNameMod() + "\\music");
                dir.mkdirs();
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("OGG", "*.ogg"));
                List<File> bestandsLijst = fc.showOpenMultipleDialog(d.getPrimaryStage());
                for (File f : bestandsLijst) {
                    if (f != null) {
                        try {
                            FileUtils.copyFile(f, new File(dir + "\\" + f.getName()));
                        } catch (IOException ex) {
                            System.out.println("Problem copying oggfile.");
                        }
                        nummers.add(f.getName());
                    }
                }
            }
        });

        btnAdd.setPrefSize(100, 20);
        Button btnProps = new Button("Properties");
        btnProps.setPrefSize(100, 20);
        vb.getChildren().addAll(btnAdd, btnProps);
        vb.setPadding(new Insets(20));
        vb.setSpacing(20);
        gp.add(vb, 5, 1, 7, 4);

        Scene sc = new Scene(gp, 600, 600);
        sc.getStylesheets().add("/stijl/song.css");
        setScene(sc);
        sizeToScene();
    }
}
