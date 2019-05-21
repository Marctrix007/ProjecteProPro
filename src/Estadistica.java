/** 
    @file Estadistica.java
    @brief Fixter que s'encarrega de les estadístiques del programa
*/

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** 
    @class Estadística
    @brief Classe que emmagatzema dades i en treu estadístics a partir d'aquestes
    @author Enrique Sambola
*/
public class Estadistica {
    
    /*
        DESCRIPCIÓ GENERAL CLASSE:
        Aquesta classe està formada per un conjunt d'estructures de dades que s'omplen al recopilar informació d'altres classes 
        per després treure una descripció estadística de alguns elements que hem considerat importants. S'ha d'entendre com 
        si fos un bloc amb les seves entrades (els mètodes "guardar") i una sortida (el toString). A partir de les
        entrades calcula els estadístics i en treu una sortida quan es demani.


        ASPECTES IMPLEMENTACIÓ:
        A l'hora de calcular els estadístics he fet servir una eina de l'API molt útil: l'stream. Aquest tracta els elements 
        d'una colecció qualsevol com un fluxe de dades al qual se li poden aplicar operacions declaratives, és a dir, no ens 
        preocupem de COM ho calcula sinó QUÈ volem que ens calculi, molt semblant al llenguatge SQL.

        A partir de l'stream, podem aplicar una altre utilitat de l'API que es diu SummayStatistics. Aquest calcula els estadístics més comuns
        com la mitjana, el màxim, el minim, el nombre d'elements, etc. mitjançant un stream de dades. Hi ha de varis tipus: Int, Long i Double. Per
        aquest cas he cregut més adient fer servir DoubleSummaryStatistics ja que la majoria d'operacions serien amb nombres de tipus Double.

        BIBLIOGRAFIA:
        Stream: https://www.oracle.com/technetwork/es/articles/java/procesamiento-streams-java-se-8-2763402-esa.html
        DoubleSummaryStatistics: https://github.com/frohoff/jdk8u-jdk/blob/master/src/share/classes/java/util/DoubleSummaryStatistics.java
    
    */
   
    private int peticionsSatisfetes;
    private int peticionsFallades;
    private HashMap<Vehicle,ArrayList<Double>> mitjanesOcupVehi;
    private HashMap<Vehicle,ArrayList<Double>> mitjanesTempsVehiEstac;
    private HashMap<PuntDeRecarrega,ArrayList<Double>> mitjanesOcupPRC;
    private HashMap<Vehicle,ArrayList<Ruta>> mitjanesKMVehicle;
    private ArrayList<Double> mitjanesTempsEspera;
    private ArrayList<Double> mitjanesTempsViatgeReal;
    private ArrayList<Double> mitjanesTempsViatgeEsperat;
    
    /*
        DESCRIPCIÓ ATRIBUTS:
        peticionsSatisfetes: enter que conté el nombre de peticions que han estat satisfetes
        peticionsFallades: enter que conté el nombre de peticions que no han estat satisfetes
        mitjanesOcuVehi: mapa format per el parell vehicle i els percentatges d'ocupació que ha tingut un vehicle al llarg de l'execució
        mitjanesTempsVehiEstac: mapa format per el parell vehicle i els temps (decimal) que ha estat estacionat un vehicle al llarg de l'execució 
        mitjanesOcupPRC: mapa format per el parell punt de recarrega i els percentatges d'ocupació que ha tingut un punt de recarrega al llarg de l'execució
        mitjanesKMVehicle: mapa format per el parell vehicle i les rutes que ha fet al llarg de l'execució
        mitjanesTempsEspera: llista dels temps (decimal) que han esperat cada peticio 
        mitjanesTempsViatgeReal: llista dels temps (decimal) de viatge que ha fet cada peticio
        mitjanesTempsViatgeEsperat: llista dels temps (decimals) de viatge estimat per cada peticio

        INVARIANT:
        peticionsSatisfetes: >=0
        peticionsFallades: >=0
        mitjanesOcuVehi: !=null
        mitjanesTempsVehiEstac: !=null
        mitjanesOcupPRC: !=null
        mitjanesKMVehicle: !=null
        mitjanesTempsEspera: !=null
        mitjanesTempsViatgeReal: !=null
        mitjanesTempsViatgeEsperat: !=null
     
    */
    
    
    /** 
        @brief Constructor per defecte
        @pre cert
        @post Nou objecte Estadística creat
    */
    Estadistica(){
        peticionsSatisfetes = 0;
        peticionsFallades = 0;
        mitjanesOcupVehi =  new HashMap<>(); 
        mitjanesTempsVehiEstac = new HashMap<>();
        mitjanesOcupPRC = new HashMap<>();
        mitjanesTempsEspera = new ArrayList();
        mitjanesKMVehicle = new HashMap<>();
        mitjanesTempsViatgeReal = new ArrayList();
        mitjanesTempsViatgeEsperat = new ArrayList();
    
    }
    
