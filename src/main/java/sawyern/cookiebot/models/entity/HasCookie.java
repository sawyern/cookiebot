package sawyern.cookiebot.models.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "HAS_COOKIE")
@Data
@EqualsAndHashCode(callSuper = true)
public class HasCookie extends DbItem {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ACCOUNT_ID")
    @NonNull
    private Account account;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "COOKIE_ID")
    @NonNull
    private Cookie cookie;
}
