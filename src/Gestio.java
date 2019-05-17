/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marc Padrós Jiménez
 */

import java.io.BufferedWriter;
import java.io.File; 
import java.util.Scanner; 
import java.util.Random; 
import java.util.TreeSet;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.SortedSet;
import java.util.ArrayList; 
import java.util.Iterator;



public class Gestio {
    
    // Cua de prioritats que conté les peticions 
    private SortedSet<Peticio> peticions;
    private Mapa mapa;
    private Temps tEspMax; 
    private Estadistica stats;
    
    
    
    
    /**
        @brief Creació de Gestió 
        @pre Cert
        @post Crea un objecte Gestió 
    **/
    
    Gestio(){
        peticions = new TreeSet();
        mapa = new Mapa();
        tEspMax = new Temps();
        stats = new Estadistica();
    }

    /**
     * @throws java.io.IOException
        @pre Cert
        @post Guarda la informació de les rutes i estadístics a un fitxer de sortida
    **/
    
    public void MostrarEstadistics()throws IOException{
        
        String dades = stats.toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("out.txt",true))) {
            writer.write(dades);
        }
        
    }
    
    public void CrearLocalitzacions() throws FileNotFoundException, IndexOutOfBoundsException {
    // Pre: --
    // Post: Crea les localitzacions que poden ser punts de recàrrega o no i les afegeix al mapa    
    
    
        System.out.println("Fitxer de localitzacions: ");
        Scanner teclat = new Scanner(System.in);
        File fitLoc = new File(teclat.nextLine());
        
        Scanner fitxerLoc = new Scanner(fitLoc);
        while (fitxerLoc.hasNextLine()) {
            String linia = fitxerLoc.nextLine();
            String[] liniaArr = linia.split(","); 
            int iden = Integer.parseInt(liniaArr[0]); 
            String nom = liniaArr[1]; 
            int popul = Integer.parseInt(liniaArr[2]); 
            if (liniaArr.length>3) {      // Si la localització és un punt de recàrrega
                int nPlaces = Integer.parseInt(liniaArr[3]); 
                String carrRapid = liniaArr[4]; 
                boolean carregaRapida = "SI".equals(carrRapid);
                PuntDeRecarrega puntRec = new PuntDeRecarrega(nom,popul,nPlaces,carregaRapida);
                mapa.AfegirLocalitzacio(puntRec);
                stats.guardarPuntRC(puntRec);
                //System.out.println(puntRec);
            }
            else {
                Localitzacio loc = new Localitzacio(nom,popul);
                mapa.AfegirLocalitzacio(loc);
                //System.out.println(loc);
            }
        }
        
    }
    
    
    public void CrearConnexions() throws FileNotFoundException, IndexOutOfBoundsException {
    // Pre: --
    // Post: Crea les connexions entre les localitzacions i les afegeix al mapa 
        System.out.println("Fitxer de connexions: ");
        Scanner teclat = new Scanner(System.in);
        File fitCon = new File(teclat.nextLine());
   
        Scanner fitxerCon = new Scanner(fitCon);
        while (fitxerCon.hasNextLine()) {
            String linia = fitxerCon.nextLine(); 
            String[] liniaArr = linia.split(","); 
            Integer origen = Integer.parseInt(liniaArr[0]);
            Integer desti = Integer.parseInt(liniaArr[1]);
            //System.out.println("abans");
            String temps = liniaArr[2];
            Temps tTrajecte = new Temps(temps);
            //System.out.println(liniaArr[3]);
            float distKm = Float.parseFloat(liniaArr[3]); 
            mapa.AfegirConnexio(origen, desti, distKm, tTrajecte);
            //System.out.println("després"); 
        }
        
    }
    
    
    public void CrearMapa() throws IndexOutOfBoundsException, FileNotFoundException {
    // Pre: --
    // Post: Crea un mapa amb les seves localitzacions i les connexions entre aquestes 
    
        Mapa m = new Mapa(); 
        
        CrearLocalitzacions();
        CrearConnexions();
            
    }
    
    
    public void CrearVehicles() throws FileNotFoundException,IndexOutOfBoundsException, PuntDeRecarrega.ExcepcioNoQuedenPlaces {
        
        System.out.println("Fitxer de vehicles: ");
        Scanner teclat = new Scanner(System.in);
        File fitVeh = new File(teclat.nextLine());
        Scanner fitxerVehicles = new Scanner(fitVeh);
                
        while(fitxerVehicles.hasNextLine()){
            String linia = fitxerVehicles.nextLine();
            String[] liniaArr = linia.split(", ");
            String matricula = liniaArr[0];
            String model = liniaArr[1];
            String tipus = liniaArr[2];
            float autonomia = Float.parseFloat(liniaArr[3]); 
            boolean carregaRapida = liniaArr[4].equals("SI");
            Integer nPlaces = Integer.parseInt(liniaArr[5]);
            String tCarrega = liniaArr[6];
            Temps tCarr = new Temps(tCarrega); 
            int puntRecarrega = Integer.parseInt(liniaArr[7]); 
            Vehicle v = new Vehicle(matricula,model,tipus,autonomia,carregaRapida,nPlaces, tCarr);
            System.out.println(v);
            stats.guardarVehicle(v);
            PuntDeRecarrega p = (PuntDeRecarrega)mapa.loc(puntRecarrega); // ubiquem el vehicle en el punt de recàrrega llegit del fitxer a les 7:00 
            p.EstacionarVehicle(v, new Temps(0,7));
        }        
          
    }
   
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
        //int[] maxPeticionsPopul = {0,1,3,4,5,6,7,8,9,10};
        
        int iden = 1; 
        for (int i=0; i<mapa.nLocalitzacions(); i++) { // i és l'identificador de cada localització 
            Localitzacio origen = mapa.loc(i); 
            int maxPeticionsOrigen = origen.popularitat();
            int nPeticionsOrigen = (int)randFloat(0,(float)maxPeticionsOrigen);     
            for (int j=0; j<nPeticionsOrigen; j++) {
                float horaTrucada = randFloat(8, (float) 21.75);     // De les 8h a les 21h45 s'atendran les trucades 
                //System.out.println(horaTrucada);
                Temps hTrucada = new Temps(horaTrucada); 
                float horaSortida = randFloat(horaTrucada+(float)0.25,22);     // Ha d'haver un marge de 15 minuts entre trucada i recollida (+ 0.25 = + 15 minuts)
                //System.out.println(horaSortida);
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
                Peticio pet = new Peticio(iden,hTrucada,hSortida,origen,desti,nClients);    
                // Afegim la petició a la cua
                //System.out.println(pet);
                peticions.add(pet); 
                iden++;   
            }                
        }
        
        System.out.println("Mida: " +peticions.size());
        
    }   
    
    public  void AtendrePeticions() throws Exception {
    // Pre: --  
    // Post: Tracta totes les peticions 
        
    
        // Es demana per teclat el temps d'espera màxim de les peticions 
        System.out.println("Temps d'espera màxim de les peticions: ");
        System.out.println(System.in.available());
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
    
       
    
    
    public void TractarPeticio(Peticio pet) throws Exception {
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
            stats.guardarRecorregutVehicle(v,rVehicle); 
        }
        else {
            stats.incrementarNombreDeFallades(); 
            peticions.remove(pet); 
        }
            
            
    }
        
    public Vehicle DemanarPuntDeRecarregaMesProperVehiclePerAtendrePeticio(Peticio pet, Ruta rVehicle, Temps horaAvis) throws Exception {
    // Pre: --
    // Post: Retorna el vehicle en el punt de recàrrega més proper a pet, que la pugui atendre sense que el seu temps d'espera sigui major a tEspMax. 
    //       rVehicle comença en el punt de recàrrega més proper a l'origen de pet i acaba en el punt de recàrrega més proper al punt de destí de pet
    //       passant pels punts d'origen i destí de pet
    //       Si no s'ha trobat cap vehicle apte, retorna null 
        
        Vehicle v = null; 
        boolean trobat = false; 
        // Es demana a Mapa el PuntDeRecarrega més proper al punt d'origen de la petició 
        ArrayDeque<Integer> PRMesPropersOrigen = mapa.PRMesProximA(pet.origen().identificador()); 
        ArrayDeque<Integer> PRMesPropersDesti = mapa.PRMesProximDesde(pet.desti().identificador()); 
        
        Iterator<Integer> itOri = PRMesPropersOrigen.iterator();
        PuntDeRecarrega pMesProperOrigen = (PuntDeRecarrega)mapa.loc(itOri.next()); 
        Iterator<Integer> itDes = PRMesPropersDesti.iterator(); 
        PuntDeRecarrega pMesProperDesti = (PuntDeRecarrega)mapa.loc(itDes.next()); 
        
        // Temps per anar del punt de recàrrega a l'origen de la petició
        Temps tPRaOrigen = mapa.CamiMinim(pMesProperOrigen.identificador(),pet.origen().identificador()).cost().temps(); 
        // Nombre de clients de la petició 
        int nPass = pet.NombreClients(); 
        // Distància en km de rVehicle 
        float recorregut; 
        Temps horaDisp = new Temps(); 
        
        while ((itOri.hasNext() && itDes.hasNext()) && !trobat) { 
            recorregut = mapa.CamiMinim(pMesProperOrigen.identificador(),pet.origen().identificador()).cost().distancia()+ 
            mapa.CamiMinim(pet.origen().identificador(), pet.desti().identificador()).cost().distancia()+
            mapa.CamiMinim(pet.desti().identificador(), pMesProperDesti.identificador()).cost().distancia(); 
            if (!pMesProperOrigen.Buit() && pMesProperDesti.PlacesLliures() > 0) { // Es comprova si els punts de recàrrega es poden admetre 
                v = pMesProperOrigen.SortidaVehicle(recorregut, nPass, tPRaOrigen, tEspMax, pet.horaSortida(), horaAvis, horaDisp); 
                if (v != null)  // Si s'ha trobat un vehicle apropiat, ja no es busca més 
                    trobat = true;
            }
            if (pMesProperOrigen.Buit() || v == null) {
            // Obtenir el següent punt de recàrrega més proper a origen en cas que el PR estigui buit o no s'hagi trobat un vehicle apropiat 
              PuntDeRecarrega PROriDescartat = pMesProperOrigen; 
              pMesProperOrigen = (PuntDeRecarrega)mapa.loc(itOri.next()); 
              // Recalcular el temps per anar del nou punt de recàrrega al punt d'origen de la petició 
              tPRaOrigen = mapa.CamiMinim(pMesProperOrigen.identificador(),pet.origen.identificador()).cost().temps();
            }
            if (pMesProperDesti.PlacesLliures() == 0) { // Obtenir el següent punt de recàrrega més proper a destí en cas que el PR no tingui places lliures 
                PuntDeRecarrega PRDestiDescartat = pMesProperDesti; 
                pMesProperDesti = (PuntDeRecarrega)mapa.loc(itDes.next());
            }
        }
        
        // Si s'ha trobat un vehicle apte, es guarda la ruta del vehicle 
        if (trobat) {
            rVehicle = mapa.CamiMinim(pMesProperOrigen.identificador(),pet.origen.identificador());
            rVehicle.Concatenar(mapa.CamiMinim(pet.origen.identificador(), pet.desti.identificador()));
            rVehicle.Concatenar(mapa.CamiMinim(pet.desti.identificador(), pMesProperDesti.identificador())); 
            double ocupPROri = (pMesProperOrigen.Capacitat()-pMesProperOrigen.PlacesLliures())/pMesProperOrigen.Capacitat();
            Temps tEstacionat = horaAvis.menys(horaDisp.menys(pMesProperOrigen.tempsCarrega(v))); 
            stats.guardarOcupacioMigPuntRC(pMesProperOrigen,ocupPROri); 
            stats.guardartempsEstacionatVehicle(v,tEstacionat); 
        }
        
        return v; 
    }
    

    public void FerTrajecte(Vehicle v, Ruta rVehicle, ArrayList<Peticio> petAtendre, Temps horaArribada) throws Exception {
        
        // Es redueix el nombre de places lliures del vehicle a partir del nombre de clients de la petició inicial, pet 
        Peticio petIni = petAtendre.get(0); 
        v.CarregarPassatgers(petIni.NombreClients());
        peticions.remove(petIni); 
        double ocupVehicle = (v.NombrePlaces()-v.NombrePlacesLliures())/v.NombrePlaces();
        stats.guardarOcupacioVehicle(v,ocupVehicle); 
        Iterator<Integer> itLoc = rVehicle.iterator(); 
        Iterator<Integer> itSeg; 
        int locAct, locSeg = 0; 
        
        while (itLoc.hasNext()) {
            locAct = itLoc.next();
            DescarregarClients(v,locAct, petAtendre); 
            TractarMesPeticions(v,locAct,horaArribada,petAtendre,rVehicle); 
            itSeg = itLoc; 
            locSeg = itSeg.next(); 
            horaArribada=horaArribada.mes(mapa.CamiMinim(locAct, locSeg).cost().temps());
            itLoc.remove();
            itLoc = rVehicle.iterator(); 
        }
        // L'últim punt de la ruta del vehicle és el punt de recàrrega on ha d'estacionar 
        PuntDeRecarrega PR = (PuntDeRecarrega) mapa.loc(locSeg); 
        PR.EstacionarVehicle(v, horaArribada);
        
    }
    
    public void DescarregarClients(Vehicle v, int loc, ArrayList<Peticio> petAtendre) {
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
    
    public void TractarMesPeticions(Vehicle v, int loc, Temps horaArribada, ArrayList<Peticio> petAtendre, Ruta rVehicle) throws Exception {
    // Pre: --
    // Post: v comprova si és capaç d'atendre més peticions que s'hagin de recollir a loc en el moment que hi és v, horaArribada.
    //       Qualsevol petició que pugui atendre s'afegeix a petAtendre. La destinació de la petició pot modificar rVehicle. 
        
        // La petició inicial que ha d'atendre el vehicle s'ignora
        // Ja s'ha carregat els seus clients en el vehicle 
        Iterator<Peticio> it = peticions.iterator(); 
        while (it.hasNext()) {
            Peticio pet = it.next(); 
                // Es comprova si el vehicle pot atendre la petició 
                // Primer es compara l'hora d'arribada amb l'hora de sortida de la petició 
                // Si horaArribada de v <= pet.horaSortida + tEspMax, ok 
                if (horaArribada.compareTo(pet.horaSortida().mes(tEspMax)) <= 0) {
                    // Es comprova si la ruta del vehicle conté la ruta de la petició 
                    // Si no la conté el recorregut que hauria de fer el vehicle és major 
                    Ruta rPet = mapa.CamiMinim(pet.origen().identificador(), pet.desti().identificador());
                    float recorregut = 0;
                    boolean modificarRuta = false;
                    Ruta rNova = null; 
                    if (!rVehicle.Conte(rPet)) {
                        // Si la ruta del vehicle no conté la ruta de la petició, la ruta del vehicle s'ha de modificar en cas que la petició es pugui atendre
                        modificarRuta = true; 
                        rNova = mapa.CamiMinim(rVehicle.ultimaLoc(), pet.desti().identificador()); 
                        // Si es troba un punt de recàrrega on pot estacionar el vehicle, es calcula el recorregut que ha de fer per portar la petició al seu destí 
                        // més per anar del destí al punt de recàrrega 
                        PuntDeRecarrega pc = pMesProperLocEstacionable(pet.desti.identificador()); 
                        if (pc != null) {
                            rNova.Concatenar(mapa.CamiMinim(pet.desti().identificador(), pc.identificador()));
                            recorregut = rVehicle.cost().distancia()+rNova.cost().distancia();
                        }
                    }
                    // Es comprova si el vehicle té prou autonomia restant
                    if (v.Autonomia()>= recorregut) {
                        // Es comprova si el vehicle té prou places lliures 
                        if (v.NombrePlacesLliures() >= pet.NombreClients()) {
                            // Si el vehicle pot atendre la petició s'afegeix a la resta de peticions que es poden atendre
                            petAtendre.add(pet); 
                            // El vehicle carrega els passatgers de la petició
                            v.CarregarPassatgers(pet.NombreClients());
                            // Es redueix l'autonomia restant del vehicle 
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

    
    private PuntDeRecarrega pMesProperLocEstacionable(int loc) throws Exception {
    // Pre: --
    // Post: Retorna el punt de recàrrega més proper a loc en el qual es pugui estacionar. Retorna null si no s'ha trobat cap punt de recàrrega estacionable. 
    
        ArrayDeque<Integer> PRMesPropersLoc = mapa.PRMesProximDesde(loc); 
        boolean trobat = false; 
        PuntDeRecarrega p = null; 
        Iterator<Integer> itPR = PRMesPropersLoc.iterator();
        while (itPR.hasNext() && !trobat) {
            p = (PuntDeRecarrega)mapa.loc(itPR.next()); 
            if (p.PlacesLliures()>0) 
                trobat = true; 
        }
        
        return p; 
     
    }
       
}
