/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import Montessori.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Norhan
 */
public class SessionCheck {
    public boolean checkSession(HttpServletRequest hsr){
               
       HttpSession sesion = hsr.getSession();
       return sesion.getAttribute("user")==null;
    } 
}
