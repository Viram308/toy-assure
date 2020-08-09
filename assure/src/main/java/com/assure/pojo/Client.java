package com.assure.pojo;

import com.commons.enums.ClientType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type"}), indexes = {@Index(name = "i_clientType",columnList = "type")})
public class Client extends AbstractPojo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClientType type;
}
