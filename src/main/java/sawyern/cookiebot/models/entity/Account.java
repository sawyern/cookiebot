package sawyern.cookiebot.models.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Account extends DbItem {
    @Column(name = "discordId")
    private String discordId;

    @Column(name = "username")
    private String username;
}
