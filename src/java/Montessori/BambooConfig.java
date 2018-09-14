/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Montessori;

/**
 *
 * @author David
 */
public class BambooConfig {

    public static String url_bamboo;
    public static String user_bamboo;
    public static String pass_bamboo;
    public static String driverClassName_bamboo;
    public static String url_renweb;
    public static String user_renweb;
    public static String pass_renweb;
    public static String driverClassName_renweb;
    public static String nameFolder_ftp_bamboo;
    public static String url_ftp_bamboo;
    public static String user_ftp_bamboo;
    public static String pass_ftp_bamboo;
    public static int port_ftp_bamboo;
    public static String schoolCode;

    public static void charge(BambooConfig1 bc) {
        url_bamboo = bc.getUrl_bamboo();
        user_bamboo = bc.getUser_bamboo();
        pass_bamboo = bc.getPass_bamboo();
        driverClassName_bamboo = bc.getDriverClassName_bamboo();
        url_renweb = bc.getUrl_renweb();
        user_renweb = bc.getUser_renweb();
        pass_renweb = bc.getPass_renweb();
        driverClassName_renweb = bc.getDriverClassName_renweb();
        nameFolder_ftp_bamboo = bc.getNameFolder_ftp_bamboo();
        url_ftp_bamboo = bc.getUrl_ftp_bamboo();
        user_ftp_bamboo = bc.getUser_ftp_bamboo();
        pass_ftp_bamboo = bc.getPass_ftp_bamboo();
        port_ftp_bamboo = bc.getPort_ftp_bamboo();
        schoolCode = bc.getSchoolCode();
    }
}
