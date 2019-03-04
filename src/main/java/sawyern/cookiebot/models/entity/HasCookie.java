package sawyern.cookiebot.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "HAS_COOKIE")
public class HasCookie extends DbItem {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "COOKIE_ID")
    private Cookie cookie;

    public HasCookie() {}
    public HasCookie(Account account, Cookie cookie) {
        this.account = account;
        this.cookie = cookie;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }
}
