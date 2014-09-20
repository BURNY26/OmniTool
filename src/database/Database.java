/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import javafx.stage.Stage;

/**
 *
 * @author Bernard
 */
public class Database {
    private String mainInstallFolder;
    private String modInstallFolder;
    private Stage primaryStage;
    private String nameMod;

    public String getNameMod() {
        return nameMod;
    }

    public void setNameMod(String nameMod) {
        this.nameMod = nameMod;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public String getMainInstallFolder() {
        return mainInstallFolder;
    }

    public void setMainInstallFolder(String mainInstallFolder) {
        this.mainInstallFolder = mainInstallFolder;
    }

    public String getModInstallFolder() {
        return modInstallFolder;
    }

    public void setModInstallFolder(String modInstallFolder) {
        this.modInstallFolder = modInstallFolder;
    }
    
    
}
