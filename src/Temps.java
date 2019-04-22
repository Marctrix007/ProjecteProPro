/**
 * @author Xavier Rodríguez i Martínez
 */
public class Temps {
    //Descripció general: Classe Temps amb Hora:minut

    private int hora;
    private int minut;

    public Temps(){
        hora = 0;
        minut = 0;
    }

    public Temps(int h, int m){
        //Pre: --
        //Post: Temps creat amb l'excés de minuts corregits
        hora = h + m/60;
        minut = m%60;
    }

    public Temps(float t){
      hora = (int) t;
      t -= hora;
      minut = (int) t*60;
    }

    public Temps mes(Temps t){
        //Pre: --
        //Post: Retorna els temps de  this  i  t  sumats.
        return new Temps(this.hora+t.hora, this.minut+t.minut);
    }

    public Temps menys(Temps t){
        //Pre: --
        //Post: Retora la diferència de  this  i  t.
        int m1 = this.hora*60 + this.minut;
        int m2 = t.hora*60 + t.minut;
        return new Temps(0, m1-m2);
    }
    

    public Temps per(float n){
        //Pre: --
        //Post: multiplica el temps  this  per  n  vegades
        int m = this.minut + this.hora*60;
        m = (int) (m*n);
        return new Temps(0, m);
    }
    
    public boolean EsMesGran(Temps t) {
                
        return ((hora > t.hora) || ((hora == t.hora) && (minut > t.minut))); 
        
    }
    
    public boolean EsMesPetit(Temps t) {
        
        return ((hora < t.hora) || ((hora == t.hora) && (minut < t.minut))); 

    }
    
    
    @Override
    public String toString(){
        if (minut <= -10) return "-" + (-hora) + ":" + (-minut);
        else if (minut < 0) return "-" + (-hora) + ":0" + (-minut);
        else if (minut<10) return hora + ":0" + minut;
        else return hora + ":" + minut;
    }
        
       
}
