package sawyern.cookiebot.models.entity;

import org.springframework.data.annotation.CreatedDate;
import sawyern.cookiebot.constants.Constants;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class DbItem extends DbObject {
    @Column(name = "NAME", length = 128)
    private String name;

    @Column(name = "REVISION")
    private Integer revision;

    @Column(name = "LAST_REVISION", nullable = false)
    private Character lastRevision;

    @Column(name = "STATUS", length = 20)
    private String status;

    @CreatedDate
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "MODIFY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    public DbItem() {
        this.setRevision(1);
        this.setLastRevision(Constants.LATEST_REVISION);
        this.setStatus(Constants.STATUS_ACTIVE);
        this.setCreateDate(new Date());
        this.setModifyDate(new Date());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Character getLastRevision() {
        return lastRevision;
    }

    public void setLastRevision(Character lastRevision) {
        this.lastRevision = lastRevision;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    @PrePersist
    public void prepersist() {
        if (this.getName() == null)
            this.setName(this.getUniqueId());
    }
}
