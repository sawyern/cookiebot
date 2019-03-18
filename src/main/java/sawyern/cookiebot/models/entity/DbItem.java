package sawyern.cookiebot.models.entity;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;
import sawyern.cookiebot.constants.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class DbItem {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    private String id;

    @Column(name = "REVISION")
    @NonNull
    private Integer revision;

    @Column(name = "LAST_REVISION", nullable = false)
    @NonNull
    private Character lastRevision;

    @Column(name = "STATUS", length = 20)
    @NonNull
    private String status;

    @CreatedDate
    @Column(name = "CREATE_DATE")
    @NonNull
    private LocalDateTime createDate;

    @Column(name = "MODIFY_DATE")
    @NonNull
    private LocalDateTime modifyDate;

    public DbItem() {
        this.revision = 1;
        this.lastRevision = Constants.LATEST_REVISION;
        this.status = Constants.STATUS_ACTIVE;
        this.createDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now();
    }
}
