/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

import atg.taglib.json.util.JSONArray;

/**
 *
 * @author norhan
 */
public class ObjetoCompartir {
    private JSONArray teachers;
    private String id ;

    public JSONArray getTeachers() {
        return teachers;
    }

    public void setTeachers(JSONArray teachers) {
        this.teachers = teachers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

  
    
}
