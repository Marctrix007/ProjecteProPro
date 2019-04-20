/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marc Padrós Jiménez
 */

import java.io.File; 
import java.util.Scanner; 
import java.io.FileNotFoundException;

public class Gestio {
    
    
    
    
    
    public static void main(String argv[]) {
        
        // Llegim el fitxer de localitzacions
        
        System.out.println("Fitxer de localitzacions: ");
        Scanner loc = new Scanner(System.in);
        File fitLoc = new File(loc.nextLine()); 
        try {
            Scanner liniaFit = new Scanner(fitLoc);
            while (liniaFit.hasNextLine()) {
                // Creem la localització d'on surten les peticions 
                int iden = Integer.parseInt(liniaFit.nextLine());
                String nom = liniaFit.nextLine();
                int popul = Integer.parseInt(liniaFit.nextLine()); 
                Localitzacio origen = new Localitzacio(iden, nom, popul);
                
                
                
            }       
        }
        catch(FileNotFoundException e) {
            System.err.println("Fitxer no existeix \n");
        }
        
        
        // Llegim el fitxer de vehicles 
        
        System.out.println("Fitxer de vehicles: ");
        Scanner veh = new Scanner(System.in);
        File fitVeh = new File(veh.nextLine()); 
        try {
            // falta crear cada vehicle 
            Scanner lin = new Scanner(fitVeh);
            while (lin.hasNextLine()) {
                System.out.println(lin.nextLine());
            }       
        }
        catch(FileNotFoundException e) {
            System.err.println("Fitxer no existeix \n");
        }
        
    
        
    }
    
}
