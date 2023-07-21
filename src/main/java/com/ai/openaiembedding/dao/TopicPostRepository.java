package com.ai.openaiembedding.dao;

import com.ai.openaiembedding.entity.TopicPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TopicPostRepository extends JpaRepository<TopicPost, String> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO topic_posts (id, topic_id, post_content, embedding_vector) VALUES (?1, ?2, ?3, ?4\\:\\:vector)", nativeQuery = true)
    void insert(String id, String topicId, String postContent, String embeddingVector);

    List<TopicPost> findAllByTopicId(String topicId);
}
