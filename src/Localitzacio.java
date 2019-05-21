/** 
    @file Localitzacio.java
    @brief Fitxer que guarda la classe Localització
*/

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
        a_iden = 0; 
    }
    
    /** 
        @brief Constructor per paràmetres
        @pre cert
        @post Nou objecte PuntDeRecarrega creat amb els paràmetres
    */
    Localitzacio(int iden, String nom, int index_popularitat){
        a_nom = nom;
        a_index_pop = index_popularitat;
        a_iden = iden; 
    }
    
    
    /** 
        @brief Retorna l'index de popularitat
        @pre cert
        @post retorna l'index de popularitat d'una localització
    */
    public int popularitat(){
        return a_index_pop;
    }
    
    /** 
        @brief Retorna l'identificador de popularitat
        @pre cert
        @post retorna l'identificador d'una localització
    */    
    public int identificador() {
        return a_iden; 
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
        return "\nPUNT " + a_iden + "\nNom del punt: " + a_nom + "\nTé popularitat: " + a_index_pop + "\n";
    }
    
    
    
    protected String a_nom; //INVARIANT: cert
    protected int a_iden; 
    protected int a_index_pop; //INVARIANT: 0 < index_pop < 10
    
    
    
    
    
    
}

