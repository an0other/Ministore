/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;

@WebServlet("/image")
public class ImageController extends HttpServlet {

    private static final String IMAGE_DIRECTORY = "D:/MiniStoreUpload"; // Đường dẫn tới folder ảnh

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String imageName = request.getParameter("name");

        // Bảo vệ tránh truy cập ../ hoặc đường dẫn sai
        if (imageName == null || imageName.isEmpty() || imageName.contains("..") || imageName.contains("/") || imageName.contains("\\")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid image name.");
            return;
        }

        File imageFile = new File(IMAGE_DIRECTORY, imageName);

        if (!imageFile.exists() || !imageFile.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found.");
            return;
        }

        // Lấy kiểu MIME theo đuôi file
        String mime = getServletContext().getMimeType(imageFile.getName());
        if (mime == null) mime = "application/octet-stream";
        response.setContentType(mime);
        response.setContentLengthLong(imageFile.length());

        // Gửi ảnh qua response
        try (InputStream in = new FileInputStream(imageFile);
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
