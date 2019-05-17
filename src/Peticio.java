/** @file Peticio.java
    @brief Classe Peticio
*/

/** @class Peticio
    @brief Guarda les dades de la petició 
    @author Marc Padrós 
*/



public class Peticio implements Comparable<Peticio> {
    
// -----------------------------------------------
    
    /*** ATRIBUTS ***/ 
    int identificador; 
    Temps horaTrucada;
    Temps horaSortida;
    Localitzacio origen;
    Localitzacio desti; 
    int nClients; 
    
    // INVARIANT:
    // identificador > 0
    // horaTrucada != null
    // horaSortida != null 
    // origen != null
    // desti != null
    // nClients > 0 
    
//--------------------------------------------------
    
    
    /**
     * @brief Creació de la petició  
     * @pre Cert
     * @post Crea Peticio amb identificador, horaTrucada, horaSortida, origen, desti i nClients 
     */
    Peticio(int identificador, Temps horaTrucada, Temps horaSortida, Localitzacio origen, Localitzacio desti, int nClients) {
        
        this.identificador = identificador;
        this.horaTrucada = horaTrucada;
        this.horaSortida = horaSortida;
        this.origen = origen;
        this.desti = desti;
        this.nClients = nClients;        
        
    }
    
    /**
     * @brief Identificador de la petició  
     * @pre Cert
     * @post Retorna l'identificador de la petició 
     */
    public int identificador() {
        return identificador; 
    }
    
    /**
     * @brief Hora de recollida de la petició 
     * @pre Cert
     * @post Retorna l'hora en la qual la petició demana sortir del punt d'origen 
     */
    public Temps horaSortida() {
        
        return horaSortida;
        
    }
    
    /**
     * @brief Hora en la qual el client truca a l'empresa
     * @pre Cert
     * @post Retorna l'hora de trucada de la petició 
     */
    public Temps horaTrucada() {
    
        return horaTrucada; 
    
    }
    
    /**
     * @brief Origen de la petició 
     * @pre Cert
     * @post Retorna el punt d'origen de la petició 
     */
    public Localitzacio origen() {
        
        return origen;
    
    }
    
    /**
     * @brief Destí de la petició 
     * @pre Cert
     * @post Retorna la destinació de la petició 
     */
    public Localitzacio desti() {
        
        return desti; 
        
    }
    
    /**
     * @brief Nombre de clients de la petició 
     * @pre Cert
     * @post Retorna el nombre de clients que han de ser recollits conjuntament en un punt 
     */
    public int NombreClients() {
        
        return nClients; 
        
    }
    
    /**
     * @brief Comparació de les hores de sortida de 2 peticions  
     * @pre Cert
     * @post Retorna un enter més petit que 0 si l'hora de sortida de this és inferior a la de p; si són iguals retorna 0 i si l'hora de sortida de p és superior a la de this retorna un enter més gran que 0 
     */
    @Override
    public int compareTo(Peticio p) {
        
        return horaSortida.compareTo(p.horaSortida); 
        
    }
    
    /**
     * @brief Dades de la petició 
     * @pre Cert
     * @post Mostra les dades de la petició 
     */
    @Override
    public String toString(){
        
        String s = "\nPETICIÓ " + identificador  + ":\n\nHora trucada: " + horaTrucada + "\nHora sortida: ";
        s+= horaSortida + "\nOrigen: " + origen + "\nDestí: " + desti  + "\nNombre de clients: " + nClients + "\n";
        return s; 
        
    }
    
}
