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
import java.util.ArrayList; 
import org.graphstream.graph.Path;



public class Gestio {
    
    // Cua de prioritats que conté les peticions 
    private static SortedSet<Peticio> peticions;
    private Mapa mapa;
    private Estadistica stats;
    
    
    public static void main(String argv[]) {
        
        //CrearLocalitzacions(); 
 
       CrearConnexions(); 
       AtendrePeticions(); 
        
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
                    //stats.guardarPuntRC(puntRec);
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
            System.err.println("El fitxer no existeix\n");

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
            
            
            Vehicle v = new Vehicle(matricula,model,tipus,autonomia,carrega,nPlaces, tCarrega);
            //afegirVehicle(v);
            stats.guardarVehicle(v);
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
    // Pre: --
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
                    des = (int)randFloat(0,(float)mapa.nLocalitzacions()); 
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
    
    public static void AtendrePeticions() {
    // Pre: --  
    // Post: Aten totes les peticions
        
    
        // Demanem per teclat el temps d'espera màxim de les peticions 
        System.out.println("Temps d'espera màxim de les peticions: ");
        Scanner s = new Scanner(System.in);
        String tEsp = s.next(); 
        Temps tEspMax = new Temps(tEsp); // falta fer un conversor a la classe Temps de String a Temps 
        
        // Creem un objecte estadístics que contindrà totes les estadístiques de la simulació 
        Estadistics est = new Estadistics(); 
        // Guardem el nombre de peticions  
        est.GuardarNombrePeticions(peticions.size());
        
        
        while (!peticions.isEmpty()) {
            TractarPeticio(peticions.first(), tEspMax, est); 
        }
        
    }
    
    
    public static void TractarPeticio(Peticio pet, Temps tEspMax, Estadistics est) {
        
        // Es demana un vehicle al punt de recàrrega més proper, que pugui atendre la petició 
        Vehicle v = null;
        PuntDeRecarrega pIni = null; 
        DemanarPuntDeRecarregaVehiclePerAtendrePeticio(pet, tEspera, v, pIni); 
        
        if (v != null) {    // Si s'ha trobat un vehicle per atendre la petició  
            Estadistics est = new Estadistics(); 
            // S'envia el vehicle des del punt de recàrrega fins al punt d'origen de la petició  
            PuntDeRecarrega pFi = mapa.PuntDeRecarregaMesProperA(pet.desti.numero()); 
            Ruta rVehicle = ObtenirRutaVehicle(pet, pIni,pFi); 
            // rutes[v.numero][v.nRutes] = rVehicle a Estadistics  
            est.AfegirRutaVehicle(v.numero(),v.nRutes(),rVehicle); 
            for (Localitzacio loc: rVehicle.LlistaLocalitzacions()) {
                while (peticions.)
            }
            
            while (rVehicle.ultimaLoc != pFi) {
                
                
                
            }
            rVehicle.AfegirLocalitzacio(rMinPrIniOri.primeraLoc()); 
            
            // El vehicle ja arribat al punt d'origen de la petició 
            // Comprova si hi ha més peticions en aquell 
//rutes[v.numero()][v.nRutes()].add(Ruta(p.numero(),pet.origen.numero())); 
            
            
        }
        
        
    }
    
    public static void DemanarPuntDeRecarregaMesProperVehiclePerAtendrePeticio(Peticio pet, Temps tEspera, Vehicle v, PuntDeRecarrega pMesProperOrigen, 
            PuntDeRecarrega pMesProperDesti) {
    // Pre: --
    // Post: Busca v en pMesProperOrigen de la petició, que pugui atendre pet sense que el seu temps d'espera sigui major a tEspera i que la ruta de v comenci 
    //       en pMesProperOrigen i acabi en pMesProperDesti. 
        
        boolean trobat = false; 
        int i = 0; 
        // Es demana a la classe Mapa el PuntDeRecarrega més proper al punt d'origen de la petició 
        pMesProperOrigen = mapa.PuntDeRecarregaMesProperA(pet.origen); // cal fer static el mètode de Mapa,  
        // També es demana el PuntDeRecarrega més proper al punt de destí de la petició 
        pMesProperDesti = mapa.PuntDeRecarregaMesProperA(pet.desti); 
        // Es crea una taula de localitzacions que conté la localització d'origen i la de destí de la petició pet, que són els punts per on ha de passar el vehicle 
        // Diga'm quin dels 2 mètodes et va millor, Xavi
        // 1r mètode (crec que queda millor però no si és possible fer-lo perque li passo un ArrayList a RutaMin, que són els punts a visitar)
        //ArrayList<Localitzacio> trajectePeticio;     
        //trajectePeticio.add(pet.origen);
        //trajectePeticio.add(pet.desti); 
        //Ruta rVehicle = mapa.RutaMin(locTrajecte, pMesProperOrigen, pMesProperDesti); 
        //double recorregut = rVehicle.Distancia(); 
        // 2n mètode
        // Es calcula la distància en km que ha de fer el vehicle 
        double recorregut = 0; 
        int nPass = pet.NombreClients(); 
        // S'obté el temps per anar del punt de recàrrega al punt d'origen de la petició 
        Temps tPRaOrigen = mapa.TempsRuta(pMesProperOrigen,pet.origen); 
        // L'hora en la qual l'empresa avisa al vehicle per atendre la petició és 5 minuts després de què el client truqui 
        Temps tAvis = pet.horaTrucada().mes(new Temps(0,5)); 
        while (i < (mapa.nPuntsRecarrega()*mapa.nPuntsRecarrega()) && !trobat) { // cal fer static mapa.nPuntsRecarrega()   
            recorregut = mapa.DistanciaMin(pMesProperOrigen,pet.origen) + mapa.DistanciaMin(pet.origen,pet.desti) + mapa.DistanciaMin(pet.desti,pMesProperDesti); 
            if (!pMesProperOrigen.Buit() && pMesProperDesti.PlacesLliures() > 0) { // Es comprova si els punts de recàrrega es poden admetre 
                v = pMesProperOrigen.SortidaVehicle(recorregut, nPass, tPRaOrigen, tEspera, pet.horaSortida(), tAvis); 
                if (v != null)  // Si s'ha trobat un vehicle apropiat, ja no es busca més 
                    trobat = true;
            }
            if (pMesProperOrigen.Buit() || v == null) {// Obtenir el següent punt de recàrrega més proper a origen en cas que el PR estigui buit o no s'hagi trobat un vehicle apropiat 
              pMesProperOrigen = mapa.SeguentPuntDeRecarregaMesProperA(pet.origen);
              tPRaOrigen = mapa.TempsRuta(pMesProperOrigen,pet.origen); // Recalcular el temps per anar del nou punt de recàrrega al punt d'origen de la petició 
            }
            if (pMesProperDesti.PlacesLliures() == 0) { // Obtenir el següent punt de recàrrega més proper a destí en cas que el PR no tingui places lliures 
                pMesProperDesti = mapa.SeguentPuntDeRecarregaMesProperA(pet.desti); 
            }
            
            i++; 
        }
        
    }
    
 
    
    public static Ruta ObtenirRutaVehicle(Peticio pet, PuntDeRecarrega pIni, PuntDeRecarrega pFi) {
        
        Ruta rMinPrIniOri = mapa.RutaMin(pIni.numero(),pet.origen.numero());
        Ruta rVehicle = new Ruta(rMinPrIniOri); 
        Ruta rMinOriDes = mapa.RutaMin(pet.origen.numero(),pet.desti.numero());
        rVehicle.Concatenar(rMinOriDes); 
        Ruta rMinDesPrFi = mapa.RutaMin(pet.desti.numero(),pFi.numero());
        rVehicle.Concatenar(rMinDesPrFi);
        return rVehicle; 
    }
    
    
    
  
    
 
   
    
    
        
       
}
