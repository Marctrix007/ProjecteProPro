/** 
    @file PuntDeRecarrega.java
    @brief Fixter que conté la classe Punt de Recàrrega
*/
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.util.Pair;


/** 
    @class PuntDeRecarrega
    @brief Classe que guarda els Vehicles de l'empresa i és subtipus de Localització
    @author Enrique Sambola
*/
public class PuntDeRecarrega extends Localitzacio {
    
   
    
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
    PuntDeRecarrega(int iden, String nom, int index_popularitat, int nPlaces, boolean CarregaRapida){
        super(iden,nom,index_popularitat);
        a_nPlaces = nPlaces;
        a_carregaRapida = CarregaRapida;
        a_parking = new HashMap();
    }
    
    /**
     * 
     * @return 
        @brief Indica si accepta carrega ràpida el punt
        @pre cert
        @post retorna si accepta carrega rapida o no el punt de Recarrega
    */
    public boolean AcceptaCarregaRapida(){       
        return a_carregaRapida;
    }
    
    /**
     * @return 
        @brief Indica el nombre de places lliures que hi ha al PC
        @pre a_nPlaces >= a_parking.size()
        @post retorna el nombre de places lliures al PC
    */
    public int PlacesLliures() {
        return (a_nPlaces - a_parking.size());
    }
    
    /**
     * @return 
        @brief Indica l'ocupació que té eñ PC
        @pre cert
        @post retorna el percentatge d'ocupació del vehicle
    */
    public double ocupacio(){
        return 100.0*a_parking.size()/a_nPlaces;
    }
    
    /**
     * @return 
        @brief Indica la capacitat del punt de recàrrega
        @pre cert
        @post retorna el nombre de places del punt de recàrrega
    */
    public int Capacitat(){
        return a_nPlaces; 
    }
    
    
    
    /**
     *  @return 
        @brief Indica si el punt de recàrrega està buit 
        @pre cert
        @post retorna cert si el punt de recàrrega està buit, fals altrament 
    */
    public boolean Buit(){
        return (a_parking.isEmpty()); 
    }
    
    
    
