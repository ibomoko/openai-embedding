package com.ai.openaiembedding.dao;

import com.ai.openaiembedding.dao.projection.PostSimilarityProjection;
import com.ai.openaiembedding.entity.PostSimilarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface PostSimilarityRepository extends JpaRepository<PostSimilarity, String> {

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO post_similarities (id, first_post_id, second_post_id, first_embedding_vector, second_embedding_vector, cos_similarity)
            VALUES (?1, ?2, ?3, ?4\\:\\:vector, ?5\\:\\:vector, 1 - (?4\\:\\:vector <=> ?5\\:\\:vector));
            """, nativeQuery = true)
    void insert(String id, String firstPostId, String secondPostId, String firstEmbeddingVector, String secondEmbeddingVector);

    @Query(value = "SELECT AVG(cos_similarity) as avgCosSimilarity, first_post_id as firstPostId FROM post_similarities WHERE first_post_id  = ?1 GROUP BY first_post_id", nativeQuery = true)
    PostSimilarityProjection findAvgCosSimilarityByFirstPostId(String firstPostId);
}
