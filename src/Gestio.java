/** @file Gestio.java
    @brief Classe Gestio
*/

/** @class Gestio
    @brief És l'encarregada de què s'atenguin les peticions 
    @author Marc Padrós 
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
import javafx.util.Pair;




public class Gestio {
    
   
    /* ATRIBUTS */
    
    private TreeSet<Peticio> peticions; // peticions estan ordenades per hora de sortida 
    private Mapa mapa;
    private Temps tEspMax; // temps d'espera màxim per cada petició 
    private Estadistica stats;
    // INVARIANT: 
    // peticions != null
    // mapa != null
    // tEspMax != null
    // stats != null 
    
    
// ------------------------------------------
    
    /* PART PÚBLICA */ 
    
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
     *  @brief  Llistat d'estadístics  
        @pre Cert
        @post Guarda la informació de les rutes, els estadístics de vehicles, localitzacions i peticions en un fitxer de sortida
    **/
    public void MostrarEstadistics()throws IOException{
       
        String dades = stats.toString();
        System.out.println("----------------------------------------------------------------------");
        System.out.println(dades);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("out.txt",true))) {
            writer.write(dades);
        }
    }
    
    /**
     * @brief Creació de les localitzacions del mapa 
     * @pre Cert
     * @post Crea les localitzacions que poden ser punts de recàrrega o no i les afegeix al mapa. Les localitzacions es llegeixen d'un fitxer d'entrada.  
    */
    public void CrearLocalitzacions() throws FileNotFoundException, IndexOutOfBoundsException {

        System.out.println("FITXER DE LOCALITZACIONS: ");
        Scanner teclat = new Scanner(System.in);
        File fitLoc = new File(teclat.nextLine());
        
        Scanner fitxerLoc = new Scanner(fitLoc);
        while (fitxerLoc.hasNextLine()) {
            String linia = fitxerLoc.nextLine();
            String[] liniaArr = linia.split(","); 
            int iden = Integer.parseInt(liniaArr[0]); 
            String nom = liniaArr[1]; 
            int popul = Integer.parseInt(liniaArr[2]); 
            if (liniaArr.length>3) {      // Si la localització és un punt de recàrrega, la línia té una dada més que indica que la localització és un punt de recàrrega 
                int nPlaces = Integer.parseInt(liniaArr[3]); 
                String carrRapid = liniaArr[4]; 
                boolean carregaRapida = "SI".equals(carrRapid);
                PuntDeRecarrega puntRec = new PuntDeRecarrega(iden,nom,popul,nPlaces,carregaRapida);
                mapa.AfegirLocalitzacio(puntRec);
                stats.guardarPuntRC(puntRec);    /*** COMPROVAR SI FUNCIONA ***/ 
                //System.out.println(puntRec);
            }
            else {
                Localitzacio loc = new Localitzacio(iden,nom,popul);
                mapa.AfegirLocalitzacio(loc);
                //System.out.println(loc);
            }
        }
        fitxerLoc.close();
        
    }
    
    /**
     * @brief Creació de les connexions entre les localitzacions del mapa 
     * @pre Cert
     * @post Crea les connexions entre les localitzacions i les afegeix al mapa. Les connexions es llegeixen d'un fitxer d'entrada.  
     */
    public void CrearConnexions() throws FileNotFoundException, IndexOutOfBoundsException {
    // Pre: --
    // Post: Crea les connexions entre les localitzacions i les afegeix al mapa 
        
        System.out.println("\nFITXER DE CONNEXIONS: ");
        Scanner teclat = new Scanner(System.in);
        File fitCon = new File(teclat.nextLine());
   
        Scanner fitxerCon = new Scanner(fitCon);
        while (fitxerCon.hasNextLine()) {
            String linia = fitxerCon.nextLine(); 
            String[] liniaArr = linia.split(","); 
            Integer origen = Integer.parseInt(liniaArr[0]);
            Integer desti = Integer.parseInt(liniaArr[1]);
            String temps = liniaArr[2];
            Temps tTrajecte = new Temps(temps);
            float distKm = Float.parseFloat(liniaArr[3]); 
            mapa.AfegirConnexio(origen, desti, distKm, tTrajecte);
        }
        fitxerCon.close();
    }
    
    /**
        @brief Creació del mapa 
        @pre Cert
        @post Crea un mapa amb les seves localitzacions i les connexions entre aquestes 
    */
    public void CrearMapa() throws IndexOutOfBoundsException, FileNotFoundException {
        
        CrearLocalitzacions();
        CrearConnexions();
            
    }
    
    /**
        @brief Creació dels vehicles 
        @pre Cert
        @post Crea els vehicles guardats en un fitxer d'entrada
    */
    public void CrearVehicles() throws FileNotFoundException,IndexOutOfBoundsException, PuntDeRecarrega.ExcepcioNoQuedenPlaces {
        
        System.out.println("\nFITXER DE VEHICLES: ");
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
            //System.out.println(v);
            stats.guardarVehicle(v);    /*** COMPROVAR SI FUNCIONA el guardarVehicle ***/ 
            // Cada vehicle estarà estacionat en el punt de recàrrega que li correspon des de les 5h de la matinada 
            PuntDeRecarrega p = (PuntDeRecarrega) mapa.loc(puntRecarrega); 
            p.EstacionarVehicle(v, new Temps(5,0));
            //System.out.println(p); /*** EL VEHICLE S'ESTACIONA CORRECTAMENT **/
        }        
        fitxerVehicles.close();
    }
   
    /**
        @brief Nombre aleatori en coma flotant dins d'un rang de valors  
        @pre Cert
        @post Retorna un nombre aleatori en coma flotant contingut entre min i max
    */
    public static float randFloat(float min, float max) {
        
        Random rand = new Random();

        float result = rand.nextFloat() * (max - min) + min;
       
        return result;
        
    }
    
    
    /** MÈTODE PROVA **/
    public void EscriurePeticionsFitxer() throws IOException {
        
        try (BufferedWriter fitPet = new BufferedWriter(new FileWriter("Peticions.txt",false))) {
            for (Peticio pet: peticions) {
                fitPet.write(pet.toString());
            }
            fitPet.close();
        }
       
    }
    
    /**
        @brief Creació de les peticions  
        @pre Cert
        @post Crea cada petició amb el seu identificador, hora de trucada, hora de sortida, punt d'origen, de destí, el nombre de clients que la tramiten, 
              i l'afegeix al llistat de peticions 
    */
    public void CrearPeticions() throws IOException {
        
        // Per cada localització es genera el nombre de peticions d'aquell punt, el nombre de peticions per cada localització està entre 0 i un màxim. 
        // Per cada petició es generen les dades 
        
        /*** COM A MOLT ES PODEN CREAR 43 PETICIONS AMB EL FITXER LOCALITZACIONS ACTUAL ***/ 
        
        int iden = 1;  // L'identificador de cada petició s'incrementa cada cop que es crea una petició nova 
        for (int i=0; i<mapa.nLocalitzacions(); i++) { // i és l'identificador de cada localització
            Localitzacio origen = mapa.loc(i);
            int maxPeticionsOrigen = origen.popularitat();
            int nPeticionsOrigen = (int)randFloat(0,(float)maxPeticionsOrigen);
            for (int j=0; j<nPeticionsOrigen; j++) {
                float horaTrucada = randFloat(8, (float) 21.75);     // De les 8h a les 21h45 s'atendran les trucades
                //System.out.println(horaTrucada);
                Temps hTrucada = new Temps(horaTrucada); // Es converteix el temps de decimals a hores, minuts
                float horaSortida = randFloat(horaTrucada+(float)0.25,22);     // Ha d'haver un marge de 15 minuts entre trucada i recollida (+ 0.25 = + 15 minuts)
                //System.out.println(horaSortida);
                Temps hSortida = new Temps(horaSortida);
                // Obtenim aleatoriament una localització de destí diferent a la d'origen
                int des;    // des és l'identificador de la localització de destí
                do {
                    des = (int)randFloat(0,(float)mapa.nLocalitzacions()); // en aquest cas randFloat retorna l'índex d'una localització
                }while (des == i); // mentre l'índex del destí i de l'origen siguin iguals es generen índexs aleatoris entre 0 i el nombre de localitzacions del mapa
                Localitzacio desti = mapa.loc(des);
                int nClients = (int)randFloat(1,4); // Com a mínim 1 client farà la petició i com a molt la faran 4
                // Es crea la petició
                Peticio pet = new Peticio(iden,hTrucada,hSortida,origen,desti,nClients);
                //System.out.println(pet);
                peticions.add(pet);      // S'afegeix la petició al llistat de peticions
                iden++;
            }
        }
        System.out.println("Nombre de peticions: " + peticions.size());     
        EscriurePeticionsFitxer(); 

    }   
    
    public  void AtendrePeticions() throws Exception {
    // Pre: --  
    // Post: Tracta totes les peticions 
        
    
        // Es demana per teclat el temps d'espera màxim de les peticions 
        System.out.println("\nTemps d'espera màxim de les peticions: ");
        //System.out.println(System.in.available());
        Scanner s = new Scanner(System.in);
        String tEsp = s.next();     // tEsp entrat de la forma hh:mm 
        tEspMax = new Temps(tEsp); 
        s.close();
        
        // Mentre quedin peticions per tractar 
        while (!peticions.isEmpty()) {
            TractarPeticio(peticions.pollFirst()); // S'agafa la primera petició de la cua i la tractem, mentre es tracta aquesta se'n poden tractar d'altres 
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
            // El vehicle fa la ruta marcada per la primera petició 
            FerTrajecte(v,rVehicle,peticionsAtendre,horaAvis); 
            stats.incrementarNombreDeEncerts();
            
        }
        else {
            stats.incrementarNombreDeFallades(); 
        }
            
            
    }
        
    public Vehicle DemanarPuntDeRecarregaMesProperVehiclePerAtendrePeticio(Peticio pet, Ruta rVehicle, Temps horaAvis) throws Exception {

        Vehicle v = null; 
        boolean trobat = false; 
        // Es demana a Mapa el punt de recàrrega més proper al punt d'origen de la petició i el més proper al punt de destí  
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
        float recorregut = 0;
        Temps horaNec = pet.horaSortida().mes(tEspMax); // horaNec significa l'hora màxima en la qual l'empresa necessita que el vehicle sigui en el punt d'origen de la petició 
        // Si el vehicle arriba al punt d'origen abans que la petició esperi més temps del màxim, el vehicle pot ser apte 
        Temps horaDisp = new Temps(); // horaDisp és l'hora en la qual el vehicle està disponible, en la qual ja està totalment carregat  
        Temps horaArribada = new Temps(); 
        // Mentre no es trobi un punt de recàrrega inicial i un punt de recàrrega final pels quals el vehicle pugui sortir del punt de recàrrega inicial,
        // atendre la petició i estacionar en el punt de recàrrega final, es van buscant altres punts  
        // Per cada punt inicial es busca quin és el vehicle apte per atendre la petició 
        while ((itOri.hasNext() && itDes.hasNext()) && !trobat) { 
            // Es calcula quin és el recorregut total que ha de fer el vehicle 
            recorregut = mapa.CamiMinim(pMesProperOrigen.identificador(),pet.origen().identificador()).cost().distancia();
            recorregut+=mapa.CamiMinim(pet.origen().identificador(), pet.desti().identificador()).cost().distancia();
            recorregut+=mapa.CamiMinim(pet.desti().identificador(), pMesProperDesti.identificador()).cost().distancia(); 
            if (!pMesProperOrigen.Buit() && pMesProperDesti.PlacesLliures() > 0) { // Es comprova si els punts de recàrrega es poden admetre 
                horaArribada = horaAvis.mes(tPRaOrigen); 
                Pair<Vehicle,Temps> sortidaVehicle = pMesProperOrigen.SortidaVehicle(recorregut, nPass, horaArribada,horaNec, horaAvis);
                // sortidaVehicle és un pair del vehicle trobat amb la seva hora de disponibilitat 
                if (sortidaVehicle != null){  // Si s'ha trobat un vehicle apte, ja no es busca més 
                    trobat = true;
                    v = sortidaVehicle.getKey(); 
                    horaDisp = sortidaVehicle.getValue();
                }
            }
            if (pMesProperOrigen.Buit() || v == null) {
            // Obtenir el següent punt de recàrrega més proper a origen en cas que el PR estigui buit o no s'hagi trobat un vehicle apte 
              pMesProperOrigen = (PuntDeRecarrega)mapa.loc(itOri.next()); 
              // Recalcular el temps per anar del nou punt de recàrrega al punt d'origen de la petició 
              tPRaOrigen = mapa.CamiMinim(pMesProperOrigen.identificador(),pet.origen().identificador()).cost().temps();
            }
            if (pMesProperDesti.PlacesLliures() == 0) { // Obtenir el següent punt de recàrrega més proper a destí en cas que el PR no tingui places lliures 
                pMesProperDesti = (PuntDeRecarrega)mapa.loc(itDes.next());
            }
        }
        
        if (trobat) {
            //System.out.println("Vehicle trobat");
            // Si s'ha trobat un vehicle apte, es guarda la ruta del vehicle com una ruta auxiliar 
            rVehicle.Concatenar(mapa.CamiMinim(pMesProperOrigen.identificador(),pet.origen().identificador()));
            rVehicle.Concatenar(mapa.CamiMinim(pet.origen().identificador(), pet.desti().identificador()));
            rVehicle.Concatenar(mapa.CamiMinim(pet.desti().identificador(), pMesProperDesti.identificador()));
            //System.out.println("Hora d'Avis = " + horaAvis + "  Hora Disponible = " + horaDisp + " Temps Carrega vehicle " + v.matricula() + " = " + pMesProperOrigen.tempsCarrega(v));
            // Es guarden els estadístics 
            // El temps que ha estat estacionat el vehicle en el punt de recàrrega és l'hora d'avís - (horaDisp - temps de càrrega) 
            Temps tEstacionat = horaAvis.menys(horaDisp.menys(pMesProperOrigen.tempsCarrega(v))); 
            stats.guardartempsEstacionatVehicle(v,tEstacionat);
            // Es guarda la ocupació en el punt de recàrrega després de fer sortir el vehicle en % 
            stats.guardarOcupacioMigPuntRC(pMesProperOrigen,pMesProperOrigen.ocupacio()); 
            // Si el vehicle arriba al punt d'origen de la petició abans o en l'hora de recollida, el temps d'espera és 0 
            Temps tEsp = new Temps(0,0);
            if (horaArribada.compareTo(pet.horaSortida()) > 0)
               tEsp = horaArribada.menys(pet.horaSortida());
            stats.guardarTempsEsperaPeticio(tEsp);
        }
        
        return v; 
        
    }
    

