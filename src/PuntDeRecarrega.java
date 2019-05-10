/** 
    @file PuntDeRecarrega.java
    @brief Fixter que conté la classe Punt de Recàrrega
*/
package proves.fitxers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/** 
    @class PuntDeRecarrega
    @brief Classe que guarda els Vehicles de l'empresa i és subtipus de Localització
    @author Enrique Sambola
*/
public class PuntDeRecarrega extends Localitzacio {
    
    
    public static void main(String argv[]) throws Exception {

        
    }
    
    /** 
        @brief Constructor per defecte
        @pre cert
        @post Nou objecte PuntDeRecarrega creat
    */
    PuntDeRecarrega(){
        super();
        a_nPlaces = 0;
        a_carregaRapida = false;
        a_parking = null;
    }
    
    /** 
        @brief Constructor amb paràmetres
        @pre cert
        @post Nou objecte Estadística creat a partir dels paràmetres
    */
    PuntDeRecarrega(String nom, int index_popularitat, int nPlaces, boolean CarregaRapida){
        super(nom,index_popularitat);
        a_nPlaces = nPlaces;
        a_carregaRapida = CarregaRapida;
        a_parking = new HashMap();
    }
    
    /**
     * 
        @brief Indica si accepta carrega ràpida el punt
        @pre cert
        @post retorna si accepta carrega rapida o no el punt de Recarrega
    */
    public boolean AcceptaCarregaRapida(){       
        return a_carregaRapida;
    }
    
    /** 
        @brief Indica el nombre de places lliures que hi ha al PC
        @pre a_nPlaces >= a_parking.size()
        @post retorna el nombre de places lliures al PC
    */
    public int PlacesLliures() {
        return (a_nPlaces - a_parking.size());
    }
    
    /** 
        @brief Indica l'ocupació que té eñ PC
        @pre cert
        @post retorna el percentatge d'ocupació del vehicle
    */
    public double ocupacio(){
        return 100.0*a_parking.size()/a_nPlaces;
    }
    
    
    /** 
        @brief Retorna el nom del punt de Recarrega
        @pre cert
        @post retorna el nom del PC
    */
    public String nom(){
        return a_nom;
    }
    
    
    /** 
        @brief Estaciona un vehicle al Punt de Recarrega
        @pre v!=null i PlacesLliures>0
        @post guarda un vehicle amb un temps d'entrada
    */
    public void EstacionarVehicle(Vehicle v, Temps tempsEntrada) throws ExcepcioNoQuedenPlaces{
  
        if(this.PlacesLliures() == 0)
           throw new ExcepcioNoQuedenPlaces("No queden places lliures"); 
        else{
            Temps tCar = v.TempsCarrega();
            if (AcceptaCarregaRapida() && v.CarregaRapida())
                tCar = tCar.per((float) 0.7);
            a_parking.put(v, tempsEntrada.mes(tCar));
        }
    }
    
    
    /** 
        @brief Surt un vehicle del punt de recarrega a atendre una petició
        @pre a_parking.size()<0, nPersones>0, 8h <=tempsSortida<= 22h
        @post retorna un vehicle v del punt de recarrega i l'elimina del punt
    */
    public Vehicle SortidaVehicle(int recorregut, int nPersones, Temps tempsSortida) throws ExcepcioNoQuedenVehicles{    // vehicle no surt fins que no està carregat 
  
        if (a_parking.isEmpty()){
            throw new ExcepcioNoQuedenVehicles("No hi ha vehicles");
        }
        else{
        
            Set<Map.Entry<Vehicle, Temps>> vehicles = a_parking.entrySet();
            Iterator<Map.Entry<Vehicle, Temps>> it_vehicles = vehicles.iterator();
            Map.Entry<Vehicle, Temps> entry_vehicle;
            Vehicle v = null;
            boolean trobat = false;
            
            while(it_vehicles.hasNext() && !trobat){
                entry_vehicle = it_vehicles.next();
                v = entry_vehicle.getKey();
                if (v.Autonomia() > recorregut && v.NombrePlaces() > nPersones && entry_vehicle.getValue().compareTo(tempsSortida) < 0){
                    trobat = true;
                }
            }
            
            if (!trobat)
                return null;
            else{
                a_parking.remove(v);
                return v;
            }
            
        }
    }    
        
        
    
    /** 
        @brief Indica que es un punt de recarrega
        @pre cert
        @post retorna cert indicant que es un punt de recarrega
    */
    @Override
    public boolean esPuntDeRecarrega(){
        return true;
    }
    
    
    /** 
        @brief toString de la classe
        @pre cert
        @post retorna l'string amb les dades d'un Punt de recàrrega
    */
    @Override
    public String toString(){
        String s = "\n-------------------------------------------\n";
        try {
            s = s + "Nom del punt: " + a_nom + "\nTe popularitat: " + a_index_pop + "\nPlaces disponibles: " + PlacesLliures();
        } catch (ExcepcioNoQuedenPlaces ex) {
            System.err.println(ex);
        }
        
        Set<Map.Entry<Vehicle, Temps>> vehicles = a_parking.entrySet();
        Iterator<Map.Entry<Vehicle, Temps>> it_vehicles = vehicles.iterator();
        Map.Entry<Vehicle, Temps> entry_vehicle;
        s = s + "\n*****************************";
        s = s + "\nVehicles que conté:\n";
        while(it_vehicles.hasNext()){
            entry_vehicle = it_vehicles.next();
            Vehicle v = entry_vehicle.getKey();
            s = s + v.toString()+"\n\n";
        
        }
        s = s + "\n-------------------------------------------\n";
        return s;
    }
    
    //ATRIBUTS
    private final int a_nPlaces; //INVARIANT: a_nPlaces > 0 i a_nPlaces >= a_parking.size()
    private final boolean a_carregaRapida; //INVARIANT: cert
    private HashMap<Vehicle, Temps> a_parking; //INVARIANT: a_parking.size()<=a_nPlaces
    // Més el de la classe Localització heretats
    
    

//------------------------------------------------------------------------------------------------------    
    //EXCEPCIONS
    private static class ExcepcioNoQuedenPlaces extends Exception {
    //Descripcio: excepcio que es crida quan no queden places al parking i no es poden afegir més vehicles  
        public ExcepcioNoQuedenPlaces(){
            super();
        }
        public ExcepcioNoQuedenPlaces(String s) {
            super(s);
        }
    }

    private static class ExcepcioNoQuedenVehicles extends Exception {
    //Descripcio: excepcio que es crida quan no hi ha vehicles i es demana que en surti un
        public ExcepcioNoQuedenVehicles() {
            super();
        }
        public ExcepcioNoQuedenVehicles(String s) {
            super(s);
        }
    }
    
    
}

