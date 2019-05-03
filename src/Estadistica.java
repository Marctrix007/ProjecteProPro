/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proves.fitxers;

import java.util.ArrayList;
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
    
    //private SortedSet<Peticio> peticions; 
    HashMap<PuntDeRecarrega,Double> ocupacions_punt;
    HashMap<Vehicle,Temps> temps_espera;
    HashMap<Vehicle,Double> ocupacio_vehicle;
    private int iteracions;

    
    private int peticionsSatisfetes;
    private int peticionsFallades;
    
    
    
    Estadistica(){
        peticionsSatisfetes = 0;
        peticionsFallades = 0;
        iteracions = 1;
        ocupacions_punt = new HashMap<>();
        temps_espera = new HashMap<>();
        ocupacio_vehicle = new HashMap<>();
    
    }
    
    Estadistica(List<PuntDeRecarrega> puntsPR){
        peticionsSatisfetes = 0;
        peticionsFallades = 0;
        iteracions = 1;
        
        ocupacions_punt = new HashMap<>();
        temps_espera = new HashMap<>();
        ocupacio_vehicle = new HashMap<>();
        
        for(PuntDeRecarrega punt: puntsPR){
            ocupacions_punt.put(punt, new Double(0));
            
        }
        
        
    
    }
    
    public void calcular(){
        calcularOcupacioMitjaPuntDeRecarrega();
        calcularTempsEstacionatVehicle();
        calcularOcupacioMigVehicle();
                
    }
    public void actualitzar(){
        iteracions++;
    }
    
    public void guardar_vehicle(Vehicle v){
        ocupacio_vehicle.put(v, v.Ocupacio());
        temps_espera.put(v, new Temps());
    }
    
    
    //N PETICIONS-------------------------------------------------------------------------
    
    public void incrementarNombreDeFallades(){
        peticionsFallades++;
    }
    public void incrementarNombreDeEncerts(){
        peticionsSatisfetes++;
    }
    
    public double percentatgePeticionsSatisfetes(){
        return (peticionsSatisfetes/(peticionsSatisfetes+peticionsFallades))*100;
    }
    
    public double percentatgePeticionsFallades(){
        return (peticionsFallades/(peticionsSatisfetes+peticionsFallades))*100;
    }
    

    
    //KM MITJANA COTXE--------------------------------------------------------------------
    
    //TEMPS MIG D'ESPERA PETICIONS--------------------------------------------------------
    
    
    //OCUPACIO MIG PUNTS DE RECARREGA-----------------------------------------------------
    
    public void dadesOcupacioMitjaPuntDeRecarrega(PuntDeRecarrega puntR){
        ocupacions_punt.put(puntR, ocupacions_punt.get(puntR)+puntR.ocupacio());
    }
   
    public void calcularOcupacioMitjaPuntDeRecarrega(){
        Set<Map.Entry<PuntDeRecarrega, Double>> op = ocupacions_punt.entrySet();
        for(Map.Entry<PuntDeRecarrega, Double> st:op){
            ocupacions_punt.put(st.getKey(), st.getValue()/iteracions);
        }
        
        //Calcular total?
    }
    
    
    
    //TEMPS ESTACIONAT MIG VEHICLE--------------------------------------------------------
    public void dadesTempsEstacionatVehicle(Vehicle v, Temps TempsEspera){
        temps_espera.put(v, temps_espera.get(v).mes(TempsEspera));
    }
    public void calcularTempsEstacionatVehicle(){
        Set<Map.Entry<Vehicle, Temps>> op = temps_espera.entrySet();
        for(Map.Entry<Vehicle, Temps> st:op){
            temps_espera.put(st.getKey(), st.getValue().per(1/iteracions));
        }
        
        //Calcular total?
    }
    
    //OCUPACIO MIG VEHICLE----------------------------------------------------------------
    public void dadesOcupacioMigVehicle(Vehicle v){
        ocupacio_vehicle.put(v, ocupacio_vehicle.get(v)+v.Ocupacio());
    }
    
    public void calcularOcupacioMigVehicle(){
        Set<Map.Entry<Vehicle, Double>> op = ocupacio_vehicle.entrySet();
        for(Map.Entry<Vehicle, Double> st:op){
            ocupacio_vehicle.put(st.getKey(), st.getValue()/iteracions);
        }
        
        //Calcular total?
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
    
    @Override
    public String toString(){
        String s = "\nESTADÍSTIQUES:\n" + "Peticions satisfetes -> " + peticionsSatisfetes + "\nPeticions fallades -> " + peticionsFallades + "\n***************\n";
        
        Set<Map.Entry<PuntDeRecarrega, Double>> op = ocupacions_punt.entrySet();
        for(Map.Entry<PuntDeRecarrega, Double> st:op){
           s = s + st.getKey().nom()+ " -----------> Mitjana Ocupacio PR = " +st.getValue().toString()+"\n\n";
        }
        
        Set<Map.Entry<Vehicle, Temps>> ts = temps_espera.entrySet();
        for(Map.Entry<Vehicle, Temps> st:ts){
           s = s + st.getKey().matricula()+ " -----------> Mitjana Temps Estacionat = " +st.getValue().toString()+"\n\n";
        }
        
        Set<Map.Entry<Vehicle, Double>> ov = ocupacio_vehicle.entrySet();
        for(Map.Entry<Vehicle, Double> st:ov){
           s = s + st.getKey().matricula()+ " -----------> Mitjana Ocupacio Vehicle = " +st.getValue().toString()+"\n\n";
        }
        
        return s;
        
    }
    
}