public void FerTrajecte(Vehicle v, Ruta rVehicle, ArrayList<Peticio> petAtendre, Temps horaArribada) throws Exception {
        
        // Es redueix el nombre de places lliures del vehicle a partir del nombre de clients de la petició inicial, pet 
        Peticio petIni = petAtendre.get(0);
        Ruta rPetIni= mapa.CamiMinim(petIni.origen().identificador(), petIni.desti().identificador()); 
        int locOri = rPetIni.treureActual();
        int locSegOri = rPetIni.primerElement(); 
        //System.out.println("FEM TRAJECTE: " + rVehicle);
        //System.out.println("Hora inicial: " + horaArribada);
        
        //Creem la ruta definitiva final
        Ruta rDef = new Ruta(); //System.out.println("Ruta aux: " + rAux);
        //Variables auxiliars
        Integer puntActual = 0;
        Integer puntDesti = 0;
        Integer puntRecarr = 0;
        boolean petIniCarregada = false; 
        boolean arribatPrimerPuntDescarrega = false; 
        
        System.out.println("\nVehicle: "+ v.matricula());
        while(!rVehicle.buida()){
            //Obtenim el primer punt 
            puntActual = rVehicle.primerElement();
            //Ens guardem el punt en la ruta definitiva
            rDef.addLast(puntActual);
            // Si s'arriba al punt d'origen de la petició inicial 
            if (puntActual == petIni.origen().identificador() && !petIniCarregada) {
                petIniCarregada = true; 
                System.out.println("Places actuals = " + v.NombrePlacesLliures() + " Places totals = " + v.NombrePlaces());
                v.CarregarPassatgers(petIni.NombreClients()); System.out.println("Carrego peticio inicial " + petIni.identificador() + " amb aquests clients " + petIni.nClients + " al punt " + petIni.origen.a_iden + " i anira " + petIni.desti.a_iden );
                System.out.println("Places actuals = " + v.NombrePlacesLliures() + " Places totals = " + v.NombrePlaces());
                stats.guardarOcupacioVehicle(v,v.Ocupacio()); // es guarda la ocupació del vehicle després de carregar els passatgers 
            }
            //Descarreguem si estem en el punt; mentre no s'arribi al punt següent del punt d'origen de la primera petició no es pot descarregar clients 
            if (puntActual == locSegOri) {
                arribatPrimerPuntDescarrega = true;
            }
            if (arribatPrimerPuntDescarrega)
                DescarregarClients(v,puntActual,petAtendre,horaArribada);
            //Tractem més peticions; fins que no s'arriba al punt d'origen de la primera petició no s'ha de comprovar si el vehicle pot atendre més peticions 
            if (petIniCarregada) {
                TractarMesPeticions(v,puntActual,horaArribada,petAtendre,rVehicle); //System.out.println("Ruta actual = " + rAux);
            }
            // Eliminem de la ruta auxiliar el primer punt 
            puntActual = rVehicle.treureActual();
            //Obtenim el següent punt 
            puntDesti = rVehicle.primerElement();
            if(puntActual!=null && puntDesti!=null){
                horaArribada=horaArribada.mes(mapa.CamiMinim(puntActual, puntDesti).cost().temps()); 
                //System.out.println("Anem de " + puntActual + " a " + puntDesti + " i arribem a les " + horaArribada);
                //Obtenim el puntDeRecarrega final
                puntRecarr = puntDesti;
                //Actualitzem pes
                rDef.afegirPes(mapa.pesEntre(puntActual,puntDesti));
            }   
        } 
        System.out.println("Ruta real: " + rDef);
        System.out.println("Places Lliures: " + v.NombrePlacesLliures());
        // L'últim punt de la ruta del vehicle és el punt de recàrrega on ha d'estacionar 
        PuntDeRecarrega PR = (PuntDeRecarrega) mapa.loc(puntRecarr); 
        PR.EstacionarVehicle(v, horaArribada);
        stats.guardarOcupacioMigPuntRC(PR, PR.ocupacio());
        //Guardem la ruta final
        stats.guardarRecorregutVehicle(v, rDef);
        
    }
    
    public void DescarregarClients(Vehicle v, int loc, ArrayList<Peticio> petAtendre, Temps horaArribada) throws Exception {
    // Pre: v té passatgers 
    // Post: v descarrega els passatgers en horaArribada que tinguin com a destinació loc. Les peticions dels passatgers que pertanyen a petAtendre han pogut ser ateses per v. 
        
        // Es busquen les peticions que el vehicle ha pogut atendre i que tenen com a destinació loc 
        for(int i = 0; i<petAtendre.size(); i++) {
            Peticio pet = petAtendre.get(i);
            if (pet.desti().identificador() == loc) {
                System.out.println("*************");
                System.out.println("Places actuals = " + v.NombrePlacesLliures() + " Places totals = " + v.NombrePlaces());
                v.DescarregarPassatgers(pet.NombreClients()); System.out.println("Descarrego peticio " + pet.identificador() + " amb aquests clients " + pet.NombreClients() + " al punt " + loc + " i anira " + pet.desti.a_iden);
                System.out.println("Places actuals = " + v.NombrePlacesLliures() + " Places totals = " + v.NombrePlaces());
                System.out.println("*************");

                //System.out.println("Peticions a Atendre:\n" +petAtendre);
                petAtendre.remove(pet); // S'elimina pet de la llista de peticions que el vehicle pot atendre, ja que ja s'ha atès 
                peticions.remove(pet); // S'elimina pet del llistat de peticions perquè ja s'ha tractat 
                //System.out.println("S'ha tret la " + pet.identificador);
                stats.guardarOcupacioVehicle(v,v.Ocupacio());
                //Temps tViatge = horaArribada.menys(pet.horaSortida()); 
                //Temps tViatgeEsp = mapa.CamiMinim(pet.origen.identificador(),pet.desti.identificador()).cost().temps(); 
                //stats.guardarTempsViatge(pet, tViatge, tViatgeEsp); HashMap<Peticio,ArrayList<Temps>>
            }
        }
 
    }
    
    public void TractarMesPeticions(Vehicle v, int loc, Temps horaArribada, ArrayList<Peticio> petAtendre, Ruta rVehicle) throws Exception {
    // Pre: --
    // Post: v comprova si és capaç d'atendre més peticions que s'hagin de recollir a loc en el moment que hi és v, horaArribada.
    //       Qualsevol petició que pugui atendre s'afegeix a petAtendre. La destinació de la petició pot modificar rVehicle. 
        
        // La petició inicial que ha d'atendre el vehicle s'ignora
        // Ja s'ha carregat els seus clients en el vehicle 
        ArrayList<Peticio> llistPeticions = new ArrayList();
        for(Peticio p:peticions) llistPeticions.add(p);
        for(int i = 0; i<llistPeticions.size(); i++){
            Peticio pet = llistPeticions.get(i); 
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
                            peticions.remove(pet);
                            //System.out.println("Peticio nova afegida:\n" + pet);
                            // El vehicle carrega els passatgers de la petició
                            System.out.println("*************");
                            System.out.println("Places actuals = " + v.NombrePlacesLliures() + " Places totals = " + v.NombrePlaces());
                            v.CarregarPassatgers(pet.NombreClients()); System.out.println("Carrego peticio " + pet.identificador() + " amb aquests clients " + pet.nClients + " al punt " + loc + " i anira " + pet.desti.a_iden);
                            System.out.println("Places actuals = " + v.NombrePlacesLliures() + " Places totals = " + v.NombrePlaces());
                            System.out.println("*************");
                          
                            // Es redueix l'autonomia restant del vehicle 
                            if (modificarRuta){ 
                                rVehicle.Concatenar(rNova); 
                            }
                            // Es marca la petició com a atesa
                            stats.guardarOcupacioVehicle(v,v.Ocupacio());
                            stats.incrementarNombreDeEncerts();
                        }
                    }                  
                }   
            }
            
        /*
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
                    Ruta rNova = new Ruta(); 
                    if (!rVehicle.Conte(rPet)) {
                        // Si la ruta del vehicle no conté la ruta de la petició, la ruta del vehicle s'ha de modificar en cas que la petició es pugui atendre
                        modificarRuta = true; 
                        rNova = mapa.CamiMinim(rVehicle.ultimaLoc(), pet.desti().identificador()); 
                        // Si es troba un punt de recàrrega on pot estacionar el vehicle, es calcula el recorregut que ha de fer per portar la petició al seu destí 
                        // més per anar del destí al punt de recàrrega 
                        PuntDeRecarrega pc = pMesProperLocEstacionable(pet.desti().identificador()); 
                        if (pc != null) {
                            rNova.Concatenar(mapa.CamiMinim(pet.desti().identificador(), pc.identificador()));
                            recorregut = rVehicle.cost().distancia()+rNova.cost().distancia();
                        }
                    }
                    // Es comprova si el vehicle té prou autonomia restant
                    if (v.Autonomia()>= recorregut) {
                        // Es comprova si el vehicle té prou places lliures 
                        if (v.NombrePlacesLliures() > pet.NombreClients()) {
                            // Si el vehicle pot atendre la petició s'afegeix a la resta de peticions que es poden atendre
                            petAtendre.add(pet); 
                            // El vehicle carrega els passatgers de la petició
                            v.CarregarPassatgers(pet.NombreClients());
                            // Es redueix l'autonomia restant del vehicle 
                            if (modificarRuta){ 
                                rVehicle.Concatenar(rNova); 
                            }
                            // Es marca la petició com a atesa
                            stats.incrementarNombreDeEncerts();
                            stats.guardarOcupacioVehicle(v,v.Ocupacio());
                            Temps tEsp = new Temps(0,0);
                            if (horaArribada.compareTo(pet.horaSortida()) > 0)
                                tEsp = horaArribada.menys(pet.horaSortida());
                            stats.guardarTempsEsperaPeticio(tEsp);
                            
                        }
                    }                  
                }   
            }
            */
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
