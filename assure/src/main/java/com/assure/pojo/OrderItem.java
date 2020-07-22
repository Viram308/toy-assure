package com.assure.pojo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(uniqueConstraints= { @UniqueConstraint(columnNames = {"orderId", "globalSkuId" })})
public class OrderItem extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long orderId;

    @NotNull
    private Long globalSkuId;

    @NotNull
    private Long orderedQuantity = 0L;

    @NotNull
    private Long allocatedQuantity = 0L;

    @NotNull
    private Long fulfilledQuantity = 0L;

    @NotNull
    private Double sellingPricePerUnit = 0.0;
}