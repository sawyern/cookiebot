package sawyern.cookiebot.models.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "COOKIE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Cookie extends DbItem {
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column(name = "type")
    public String type;
}
