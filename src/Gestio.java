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
        // El try catch s'ha de fer al main no al mètode
        Scanner teclat = new Scanner(System.in);
        System.out.println("Fitxer de vehicles: ");
        Scanner fitxerVehicles = new Scanner(new File(teclat.nextLine()));
        teclat.close();
        while(fitxerVehicles.hasNext()){
            String matricula = fitxerVehicles.next();
            String nom = fitxerVehicles.next();
            if (fitxerVehicles.hasNext()){
                nom = nom + " " + fitxerVehicles.next();
            }
            
            int autonomia = fitxerVehicles.nextInt();
            int carrega = fitxerVehicles.nextInt();
            int nPlaces = fitxerVehicles.nextInt();
            
            //afegirVehicle(new Vehicle(matricula,nom,autonomia,carrega,nPlaces));
            
        }
        
        fitxerVehicles.close();
    
        
    }
    
}
