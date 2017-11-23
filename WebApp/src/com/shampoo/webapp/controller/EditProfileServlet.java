package com.shampoo.webapp.controller;

import com.shampoo.webapp.model.UserBean;
import com.shampoo.webapp.requester.UserManagementClient;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "EditProfileServlet")
public class EditProfileServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //File Upload
        UserBean userData = (UserBean) request.getSession().getAttribute("userData");
        String newName = userData.getName();
        String newPhone = userData.getPhoneNumber();
        Integer newStatusDriver = userData.getDriverStatus();
        String newProfilePicName = userData.getProfilePicture();
        String userName = userData.getUsername();

        if (ServletFileUpload.isMultipartContent(request)) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(new File("C:\\temp"));
            int maxMemSize = 1000 * 1024;
            factory.setSizeThreshold(maxMemSize);

            ServletFileUpload upload = new ServletFileUpload(factory);
            int maxFileSize = 5000 * 1024;
            upload.setSizeMax(maxFileSize);

            try {
                List<FileItem> fileItems = upload.parseRequest(request);

                newStatusDriver = 0;
                for (FileItem item : fileItems) {
                    System.out.println(item.getFieldName());
                    if (!item.isFormField()) {
                        System.out.println("File Name = " + item.getName());
                        if (!item.getName().isEmpty()) {
                            String extension = FilenameUtils.getExtension(new File(item.getName()).getName());
                            System.out.println(extension);
                            System.out.println(getServletContext().getRealPath("/"));
                            newProfilePicName = userName + "." + extension;
                            String filePath = "/images/";
                            System.out.println(getServletContext().getRealPath(filePath + File.separator + userName + "." + extension));
                            item.write(new File(getServletContext().getRealPath(filePath + File.separator + userName + "." + extension)));
                        }
                    } else {
                        String fieldName = item.getFieldName();
                        System.out.println(fieldName);
                        switch (fieldName) {
                            case "name":
                                newName = item.getString();
                                break;
                            case "phonenumber":
                                newPhone = item.getString();
                                break;
                            case "driverstatus":
                                newStatusDriver = 1;
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            CookieHandler cookieHandler = new CookieHandler();
            if (!(cookieHandler.isTextValid(newName) && cookieHandler.isTextValid(newPhone))) {
                response.sendRedirect("editprofile.jsp");
            } else {
                UserManagementClient userManagementClient = new UserManagementClient();
                String access_token = (cookieHandler.getAccessTokenCookie(request));
                if (access_token != null) {
                    String result = userManagementClient.getUserManagement().changeCurrentUserData(access_token, newName, newPhone, newProfilePicName, newStatusDriver);
                    switch (result) {
                        case "Successful":
                            userData.setName(newName);
                            userData.setPhoneNumber(newPhone);
                            userData.setDriverStatus(newStatusDriver);
                            userData.setProfilePicture(newProfilePicName);
                            response.sendRedirect("profile.jsp");
                            break;
                        case "Error":
                            response.getOutputStream().println("<script type=\"text/javascript\">");
                            response.getOutputStream().println("alert(\"Failed to edit your profile!\");");
                            response.getOutputStream().println("window.location =\"editprofile.jsp\"");
                            response.getOutputStream().println("</script>");
                            break;
                        default:
                            response.getOutputStream().println("<script type=\"text/javascript\">");
                            response.getOutputStream().println("alert(\"Your token is invalid or expired!\");");
                            response.getOutputStream().println("window.location =\"handleLogout.jsp\"");
                            response.getOutputStream().println("</script>");
                            break;
                    }
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

    }
}
