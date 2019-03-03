package sawyern.mahjongcalculator.demo.models.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;

@Entity
@Table(name = "Account", uniqueConstraints = { @UniqueConstraint(columnNames = {"NAME", "REVISION"}) })
public class Account {
    @Email
    private String email;
}
