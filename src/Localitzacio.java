/** 
    @file Localitzacio.java
    @brief Fixter que guarda la classe Localització
*/

package proves.fitxers;

/** 
    @class Localitzacio
    @brief Classe que guarda la informacio d'una Localització de l'empresa
    @author Enrique Sambola
*/



public class Localitzacio {
    
    
    /** 
        @brief Constructor per defecte
        @pre cert
        @post Nou objecte Localitzacio creat
    */
    Localitzacio(){
        a_nom = " ";
        a_index_pop = 0;
    }
    
    /** 
        @brief Constructor per paràmetres
        @pre cert
        @post Nou objecte PuntDeRecarrega creat amb els paràmetres
    */
    Localitzacio(String nom, int index_popularitat){
        a_nom = nom;
        a_index_pop = index_popularitat;
    }
    
    
    /** 
        @brief Retorna l'index de popularitat
        @pre cert
        @post retorna l'index de popularitat d'una localització
    */
    int popularitat(){
        return a_index_pop;
    }
    
    
    /** 
        @brief indica que no és un punt de recàrrega
        @pre cert
        @post retorna false indicant que no és un Punt de Recàrrega
    */
    boolean esPuntDeRecarrega(){  
        return false;
    }
    
    
    /** 
        @brief toString de la classe
        @pre cert
        @post retorna l'string amb les dades d'una localització
    */
    @Override
    public String toString(){
        return "\nNom del punt: " + a_nom + "\nTe popularitat: " + a_index_pop + "\n";
    }
    
    
    
    protected String a_nom; //INVARIANT: cert
    protected int a_index_pop; //INVARIANT: 0 < index_pop < 10
    
    
    
    
    
    
}
