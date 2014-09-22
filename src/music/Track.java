/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package music;

import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 *
 * @author Bernard
 */
public class Track extends HBox{
    
    private String name ;
    private ArrayList<Condition> conditions;
    
    public Track(String name){
        conditions = new ArrayList<>();
        this.name=name;
        
        Label lblName = new Label(name);
        lblName.autosize();
        //lblName.setOnMouseClicked(this);
        Button btnConditions = new Button("edit conditions");
        getChildren().add(lblName);
        getChildren().add(btnConditions);
    }
}
