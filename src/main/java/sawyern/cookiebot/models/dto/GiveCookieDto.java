package sawyern.cookiebot.models.dto;

public class GiveCookieDto {
    private String senderId;
    private String recieverId;
    private int numCookies;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public int getNumCookies() {
        return numCookies;
    }

    public void setNumCookies(int numCookies) {
        this.numCookies = numCookies;
    }
}
