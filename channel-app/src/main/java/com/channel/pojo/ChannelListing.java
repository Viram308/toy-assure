package com.channel.pojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"channelId", "globalSkuId", "channelSkuId"}),indexes = {@Index(name = "i_clientId_globalSkuId",columnList = "clientId,globalSkuId",unique = true)})
public class ChannelListing extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long channelId;

    @NotNull
    @Column(nullable = false)
    private String channelSkuId;

    @NotNull
    @Column(nullable = false)
    private Long clientId;

    @NotNull
    @Column(nullable = false)
    private Long globalSkuId;
}