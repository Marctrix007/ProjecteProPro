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
import java.util.ArrayList;
import java.util.Random; 
import java.util.Comparator;
import java.util.PriorityQueue;
import java.io.FileNotFoundException;

public class Gestio {
    
    
    
    
    
    public static void main(String argv[]) {
        
        
        
    
        
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
            if (nom == "PR") {      // Si la localització és un punt de recàrrega 
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
            int hora = fitxerCon.nextInt();
            int minut = fitxerCon.nextInt();
            Temps tTrajecte = new Temps(hora,minut); 
            float distKm = fitxerCon.nextFloat(); 
            m.AfegirConnexio(origen, desti, distKm, tTrajecte);
        }
        
    }
    
    public void CrearMapa() {
        
        Mapa m = new Mapa(); 
        
        CrearLocalitzacions(m);
        CrearConnexions(m);
            
    }
    
     public void CrearVehicles() {
        
        System.out.println("Fitxer de vehicles: ");
        Scanner teclat = new Scanner(System.in);
        File fitVeh = new File(teclat.nextLine()); 
        Scanner fitxerVehicles = new Scanner(fitVeh);
        teclat.close();
        while(fitxerVehicles.hasNext()){
            String matricula = fitxerVehicles.next();
            String model = fitxerVehicles.next();
            String tipus = fitxerVehicles.next();
            float autonomia = fitxerVehicles.nextFloat();
            String carrRapida = fitxerVehicles.next();
            boolean carregaRapida = false;
            if (carrRapida == "SI") 
                carregaRapida = true; 
            int nPlaces = fitxerVehicles.nextInt();
            int hora = fitxerVehicles.nextInt();
            int minut = fitxerVehicles.nextInt();
            Temps tCarrega = new Temps(hora,minut); 
            //afegirVehicle(new Vehicle(matricula,model,tipus,autonomia,carrega,nPlaces, tCarrega));
            
        }
        
        fitxerVehicles.close();
          
    }
    
     
    
     
    public ArrayList<Integer> MaxPeticionsPopul() {
    // Pre: --
    // Post: Retorna el llistat del màxim de peticions que poden produir-se en una localització donat el seu índex de popularitat 
    
        ArrayList<Integer> maxPeticionsPopul = new ArrayList<>(); 
        maxPeticionsPopul.add(0);
        maxPeticionsPopul.add(10);
        maxPeticionsPopul.add(20);
        maxPeticionsPopul.add(30);
        maxPeticionsPopul.add(40);
        maxPeticionsPopul.add(50);
        maxPeticionsPopul.add(60); 
        maxPeticionsPopul.add(70);
        maxPeticionsPopul.add(80);
        maxPeticionsPopul.add(90);
        maxPeticionsPopul.add(100);
        
        return maxPeticionsPopul; 
        
    }
    
    public static float randFloat(float min, float max) {
    // Pre: --
    // Post: Retorna un nombre amb coma flotant contingut entre min i max 
    
        Random rand = new Random();

        float result = rand.nextFloat() * (max - min) + min;

        return result;

    }
    
    public void CrearPeticions(Mapa m) {
    // Pre: m té localitzacions
    // Post: Crea cada petició amb la seva hora de trucada, hora de sortida, punt d'origen, de destí, el nombre de clients que la tramiten 
    //       i l'afegeix a una cua de prioritats ordenada segons l'hora de sortida 
    
        // Creem la cua de prioritats buida 
        Comparator<Peticio> comparator = new ComparadorPeticions();
        PriorityQueue<Peticio> cua = new PriorityQueue<>(0,comparator); 
    
        int iden = 1; 
        for (int i=0; i<m.nLocalitzacions(); i++) {
            // Creem cada petició 
            Localitzacio origen = m.loc(i); 
            int maxPeticionsOrigen = MaxPeticionsPopul().get(origen.popularitat()); 
            int nPeticionsOrigen = (int)randFloat(1,(float)maxPeticionsOrigen);     // Com a mínim en un punt s'atendrà una petició 
            for (int j=0; j<nPeticionsOrigen; j++) {
                float horaTrucada = randFloat(8, (float) 21.75);     // De les 8h a les 21h45 s'atendran les trucades 
                Temps hTrucada = new Temps(horaTrucada); 
                float horaSortida = randFloat(horaTrucada+(float)0.25,22);     // Ha d'haver un marge de 15 minuts entre trucada i recollida (+ 0.25 = + 15 minuts)
                Temps hSortida = new Temps(horaSortida); 
                int d;  
                if (i < m.nLocalitzacions()-1)      // d ha de ser diferent de i (desti != origen) 
                    d = (int)randFloat(i+1,m.nLocalitzacions()-1); 
                else 
                    d = (int)randFloat(0,i-1);
                Localitzacio desti = m.loc(d); 
                int nClients = (int)randFloat(1,4); // Com a mínim 1 client farà la petició i com a molt la faran 4 
                Peticio pet = new Peticio(iden,hTrucada,hSortida,origen,desti,nClients,0); 
                
                // Afegim la petició a la cua
                cua.add(pet);
                iden++;   
            }                
        }
    }   
        
    private class ComparadorPeticions implements Comparator<Peticio> {
    
        @Override
        public int compare(Peticio pet1, Peticio pet2) {
            
            if (pet1.horaSortida().EsMesPetit(pet2.horaSortida()))
            {
                return -1;
            }
            if (pet1.horaSortida().EsMesGran(pet2.horaSortida()))
            {
                return 1;
            }
            return 0;
            
        }   
    }
        
        
        
    
    
    
}
