/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Bernard
 */
//this class reads files
public class SpyGlass {

    private ArrayList<CultureGroup> cglijst;
    private File f;

    public SpyGlass() {
        try {
            cglijst = new ArrayList<>();
            f = new File("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Crusader Kings II\\common\\cultures\\00_cultures.txt");
            BufferedReader br = new BufferedReader(new FileReader(f));
            readCultures(br);
            f = new File("C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Crusader Kings II\\music\\songs.txt");
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException fnfe) {
            System.out.println("00_cultures.txt not found in public SpyGlass()");
        } catch (IOException io) {
            System.out.println("Problem reading 00_cultures.txt");
        }
    }

    public void readCultures(BufferedReader br) throws IOException {
        String huidig;
        huidig = br.readLine();
        while (huidig != null) {
            System.out.println(huidig + " \t\t readCultures");
            if (huidig.contains("= {")) {
                String nameCG = extractName(huidig);
                CultureGroup cg = new CultureGroup(nameCG);
                readSubCultures(br, cg);
                cglijst.add(cg);
            }
            System.out.println(huidig + " \t\t readCultures");
            huidig = br.readLine();
        }
    }

    private void readSubCultures(BufferedReader br, CultureGroup cg) throws IOException {
        String huidig;
        huidig = br.readLine();
        int lacco = 1;
        int racco = 0;
        while (lacco != racco) {
            if (huidig.contains("= {") && !huidig.contains("names") && !huidig.contains("color")) {
                String nameC = extractName(huidig);
                Culture cult = new Culture(nameC);
                cg.offspring.add(cult);
            }
            lacco = lacco + quantChar(huidig, '{');
            racco = racco + quantChar(huidig, '}');
            System.out.println(huidig + " \t\t\t readSubCultures");
            huidig = br.readLine();
        }
    }

    public String extractName(String s) {
        String a = s.trim();
        int gelijkheidsteken = a.indexOf("=");
        return (a.substring(0, gelijkheidsteken - 1));
    }

    private int quantChar(String s, char c) {
        int teller = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                teller++;
            }
        }
        return teller;
    }

    public void writeCGLijst() {
        for (CultureGroup cg : cglijst) {
            System.out.println("========================");
            System.out.println(cg.toString());
        }
    }
}
