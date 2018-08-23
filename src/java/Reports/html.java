/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

import Montessori.DBConect;
import Montessori.User;
import Reports.DataFactoryFolder.*;
import controladores.upload;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
//import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.HtmlExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author nmohamed
 */
@WebServlet(name = "html", urlPatterns = {"/html"})
public class html extends HttpServlet {

    private DataFactory createFactory(String reportType,String cTerm, String cYear) {
        if (reportType == null) {
            return new FactoryActivityLog();
        }

        switch (reportType) {
            case "progress_prePrimary":
                return new FactoryProgressReport_Pre_Primary(cTerm,cYear);

            case "progress_Yr1_4":
                return new FactoryProgressReport_grade4(cTerm,cYear);

            case "academic_Gr7":
                return new FactoryAcademicReport_grade7("false",cTerm,cYear);

            default:
                return null;
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException, JRException, ParseException {
        Connection conn = null;

        ServletOutputStream os = response.getOutputStream();
        String codeSchool = "AH-ZAF";
        String[] stids = request.getParameterValues("destino[]");
        String reportType = request.getParameter("typeReport");
        String checkArchive = request.getParameter("checkArchive");
        String start = request.getParameter("TXThorainicio");
        String finish = request.getParameter("TXThorafin");
        
        String termId =  request.getParameter("termId");
        String yearId =  request.getParameter("yearId");
        
        DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
        JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.xpath.executer.factory",
                "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
        JasperFillManager jasperFillManager = JasperFillManager.getInstance(context);
        //PrintWriter out = response.getWriter()) 
//                Class.forName("org.postgresql.Driver");
//                conn = DriverManager.getConnection("jdbc:postgresql://192.168.1.9:5432/Maintenance_jobs?user=eduweb&password=Madrid2016");
        response.setContentType("text/html;charset=UTF-8");
        HtmlExporter exporter = new HtmlExporter();

        // VARIARA SEGUNE L TIPO DE REPORTE SELECCIONADO
        //DataFactory d = new FactoryProgressReport_Pre_Primary();
        //DataFactory d = new FactoryProgressReport_grade4();
        boolean showGrade = false;
        DataFactory d;
        if (reportType != null && reportType.equals("academic_Gr7_JP")) {
            d = new FactoryAcademicReport_grade7("true",termId,yearId);
        } else {
            d = createFactory(reportType,termId,yearId);
        }

        // InputStream jasperStream = this.getClass().getResourceAsStream("progress_report_2017_gr4.jasper");
        InputStream jasperStream = this.getClass().getResourceAsStream(d.getNameReport());
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        Map<String, Object> map = new HashMap<String, Object>();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
//             
//            String startdate =sdf.format(start);
//            java.util.Date enddate = sdf.parse(end);
//           String where = "timestamp between '"+start+"' and '"+end+"'";
//        map.put("studentid",stids[0]);
//            map.put("end",enddate);
        //   List<SimpleExporterInput> list = new ArrayList<SimpleExporterInput>();
        JRDataSource datasource;
        if (!d.getClass().equals(FactoryActivityLog.class)) {
            datasource = new JRBeanCollectionDataSource(d.getDataSource(request, stids[0], this.getServletContext()), true);
        } else {
            datasource = new JRBeanCollectionDataSource(FactoryActivityLog.getDataSource(request, ((User) request.getSession().getAttribute("user")).getName(), start, finish, stids[0], this.getServletContext()), true);

        }
        JasperPrint jasperPrint = jasperFillManager.fill(jasperReport, map, datasource);//fill(jasperReport,map, conn);
        byte[] bitesFtp;
        if (checkArchive != null && checkArchive.equals("on")) {
            bitesFtp = JasperExportManager.exportReportToPdf(jasperPrint);
            //private void uploadFileToFTP(byte[] b,String codeSchool,String studentId,String term,String year,String nameReport) {
            uploadFileToFTP(bitesFtp, codeSchool, stids[0],termId, yearId, reportType);
        }
        for (int i = 1; i < stids.length; i++) {
            JRDataSource datasource2;
            if (!d.getClass().equals(FactoryActivityLog.class)) {
                datasource2 = new JRBeanCollectionDataSource(d.getDataSource(request, stids[i], this.getServletContext()), true);
                JasperPrint jasperPrintAux = jasperFillManager.fill(jasperReport, map, datasource2);//fill(jasperReport,map, conn);
                for (int j = 0; j < jasperPrintAux.getPages().size(); j++) {
                    jasperPrint.addPage(jasperPrintAux.getPages().get(j));
                }
                if (checkArchive != null && checkArchive.equals("on")) {
                    bitesFtp = JasperExportManager.exportReportToPdf(jasperPrintAux);
                    //private void uploadFileToFTP(byte[] b,String codeSchool,String studentId,String term,String year,String nameReport) {
                    uploadFileToFTP(bitesFtp, codeSchool, stids[i],termId, yearId, reportType);
                }
            } else {
                datasource2 = new JRBeanCollectionDataSource(FactoryActivityLog.getDataSource(request, ((User) request.getSession().getAttribute("user")).getName(), start, finish, stids[i], this.getServletContext()), true);
                JasperPrint jasperPrintAux = jasperFillManager.fill(jasperReport, map, datasource2);//fill(jasperReport,map, conn);
                for (int j = 0; j < jasperPrintAux.getPages().size(); j++) {
                    jasperPrint.addPage(jasperPrintAux.getPages().get(j));
                }

            }

        }

        if (!d.getClass().equals(FactoryActivityLog.class)) {//pdf Reports download      

            byte[] bites = JasperExportManager.exportReportToPdf(jasperPrint);
            //private void uploadFileToFTP(byte[] b,String codeSchool,String studentId,String term,String year,String nameReport) {

            response.setHeader("Content-disposition", "attachment; filename=Inform.pdf");
            response.setContentLength(bites.length);

            os.write(bites);
            os.close();

            /*
            response.setHeader("Content-disposition", "attachment; filename=Inform.doc");
           // response.setContentType("application/octet-stream");
            //response.setContentLength(4096);
            JRDocxExporter exporter2 = new JRDocxExporter();

            exporter2.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter2.setExporterOutput(new SimpleOutputStreamExporterOutput(os));

         
            exporter2.exportReport();*/
        } else {
            //XML Report Activity Log.
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(os));
            exporter.exportReport();
        }

        /*  exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleHtmlExporterOutput(os));
         */
        //            exporter.setConfiguration(createHtmlConfiguration());
// exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8"); 
//exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint); 
//exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, htmlStream); 
//exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR,Boolean.TRUE); 
//exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, "./Images/"); 
//exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "/Images/");            
//exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
        //  exporter.exportReport ();
    }

    private void uploadFileToFTP(byte[] b, String codeSchool, String studentId, String term, String year, String nameReport) {
        String server = "192.168.1.36";
        int port = 21;
        String user = "david";
        String pass = "david";
//** arreglar **//
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String rutaCompleta = "/"+DBConect.codeSchool+"/ReportCards/" + studentId;
          
            if (!ftpClient.changeWorkingDirectory(rutaCompleta));
            {
                ftpClient.changeWorkingDirectory("/"+DBConect.codeSchool+"/ReportCards/");
                ftpClient.mkd(studentId);
                ftpClient.changeWorkingDirectory(studentId);
            }

            /*  ftpClient.mkd("/ReportCards/");
            String rutaCompleta = "/ReportCards/59/3/10115";
            boolean ok = ftpClient.changeWorkingDirectory(rutaCompleta);*/
//            if (!ftpClient.changeWorkingDirectory(rutaCompleta));
//            {
//                ftpClient.changeWorkingDirectory("/MontessoriObservations");
//
//                ftpClient.mkd(idobs);
//                ftpClient.changeWorkingDirectory(idobs);
//                
//                if (filePart.getSubmittedFileName() != null && ftpClient.listNames().length > 0) {
//
//                    ftpClient.deleteFile(ftpClient.listNames()[0]);
//                }
//            }
            String filename = year + "_" + term + "_" + nameReport + ".pdf";
            InputStream stream = new ByteArrayInputStream(b);
            ftpClient.storeFile(filename, stream);
            stream.close();
            // ftpClient.storeFile(filename, );

            ftpClient.logout();

//            Resource r = new Resource();
//
//            r.setLesson_id(lessonId);
//            r.setLink(filename);
//            r.setName(name);
//            r.setType("File");
//            String idResource = rCont.addResources(r, request, response);
            // }
        } catch (Exception ex) {
            Logger.getLogger(upload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(html.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(html.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (JRException ex) {
            Logger.getLogger(html.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(html.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(html.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(html.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (JRException ex) {
            Logger.getLogger(html.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(html.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
