package sawyern.cookiebot.models.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNT")
@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends DbItem {
    @Column(name = "discordId")
    @NonNull
    private String discordId;

    @Column(name = "username")
    @NonNull
    private String username;
}
