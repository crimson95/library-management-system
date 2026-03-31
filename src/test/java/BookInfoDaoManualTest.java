import DAO.book.BookInfoDAO;
import DAO.book.BookInfoDAOImpl;
import DTO.book.BookInfoDTO;

import java.util.List;

public class BookInfoDaoManualTest {
    public static void main(String[] args) {
        BookInfoDAO bookInfoDAO = new BookInfoDAOImpl();
        String testCondition = "DAO_TEST_INFO";
        String testIsbn = "978-0134685991";

        BookInfoDTO newInfo = new BookInfoDTO(
            0,
            testCondition,
            BookInfoDTO.STATUS_AVAILABLE,
            testIsbn
        );
        bookInfoDAO.addBookInfo(newInfo);
        System.out.println("ADD OK");

        List<BookInfoDTO> allInfos = bookInfoDAO.getAllBookInfo();
        BookInfoDTO created = allInfos.stream()
            .filter(info -> testCondition.equals(info.getCondition()) && testIsbn.equals(info.getBookISBN()))
            .findFirst()
            .orElse(null);
        System.out.println("FOUND CREATED: " + (created != null));
        if (created == null) {
            return;
        }

        BookInfoDTO found = bookInfoDAO.getBookInfoByID(created.getBookID());
        System.out.println("GET BY ID: " + (found != null));

        created.setCondition("DAO_TEST_INFO_UPDATED");
        created.setStatus(BookInfoDTO.STATUS_REPAIR);
        bookInfoDAO.updateBookInfo(created);

        BookInfoDTO updated = bookInfoDAO.getBookInfoByID(created.getBookID());
        boolean isUpdated = updated != null
            && "DAO_TEST_INFO_UPDATED".equals(updated.getCondition())
            && updated.getStatus() == BookInfoDTO.STATUS_REPAIR;
        System.out.println("UPDATED: " + isUpdated);

        bookInfoDAO.deleteBookInfo(created.getBookID());
        BookInfoDTO deleted = bookInfoDAO.getBookInfoByID(created.getBookID());
        System.out.println("DELETED: " + (deleted == null));
    }
}
