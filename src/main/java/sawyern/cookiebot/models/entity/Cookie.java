package sawyern.cookiebot.models.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Cookie")
public class Cookie extends DbItem {
    public String type;

    public Cookie() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
