package sawyern.cookiebot.models.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COOKIE")
@Data
@EqualsAndHashCode(callSuper = true)
public class Cookie extends DbItem {
    @Column(name = "type")
    @NonNull
    public String type;
}
