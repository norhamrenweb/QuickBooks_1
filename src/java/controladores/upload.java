package controladores;

import Montessori.Resource;
import static Montessori.Resource.RUTA_FTP;
import controladores.ResourcesControlador;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Worker;
import javax.servlet.ServletContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

@WebServlet(name = "upload", urlPatterns = {"/upload"})
@MultipartConfig
public class upload extends HttpServlet {

   /**
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
    * methods.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
    protected void processResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException
    {
        
        String idFile  = request.getParameter("idNameFileDown");
        Resource rLoaded = new Resource();
        ResourcesControlador rCont = new ResourcesControlador();
        try {
            rLoaded = rCont.loadResource(idFile,request);
        } catch (Exception ex) {
            Logger.getLogger(upload.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       String url =  "http://localhost:8080/QuickBooks_1/lessonresources/loadResources.htm?LessonsSelected="+rLoaded.getLesson_id();
            
       
        String filePath = rLoaded.getLink();
        File downloadFile = new File(filePath);
        FileInputStream inStream = new FileInputStream(downloadFile);
         
   
        // obtains ServletContext
        ServletContext context = getServletContext();
         
        // gets MIME type of the file
        String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {        
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);
         
        // modifies response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
         
        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        response.setHeader(headerKey, headerValue);
         
        // obtains response's output stream
        OutputStream outStream = response.getOutputStream();
         
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
         
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
         
        inStream.close();
        outStream.close();
        response.sendRedirect(url);
    }
   protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException
   {
            //get the file chosen by the user
            Part filePart = request.getPart("fileToUpload");
            ResourcesControlador rCont = new ResourcesControlador();
		//get the InputStream to store the file somewhere
	    InputStream fileInputStream = filePart.getInputStream();
	    String name  = request.getParameter("idNameFile");
            String lessonId = request.getParameter("lessonid");
            String url =  "http://localhost:8080/QuickBooks_1/lessonresources/loadResources.htm?LessonsSelected="+lessonId;
           String server = "ftp2.renweb.com";
		int port = 21;
		String user = "AH-ZAF";
		String pass = "e3f14+7mANDp";

		FTPClient ftpClient = new FTPClient();
         FileInputStream fis = null;
            try {
                ftpClient.connect(server, port);
			ftpClient.login(user, pass);
//			ftpClient.enterLocalPassiveMode();
//
//			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                //if(!rCont.existe(name+"-"+ filePart.getSubmittedFileName(), request)){
//                    File fileToSave = new File(RUTA_FTP +lessonId+"/"+name+"-"+ filePart.getSubmittedFileName());
//                    Files.copy(fileInputStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);   
//                    //GET VALUES FOR SAVE IN BBDD
//
//                    String path = fileToSave.toPath().toString();
                String filename = filePart.getSubmittedFileName();
//                fis = new FileInputStream(filename);
                boolean success = ftpClient.changeWorkingDirectory("/MontessoriTesting/"+lessonId);
                              
                ftpClient.storeFile(filename, fileInputStream);
                ftpClient.logout();
                    Resource r = new Resource();

                    r.setLesson_id(lessonId);
                   // r.setLink(path);
                    r.setName(name);
                    r.setType("File");
                    String idResource = rCont.addResources(r,request,response);
               // }
               
            } catch (Exception ex) {
                Logger.getLogger(upload.class.getName()).log(Level.SEVERE, null, ex);
            }
            //create output HTML that uses the 
            //response.reset(); 
            //response.encodeRedirectURL("http://localhost:8080/QuickBooks_1/lessonresources/loadResources.htm?LessonsSelected=19");
            
            response.sendRedirect(url);
            
  }
  @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
       try {
           processResponse(request, response);
       } catch (ClassNotFoundException ex) {
     
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
	
