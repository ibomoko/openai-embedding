package com.ai.openaiembedding.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "topic_posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicPost {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.ai.openaiembedding.entity.generator.IdGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(name = "topic_id", insertable = false, updatable = false)
    private String topicId;

    @Column(name = "post_content")
    private String postContent;

    @Column(name = "embedding_vector")
    private String embeddingVector;

}
