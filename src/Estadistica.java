/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proves.fitxers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

/**
 *
 * @author qsamb
 */
public class Estadistica {
    
    static final int FALLADA = -1;
    static final int SATISFETA = 1;
    

    
    private int peticionsSatisfetes;
    private int peticionsFallades;
    
    
    //Vehicles
    private HashMap<Vehicle,ArrayList<Double>> mitjanesOcupVehi;
    private HashMap<Vehicle,ArrayList<Double>> mitjanesTempsVehiEstac;
    private HashMap<PuntDeRecarrega,ArrayList<Double>> mitjanesOcupPRC;
    
    
    Estadistica(){
        peticionsSatisfetes = 0;
        peticionsFallades = 0;
        mitjanesOcupVehi =  new HashMap<>(); 
        mitjanesTempsVehiEstac = new HashMap<>();
        mitjanesOcupPRC = new HashMap<>();
    
    }
    
    public void guardarVehicle(Vehicle v){
    //Pre: Vehicle v no guardat abans
    //Post: Guarda el vehicle v per fer estadistics
        mitjanesOcupVehi.put(v, new ArrayList());
        mitjanesTempsVehiEstac.put(v, new ArrayList());
    }
    
    public void guardarPuntRC(PuntDeRecarrega p){
    //Pre: PuntDeRecarrega p no guardat abans
    //Post: Gurada el punt p per fer estadistics
        mitjanesOcupPRC.put(p, new ArrayList());
    }
    
    
//N PETICIONS-------------------------------------------------------------------------
    
    public void incrementarNombreDeFallades(){
    //Pre: --
    //Post: augmenta en 1 el nombre de fallades de peticions       
        peticionsFallades++;
    }
    public void incrementarNombreDeEncerts(){
    //Pre: -- 
    //Post: augmenta en 1 el nombre d'encerts de peticions     
        peticionsSatisfetes++;
    }
    
    public double percentatgePeticionsSatisfetes(){
    //Pre: --
    //Post: retorna el percentage de peticions que han estat satisfetes  
        return (peticionsSatisfetes/(peticionsSatisfetes+peticionsFallades))*100;
    }
    
    public double percentatgePeticionsFallades(){
    //Pre: --
    //Post: retorna el percentage de peticions que han estat fallades      
        return (peticionsFallades/(peticionsSatisfetes+peticionsFallades))*100;
    }
    

    
//KM MITJANA COTXE--------------------------------------------------------------------
    
//TEMPS MIG D'ESPERA PETICIONS--------------------------------------------------------
    
    
//OCUPACIO MIG PUNTS DE RECARREGA-----------------------------------------------------
    public void guardarOcupacioMigPuntRC(PuntDeRecarrega p, Double ocup){
    //Pre: --
    //Post: al punt de recarrega p es guarda la seva ocupacio per fer estadistics  
        mitjanesOcupPRC.get(p).add(ocup);
    }
    
    public String StringPRCMitjanaOcup(PuntDeRecarrega p){
    //Pre: p no null
    //Post: retorna el string que conté la mitjana, la desviacio estandard i la variacio de la Ocupacio Mitja dels Punts de Recarrega
        ArrayList<Double> mitjanes = mitjanesOcupPRC.get(p);
        
        DoubleSummaryStatistics statsMitjanaOcup = mitjanes.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
        
        return " Mitjana ocupacio Punt = " + statsMitjanaOcup.getAverage() + " SD = " + StandardDeviation(mitjanes,statsMitjanaOcup.getAverage()) + " Var = " + Variacio(mitjanes,statsMitjanaOcup.getAverage());
        
    }
    
    
    
 //TEMPS ESTACIONAT MIG VEHICLE--------------------------------------------------------
    
    public void guardartempsEstacionatVehicle(Vehicle v, Temps t){
    //Pre: --
    //Post: al vehicle v es guarda el temps que ha estat estacionat per fer estadistics     
        mitjanesTempsVehiEstac.get(v).add(t.conversioDouble());
        System.out.println("Temps Estacionat guardat: " + t.conversioDouble());
    }
    
    public String StringTempsMitjana(Vehicle v){
    //Pre: v no null
    //Post: retorna el string que conté la mitjana, la desviacio estandard i la variacio del Temps Mig dels Punts de Recarrega   
        ArrayList<Double> mitjanes = mitjanesTempsVehiEstac.get(v); System.out.println("***Elements = " + mitjanes +"***\n");
        
        DoubleSummaryStatistics statsMitjanaTemps = mitjanes.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
       
        
        return "\n--- Mitjana Temps Estacionat = " + statsMitjanaTemps.getAverage() + " SD = " + StandardDeviation(mitjanes,statsMitjanaTemps.getAverage()) + " Var = " + Variacio(mitjanes,statsMitjanaTemps.getAverage());
        
    }
    
//OCUPACIO MIG VEHICLE----------------------------------------------------------------

    public void guardarOcupacioVehicle(Vehicle v, Double ocup){
    //Pre: --
    //Post: al vehicle v es guarda la seva ocupacio per fer estadistics      
        mitjanesOcupVehi.get(v).add(ocup);        
    }
    
    public String StringMitjana(Vehicle v){
    //Pre: v no null
    //Post: retorna el string que conté la mitjana, la desviacio estandard i la variacio de la Ocupacio Mitjana dels Vehicles     
        ArrayList<Double> mitjanes = mitjanesOcupVehi.get(v);
        
        DoubleSummaryStatistics statsMitjanaOcup = mitjanes.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
        
        return " Mitjana ocupació = " + statsMitjanaOcup.getAverage() + " SD = " + StandardDeviation(mitjanes,statsMitjanaOcup.getAverage()) + " Var = " + Variacio(mitjanes,statsMitjanaOcup.getAverage());
    }
    
    
    public double StandardDeviation(ArrayList<Double> numeros, double mitjana){
    //Pre: numeros.size() > 0 i mitjana > 0
    //Post: retorna la desviacio estandard de la llista de numeros a partir de la mitjana, NaN si la llista està buida
        if (numeros.size()==0){
            return Double.NaN;
        }
        else{
            double sumaMitjanes = numeros.stream().mapToDouble((x) -> Math.pow(x.doubleValue() - mitjana, 2.0)).sum();
            return Math.sqrt(sumaMitjanes/(numeros.size()-1));
        }
    }
    
    public double Variacio(ArrayList<Double> numeros, double mitjana){
    //Pre: numeros.size()>0 i mitjana>0
    //Post: retorna la variacio de llista de numeros a partir de la mitjana, NaN si la llista de està buida
        return Math.pow(StandardDeviation(numeros,mitjana),2);
    }
    
    
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
        
        

        return s;
        
    }
    
    //PROVES FETES
    /*public void calcularEstadisticsPeticions(){
        DoubleSummaryStatistics statsPeticionsFallades = peticionsSegonsEstat(peticions,FALLADA);
        DoubleSummaryStatistics statsPeticionsSatisfetes = peticionsSegonsEstat(peticions,SATISFETA);
        
        peticionsSatisfetes = (int) statsPeticionsFallades.getCount();
        peticionsFallades = (int) statsPeticionsSatisfetes.getCount();
       
    }*/
    
    /*public DoubleSummaryStatistics peticionsSegonsEstat(int estatDemanat){
    
        return peticions.stream().filter(x -> x.estatActual() == estatDemanat).collect(Collectors.summarizingDouble(Peticio::estatActual));
    }*/
    
    /*public void guardarPeticioPerEstadistics(Peticio p){
        peticions.add(p);
    }*/
    
}
