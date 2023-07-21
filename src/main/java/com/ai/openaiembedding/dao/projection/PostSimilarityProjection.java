package com.ai.openaiembedding.dao.projection;

public interface PostSimilarityProjection {
    Double getAvgCosSimilarity();
    String getFirstPostId();
}
