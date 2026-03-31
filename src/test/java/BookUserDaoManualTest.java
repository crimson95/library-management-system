import DAO.book.BookUserDAO;
import DAO.book.BookUserDAOImpl;
import DTO.book.BookUserDTO;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class BookUserDaoManualTest {
    public static void main(String[] args) {
        BookUserDAO bookUserDAO = new BookUserDAOImpl();
        String testUsername = "NQJ67dMN2jb";
        int testBookInfoId = 1001;
        Date testStartDate = Date.valueOf("2026-02-27");
        BigDecimal testLateFee = new BigDecimal("11.11");

        BookUserDTO newRecord = new BookUserDTO(
            0,
            testStartDate,
            null,
            testLateFee,
            testUsername,
            testBookInfoId
        );
        bookUserDAO.addBookUser(newRecord);
        System.out.println("ADD OK");

        List<BookUserDTO> allRecords = bookUserDAO.getAllBookUser();
        BookUserDTO created = allRecords.stream()
            .filter(r ->
                testUsername.equals(r.getUsername())
                && r.getBookID() == testBookInfoId
                && testStartDate.equals(r.getStartDate())
                && r.getReturnDate() == null
                && r.getLateFee().compareTo(testLateFee) == 0
            )
            .findFirst()
            .orElse(null);
        System.out.println("FOUND CREATED: " + (created != null));
        if (created == null) {
            return;
        }

        BookUserDTO found = bookUserDAO.getBookUserByID(created.getBookUserID());
        System.out.println("GET BY ID: " + (found != null));

        created.setReturnDate(Date.valueOf("2026-03-03"));
        created.setLateFee(new BigDecimal("15.75"));
        bookUserDAO.updateBookUser(created);

        BookUserDTO updated = bookUserDAO.getBookUserByID(created.getBookUserID());
        boolean isUpdated = updated != null
            && Date.valueOf("2026-03-03").equals(updated.getReturnDate())
            && updated.getLateFee().compareTo(new BigDecimal("15.75")) == 0;
        System.out.println("UPDATED: " + isUpdated);

        bookUserDAO.deleteBookUser(created.getBookUserID());
        BookUserDTO deleted = bookUserDAO.getBookUserByID(created.getBookUserID());
        System.out.println("DELETED: " + (deleted == null));
    }
}
