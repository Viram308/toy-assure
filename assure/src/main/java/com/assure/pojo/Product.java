package com.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(uniqueConstraints= @UniqueConstraint(columnNames = {"clientSkuId", "clientId"}))
public class Product extends AbstractPojo implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="s")
    @SequenceGenerator(name="s", initialValue=1, allocationSize = 1)
    private Long globalSkuId;

    @NotNull
    @Size(min = 1)
    private String clientSkuId;

    @NotNull
    private Long clientId;

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    private String brandId;

    @NotNull
    private Double mrp;

    @Column(columnDefinition = "TEXT")
    private String description;

}
