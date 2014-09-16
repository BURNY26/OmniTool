/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Bernard
 */
public class MusicPanel extends Stage {

    private ArrayList<Track> musicList;

    public MusicPanel() {
        musicList = new ArrayList<>();
        ScrollPane sp = new ScrollPane();
        VBox vb = new VBox();
        Button btnAdd = new Button("Add song");
        vb.getChildren().add(btnAdd);
        sp.setContent(vb);
        
        Scene sc = new Scene(sp, 600, 600);
        sc.getStylesheets().add("/stijl/song.css");
        setScene(sc);
        sizeToScene();
    }
}
