package sawyern.cookiebot.models.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;

@Entity
@Table(name = "Account", uniqueConstraints = { @UniqueConstraint(columnNames = {"NAME", "REVISION"}) })
public class Account extends DbItem {
    @Email
    private String username;
}
