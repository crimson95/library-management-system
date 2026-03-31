package DTO.book;

import java.sql.Date;

/**
 * Data transfer object for book catalog records.
 */
public class BookDTO {
    private String isbn;
    private String title;
    private Date dateAcquired;
    private String description;
    private int authorID;
    private String authorName;
    private int publisherID;
    private String publisherName;
    private int totalCopies;
    private int availableCopies;

    /**
     * Creates a book DTO.
     *
     * @param isbn book ISBN
     * @param title book title
     * @param dateAcquired acquisition date
     * @param description description
     * @param authorID author id
     * @param publisherID publisher id
     */
    public BookDTO(String isbn, String title, Date dateAcquired, String description, int authorID, String authorName,int publisherID, String publisherName) {
        this.isbn = isbn;
        this.title = title;
        this.dateAcquired = dateAcquired;
        this.description = description;
        this.authorID = authorID;
        this.authorName = authorName;
        this.publisherID = publisherID;
        this.publisherName = publisherName;
    }

    public String getIsbn(){
        return isbn;
    }

    public void setIsbn(String isbn){
        this.isbn = isbn;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public Date getDateAcquired(){
        return dateAcquired;
    }

    public void setDateAcquired(Date dateAcquired){
        this.dateAcquired = dateAcquired;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getAuthorID(){
        return authorID;
    }

    public void setAuthorID(int authorID){
        this.authorID = authorID;
    }

    public String getAuthorName(){ return authorName; }

    public void setAuthorName(String authorName){ this.authorName = authorName; }

    public int getPublisherID(){
        return publisherID;
    }

    public void setPublisherID(int publisherID){
        this.publisherID = publisherID;
    }

    public String getPublisherName(){ return publisherName; }

    public void setPublisherName(String publisherName){ this.publisherName = publisherName; }

    public  int getTotalCopies(){
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies){
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies(){
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies){
        this.availableCopies = availableCopies;
    }
}
