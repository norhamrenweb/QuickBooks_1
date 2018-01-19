package controladores;

import Montessori.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.apache.tomcat.util.http.fileupload.IOUtils;

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
    protected void processResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException {
        String presentationName = request.getParameter("lessonsName");
        String idFile = request.getParameter("idNameFileDown");
        Resource rLoaded = new Resource();
        ResourcesControlador rCont = new ResourcesControlador();

        try {
            rLoaded = rCont.loadResource(idFile, request);
        } catch (Exception ex) {
            Logger.getLogger(upload.class.getName()).log(Level.SEVERE, null, ex);
        }
        String url = "/lessonresources/loadResources.htm?LessonsSelected=" + rLoaded.getLesson_id() + "-" + presentationName;
        String server = "192.168.1.36";
        int port = 21;
        String user = "david";
        String pass = "david";

        presentationName = presentationName.replace("/", "_");
        presentationName = presentationName.replace(" ", "-");
        String filePath = "/MontessoriTesting/" + rLoaded.getLesson_id() + "-" + presentationName + "/" + rLoaded.getLink();
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        ftpClient.login(user, pass);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        InputStream inStream = ftpClient.retrieveFileStream(filePath);

        // obtains ServletContext
        ServletContext context = request.getServletContext();
        String appPath = context.getRealPath("");
        System.out.println("appPath = " + appPath);

        // gets MIME type of the file
        String mimeType = context.getMimeType(rLoaded.getLink());
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);

        // modifies response
        response.setContentType(mimeType);

        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", rLoaded.getLink());
        response.setHeader(headerKey, headerValue);
        IOUtils.copy(inStream, response.getOutputStream());

        response.flushBuffer();
        response.sendRedirect(request.getContextPath() + url);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException {
        //get the file chosen by the user
        Part filePart = request.getPart("fileToUpload");
        ResourcesControlador rCont = new ResourcesControlador();
        //get the InputStream to store the file somewhere
        InputStream fileInputStream = filePart.getInputStream();
        String urlBase = request.getParameter("txtUrl"); // CAMBIAR 
        String name = request.getParameter("idNameFile");
        String lessonId = request.getParameter("lessonid");
        String presentationName = request.getParameter("lessonsName");

        String url = "/lessonresources/loadResources.htm?LessonsSelected=" + lessonId + "-" + presentationName;
        String server = "192.168.1.36";
        int port = 21;
        String user = "david";
        String pass = "david";

        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String filename = name + "-" + filePart.getSubmittedFileName();
            presentationName = presentationName.replace("/", "_");
            presentationName = presentationName.replace(" ", "-");
            String rutaCompleta = "/MontessoriTesting/'" + lessonId + "-" + presentationName + "'";
            if (!ftpClient.changeWorkingDirectory(rutaCompleta));
            {
                ftpClient.changeWorkingDirectory("/MontessoriTesting");
                ftpClient.mkd(lessonId + "-" + presentationName);
                ftpClient.changeWorkingDirectory(lessonId + "-" + presentationName);
            }

            ftpClient.storeFile(filename, fileInputStream);
            ftpClient.logout();
            Resource r = new Resource();

            r.setLesson_id(lessonId);
            r.setLink(filename);
            r.setName(name);
            r.setType("File");
            String idResource = rCont.addResources(r, request, response);
            // }

        } catch (Exception ex) {
            Logger.getLogger(upload.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.sendRedirect(request.getContextPath() + url);
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
    }
}
