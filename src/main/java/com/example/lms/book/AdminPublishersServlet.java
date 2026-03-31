package com.example.lms.book;

import DTO.book.AuthorDTO;
import DTO.book.PublisherDTO;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BusinessValidationException;
import service.book.BookService;

import java.io.IOException;
import java.util.List;

/**
 * Admin servlet for publisher management pages.
 * <p>
 * Supports list/add/update/delete actions and delegates business rules
 * to {@link BookService}.
 */
@WebServlet("/admin/publishers")
public class AdminPublishersServlet extends HttpServlet {
    private final BookService bookService = new BookService();

    /**
     * Handles publisher page routing and GET-based actions.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //Action-driven routing from query string.
        String action = request.getParameter("action");
        if (action == null) action = "";

        try{
            switch (action){
                case "add":{
                    // Open add form.
                    request.getRequestDispatcher("/WEB-INF/admin/publisher/admin-publishers-add.jsp").forward(request, response);
                    return;
                }

                case "update":{
                    // Load selected publisher for edit form.
                    String publisherIDRaw = request.getParameter("publisherID");
                    if (publisherIDRaw == null) {
                        int publisherID = Integer.parseInt(publisherIDRaw);
                        PublisherDTO publisher = bookService.findPublisherByID(publisherID);
                        request.setAttribute("publisher", publisher);
                        request.getRequestDispatcher("/WEB-INF/admin/publisher/admin-publishers-edit.jsp").forward(request, response);
                    }else{
                        response.sendRedirect(request.getContextPath() + "/admin/publishers");
                    }
                    return;
                }

                case "delete":{
                    // Execute delete and return to list page.
                    String deleteIDRaw = request.getParameter("publisherID");
                    if (deleteIDRaw == null) {
                        bookService.deletePublisher(Integer.parseInt(deleteIDRaw));
                    }
                    response.sendRedirect(request.getContextPath() + "/admin/publishers");
                    return;
                }

                default:{
                    // Default page: publisher listing.
                    List<PublisherDTO> publishers = bookService.findAllPublishers();
                    request.setAttribute("publisherList", publishers);
                    request.getRequestDispatcher("/WEB-INF/admin/publisher/admin-publishers.jsp").forward(request, response);
                    break;
                }
            }
        }catch (BusinessValidationException e){
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/admin/publisher/admin-publishers.jsp").forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
            request.setAttribute("error", "System error or this publisher has existing books.");
            request.setAttribute("publisherList", bookService.findAllPublishers());
            request.getRequestDispatcher("/WEB-INF/admin/publisher/admin-publishers.jsp").forward(request, response);
        }
    }

    /**
     * Handles form submissions for add/update publisher operations.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action){
            case "add":{
                // Create publisher from submitted form fields.
                String publisherName = request.getParameter("publisher_name");

                try{
                    PublisherDTO newPublisher = new PublisherDTO(0, publisherName);
                    bookService.addPublisher(newPublisher);
                    response.sendRedirect(request.getContextPath() + "/admin/publishers");
                    return;
                }catch (BusinessValidationException e){
                    request.setAttribute("error", e.getMessage());
                    request.getRequestDispatcher("/WEB-INF/admin/publisher/admin-publishers-add.jsp").forward(request, response);
                    return;
                }
            }

            case "update":{
                // Update existing publisher from submitted form fields.
                String publisherIDRaw = request.getParameter("publisherID");
                String publisherName = request.getParameter("publisher_name");

                try{
                    int publisherID = Integer.parseInt(publisherIDRaw);
                    PublisherDTO updatePublisher = new PublisherDTO(publisherID, publisherName);
                    bookService.updatePublisher(updatePublisher);
                    response.sendRedirect(request.getContextPath() + "/admin/publishers");
                    return;
                }catch (BusinessValidationException e){
                    request.setAttribute("error", e.getMessage());
                    request.setAttribute("publisher", new PublisherDTO(Integer.parseInt(publisherIDRaw), publisherName));
                    request.getRequestDispatcher("/WEB-INF/admin/publisher/admin-publishers-edit.jsp").forward(request, response);
                    return;
                }
            }
        }
        doGet(request, response);
    }
}
