package controladores;

import Montessori.DBConect;
import Montessori.User;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.tomcat.util.http.fileupload.IOUtils;

@WebServlet(name = "savecomment", urlPatterns = {"/savecomment"})
@MultipartConfig
public class ImageObservation extends HttpServlet {

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
        String obsdate = request.getParameter("date");
        String obsid = request.getParameter("id");
        String server = "192.168.1.36";
        int port = 21;
        String user = "david";
        String pass = "david";

        String filePath = "/MontessoriObservations/" + obsid + "_" + obsdate + "/";
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        ftpClient.login(user, pass);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        if (ftpClient.changeWorkingDirectory(filePath)) {
            String s[] = ftpClient.listNames();
            filePath = s[0];
            InputStream inStream = ftpClient.retrieveFileStream(s[0]);
            // obtains ServletContext
            ServletContext context = request.getServletContext();
            String appPath = context.getRealPath("");
            System.out.println("appPath = " + appPath);

            // gets MIME type of the file
            String mimeType = context.getMimeType(filePath);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
            System.out.println("MIME type: " + mimeType);

            // modifies response
            response.setContentType(mimeType);

            // forces download
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", filePath);
            response.setHeader(headerKey, headerValue);
            IOUtils.copy(inStream, response.getOutputStream());

            response.flushBuffer();
        }
        //response.sendRedirect(request.getContextPath());
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, JSONException {
        String obsjson = request.getParameter("obj");
        JSONObject json = new JSONObject(obsjson);
        String idobs = "";
        //get the file chosen by the user
        String updateComment = request.getParameter("update");

        String photoBoolean = "true";
        Part filePart = request.getPart("fileToUpload");
        if (filePart.getSubmittedFileName() == null) {
            photoBoolean = "false";
        }
        try {
            HttpSession sesion = request.getSession();
            User user = (User) sesion.getAttribute("user");
 
            String consulta;
            if (updateComment == null) {
                consulta = "insert into classobserv(logged_by,date_created,comment,category,student_id,commentdate,term_id,yearterm_id,foto)values('" + user.getId() + "',now(),'" + json.getString("observation") + "','" + json.getString("type") + "','" + json.getString("studentid") + "','" + json.getString("date") + "','" + json.getString("termId") + "','" + json.getString("yearId") + "'," + photoBoolean + ")";
                DBConect.eduweb.executeUpdate(consulta, Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = DBConect.eduweb.getGeneratedKeys();
                while (rs.next()) {
                    idobs = rs.getString("id");
                }
            } else {
                String photoUpdateBoolean = "";
                if (filePart.getSubmittedFileName() != null) {
                    photoUpdateBoolean = ",foto = true ";
                }

                consulta = "update classobserv set date_created = now(), comment = '" + json.getString("observation") + "' ,category = '" + json.getString("type") + "', commentdate = '" + json.getString("dateString") + "' " + photoUpdateBoolean + " where id = '" + json.getString("id") + "'";
                DBConect.eduweb.executeUpdate(consulta);
                idobs = json.getString("id");
            }

        } catch (SQLException ex) {
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));

        }

        ResourcesControlador rCont = new ResourcesControlador();
        //get the InputStream to store the file somewhere
        InputStream fileInputStream = filePart.getInputStream();
        String server = "192.168.1.36";
        int port = 21;
        String user = "david";
        String pass = "david";

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.mkd("/MontessoriObservations/");
            String rutaCompleta = "/MontessoriObservations/" + idobs;

            if (!ftpClient.changeWorkingDirectory(rutaCompleta));
            {
                ftpClient.changeWorkingDirectory("/MontessoriObservations");

                ftpClient.mkd(idobs);
                ftpClient.changeWorkingDirectory(idobs);
                
                if (filePart.getSubmittedFileName() != null && ftpClient.listNames().length > 0) {

                    ftpClient.deleteFile(ftpClient.listNames()[0]);
                }
            }

            String filename = idobs + "-" + filePart.getSubmittedFileName();
            if (filePart.getSubmittedFileName() != null) {

                ftpClient.storeFile(filename, fileInputStream);
            }
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

    private String terminacion(String nombre) {
        String ret = "";
        boolean copiar = false;
        for (int i = 0; i < nombre.length(); i++) {
            if (nombre.charAt(i) == '.') {
                copiar = true;
            }
            if (copiar) {
                ret += nombre.charAt(i);
            }
        }
        return ret;
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
        } catch (JSONException ex) {
            Logger.getLogger(ImageObservation.class.getName()).log(Level.SEVERE, null, ex);
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
