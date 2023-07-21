package com.ai.openaiembedding.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "post_similarities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSimilarity {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.ai.openaiembedding.entity.generator.IdGenerator")
    private String id;

    @Column(name = "first_post_id")
    private String firstPostId;

    @Column(name = "second_post_id")
    private String secondPostId;

    @Column(name = "cos_similarity")
    private Double cosSimilarity;

}
