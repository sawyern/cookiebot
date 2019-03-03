package sawyern.cookiebot.models.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

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

    @CreatedBy
    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "MODIFY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    @LastModifiedBy
    @Column(name = "MODIFY_USER", length = 12)
    private String modifyUser;

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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    @PrePersist
    public void prepersist() {
        if (this.getName() == null)
            this.setName(this.getUniqueId());
    }
}