     /** 
        @brief Guarda el vehicle per fer estadístics
        @pre Vehicle v no guardat abans i v!=null
        @post Vehicle v guardat
    */   
    public void guardarVehicle(Vehicle v){
        mitjanesOcupVehi.put(v, new ArrayList<>());
        mitjanesTempsVehiEstac.put(v, new ArrayList<>());
        mitjanesKMVehicle.put(v, new ArrayList<>());
    }
    
    /** 
        @brief Guarda el punt de recàrrega per fer estadístics
        @pre Punt de Recàrrega p no guardat abans i p!=null
        @post Punt de Recàrrega p guardat
    */   
    public void guardarPuntRC(PuntDeRecarrega p){
        mitjanesOcupPRC.put(p, new ArrayList<>());
    }
    
    /** 
        @brief Guarda el recorregut que ha fet el vehicle per fer estadistics
        @pre Vehicle v ja guardat i v!=null i r!=null
        @post guarda la ruta r d'un vehicle v concret
    */
    public void guardarRecorregutVehicle(Vehicle v, Ruta r){ //---------
        mitjanesKMVehicle.get(v).add(r);
    }
    
    /** 
        @brief Guarda el Temps que ha esperat una peticio a ser atesa
        @pre espera!=null
        @post guarda el temps d'espera d'una peticio
    */
    public void guardarTempsEsperaPeticio(Temps espera){ 
        mitjanesTempsEspera.add(espera.conversioDouble());
    }
    
    /** 
        @brief Guarda l'ocupació del punt de recàrrega
        @pre p!=null
        @post guarda l'ocupació d'un punt de recàrrega concret
    */
    public void guardarOcupacioMigPuntRC(PuntDeRecarrega p, Double ocup){  //---------
        mitjanesOcupPRC.get(p).add(ocup);
    }
    
    /** 
        @brief Guardar el temps que ha estat estacionat un vehicle
        @pre v!=null
        @post guarda el temps que ha estat estacionat un vehicle
    */
    public void guardartempsEstacionatVehicle(Vehicle v, Temps t){  //---------
        mitjanesTempsVehiEstac.get(v).add(t.conversioDouble());
    }
    
    /** 
        @brief Guarda l'ocupació que ha tingut un vehicle
        @pre v!=null
        @post guarda l'ocupació d'un vehicle
    */
    public void guardarOcupacioVehicle(Vehicle v, Double ocup){ //---------
        mitjanesOcupVehi.get(v).add(ocup);        
    }
    
    
    /** 
        @brief Guarda el temps de viatge que hi han hagut
        @pre cert
        @post guarda el temps de viatge real i esperat d'una peticio
    */
    public void guardarTempsViatge(Temps real, Temps esperat){
        mitjanesTempsViatgeEsperat.add(esperat.conversioDouble());
        mitjanesTempsViatgeReal.add(real.conversioDouble());
    }
    
    
    
//N PETICIONS-------------------------------------------------------------------------
    
    
    
    /** 
        @brief Incrementa les peticions que han sigut fallades
        @pre cert
        @post augmenta en 1 el nombre de fallades de peticions 
    */   
    public void incrementarNombreDeFallades(){ //---------     
        peticionsFallades++;
    }
    
    /** 
        @brief Incrementa les peticions que han sigut ateses correctament
        @pre cert
        @post augmenta en 1 el nombre d'encerts de peticions 
    */ 
    public void incrementarNombreDeEncerts(){ //---------  
        peticionsSatisfetes++;
    }
    
