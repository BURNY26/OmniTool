/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omnitool;

import java.awt.Color;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Bernard
 */
public class ProvincesPanel extends GridPane {

    private Label lblMap;
    private ComboBox cbLvl;
    private TextField decoupled;
    private ScrollPane scroll;
    private Image basicMap, currentMap;
    private ArrayList<Empire> empireList;
    private ArrayList<Kingdom> kingdomList;
    private ArrayList<Duchy> duchyList;
    private ArrayList<County> countyList;
    private HashMap<Color, String> basicRGB, gameRGB;
    private HashMap<Integer, String> provinceID;
    private HashMap<Integer, Couple> basicProvince;
    private HashMap<Color, County> grail;
    private String lastReadLine;
    private int teller = 0;
    private boolean titularDuchiesFound;

    private String provincesbmpPath = "C:\\Users\\Bernard\\Documents\\Paradox Interactive\\Crusader Kings II\\mod\\viking\\map\\provinces.bmp";
    private File landedtitlesFile = new File("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Crusader Kings II\\common\\landed_titles\\landed_titles.txt");
    private String modPath = "C:\\Users\\Bernard\\Documents\\Paradox Interactive\\Crusader Kings II\\mod\\viking";

    public ProvincesPanel(Boolean vanilla, Boolean kopie) throws FileNotFoundException, UnsupportedEncodingException {
        lastReadLine = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Crusader Kings II\\common\\landed_titles\\landed_titles.txt"), "UTF-8"));
        titularDuchiesFound = false;
        countyList = new ArrayList<>();
        duchyList = new ArrayList<>();
        kingdomList = new ArrayList<>();
        empireList = new ArrayList<>();
        basicRGB = new HashMap<>();
        provinceID = new HashMap<>();
        basicProvince = new HashMap<>();
        grail = new HashMap<>();
        if (vanilla == true) {
            createCopyProvBMPDefCSV();
            createCopyLandedTitles();
            //overridable method call is only dangerous in case of inheritance :http://stackoverflow.com/questions/3404301/whats-wrong-with-overridable-method-calls-in-constructors
        }
        readHistoryProvinces();
        initBasicRGB(retrieveDefinitionsCSVContent(br));
        readHierarchy(br);
        giveCountyBasicRGB();
        giveCountyBossRGB();
    }

    // init de bRGB,bbRGB,bbbRGB met de waarden van de superstructuren
    public void giveCountyBossRGB() {
        for (County c : countyList) {
            c.setbRGB(c.getSuper().getRGB());
            c.setbbRGB(((Duchy) c.getSuper()).getSuper().getRGB());
            c.setbbbRGB(((Kingdom) (((Duchy) c.getSuper()).getSuper())).getSuper().getRGB());
        }
    }

