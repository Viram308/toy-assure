package com.channel.pojo;

import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import java.time.ZonedDateTime;

@MappedSuperclass
public abstract class AbstractPojo {

    @Version
    private int version;

    private ZonedDateTime createAt = ZonedDateTime.now();

    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @PreUpdate
    public void setUpdatedAt(){
        this.updatedAt = ZonedDateTime.now();
    }
}