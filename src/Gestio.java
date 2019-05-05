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
import java.util.ArrayDeque;
import java.util.SortedSet;
import java.util.ArrayList; 
import java.util.Iterator;
import java.util.PriorityQueue;



public class Gestio {
    
    // Cua de prioritats que conté les peticions 
    private static SortedSet<Peticio> peticions;
    private Mapa mapa;
    Temps tEspMax; 
    private Estadistica stats;
    
    
    public static void main(String argv[]) {
        
       CrearLocalitzacions(); 
       CrearConnexions(); 
       AtendrePeticions(); 
       MostrarEstadistics();  
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
                Temps tTrajecte = new Temps(temps);
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
    
    public  void AtendrePeticions() {
    // Pre: --  
    // Post: Tracta totes les peticions 
        
    
        // Es demana per teclat el temps d'espera màxim de les peticions 
        System.out.println("Temps d'espera màxim de les peticions: ");
        Scanner s = new Scanner(System.in);
        String tEsp = s.next();     // tEsp entrat de la forma hh:mm 
        tEspMax = new Temps(tEsp); 
        
        
        // Es crea l'objecte estadistica 
        stats = new Estadistica(); 
        // Mentre quedin peticions per tractar 
        while (!peticions.isEmpty()) {
            TractarPeticio(peticions.first()); // S'agafa la primera petició de la cua i la tractem, mentre es tracta aquesta se'n poden tractar d'altres 
        }
        
    }
    
       
    
    
    public void TractarPeticio(Peticio pet) {
    // Pre: --
    // Post: Marca pet com a atesa o com a fallida. Mentre es tracta pet es poden tractar altres peticions. 
    
        // Es demana un vehicle al punt de recàrrega més proper, que pugui atendre la petició 
        Ruta rVehicle = new Ruta(); 
        // L'hora en la qual l'empresa avisa al vehicle per atendre la petició és 5 minuts després de què el client truqui 
        Temps horaAvis = pet.horaTrucada().mes(new Temps(0,5)); 
        Vehicle v = DemanarPuntDeRecarregaMesProperVehiclePerAtendrePeticio(pet,rVehicle, horaAvis); 
        if (v != null) {    // Si s'ha trobat un vehicle per atendre la petició  
            // Es crea una taula amb les peticions que el vehicle pot atendre
            ArrayList<Peticio> peticionsAtendre = new ArrayList<>(); 
            // S'afegeix la petició inicial a la taula 
            peticionsAtendre.add(pet);
            stats.incrementarNombreDeEncerts(); 
            // El vehicle fa la ruta marcada per la primera petició 
            FerTrajecte(v,rVehicle,peticionsAtendre,horaAvis); 
                 
        }
        else {
            stats.incrementarNombreDeFallades(); 
            peticions.remove(pet); 
        }
            
            
    }
        
    public static Vehicle DemanarPuntDeRecarregaMesProperVehiclePerAtendrePeticio(Peticio pet, Ruta rVehicle, Temps horaAvis) {
    // Pre: --
    // Post: Retorna el vehicle en el punt de recàrrega més proper a pet, que la pugui atendre sense que el seu temps d'espera sigui major a tEspMax. 
    //       rVehicle comença en el punt de recàrrega més proper a l'origen de pet i acaba en el punt de recàrrega més proper al punt de destí de pet
    //       passant pels punts d'origen i destí de pet
    //       Si no s'ha trobat cap vehicle apte, retorna null 
        
        Vehicle v = null; 
        boolean trobat = false; 
        // Es demana a Mapa el PuntDeRecarrega més proper al punt d'origen de la petició 
        PriorityQueue<PuntDeRecarrega> PRMesPropersOrigen = mapa.PRmesProximsA(pet.origen); 
        PuntDeRecarrega pMesProperOrigen = PRMesPropersOrigen.peek(); 
        // També es demana el PuntDeRecarrega més proper des del punt de destí de la petició 
        PriorityQueue<PuntDeRecarrega> PRMesPropersDesti = mapa.PRmesProximsDesDe(pet.desti); 
        PuntDeRecarrega pMesProperDesti = PRMesPropersDesti.peek(); 
        
        // Temps per anar del punt de recàrrega a l'origen de la petició
        Temps tPRaOrigen = mapa.CamiMinim(pMesProperOrigen.identificador(),pet.origen.identificador()).cost().temps(); 
        // Nombre de clients de la petició 
        int nPass = pet.NombreClients(); 
        // Distància en km de rVehicle 
        float recorregut = 0; 
                
        while ((!PRMesPropersOrigen.isEmpty() && !PRMesPropersDesti.isEmpty()) && !trobat) { 
            recorregut = mapa.CamiMinim(pMesProperOrigen.identificador(),pet.origen.identificador()).cost().distancia()+ 
            mapa.CamiMinim(pet.origen.identificador(), pet.desti.identificador()).cost().distancia()+
            mapa.CamiMinim(pet.desti.identificador(), pMesProperDesti.identificador()).cost().distancia(); 
            if (!pMesProperOrigen.Buit() && pMesProperDesti.PlacesLliures() > 0) { // Es comprova si els punts de recàrrega es poden admetre 
                v = pMesProperOrigen.SortidaVehicle(recorregut, nPass, tPRaOrigen, tEspMax, pet.horaSortida(), horaAvis); 
                if (v != null)  // Si s'ha trobat un vehicle apropiat, ja no es busca més 
                    trobat = true;
            }
            if (pMesProperOrigen.Buit() || v == null) {
            // Obtenir el següent punt de recàrrega més proper a origen en cas que el PR estigui buit o no s'hagi trobat un vehicle apropiat 
              PuntDeRecarrega PROriDescartat = PRMesPropersOrigen.remove(); 
              pMesProperOrigen = PRMesPropersOrigen.peek();
               // Recalcular el temps per anar del nou punt de recàrrega al punt d'origen de la petició 
              tPRaOrigen = mapa.CamiMinim(pMesProperOrigen.identificador(),pet.origen.identificador()).cost().temps();
            }
            if (pMesProperDesti.PlacesLliures() == 0) { // Obtenir el següent punt de recàrrega més proper a destí en cas que el PR no tingui places lliures 
                PuntDeRecarrega PRDestiDescartat = PRMesPropersDesti.remove(); 
                pMesProperDesti = PRMesPropersDesti.peek(); 
            }
        }
        
        // Si s'ha trobat un vehicle apte, es guarda la ruta del vehicle 
        if (trobat) {
            rVehicle = mapa.CamiMinim(pMesProperOrigen.identificador(),pet.origen.identificador());
            rVehicle.Concatenar(mapa.CamiMinim(pet.origen.identificador(), pet.desti.identificador()));
            rVehicle.Concatenar(mapa.CamiMinim(pet.desti.identificador(), pMesProperDesti.identificador())); 
            double ocupPROri = (pMesProperOrigen.Capacitat()-pMesProperOrigen.PlacesLliures())/pMesProperOrigen.Capacitat();
            Temps tEstacionat = horaAvis.menys(pMesProperOrigen.horaDisponibilitat(v)); 
            stats.guardarOcupacioMigPuntRC(pMesProperOrigen,ocupMitjPROri); 
            stats.guardartempsEstacionatVehicle(v,tEstacionat); 
        }
        
        return v; 
    }
    

    public static void FerTrajecte(Vehicle v, Ruta rVehicle, ArrayList<Peticio> petAtendre, Temps horaArribada) throws PuntDeRecarrega.ExcepcioNoQuedenPlaces {
        
        // Es redueix l'autonomia del vehicle a partir dels km de la ruta
        v.ReduirAutonomiaRestant(rVehicle.cost().distancia());
        // Es redueix el nombre de places lliures del vehicle a partir del nombre de clients de la petició inicial, pet 
        Peticio petIni = petAtendre.get(0); 
        v.CarregarPassatgers(petIni.NombreClients());
        double ocupVehicle = (v.NombrePlaces()-v.NombrePlacesLliures())/v.NombrePlaces();
        stats.guardarOcupacioVehicle(v,ocupVehicle); 
        int aux = 0;
        ArrayDeque<Integer> llistaLoc = rVehicle.Localitzacions(); 
        
        for (int loc: llistaLoc) {
            DescarregarClients(v,loc, petAtendre); 
            TractarMesPeticions(v,loc,horaArribada,petAtendre,rVehicle, petIni); 
            aux = llistaLoc.remove(); 
            horaArribada=horaArribada.mes(mapa.CamiMinim(aux, llistaLoc.getFirst()).cost().temps());
            llistaLoc.addFirst(aux);
            llistaLoc = rVehicle.Localitzacions(); 
        }
        // L'últim punt de la ruta del vehicle és el punt de recàrrega on ha d'estacionar 
        PuntDeRecarrega PR = (PuntDeRecarrega) mapa.loc(llistaLoc.getLast()); 
        PR.EstacionarVehicle(v, horaArribada);
        
    }
    
    public static void DescarregarClients(Vehicle v, int loc, ArrayList<Peticio> petAtendre) {
    // Pre: v té passatgers 
    // Post: v descarrega els passatgers que tinguin com a destinació loc. Les peticions dels passatgers que pertanyen a petAtendre han pogut ser ateses per v. 
        
        // Es busquen les peticions que el vehicle ha pogut atendre i que tenen com a destinació loc 
        Iterator<Peticio> it = petAtendre.iterator();
        while (it.hasNext()) {
            Peticio pet = it.next(); 
            if (pet.desti().identificador() == loc) {
                v.DescarregarPassatgers(pet.NombreClients());
                petAtendre.remove(pet); // S'elimina pet de la llista de peticions que es poden atendre, ja que ja s'ha atès 
                peticions.remove(pet); 
                double ocupVehicle = (v.NombrePlaces()-v.NombrePlacesLliures())/v.NombrePlaces();
                stats.guardarOcupacioVehicle(v,ocupVehicle);
            }
        }
  
        
    }
    
    public static void TractarMesPeticions(Vehicle v, int loc, Temps horaArribada, ArrayList<Peticio> petAtendre, Ruta rVehicl, Peticio petIni) {
    // Pre: --
    // Post: v comprova si és capaç d'atendre més peticions que s'hagin de recollir a loc en el moment que hi és v, horaArribada.
    //       Qualsevol petició que pugui atendre s'afegeix a petAtendre. La destinació de la petició pot modificar rVehicle. 
        
        // La petició inicial que ha d'atendre el vehicle s'ignora
        // Ja s'ha carregat els seus clients en el vehicle 
        Iterator<Peticio> it = peticions.iterator(); 
        while (it.hasNext()) {
            Peticio pet = it.next(); 
            if (pet != petIni) {
                // Es comprova si el vehicle pot atendre la petició 
                // Primer es compara l'hora d'arribada amb l'hora de sortida de la petició 
                // Si horaArribada de v <= pet.horaSortida + tEspMax, ok 
                if (horaArribada.compareTo(pet.horaSortida().mes(tEspMax)) || horaArribada.compareTo(pet.horaSortida().mes(tEspMax))) {
                    // Es comprova si la ruta del vehicle conté la ruta de la petició 
                    // Si no la conté el recorregut que hauria de fer el vehicle és major 
                    Ruta rPet = mapa.CamiMinim(pet.origen().identificador(), pet.desti().identificador());
                    float recorregut = 0;
                    boolean modificarRuta = false;
                    Ruta rNova = null; 
                    if (!rVehicle.conte(rPet)) {
                        // Si la ruta del vehicle no conté la ruta de la petició, la ruta del vehicle s'ha de modificar en cas que la petició es pugui atendre
                        modificarRuta = true; 
                        // Si es troba un punt de recàrrega on pot estacionar el vehicle, es calcula el recorregut que ha de fer per portar la petició al seu destí 
                        // més per anar del destí al punt de recàrrega 
                        if (pMesProperLocEstacionable(loc) != null) {
                            PuntDeRecarrega p = pMesProperLocEstacionable(loc); 
                            rNova = rPet.Concatenar(mapa.CamiMinim(pet.desti().identificador(), p.identificador()));
                            recorregut = rNova.cost().distancia();
                        }
                    }
                    // Es comprova si el vehicle té prou autonomia restant
                    if (v.AutonomiaRestant() >= recorregut) {
                        // Es comprova si el vehicle té prou places lliures 
                        if (v.NombrePlacesLliures() >= pet.NombreClients()) {
                            // Si el vehicle pot atendre la petició s'afegeix a la resta de peticions que es poden atendre
                            petAtendre.add(pet); 
                            // El vehicle carrega els passatgers de la petició
                            v.CarregarPassatgers(pet.NombreClients());
                            // Es redueix l'autonomia restant del vehicle 
                            v.ReduirAutonomiaRestant(recorregut);
                            if (modificarRuta) 
                                rVehicle.Concatenar(rNova); 
                            // Es marca la petició com a atesa
                            stats.incrementarNombreDeEncerts();
                            double ocupVehicle = (v.NombrePlaces()-v.NombrePlacesLliures())/v.NombrePlaces();
                            stats.guardarOcupacioVehicle(v,ocupVehicle);
                        }
                    }                  
                }   
            }
        }
        
    }
    
    
    public static PuntDeRecarrega pMesProperLocEstacionable(int loc) {
    // Pre: --
    // Post: Retorna el punt de recàrrega més proper a loc en el qual es pugui estacionar. Retorna null si no s'ha trobat cap punt de recàrrega estacionable. 
    
        PriorityQueue<PuntDeRecarrega> PRMesPropersLoc = mapa.PRmesProximsDesDe(loc); 
        boolean trobat = false; 
        PuntDeRecarrega p = null; 
        while (!PRMesPropersLoc.isEmpty() && !trobat) {
            if (PRMesPropersLoc.peek().PlacesLliures()>0) {
                trobat = true; 
                p = PRMesPropersLoc.peek(); 
            }
        }
        
        return p; 
     
    }
       
}
