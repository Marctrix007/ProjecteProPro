
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author qsamb
 */
public class PuntDeRecarrega extends Localitzacio {
    
    PuntDeRecarrega(){
        super();
        a_nPlaces = 0;
        a_carregaRapida = false;
        a_parking = new HashMap();
    }
    
    PuntDeRecarrega(String nom, int index_popularitat, int nPlaces, boolean CarregaRapida){
        super(nom,index_popularitat);
        a_nPlaces = nPlaces;
        a_carregaRapida = CarregaRapida;
        a_parking = new HashMap();
    }
    
    public boolean AcceptaCarregaRapida(){
    //Pre: a_nPlaces > mida a_parking
    //Post: retorna el nombre de places lliures al PC        
        return a_carregaRapida;
    }
    
    public int PlacesLliures() throws ExcepcioNoQuedenPlaces{
    //Pre: a_nPlaces > mida a_parking
    //Post: retorna el nombre de places lliures al PC
        return (a_nPlaces - a_parking.size());
    }
  
    
    public void EstacionarVehicle(Vehicle v, Temps tempsEntrada) throws ExcepcioNoQuedenPlaces{
     /*
        Pre: han de haver places al punt de recarrega
        Post: vehicle v estacionat conjuntament amb el tempEntrada + tCar
    
    */      
        if(this.PlacesLliures() == 0)
           throw new ExcepcioNoQuedenPlaces("No queden places lliures"); 
        else{
            Temps tCar = v.TempsCarrega();
            if (AcceptaCarregaRapida() && v.CarregaRapida())
                tCar = tCar.per((float) 0.7);
            a_parking.put(v, tempsEntrada.mes(tCar));
        }
    }
    
    public Vehicle SortidaVehicle(int recorregut, int nPersones, Temps tempsSortida) throws ExcepcioNoQuedenVehicles{    // vehicle no surt fins que no està carregat 
    /*
        Pre: recorregut > 0, nPersones > 0 i han de haver vehicles estacionats al punt de recarrega
        Post: surt un Vehicle v qualsevol que la seva autonomia > recorregut, NombrePlaces > nPersones i que tempsCarrega < tempsSortida
    
    */    
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
        
        
    
    
    @Override
    public boolean esPuntDeRecarrega(){
        return true;
    }
    
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
    private final int a_nPlaces; //INVARIANT: a_nPlaces > 0 i a_nPlaces > a_parking
    private final boolean a_carregaRapida; //INVARIANT: cert
    private HashMap<Vehicle, Temps> a_parking; //INVARIANT: cert
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
