package com.assure.pojo;
import com.commons.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(uniqueConstraints= @UniqueConstraint(columnNames = {"channelId", "channelOrderId"}))
public class Order extends AbstractPojo implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long clientId;

    @NotNull
    private Long customerId;

    @NotNull
    private Long channelId;

    @NotNull
    private String channelOrderId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}