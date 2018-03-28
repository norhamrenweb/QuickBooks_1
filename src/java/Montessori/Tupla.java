/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

/**
 *
 * @author Norhan
 */
public class Tupla <X, Y>{
    public final X x; 
    public final Y y; 
    public Tupla(X x, Y y) { 
        this.x = x; 
        this.y = y;
    } 
    public String toString(){
        return "x: " + x +"- y:"+ y+" ";
    }
    
    public String text(){
        return x +" "+ y;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }
}
