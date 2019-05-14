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
    
   
    private int peticionsSatisfetes;
    private int peticionsFallades;
    private HashMap<Vehicle,ArrayList<Double>> mitjanesOcupVehi;
    private HashMap<Vehicle,ArrayList<Double>> mitjanesTempsVehiEstac;
    private HashMap<PuntDeRecarrega,ArrayList<Double>> mitjanesOcupPRC;
    private HashMap<Vehicle,ArrayList<Ruta>> mitjanesKMVehicle;
    private ArrayList<Double> mitjanesTempsEspera;
    /*
    INVARIANT:
    peticionsSatisfetes: >=0
    peticionsFallades: >=0
    mitjanesOcuVehi: !=null
    mitjanesTempsVehiEstac: !=null
    mitjanesOcupPRC: !=null
    mitjanesKMVehicle: !=null
    mitjanesTempsEspera: !=null
        
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
    
    }
    
     /** 
        @brief Guarda el vehicle per fer estadístics
        @pre Vehicle v no guardat abans i v!=null
        @post Vehicle v guardat
    */   
    public void guardarVehicle(Vehicle v){
        mitjanesOcupVehi.put(v, new ArrayList());
        mitjanesTempsVehiEstac.put(v, new ArrayList());
        mitjanesKMVehicle.put(v, new ArrayList());
    }
    
    /** 
        @brief Guarda el punt de recàrrega per fer estadístics
        @pre Punt de Recàrrega p no guardat abans i p!=null
        @post Punt de Recàrrega p guardat
    */   
    public void guardarPuntRC(PuntDeRecarrega p){
        mitjanesOcupPRC.put(p, new ArrayList());
    }
    

    
    
//N PETICIONS-------------------------------------------------------------------------
    
    
    
    /** 
        @brief Incrementa les peticions que han sigut fallades
        @pre cert
        @post augmenta en 1 el nombre de fallades de peticions 
    */   
    public void incrementarNombreDeFallades(){      
        peticionsFallades++;
    }
    
    /** 
        @brief Incrementa les peticions que han sigut ateses correctament
        @pre cert
        @post augmenta en 1 el nombre d'encerts de peticions 
    */ 
    public void incrementarNombreDeEncerts(){    
        peticionsSatisfetes++;
    }
    
    /** 
        @brief Retorna el percentatge de peticions satisfetes
        @pre cert
        @post retorna el percentage de peticions que han estat satisfetes  
    */ 
    public double percentatgePeticionsSatisfetes(){  
        return (peticionsSatisfetes/(peticionsSatisfetes+peticionsFallades))*100;
    }
    
    /** 
        @brief Retorna el percentatge de peticions fallades
        @pre cert
        @post retorna el percentage de peticions que han estat fallades
    */
    public double percentatgePeticionsFallades(){
        return (peticionsFallades/(peticionsSatisfetes+peticionsFallades))*100;
    }
    

    
//KM MITJANA COTXE--------------------------------------------------------------------
    
    /** 
        @brief Guarda el recorregut que ha fet el vehicle per fer estadistics
        @pre Vehicle v ja guardat i v!=null i r!=null
        @post guarda la ruta r d'un vehicle v concret
    */
    public void guardarRecorregutVehicle(Vehicle v, Ruta r){
        mitjanesKMVehicle.get(v).add(r);
    }
    
    /** 
        @brief Calcula els kilometres de mitjana que ha fet un cotxe en totes les seves rutes
        @pre v!=null
        @post retorna un string amb la mitjana de kilometres fets per un vehicle  
    */
    public String StringKMMitjana(Vehicle v){
        
        ArrayList<Ruta> mitjanes = mitjanesKMVehicle.get(v);
        
        DoubleSummaryStatistics statsMitjanaKM = mitjanes.stream().collect(Collectors.summarizingDouble(Ruta::kmFets));
        
        return " Km mig fet = " + statsMitjanaKM.getAverage();
    }
    
    
//TEMPS MIG D'ESPERA PETICIONS--------------------------------------------------------
    
    /** 
        @brief Guarda el Temps que ha esperat una peticio a ser atesa
        @pre espera!=null
        @post guarda el temps d'espera d'una peticio
    */
    public void guardarTempsEsperaPeticio(Temps espera){
        mitjanesTempsEspera.add(espera.conversioDouble());
    }
    
    /** 
        @brief Calcula els estadístics del temps d'espera de les peticions
        @pre cert
        @post retorna un string amb la mitjana, el maxim i minim temps d'espera de les peticions
    */
    public String StringTempsMigEspera(){
        
        DoubleSummaryStatistics statsMitjanaTempsE = mitjanesTempsEspera.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
     
        return "Temps mig d'espera = " + statsMitjanaTempsE.getAverage() + "\nTemps màxim d'espera = " + statsMitjanaTempsE.getMax() + "\nTemps mínim d'espera = " + statsMitjanaTempsE.getMin() + "\nSD = " + StandardDeviation(mitjanesTempsEspera,statsMitjanaTempsE.getAverage()) + "\nVariació = " + Variacio(mitjanesTempsEspera,statsMitjanaTempsE.getAverage());
    }
    
    
    
