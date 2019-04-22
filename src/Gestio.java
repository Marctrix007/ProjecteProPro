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
        
    
        
    }
    
    
    public void CrearVehicles() {
        
        System.out.println("Fitxer de vehicles: ");
        Scanner teclat = new Scanner(System.in);
        File fitVeh = new File(teclat.nextLine()); 
        Scanner fitxerVehicles = new Scanner(fitVeh);
        teclat.close();
        while(fitxerVehicles.hasNext()){
            String matricula = fitxerVehicles.next();
            String marca = fitxerVehicles.next();
            String model = fitxerVehicles.next();
            String tipus = fitxerVehicles.next();
            int autonomia = fitxerVehicles.nextInt();
            int carrega = fitxerVehicles.nextInt();
            int nPlaces = fitxerVehicles.nextInt();
            
            //afegirVehicle(new Vehicle(matricula,nom,model,tipus,autonomia,carrega,nPlaces));
            
        }
        
        fitxerVehicles.close();
          
    }
    
    public void CrearLocalitzacions(Mapa m) {
        
        System.out.println("Fitxer de localitzacions: ");
        Scanner teclat = new Scanner(System.in);
        File fitLoc = new File(teclat.nextLine()); 
       
        Scanner fitxerLoc = new Scanner(fitLoc);
        while (fitxerLoc.hasNextLine()) {
            int iden = fitxerLoc.nextInt();
            String nom = fitxerLoc.next();
            int popul = fitxerLoc.nextInt(); 
            boolean puntRecarr = false; 
            if (nom == "PC") {      // Si la localització és un punt de recàrrega 
                puntRecarr = true;
                int nPlaces = fitxerLoc.nextInt();
                String carrRapid = fitxerLoc.next();
                boolean carregaRapida = false; 
                if (carrRapid == "SI")
                    carregaRapida = true;
                
                Localitzacio puntRec = new PuntDeRecarrega(iden,nom,popul,nPlaces,carregaRapida); 
                m.AfegirLocalitzacio(puntRec);
            }
            else { 
                Localitzacio loc = new Localitzacio(iden,nom,popul);   
                m.AfegirLocalitzacio(loc);
            }
        }
        
    }
    
    public void CrearConnexions(Mapa m) {
        
        System.out.println("Fitxer de connexions: ");
        Scanner teclat = new Scanner(System.in);
        File fitCon = new File(teclat.nextLine()); 
       
        Scanner fitxerCon = new Scanner(fitCon);
        while (fitxerCon.hasNextLine()) {
            int origen = fitxerCon.nextInt();
            int desti = fitxerCon.nextInt();
            int tempsEstimat = fitxerCon.nextInt();
            float distKm = fitxerCon.nextFloat(); 
            m.AfegirConnexio(origen, desti, distKm, tempsEstimat);
        }
        
    }
    
    public void CrearMapa() {
        
        Mapa m = new Mapa(); 
        
        CrearLocalitzacions(m);
        CrearConnexions(m);
            
    }
    
    public void CrearPeticions() {
        
        
        
    }
        
        
    
    
    
}
