package com.github.crimson95.lms.book;

import DTO.book.BookUserDTO;
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
 * Admin servlet handling circulation desk operations.
 * <p>
 * Supports displaying all borrow records, processing new book checkouts (borrow),
 * and processing book returns.
 */
@WebServlet("/admin/circulation")
public class AdminCirculationServlet extends HttpServlet {
    private final BookService  bookService = new BookService();

    /**
     * Handles GET requests to display the circulation desk dashboard.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            // Fetch all borrow records to display on the page
            List<BookUserDTO> borrowList = bookService.getAllBorrowRecords();
            request.setAttribute("borrowList",borrowList);

            request.getRequestDispatcher("/WEB-INF/admin/book/admin-circulation.jsp").forward(request,response);

        }catch(Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot load circulation data.");
        }
    }

    /**
     * Handles POST requests for borrow and return actions.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if(action == null) action = "";

        switch(action){
            case "borrow":{
                // Read input fields from the borrow form
                String username = request.getParameter("username");
                String bookIDRaw = request.getParameter("bookID");

                try{
                    int bookID = Integer.parseInt(bookIDRaw);

                    // Delegate to service layer to handle the transaction
                    bookService.borrowBook(username, bookID);

                    // PRG Pattern: Redirect to avoid duplicate submissions on page refresh
                    response.sendRedirect(request.getContextPath() + "/admin/circulation");
                    return;

                }catch (BusinessValidationException e){
                    // Catch business rules errors
                    request.setAttribute("error",e.getMessage());
                }catch(NumberFormatException e){
                    request.setAttribute("error","Invalid Book ID format. Must be a number.");
                }catch(Exception e){
                    e.printStackTrace();
                    request.setAttribute("error", "System error or invalid username while processing borrow request.");
                }
                break; // Break to fallback to doGet and display errors
            }

            case "return":{
                // Read the borrow record ID from the return button/form
                String bookUserIDRaw = request.getParameter("bookUserID");

                try{
                    int bookUserID = Integer.parseInt(bookUserIDRaw);

                    // Delegate to service layer to handle the return transaction and calculate fees
                    bookService.returnBook(bookUserID);

                    response.sendRedirect(request.getContextPath() + "/admin/circulation");
                    return;
                }catch (BusinessValidationException e){
                    request.setAttribute("error",e.getMessage());
                }catch(NumberFormatException e){
                    request.setAttribute("error","Invalid Record ID format.");
                }catch(Exception e){
                    e.printStackTrace();
                    request.setAttribute("error", "System error while processing return request.");
                }
                break; // Break to fallback to doGet and display errors
            }

            case "pay":{
                String bookUserIDRaw = request.getParameter("bookUserID");

                try{
                    int bookUserID = Integer.parseInt(bookUserIDRaw);

                    // Delegate to service layer to clear the fee
                    bookService.payLateFee(bookUserID);

                    response.sendRedirect(request.getContextPath() + "/admin/circulation");
                    return;
                }catch (BusinessValidationException e){
                    request.setAttribute("error",e.getMessage());
                }catch (NumberFormatException e){
                    request.setAttribute("error","Invalid Record ID format.");
                }catch(Exception e){
                    e.printStackTrace();
                    request.setAttribute("error", "System error while processing pay request.");
                }
                break;   // Break to fallback to doGet and display errors
            }
        }

        // If an error occurred or action was invalid, fall back to the GET flow to display the page with errors
        doGet(request,response);
    }
}
