/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Bernard
 */
public class Warning extends Stage{
    
    public Warning(){
        Label lblTxt = new Label("The maininstall ,modinstall or modname haven't been filled in yet.");
        StackPane sp = new StackPane();
        sp.getChildren().add(lblTxt);
        Scene sc = new Scene (sp,500,20);
        setScene(sc);
        sizeToScene();
        this.show();
    }
    
}