    public void createCopyLandedTitles() {
        File dir = new File(modPath + "\\common\\landed_titles");
        dir.mkdir();
        try {
            FileUtils.copyFile(landedtitlesFile, new File(dir, "landed_titles.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("landed_titles.txt not found in createCopyLandedtitles()");
        }
    }

    public void createCopyProvBMPDefCSV() {
        File provbmp = new File("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Crusader Kings II\\map\\provinces.bmp");
        File defcsv = new File("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Crusader Kings II\\map\\definition.csv");
        File modfolderProvbmp = new File(modPath + "\\map");
        modfolderProvbmp.mkdir();
        try {
            FileUtils.copyFile(provbmp, new File(modfolderProvbmp, "provinces.bmp"));
            FileUtils.copyFile(defcsv, new File(modfolderProvbmp, "definition.csv"));
        } catch (IOException e) {
            System.out.println("provinces.bmp or definition.csv not found in createCopyProvBMPDefCSV");
        }

    }

    // haalt alle informatie uit definitions.csv en zet deze om naar ene bewerkbare string
    public String retrieveDefinitionsCSVContent(BufferedReader br) {
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(modPath + "\\map\\definition.csv"), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String lijn = br.readLine();
            while (lijn != null) {
                sb.append(lijn);
                sb.append("\n");
                lijn = br.readLine();
            }
            String DefCSV = sb.toString();
            return DefCSV;
        } catch (FileNotFoundException fnfe) {
            System.out.println("definition.csv not found for readDefinitionCSV()");
        } catch (IOException io) {
            System.out.println("problem occurred reading definition.csv for readDefinitionCSV()");
        }
        return "Error in readDefinitionCSV()";
    }

    //initialiseert basicRGB met de waarden gevonden uit definitionCSV
    public void initBasicRGB(String definitionCSVContent) {
        String[] lines = definitionCSVContent.split("\n");
        String[] values;
        int i = 0;
        for (String s : lines) {
            values = s.split(";");
            if (!s.isEmpty() && values.length == 6 && i != 0 && !values[0].equals("")) {
                int red = Integer.parseInt(values[1]);
                int green = Integer.parseInt(values[2]);
                int blue = Integer.parseInt(values[3]);
                if (basicProvince.get(Integer.parseInt(values[0])) != null) {
                    basicProvince.get(Integer.parseInt(values[0])).setColor(new Color(red, green, blue));
                }
            }
            i++;
        }
    }

    public void giveCountyBasicRGB() {
        String name = "";
        for (County c : countyList) {
            name = c.getName();
            String basicName = "";
            for (Entry<Integer, Couple> e : basicProvince.entrySet()) {
                basicName = e.getValue().getName();
                if (name.equalsIgnoreCase(purgeString(basicName))) {
                    c.setBasicRGB(e.getValue().getColor());
                    grail.put(e.getValue().getColor(), c);
                }
            }
            checkExceptionName(c);
            if (c.getBasicRGB() == null) {
                System.out.println("Warning: " + c.getName() + " does not have a basic rgb");
            }
        }

    }

    public void readHistoryProvinces() {
        File folder = new File("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Crusader Kings II\\history\\provinces");
        File[] fileList = folder.listFiles();
        System.out.println("readHistoryProvinces");
        for (File s : fileList) {
            filterIDandName(s.getAbsolutePath());
        }
    }

    // zal ....\\provinces\\1 - Vestisland.txt opsplitsen en id + naam in basicProvince steken
    public void filterIDandName(String lijn) {
        int lastSlash = lijn.lastIndexOf('\\');
        int lastStripe = lijn.indexOf("-");
        int lastPoint = lijn.lastIndexOf('.');
        basicProvince.put(Integer.parseInt(lijn.substring(lastSlash + 1, lastStripe - 1).trim()), new Couple(lijn.substring(lastStripe + 2, lastPoint)));
    }

    //controleert of de meegegeven string van def.csv versch is met die van landed_titles
    // jarnbaraland = c_dalarna
    public void checkExceptionName(County c) {
        if (c.getName().equals("dalarna")) {
            for (Entry<Integer, Couple> e : basicProvince.entrySet()) {
                if (e.getValue().getName().equalsIgnoreCase("jarnbaraland")) {
                    c.setBasicRGB(e.getValue().getColor());
                    grail.put(e.getValue().getColor(), c);
                }
            }
        } else if (c.getName().equals("ifni")) {
            for (Entry<Integer, Couple> e : basicProvince.entrySet()) {
                if (e.getValue().getName().equalsIgnoreCase("taroudant")) {
                    c.setBasicRGB(e.getValue().getColor());
                    grail.put(e.getValue().getColor(), c);
                }
            }
        } else if (c.getName().equals("znojmo")) {
            for (Entry<Integer, Couple> e : basicProvince.entrySet()) {
                if (e.getValue().getName().equalsIgnoreCase("morava")) {
                    c.setBasicRGB(e.getValue().getColor());
                    grail.put(e.getValue().getColor(), c);
                }
            }
        } else if (c.getName().equals("wurzburg")) {
            for (Entry<Integer, Couple> e : basicProvince.entrySet()) {
                if (e.getValue().getName().equalsIgnoreCase("franken")) {
                    c.setBasicRGB(e.getValue().getColor());
                    grail.put(e.getValue().getColor(), c);
                }
            }
        } else if (c.getName().equals("padova")) {
            for (Entry<Integer, Couple> e : basicProvince.entrySet()) {
                if (e.getValue().getName().equalsIgnoreCase("padua")) {
                    c.setBasicRGB(e.getValue().getColor());
                    grail.put(e.getValue().getColor(), c);
                }
            }
        } else if (c.getName().equals("padova")) {
            for (Entry<Integer, Couple> e : basicProvince.entrySet()) {
                if (e.getValue().getName().equalsIgnoreCase("padua")) {
                    c.setBasicRGB(e.getValue().getColor());
                    grail.put(e.getValue().getColor(), c);
                }
            }
        } else if (c.getName().equals("al_aqabah")) {
            for (Entry<Integer, Couple> e : basicProvince.entrySet()) {
                if (e.getValue().getName().equalsIgnoreCase("al 'aqabah")) {
                    c.setBasicRGB(e.getValue().getColor());
                    grail.put(e.getValue().getColor(), c);
                }
            }
        } else if (c.getName().equals("asayita")) {
            for (Entry<Integer, Couple> e : basicProvince.entrySet()) {
                if (e.getValue().getName().equalsIgnoreCase("asaita")) {
                    c.setBasicRGB(e.getValue().getColor());
                    grail.put(e.getValue().getColor(), c);
                }
            }
        } else if (c.getName().equals("french_leon")) {
            for (Entry<Integer, Couple> e : basicProvince.entrySet()) {
                if (e.getValue().getName().equalsIgnoreCase("leon") && e.getKey() == 103) {
                    c.setBasicRGB(e.getValue().getColor());
                    grail.put(e.getValue().getColor(), c);
                }
            }
        } else if (c.getName().equals("leon")) {
            for (Entry<Integer, Couple> e : basicProvince.entrySet()) {
                if (e.getValue().getName().equalsIgnoreCase("leon") && e.getKey() == 190) {
                    c.setBasicRGB(e.getValue().getColor());
                    grail.put(e.getValue().getColor(), c);
                }
            }
        }
    }

    public County readCounty(BufferedReader br, GeoUnit superstruct, String countyName) {
        County c = new County(teller, countyName, null, null, null, null, superstruct, 0);
        ArrayList<GeoUnit> substruct = new ArrayList<>();
        c.setSubstruct(substruct);
        try {
            while (!lastReadLine.contains("color")) {
                setLastReadLine(br.readLine());
                System.out.println(lastReadLine + "\t\t rc looking for colour of " + c.getName());
            }
            Color kleur = extractColor(lastReadLine);
            c.setRGB(kleur);
            setLastReadLine(br.readLine());
            System.out.println(lastReadLine + "\t\trc nothing is done with this one ");
            while (startsWithE(lastReadLine) == false && startsWithK(lastReadLine) == false && startsWithD(lastReadLine) == false && startsWithC(lastReadLine) == false && titularDuchiesFound == false) {
                if (lastReadLine.contains("b_") && !lastReadLine.contains("#")) {
                    substruct.add(new Holding(extractName(lastReadLine), null, null));
                    System.out.println(lastReadLine + "\t\t will be added to " + c.getName());
                }
                setLastReadLine(br.readLine());
                System.out.println(lastReadLine + "\t\t rc w1 nothing is done with this one ");
                if (lastReadLine.contains("# TITULAR DUCHIES")) {
                    titularDuchiesFound = true;
                }
            }
            countyList.add(c);
            c.setEndPositionLandedTitles(teller);
            return (c);
        } catch (FileNotFoundException fnfe) {
            System.out.println("landedtitles niet gevonden in readCounty()");
        } catch (IOException ex) {
            System.out.println("er is een probleem opgetreden bij het inlezen van landedtitles in readCounty()");
        }
        return null;
    }

    public Duchy readDuchy(BufferedReader br, GeoUnit superstruct, String duchyName) {
        Duchy d = new Duchy(teller, null, null, null, null, null, superstruct, 0);
        duchyList.add(d);
        d.setName(duchyName);
        ArrayList<GeoUnit> substruct = new ArrayList<>();
        d.setSubstruct(substruct);
        try {
            while (!lastReadLine.contains(duchyName)) {
                setLastReadLine(br.readLine());
                System.out.println(lastReadLine);
                System.out.println("\t\t looking for duchyName: " + duchyName);
            }
            while (!lastReadLine.contains("color")) {
                setLastReadLine(br.readLine());
                System.out.println(lastReadLine + "\t\tlooking for duchyColor of " + d.getName());
            }
            Color kleur = extractColor(lastReadLine);
            d.setRGB(kleur);
            setLastReadLine(br.readLine());
            System.out.println(lastReadLine + "\t\t   rd nothing is done with the line above");
            while (!lastReadLine.contains("capital")) {
                setLastReadLine(br.readLine());
                System.out.println(lastReadLine + "\t\t looking for the capital of " + d.getName());
            }
            String capital = provinceID.get(extractCapital(lastReadLine));
            d.setCapital(capital);
            while (!startsWithC(lastReadLine)) {
                setLastReadLine(br.readLine());
                System.out.println(lastReadLine + " \t\tlooking for a c_ for duchy of : " + d.getName());
            }
            System.out.println(lastReadLine + "\t\t found the c_ for duchy of : " + d.getName());
            while (!startsWithD(lastReadLine) && !startsWithK(lastReadLine) && !startsWithE(lastReadLine) && titularDuchiesFound == false) {
                if (startsWithC(lastReadLine)) {
                    County c = readCounty(br, d, extractName(lastReadLine));
                    substruct.add(c);
                    System.out.println(c.getName() + "\t\t is added to " + c.getSuper().getName());
                }
            }
            d.setEndPositionLandedTitles(teller);
            return d;
        } catch (FileNotFoundException fnfe) {
            System.out.println("landedtitles.txt not found in readDuchy()");
        } catch (IOException io) {
            System.out.println("problem reading landedtitlestxt in readDuchy()");
        }
        return null;
    }

    public Kingdom readKingdom(BufferedReader br, GeoUnit superstruct, String kingdomName) {
        Kingdom k = new Kingdom(teller, kingdomName, null, null, null, null, superstruct, 0);
        kingdomList.add(k);
        ArrayList<GeoUnit> substruct = new ArrayList<>();
        k.setSubstruct(substruct);
        try {
            while (!lastReadLine.contains("color")) {
                setLastReadLine(br.readLine());
                System.out.println(lastReadLine + "\t\t looking for color of " + k.getName());
            }
            Color kleur = extractColor(lastReadLine);
            k.setRGB(kleur);
            while (!lastReadLine.contains("capital")) {
                setLastReadLine(br.readLine());
                System.out.println(lastReadLine + "\t\t looking for capital of k_ : " + k.getName());
            }
            String capital = provinceID.get(extractCapital(lastReadLine));
            k.setCapital(capital);
            while (!startsWithD(lastReadLine) && !startsWithE(lastReadLine)) {
                setLastReadLine(br.readLine());
                System.out.println(lastReadLine + " looking for duchies in k_" + k.getName());
            }
            while (!startsWithK(lastReadLine) && !startsWithE(lastReadLine) && titularDuchiesFound == false) {
                if (startsWithD(lastReadLine)) {
                    while (startsWithD(lastReadLine)) {
                        Duchy d = readDuchy(br, k, extractName(lastReadLine));
                        substruct.add(d);
                        System.out.println("\t\t" + d.getName() + " is added to " + d.getSuper().getName());
                    }
                }
                if (!startsWithD(lastReadLine) && !startsWithK(lastReadLine) && !startsWithE(lastReadLine) && titularDuchiesFound == false) {
                    setLastReadLine(br.readLine());
                    System.out.println(lastReadLine + "\t\t rk nothing is done with this line ");
                }
            }
            k.setPositionLandedTitles(teller);
            return k;
        } catch (FileNotFoundException fnfe) {
            System.out.println("landedtitles.txt not found in readDuchy()");
        } catch (IOException io) {
            System.out.println("problem reading landedtitlestxt in readDuchy()");
        }
        return null;
    }

    public Empire readEmpire(BufferedReader br, String nameEmpire) {
        Empire e = new Empire(teller, null, null, null, null, null, 0);
        empireList.add(e);
        e.setName(nameEmpire);
        ArrayList<GeoUnit> substruct = new ArrayList<>();
        e.setSubstruct(substruct);
        try {

            while (!lastReadLine.contains("color")) {
                setLastReadLine(br.readLine());
                System.out.println(lastReadLine + "\t\t looking for the color to " + nameEmpire);
            }
            Color kleur = extractColor(lastReadLine);
            e.setRGB(kleur);
            while (!lastReadLine.contains("capital")) {
                setLastReadLine(br.readLine());
                System.out.println(lastReadLine + "\t\t looking for the capital to " + nameEmpire);
            }
            String capital = provinceID.get(extractCapital(lastReadLine));
            e.setCapital(capital);
            while (!lastReadLine.startsWith("e_") && titularDuchiesFound == false) {
                if (lastReadLine.contains("k_")) {
                    while (startsWithK(lastReadLine)) {
                        System.out.println(lastReadLine + "\t\t starts with a k_ and will be added to " + nameEmpire);
                        substruct.add(readKingdom(br, e, extractName(lastReadLine)));
                    }
                }
                if ((lastReadLine.contains("e_roman_empire") || !startsWithE(lastReadLine)) && titularDuchiesFound == false) {
                    setLastReadLine(br.readLine());
                    System.out.println(lastReadLine + "\t\t re nothing is done with this line");
                }
            }
            e.setEndPositionLandedTitles(teller);
            return e;
        } catch (FileNotFoundException fnfe) {
            System.out.println("landedtitles.txt not found in readDuchy()");
        } catch (IOException io) {
            System.out.println("problem reading landedtitlestxt in readDuchy()");
        }
        return null;
    }

    public void readHierarchy(BufferedReader br) {
        try {
            while (!lastReadLine.contains("# EMPIRES")) {
                setLastReadLine(br.readLine());
            }
            while (titularDuchiesFound == false) {
                if (lastReadLine.startsWith("e_") && !lastReadLine.contains("e_roman_empire")) {
                    Empire e = readEmpire(br, extractName(lastReadLine));
                    empireList.add(e);
                }
                if (!lastReadLine.startsWith("e_")) {
                    setLastReadLine(br.readLine());
                }
            }
            br.close();
        } catch (FileNotFoundException io) {
            System.out.println("landedtitles.txt not found in readHierarchy()");
        } catch (IOException io) {
            System.out.println("io error in readHierarchy()");
        }
    }

    public boolean isNumber(String a) {
        try {
            int b = Integer.parseInt(a);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public Color extractColor(String lijn) {
        String sb = lijn;
        int a = sb.indexOf("{");
        int b = sb.indexOf("}");
        String rgb = sb.substring(a + 1, b);
        String[] values = rgb.split(" ");
        int firstNumber = getRed(values);
        int red = Integer.parseInt(values[firstNumber]);
        while (red > 255) {
            red -= 255;
        }
        int green = Integer.parseInt(values[firstNumber + 1]);
        while (green > 255) {
            green -= 255;
        }
        int blue = Integer.parseInt(values[firstNumber + 2]);
        while (blue > 255) {
            blue -= 255;
        }
        return (new Color(red, green, blue));
    }

    public String extractName(String lijn) {
        String sb = lijn;
        sb = sb.toLowerCase();
        int a = sb.indexOf("_");
        int b = sb.indexOf("=");
        return sb.substring(a + 1, b).trim();
    }

    public int extractCapital(String lijn) {
        String[] values = lijn.split(" ");
        for (int i = 0; i < values.length; i++) {
            if (isNumber(values[i])) {
                return (Integer.parseInt(values[i]));
            }
        }
        return 0;
    }

    public boolean startsWithE(String lijn) {
        return lijn.trim().startsWith("e_");
    }

    public boolean startsWithK(String lijn) {
        return lijn.trim().startsWith("k_");
    }

    public boolean startsWithD(String lijn) {
        return lijn.trim().startsWith("d_");
    }

    public boolean startsWithC(String lijn) {
        return lijn.trim().startsWith("c_");
    }

    // wnr bufferedreader leest ,gaat teller++
    public void setLastReadLine(String lijn) {
        lastReadLine = lijn;
        teller++;
    }

    public void displayHierarchy() {
        for (Empire e : empireList) {
            System.out.println(e.toString());
        }
        System.out.println("end empire");
        for (Kingdom k : kingdomList) {
            System.out.println(k.toString());
        }
        System.out.println("end kingdom");
        for (Duchy d : duchyList) {
            System.out.println(d.toString());
        }
        System.out.println("end duchy");
        for (County c : countyList) {
            System.out.println(c.toString());
        }

    }

    public int getRed(String[] values) {
        int i = 0;
        int j = -1;
        for (String s : values) {
            try {
                j++;
                i = Integer.parseInt(s);
                return j;
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }

    //de methode geconstateerd om een waarde in te geven bij een hashmap en de sleutel terug te krijgen, nu nog omzetten naar een methoe die bruikbaar is met countylist
    //!!!!!readHierarchy moet al uitgevoerd zijn zodat countyList != null
    public void displayCountyBasicRGB(ArrayList<County> countyList) {
        for (County c : countyList) {
            System.out.println(c.getBasicRGB());
        }
    }

    public ArrayList<County> getCountyList() {
        return countyList;
    }

    public ArrayList<Duchy> getDuchyList() {
        return duchyList;
    }

    public ArrayList<Kingdom> getKingdomList() {
        return kingdomList;
    }

    public ArrayList<Empire> getEmpireList() {
        return empireList;
    }

    public HashMap<Integer, Couple> getBasicProvince() {
        return basicProvince;
    }

    public String purgeString(String a) {
        a = a.toLowerCase();
        if (a.contains("ä")) {
            while (a.contains("ä")) {
                a = a.replace('ä', 'a');
            }
        }
        if (a.contains("à")) {
            while (a.contains("à")) {
                a = a.replace('à', 'a');
            }
        }
        if (a.contains("å")) {
            while (a.contains("å")) {
                a = a.replace('å', 'a');
            }
        }
        if (a.contains("á")) {
            while (a.contains("á")) {
                a = a.replace('á', 'a');
            }
        }
        if (a.contains("Á")) {
            while (a.contains("Á")) {
                a = a.replace("Á", "a");
            }
        }
        if (a.contains("ö")) {
            while (a.contains("ö")) {
                a = a.replace('ö', 'o');
            }
        }
        if (a.contains("ó")) {
            while (a.contains("ó")) {
                a = a.replace('ó', 'o');
            }
        }
        if (a.contains("ø")) {
            while (a.contains("ø")) {
                a = a.replace('ø', 'o');
            }
        }
        if (a.contains("é")) {
            while (a.contains("é")) {
                a = a.replace("é", "e");
            }
        }
        if (a.contains("É")) {
            while (a.contains("É")) {
                a = a.replace("É", "e");
            }
        }
        if (a.contains("è")) {
            while (a.contains("è")) {
                a = a.replace("è", "e");
            }
        }
        if (a.contains("ñ")) {
            while (a.contains("ñ")) {
                a = a.replace("ñ", "n");
            }
        }
        if (a.contains("ü")) {
            while (a.contains("ü")) {
                a = a.replace("ü", "u");
            }
        }
        if (a.contains("ç")) {
            while (a.contains("ç")) {
                a = a.replace("ç", "c");
            }
        }
        if (a.contains(". ")) {
            while (a.contains(". ")) {
                a = a.replace(". ", "_");
            }
        }
        if (a.contains("\'")) {
            while (a.contains("\'")) {
                a = a.replace("\'", "_");
            }
        }
        if (a.contains(" ")) {
            while (a.contains(" ")) {
                a = a.replace(" ", "_");
            }
        }
        if (a.contains("í")) {
            while (a.contains("í")) {
                a = a.replace("í", "i");
            }
        }
        return a;
    }

    public County checkPresenceCountybyRGB(Color color) {
        for (County c : getCountyList()) {
            if (c.getRGB().equals(color)) {
                return c;
            }
        }
        return null;
    }

    public Duchy checkPresenceDuchybyRGB(Color color) {
        for (Duchy d : getDuchyList()) {
            if (d.getRGB().equals(color)) {
                return d;
            }
        }
        return null;
    }

    public Kingdom checkPresenceKingdombyRGB(Color color) {
        for (Kingdom k : getKingdomList()) {
            if (k.getRGB().equals(color)) {
                return k;
            }
        }
        return null;
    }

    public Empire checkPresenceEmpirebyRGB(Color color) {
        for (Empire e : getEmpireList()) {
            if (e.getRGB().equals(color)) {
                return e;
            }
        }
        return null;
    }

    public HashMap<Color, County> getGrail() {
        return grail;
    }

}