    /** 
        @brief Retorna el percentatge de peticions satisfetes
        @pre cert
        @post retorna el percentage de peticions que han estat satisfetes  
    */ 
    private double percentatgePeticionsSatisfetes(){  
        return arrodonir(peticionsSatisfetes*100.00/(peticionsSatisfetes+peticionsFallades));
    }
    
    /** 
        @brief Retorna el percentatge de peticions fallades
        @pre cert
        @post retorna el percentage de peticions que han estat fallades
    */
    private double percentatgePeticionsFallades(){
        return arrodonir(peticionsFallades*100.00/(peticionsSatisfetes+peticionsFallades));
    }
    

    
//KM MITJANA COTXE--------------------------------------------------------------------
    

    
    /** 
        @brief Calcula els kilometres de mitjana que ha fet un cotxe en totes les seves rutes
        @pre v!=null
        @post retorna un string amb la mitjana de kilometres fets per un vehicle  
    */
    private String StringKMMitjana(Vehicle v){
        
        ArrayList<Ruta> mitjanes = mitjanesKMVehicle.get(v);
        
        if(mitjanes.isEmpty()){
            return "No hi ha dades de les rutes del vehicle";
        }
        else{
            DoubleSummaryStatistics statsMitjanaKM = mitjanes.stream().collect(Collectors.summarizingDouble(Ruta::kmFets));
            DoubleSummaryStatistics statsMitjanaTemps = mitjanes.stream().collect(Collectors.summarizingDouble(Ruta::tempsRuta));

            return "Km totals = " + arrodonir(statsMitjanaKM.getSum()) +" km"+ 
                    "\n       Kms mig fets = " + arrodonir(statsMitjanaKM.getAverage()) + " km" +
                    "\n       Temps total = " + new Temps(statsMitjanaTemps.getSum()) +
                    "\n       Temps mig = " + new Temps(statsMitjanaTemps.getAverage());
        }
    }
    
    
//TEMPS MIG D'ESPERA PETICIONS--------------------------------------------------------
    
    
    
    /** 
        @brief Calcula els estadístics del temps d'espera de les peticions
        @pre cert
        @post retorna un string amb la mitjana, el maxim i minim temps d'espera de les peticions
    */
    private String StringTempsMigEspera(){
        if(mitjanesTempsEspera.isEmpty()){
            return "\nNo hi ha dades del Temps mig d'espera";
        }
        else{
            DoubleSummaryStatistics statsMitjanaTempsE = mitjanesTempsEspera.stream().collect(Collectors.summarizingDouble(Double::doubleValue));

            return "\nTemps mig d'espera = " + new Temps(statsMitjanaTempsE.getAverage() ) + "\nTemps màxim d'espera = " + new Temps(statsMitjanaTempsE.getMax()) + "\nTemps mínim d'espera = " + new Temps(statsMitjanaTempsE.getMin()) + "\nSD = " + new Temps(StandardDeviation(mitjanesTempsEspera,statsMitjanaTempsE.getAverage())) + "\nVariació = " + new Temps(Variacio(mitjanesTempsEspera,statsMitjanaTempsE.getAverage()));
        }
    }
    
    
    
//OCUPACIO MIG PUNTS DE RECARREGA-----------------------------------------------------
    
    
    /** 
        @brief Calcula els estadístics dels punts de recarrega
        @pre p!=null
        @post retorna el string que conté la mitjana, la desviacio estandard i la variacio de la Ocupacio Mitja dels Punts de Recarrega
    */
    private String StringPRCMitjanaOcup(PuntDeRecarrega p){
        ArrayList<Double> mitjanes = mitjanesOcupPRC.get(p);
        
        if(mitjanes.isEmpty()){
            return "No hi ha dades de la Mitjana ocupacio Punt";
        }
        else{
            DoubleSummaryStatistics statsMitjanaOcup = mitjanes.stream().collect(Collectors.summarizingDouble(Double::doubleValue));

            return " Mitjana ocupacio Punt = " + arrodonir(statsMitjanaOcup.getAverage()) +"% "+ " SD = " + StandardDeviation(mitjanes,statsMitjanaOcup.getAverage()) + " Var = " + Variacio(mitjanes,statsMitjanaOcup.getAverage());
        }
    }
    
    
    
 //TEMPS ESTACIONAT MIG VEHICLE--------------------------------------------------------
    
