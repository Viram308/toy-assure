package com.channel.pojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"channelId", "channelSkuId", "globalSkuId"}))
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