/**
@class Temps
@brief Temps en hh:mm
@author Xavier Rodríguez i Martínez
 */

public class Temps implements Comparable<Temps>{

    private final int hora;
    private final int minut;
    
    /**
    @pre --
    @post Constructor per defecte
    */
    public Temps(){
        hora = 0;
        minut = 0;
    }
    
    /**
    @pre --
    @post Temps creat amb l'excés de minuts corregit
    */
    public Temps(int h, int m){
        hora = h + m/60;
        minut = m%60;
    }

    /**
    @pre --
    @post L'hora és l'enter i els minuts la fracció centesimal
    */
    public Temps(float t){
      hora = (int) t;
      t -= hora;
      t*=60;
      minut = (int)t;
    }
    
    /**
    @pre s  té forma hh:mm
    @post Temps creat
    */
    public Temps(String s) {   
        String[] tempsArr = s.split(":"); 
        hora = Integer.parseInt(tempsArr[0]);
        minut = Integer.parseInt(tempsArr[1]);
        
    }
    
    /**
    @pre --
    @post Retorna el temps de  this  i  t  sumats
    */
    public Temps mes(Temps t){
        return new Temps(this.hora+t.hora, this.minut+t.minut);
    }

    /**
    @pre --
    @post Retorna la diferència de  this  i  t
    */
    public Temps menys(Temps t){
        int m1 = this.hora*60 + this.minut;
        int m2 = t.hora*60 + t.minut;
        return new Temps(0, m1-m2);
    }

    /**
    @pre --
    @post Multiplica el temps  this  per  n  vegades
    */
    public Temps per(float n){
        int m = this.minut + this.hora*60;
        m = (int) (m*n);
        return new Temps(0, m);
    }
    
    /**
    @pre --
    @post Retorna el Temps en format double
    */
    public double conversioDouble(){
        return hora + minut/60.0;
    }
    
    @Override
    public int compareTo(Temps t){
      int r = this.hora - t.hora;
      if (r==0) r = this.minut - t.minut;
      return r;
    }

    @Override
    public String toString(){
        if (minut <= -10) return "-" + (-hora) + ":" + (-minut);
        else if (minut < 0) return "-" + (-hora) + ":0" + (-minut);
        else if (minut<10) return hora + ":0" + minut;
        else return hora + ":" + minut;
    }
}
