package DTO.book;

/**
 * Data transfer object for publisher records.
 */
public class PublisherDTO {
    private int publisherID;
    private String publisherName;

    /**
     * Creates a publisher DTO.
     *
     * @param publisherID publisher id
     * @param publisherName publisher name
     */
    public PublisherDTO(int publisherID, String publisherName){
        this.publisherID = publisherID;
        this.publisherName = publisherName;
    }

    public int getPublisherID(){
        return publisherID;
    }

    public void setPublisherID(int publisherID){
        this.publisherID = publisherID;
    }

    public String getPublisherName(){
        return publisherName;
    }

    public void setPublisherName(String publisherName){
        this.publisherName = publisherName;
    }
}
