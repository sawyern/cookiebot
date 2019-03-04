package sawyern.cookiebot.models.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COOKIE")
public class Cookie extends DbItem {
    @Column(name = "type")
    public String type;

    public Cookie() {
        super();
    }
    public Cookie(String type) {
        super();
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
