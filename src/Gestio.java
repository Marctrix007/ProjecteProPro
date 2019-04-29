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
import java.util.Random; 
import java.util.TreeSet;
import java.io.FileNotFoundException;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Gestio {
    
    // Cua de prioritats que conté les peticions 
    private SortedSet<Peticio> peticions;
    private final Temps tEspera = new Temps(0,10); 
    private Mapa mapa;
    
    
    public static void main(String argv[]) {
        
        //CrearLocalitzacions(); 
 
       CrearConnexions(); 
        
    }
    
    
    public static void CrearLocalitzacions() {
    // Pre: --
    // Post: Crea les localitzacions que poden ser punts de recàrrega o no i les afegeix al mapa    
    
        System.out.println("Fitxer de localitzacions: ");
        Scanner teclat = new Scanner(System.in);
        File fitLoc = new File(teclat.nextLine());
        
        try {
           
            Scanner fitxerLoc = new Scanner(fitLoc);
            while (fitxerLoc.hasNextLine()) {
                String linia = fitxerLoc.nextLine();
                String[] liniaArr = linia.split(","); 
                String iden = liniaArr[0];
                System.out.println(iden);
                String nom = liniaArr[1]; 
                System.out.println(nom);
                int popul = Integer.parseInt(liniaArr[2]); 
                System.out.println(popul);
                if ("PR".equals(nom)) {      // Si la localització és un punt de recàrrega
                    int nPlaces = Integer.parseInt(liniaArr[3]); 
                    System.out.println(nPlaces);
                    String carrRapid = liniaArr[4]; 
                    System.out.println(carrRapid);
                    boolean carregaRapida = false;
                    if ("SI".equals(carrRapid))
                        carregaRapida = true;
                    
                    //Localitzacio puntRec = new PuntDeRecarrega(nom,popul,nPlaces,carregaRapida);
                    //mapa.AfegirLocalitzacio(iden,puntRec);
                }
                else {
                    //Localitzacio loc = new Localitzacio(nom,popul);
                    //mapa.AfegirLocalitzacio(iden,loc);
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println("El fitxer no existeix\n");
        }
    }
    
    
    public static void CrearConnexions() {
    // Pre: --
    // Post: Crea les connexions entre les localitzacions i les afegeix al mapa 
    
        System.out.println("Fitxer de connexions: ");
        Scanner teclat = new Scanner(System.in);
        File fitCon = new File(teclat.nextLine());
        
        try {
   
            Scanner fitxerCon = new Scanner(fitCon);
            while (fitxerCon.hasNextLine()) {
                String linia = fitxerCon.nextLine(); 
                String[] liniaArr = linia.split(","); 
                String id = liniaArr[0];
                System.out.println(id);
                String origen = liniaArr[1];
                System.out.println(origen);
                String desti = liniaArr[2];
                System.out.println(desti);
                String temps = liniaArr[3]; 
                String[] tempsArr = temps.split(":"); 
                int hora = Integer.parseInt(tempsArr[0]);
                int minut = Integer.parseInt(tempsArr[1]);
                Temps tTrajecte = new Temps(hora,minut);
                System.out.println(tTrajecte);
                float distKm = Float.parseFloat(liniaArr[4]); 
                System.out.println(distKm); 
                //mapa.AfegirConnexio(id, origen, desti, distKm, tTrajecte);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Gestio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /*
    public void CrearMapa() {
    // Pre: --
    // Post: Crea un mapa amb les seves localitzacions i les connexions entre aquestes 
    
        Mapa m = new Mapa(); 
        
        CrearLocalitzacions();
        CrearConnexions();
            
    }
    */
    /*
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
            
            
            
            //afegir_vehicle(new Vehicle(matricula,model,tipus,autonomia,carrega,nPlaces, tCarrega));
            
        }
        
        fitxerVehicles.close();
          
    }
    */
    /*
    public void afegir_vehicle(Vehicle v){
        
        PuntDeRecarrega p; 
        do{
           p = mapa.randomPuntDeRecarrega();
        }while(p.PlacesLliures()<0);
        
        p.EstacionarVehicle(v, new Temps(7,0));
       
    }
      */  
    public static float randFloat(float min, float max) {
    // Pre: --
    // Post: Retorna un nombre amb coma flotant contingut entre min i max 
    
        Random rand = new Random();

        float result = rand.nextFloat() * (max - min) + min;

        return result;

    }
    
    public void CrearPeticions() {
    // Pre: 
    // Post: Crea cada petició amb el seu identificador, hora de trucada, hora de sortida, punt d'origen, de destí, el nombre de clients que la tramiten, 
    //       el seu estat inicial i l'afegeix a una cua de prioritats ordenada segons l'hora de sortida 
    
        // Creem les peticions  
        peticions = new TreeSet<>(); 
        
        // Creem una taula amb el màxim de peticions que poden produir-se en una localització
        // segons el seu índex de popularitat 
        int[] maxPeticionsPopul = {0,10,20,30,40,50,60,70,80,90,100};
        
        int iden = 1; 
        for (int i=0; i<mapa.nLocalitzacions(); i++) { // i és l'identificador de cada localització 
            Localitzacio origen = mapa.loc(i); 
            int maxPeticionsOrigen = maxPeticionsPopul[origen.popularitat()] / mapa.nLocalitzacions(); 
            int nPeticionsOrigen = (int)randFloat(0,(float)maxPeticionsOrigen);     // Com a mínim en un punt s'atendrà una petició 
            for (int j=0; j<nPeticionsOrigen; j++) {
                float horaTrucada = randFloat(8, (float) 21.75);     // De les 8h a les 21h45 s'atendran les trucades 
                Temps hTrucada = new Temps(horaTrucada); 
                float horaSortida = randFloat(horaTrucada+(float)0.25,22);     // Ha d'haver un marge de 15 minuts entre trucada i recollida (+ 0.25 = + 15 minuts)
                Temps hSortida = new Temps(horaSortida); 
                // Obtenim aleatoriament una localització de destí diferent a la d'origen 
                int des;    // des és l'identificador de la localització de destí 
                do {
                    des = (int)randFloat(1,(float)mapa.nLocalitzacions()); 
                }while (des == i); 
                Localitzacio desti = mapa.loc(des); 
                int nClients = (int)randFloat(1,4); // Com a mínim 1 client farà la petició i com a molt la faran 4 
                // Creem la petició
                // L'estat inicial és 0, que vol dir, que s'ha d'atendre la petició 
                Peticio pet = new Peticio(iden,hTrucada,hSortida,origen,desti,nClients,0);    
                // Afegim la petició a la cua
                peticions.add(pet); 
                iden++;   
            }                
        }
    }   
        
       
}