//OCUPACIO MIG PUNTS DE RECARREGA-----------------------------------------------------
    
    /** 
        @brief Guarda l'ocupació del punt de recàrrega
        @pre p!=null
        @post guarda l'ocupació d'un punt de recàrrega concret
    */
    public void guardarOcupacioMigPuntRC(PuntDeRecarrega p, Double ocup){  
        mitjanesOcupPRC.get(p).add(ocup);
    }
    
    
    /** 
        @brief Calcula els estadístics dels punts de recarrega
        @pre p!=null
        @post retorna el string que conté la mitjana, la desviacio estandard i la variacio de la Ocupacio Mitja dels Punts de Recarrega
    */
    public String StringPRCMitjanaOcup(PuntDeRecarrega p){
    
        ArrayList<Double> mitjanes = mitjanesOcupPRC.get(p);
        
        DoubleSummaryStatistics statsMitjanaOcup = mitjanes.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
        
        return " Mitjana ocupacio Punt = " + statsMitjanaOcup.getAverage() + " SD = " + StandardDeviation(mitjanes,statsMitjanaOcup.getAverage()) + " Var = " + Variacio(mitjanes,statsMitjanaOcup.getAverage());
        
    }
    
    
    
 //TEMPS ESTACIONAT MIG VEHICLE--------------------------------------------------------
    
    /** 
        @brief Guardar el temps que ha estat estacionat un vehicle
        @pre v!=null
        @post guarda el temps que ha estat estacionat un vehicle
    */
    public void guardartempsEstacionatVehicle(Vehicle v, Temps t){  
        mitjanesTempsVehiEstac.get(v).add(t.conversioDouble());
    }
    
    /** 
        @brief Calcula els estadístics del vehicle
        @pre v!=null
        @post retorna el string que conté la mitjana, la desviacio estandard i la variacio del Temps que ha estat estacionat un vehicle
    */
    public String StringTempsMitjana(Vehicle v){
  
        ArrayList<Double> mitjanes = mitjanesTempsVehiEstac.get(v); System.out.println("***Elements = " + mitjanes +"***\n");
        
        DoubleSummaryStatistics statsMitjanaTemps = mitjanes.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
       
        
        return "\n--- Mitjana Temps Estacionat = " + statsMitjanaTemps.getAverage() + " SD = " + StandardDeviation(mitjanes,statsMitjanaTemps.getAverage()) + " Var = " + Variacio(mitjanes,statsMitjanaTemps.getAverage());
        
    }
    
//OCUPACIO MIG VEHICLE----------------------------------------------------------------
    
    /** 
        @brief Guarda l'ocupació que ha tingut un vehicle
        @pre v!=null
        @post guarda l'ocupació d'un vehicle
    */
    public void guardarOcupacioVehicle(Vehicle v, Double ocup){    
        mitjanesOcupVehi.get(v).add(ocup);        
    }
    
    /** 
        @brief Calcula els estadístics dels vehicle
        @pre v!=null
        @post retorna el string que conté la mitjana, la desviacio estandard i la variacio de la Ocupacio Mitjana dels vehicles
    */
    public String StringMitjana(Vehicle v){
 
        ArrayList<Double> mitjanes = mitjanesOcupVehi.get(v);
        
        DoubleSummaryStatistics statsMitjanaOcup = mitjanes.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
        
        return " Mitjana ocupació = " + statsMitjanaOcup.getAverage() + " SD = " + StandardDeviation(mitjanes,statsMitjanaOcup.getAverage()) + " Var = " + Variacio(mitjanes,statsMitjanaOcup.getAverage());
    }
    
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
            return Math.sqrt(sumaMitjanes/(numeros.size()-1));
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
        return Math.pow(StandardDeviation(numeros,mitjana),2);
    }
    
    /** 
        @brief toString de la classe
        @pre cert
        @post retorna un string amb tots els estadístics
    */
    @Override
    public String toString(){
        String s = "\nESTADÍSTIQUES:\n" + "Peticions satisfetes -> " + peticionsSatisfetes + "\nPeticions fallades -> " + peticionsFallades + "\n***************\n";
        
        Set<Map.Entry<PuntDeRecarrega,ArrayList<Double>>> ocupMitjPRC = mitjanesOcupPRC.entrySet();
        for(Map.Entry<PuntDeRecarrega,ArrayList<Double>> punt:ocupMitjPRC){
            s = s + "\n" + punt.getKey().nom() + StringPRCMitjanaOcup(punt.getKey());
        }
        
        
        Set<Map.Entry<Vehicle,ArrayList<Double>>> ocupMitj = mitjanesOcupVehi.entrySet();
        for(Map.Entry<Vehicle,ArrayList<Double>> element:ocupMitj){
            s = s + "\n" + element.getKey().matricula() + StringMitjana(element.getKey()) + StringTempsMitjana(element.getKey());
        }
        
        s = s + StringTempsMigEspera();

        return s;
        
    }

    
}
