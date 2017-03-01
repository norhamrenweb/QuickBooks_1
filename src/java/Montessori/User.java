/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

/**
 *
 * @author nmohamed
 */
public class User {
    
    private int id;
    private String name;
    private String lang;
     private int type;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
     private String password;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    
    
}
