package com.assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
public class Bin extends AbstractPojo implements Serializable {
    @Id
    @SequenceGenerator(name="seq", initialValue=1000, allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    private Long binId;
}