    /**
     * @return 
        @brief Retorna el nom del punt de Recarrega
        @pre cert
        @post retorna el nom del PC
    */
    public String nom(){
        return a_nom;
    }
    
    
    /**
     * @param v
     * @param tempsEntrada
     * @throws PuntDeRecarrega.ExcepcioNoQuedenPlaces
        @brief Estaciona un vehicle al Punt de Recarrega
        @pre v!=null i PlacesLliures>0
        @post guarda un vehicle amb un temps d'entrada
    */
    public void EstacionarVehicle(Vehicle v, Temps tempsEntrada) throws ExcepcioNoQuedenPlaces{
  
        if(this.PlacesLliures() == 0)
           throw new ExcepcioNoQuedenPlaces("No queden places lliures"); 
        else{
            if (tempsEntrada.compareTo(new Temps(5,0)) == 0) // si el vehicle s'estaciona per primer cop, és a dir, a les 5h del matí suposem que estarà disponible a partir de les 7h 
                a_parking.put(v,new Temps(7,0)); 
            else 
                a_parking.put(v, tempsEntrada.mes(tempsCarrega(v)));
        }
        
        //System.out.println("Hora disponibilitat: ");    /** L'HORA ES GUARDA BÉ ***/
        //System.out.println(a_parking.get(v) + "\n");
       
    }
    
    
    /** 
        @brief Surt un vehicle del punt de recarrega a atendre una petició
        @pre a_parking.size()<0, nPersones>0, 8h <=tempsSortida<= 22h
        @post retorna un vehicle v del punt de recarrega i l'elimina del punt
    */
    /** 
        @brief Surt un vehicle del punt de recarrega a atendre una petició
        @pre a_parking.size()<0, nPersones>0, 8h <=tempsSortida<= 22h
        @post retorna un vehicle v del punt de recarrega i l'elimina del punt
    */
    public Pair<Vehicle,Temps> SortidaVehicle(float recorregut, int nPersones, Temps horaArribada, Temps horaPeticio, Temps horaMax, Temps horaAvis) throws ExcepcioNoQuedenVehicles{    // vehicle no surt fins que no està carregat 
  
        if (Buit()){
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
                System.out.println("*********\n");
                System.out.println("VEHICLE A ANALITZAR: " + v.matricula() + " amb tDisp = " + a_parking.get(v));
                System.out.println("Temps disponible: " + entry_vehicle.getValue() + " Hora d'Avis: " + horaAvis);
                if (entry_vehicle.getValue().compareTo(horaAvis) <= 0) {// si tDisp <= horaAvis, el vehicle està disponible quan l'avisa l'empresa 
                    System.out.println("Hora Peticio: " + horaPeticio + " Hora Arribada: " + horaArribada + " Hora Max: " + horaMax);
                    System.out.println("Té autonomia de " + v.Autonomia() + " i ha de recorre " + recorregut + " amb unes places de " + v.NombrePlaces() +  " per " + nPersones + " clients");
                    boolean arribarAbans = false, arribarDespres = false;  
                    // si el vehicle arriba al punt d'origen de la petició 10 minuts abans de l'hora de sortida 
                    if (horaPeticio.menys(horaArribada).compareTo(new Temps(0,10)) <= 0 && horaPeticio.menys(horaArribada).compareTo(new Temps(0,0))>=0)  
                        arribarAbans = true;
                    // si el vehicle arriba al punt d'origen de la petició entre l'hora de sortida i l'hora sortida més el temps d'espera màxim 
                    else if (horaArribada.compareTo(horaPeticio) > 0 && horaArribada.compareTo(horaMax) <= 0) 
                        arribarDespres = true;     
                    if (arribarAbans || arribarDespres && v.Autonomia() >= recorregut && v.NombrePlaces() > nPersones){
                        trobat = true;
                        System.out.println("VEHICLE TROBAT");
                    }
                }
            }           
            if (!trobat)
                return null;
            else{
                Temps horaDisp = a_parking.get(v);
                a_parking.remove(v);
                return new Pair(v,horaDisp);
            }
        }
    } 
    
    /** 
        @brief Indica l'hora de disponibilitat d'un vehicle determinat 
        @pre v està estacionat en el punt de recàrrea 
        @post retorna l'hora de disponibilitat de v
    */
    public Temps horaDisponibilitat(Vehicle v){
        return a_parking.get(v); 
    }
    
    /** 
        @brief Temps de càrrega d'un vehicle determinat 
        @pre v està estacionat en el punt de recàrrega
        @post retorna el temps de càrrega de v 
    */
    public Temps tempsCarrega(Vehicle v){
        Temps tCar = v.TempsCarrega();
        if (AcceptaCarregaRapida() && v.CarregaRapida())
            tCar = tCar.per(0.7);
        return tCar; 
    }
    
    
    /** 
        @brief Indica que és un punt de recarrega
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
        s = s + "PUNT DE RECÀRREGA " + a_iden + "\nNom del punt: " + a_nom + "\nTé popularitat: " + a_index_pop + "\nPlaces disponibles: " + PlacesLliures() + "\nCàrrega ràpida: ";
        if (a_carregaRapida) {
            s = s + "SÍ";
        }
        else s = s + "NO";
        
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
    static class ExcepcioNoQuedenPlaces extends Exception {
    //Descripcio: excepcio que es crida quan no queden places al parking i no es poden afegir més vehicles  
        public ExcepcioNoQuedenPlaces(){
            super();
        }
        public ExcepcioNoQuedenPlaces(String s) {
            super(s);
        }
    }

    static class ExcepcioNoQuedenVehicles extends Exception {
    //Descripcio: excepcio que es crida quan no hi ha vehicles i es demana que en surti un
        public ExcepcioNoQuedenVehicles() {
            super();
        }
        public ExcepcioNoQuedenVehicles(String s) {
            super(s);
        }
    }
    
    
}