    /** 
        @brief Calcula els estadístics del vehicle
        @pre v!=null
        @post retorna el string que conté la mitjana, la desviacio estandard i la variacio del Temps que ha estat estacionat un vehicle
    */
    private String StringTempsMitjana(Vehicle v){
  
        ArrayList<Double> mitjanes = mitjanesTempsVehiEstac.get(v);
        
        if(mitjanes.isEmpty()){
            return "No hi ha dades del Temps Mig Estacionat";
        }
        else{
            DoubleSummaryStatistics statsMitjanaTemps = mitjanes.stream().collect(Collectors.summarizingDouble(Double::doubleValue));

            return "Mitjana Temps Estacionat = " + new Temps(statsMitjanaTemps.getAverage()) + " SD = " + new Temps(StandardDeviation(mitjanes,statsMitjanaTemps.getAverage())) + " Var = " + new Temps(Variacio(mitjanes,statsMitjanaTemps.getAverage()));

        }
    }
    
//OCUPACIO MIG VEHICLE----------------------------------------------------------------
    
    
    
    /** 
        @brief Calcula els estadístics dels vehicle
        @pre v!=null
        @post retorna el string que conté la mitjana, la desviacio estandard i la variacio de la Ocupacio Mitjana dels vehicles
    */
    private String StringMitjana(Vehicle v){
 
        ArrayList<Double> mitjanes = mitjanesOcupVehi.get(v); 
        if(mitjanes.isEmpty()){
            return "No hi ha dades de l'ocupacio del vehicle";
        }
        else{
            DoubleSummaryStatistics statsMitjanaOcup = mitjanes.stream().collect(Collectors.summarizingDouble(Double::doubleValue));

            return "Mitjana ocupació = " +  arrodonir(statsMitjanaOcup.getAverage())   +"% "+ " SD = " + StandardDeviation(mitjanes,statsMitjanaOcup.getAverage()) + " Var = " + Variacio(mitjanes,statsMitjanaOcup.getAverage());
        }
    }
    
//TEMPS VIATGE----------------------------------------------------------------
 
    /** 
        @brief Calcula els estadístics dels temps de viatge de les peticions
        @pre cert
        @post retorna el string que conté la mitjana, maxim, minim, la desviacio estandard i la variacio dels Temps de Viatge
    */
    private String StringTempsViatgeStats(){
        if(mitjanesTempsViatgeEsperat.isEmpty()){
            return("No hi ha dades del temps de viatge");
        }
        else{
            
            DoubleSummaryStatistics statsTempsVReal = mitjanesTempsViatgeReal.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
            DoubleSummaryStatistics statsTempsVEspe = mitjanesTempsViatgeEsperat.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
            
            double desStand1 = StandardDeviation(mitjanesTempsViatgeReal,statsTempsVReal.getAverage());
            double desStand2 = StandardDeviation(mitjanesTempsViatgeEsperat,statsTempsVEspe.getAverage());
            double var1 = Math.pow(desStand1,2);
            double var2 = Math.pow(desStand2,2);

            
            return "\nMitjana Temps de Viatge Real = " + new Temps(statsTempsVReal.getAverage()) + "  ---------  " + " Mitjana Temps de Viatge Esperat = " + new Temps(statsTempsVEspe.getAverage()) +
                   "\nMaxim Temps de Viatge Real = " + new Temps(statsTempsVReal.getMax()) + "  ---------  " + " Maxim Temps de Viatge Esperat = " + new Temps(statsTempsVEspe.getMax()) +
                   "\nMinim Temps de Viatge Real = " + new Temps(statsTempsVReal.getMin()) + "  ---------  " + " Minim Temps de Viatge Esperat = " + new Temps(statsTempsVEspe.getMin()) +
                   "\nSD Temps de Viatge Real = " + new Temps(desStand1) +  "  ---------  " + " SD Temps de Viatge Esperat = " + new Temps(desStand2) +
                   "\nVariacio Temps de Viatge Real = " + new Temps(var1) +  "  ---------  " + " Variacio Temps de Viatge Esperat = " + new Temps(var2) +
                   "\nSD Diferencies = " + new Temps(Math.abs(desStand1-desStand2)) + 
                   "\nVariacio Diferencies = " + new Temps(Math.abs(var2-var1));
        
        }
    }
    

//MÈTODES PRIVATS ÚTILS
    
