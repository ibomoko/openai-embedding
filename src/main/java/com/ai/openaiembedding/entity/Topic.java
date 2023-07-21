package com.ai.openaiembedding.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "topics")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Topic {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.ai.openaiembedding.entity.generator.IdGenerator")
    private String id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "name")
    private String name;

}