    /** 
        @brief Calcula la desviació estandard conjuntament amb la mitjana
        @pre numeros.size()>0
        @post retorna la desviació estandard de la llista de numeros a partir de la mitjana, NaN si la llista està buida
    */
    private double StandardDeviation(ArrayList<Double> numeros, double mitjana){
        
        if (numeros.size()==0){
            return Double.NaN;
        }
        else{
            double sumaMitjanes = numeros.stream().mapToDouble((x) -> Math.pow(x.doubleValue() - mitjana, 2.0)).sum();
            return arrodonir(Math.sqrt(sumaMitjanes/(numeros.size()-1) )) ;
        }
    }
    
    /** 
        @brief Calcula la variació conjuntament amb la mitjana
        @pre numeros.size()>0
        @post retorna la variació de la llista de numeros a partir de la mitjana, NaN si la llista està buida
    */
    private double Variacio(ArrayList<Double> numeros, double mitjana){
    //Pre: numeros.size()>0 i mitjana>0
    //Post: retorna la variacio de llista de numeros a partir de la mitjana, NaN si la llista de està buida
        return arrodonir(Math.pow(StandardDeviation(numeros,mitjana),2));
    }
    
    /**
     * @brief permet arrodonir un numero amb dos decimals
     * @pre cert
     * @post arrodoneix un double amb dos decimals
     */
    private double arrodonir(Double valor){
        return ((double)Math.round(valor * 100d) / 100d);
    }
    
    
    /**
     *  @brief Info de totes les rutes
     *  @pre v!=null
     *  @post retorna un string amb el numero de ruta, la ruta, els kms de la ruta i el temps de la ruta d'un vehicle
    */
    private String RutesVehicle(Vehicle v){
        ArrayList<Ruta> rutes = mitjanesKMVehicle.get(v);
        String s = "";
        int i = 0;
        for(Ruta r:rutes){
            s = s + 
                "\n       Ruta " + i +
                "\n       " + r.toString() +
                "\n        \n";
            i++;
        }
        
        return s;
    }
    
    
    
    
    
//TO STRING --------------------------------------------------------------------
    
    /** 
        @brief toString de la classe
        @pre cert
        @post retorna un string amb tots els estadístics
    */
    @Override
    public String toString(){ //Format
        String s = "\nESTADÍSTIQUES Porta'm a ProP:\n" + 
                "---------------------------------------\n" +
                "\nPETICIONS:"+
                "\nPercentatge de peticions satisfetes -> " + percentatgePeticionsSatisfetes() + "%" + 
                "\nPercentatge de peticions fallades -> " + percentatgePeticionsFallades() + "%" +
                "\nPeticions satisfetes -> " + peticionsSatisfetes +
                "\nPeticions fallades -> " + peticionsFallades +
                "\nPeticions totals -> " + (peticionsFallades+peticionsSatisfetes) +
                StringTempsMigEspera()+
                "\n***************\n";
        
        
        
        s = s+"\n\nVEHICLES:";
        
        Set<Map.Entry<Vehicle,ArrayList<Double>>> ocupMitj = mitjanesOcupVehi.entrySet();
        for(Map.Entry<Vehicle,ArrayList<Double>> element:ocupMitj){
            s = s + 
                "\n ----> " + element.getKey().matricula() +
                "       " + RutesVehicle(element.getKey()) +
                "\n       " + StringKMMitjana(element.getKey()) +                  
                "\n       " + StringMitjana(element.getKey()) + 
                "\n       " + StringTempsMitjana(element.getKey()) +
                "\n***************";
                    
        }
        
        s = s+"\n\nPUNTS DE RECARREGA:";
        
        Set<Map.Entry<PuntDeRecarrega,ArrayList<Double>>> ocupMitjPRC = mitjanesOcupPRC.entrySet();
        for(Map.Entry<PuntDeRecarrega,ArrayList<Double>> punt:ocupMitjPRC){
            s = s + 
                "\n ----> " + punt.getKey().nom() + 
                "\n       " + StringPRCMitjanaOcup(punt.getKey());
        }
        
        s = s+"\n\nTEMPS VIATGE:";
        s = s+StringTempsViatgeStats();
        


        return s;
        
    }

    
}
