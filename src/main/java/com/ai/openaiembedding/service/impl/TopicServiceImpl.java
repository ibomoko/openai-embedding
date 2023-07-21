package com.ai.openaiembedding.service.impl;

import com.ai.openaiembedding.constants.OpenAIConstants;
import com.ai.openaiembedding.converter.TopicEntityConverter;
import com.ai.openaiembedding.dao.PostSimilarityRepository;
import com.ai.openaiembedding.dao.TopicPostRepository;
import com.ai.openaiembedding.dao.TopicRepository;
import com.ai.openaiembedding.dao.projection.PostSimilarityProjection;
import com.ai.openaiembedding.entity.Topic;
import com.ai.openaiembedding.entity.TopicPost;
import com.ai.openaiembedding.entity.generator.IdGenerator;
import com.ai.openaiembedding.error.exception.ResourceAlreadyExistsException;
import com.ai.openaiembedding.error.exception.ResourceNotFoundException;
import com.ai.openaiembedding.model.request.embedding.EmbeddingRequest;
import com.ai.openaiembedding.model.request.topic.TopicCreateRequest;
import com.ai.openaiembedding.model.response.embedding.EmbeddingResponse;
import com.ai.openaiembedding.model.response.topic.LessRelatedPostResponse;
import com.ai.openaiembedding.model.response.topic.TopicCreateResponse;
import com.ai.openaiembedding.service.TopicService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final TopicPostRepository topicPostRepository;
    private final PostSimilarityRepository postSimilarityRepository;
    private final TopicEntityConverter topicEntityConverter;
    private final OpenAIConstants openAIConstants;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public TopicServiceImpl(TopicRepository topicRepository,
                            TopicPostRepository topicPostRepository,
                            TopicEntityConverter topicEntityConverter,
                            WebClient.Builder webClientBuilder,
                            OpenAIConstants openAIConstants,
                            ObjectMapper objectMapper,
                            PostSimilarityRepository postSimilarityRepository) {
        this.topicRepository = topicRepository;
        this.topicPostRepository = topicPostRepository;
        this.postSimilarityRepository = postSimilarityRepository;
        this.topicEntityConverter = topicEntityConverter;
        this.openAIConstants = openAIConstants;
        this.objectMapper = objectMapper;
        this.webClient = webClientBuilder
                .baseUrl(openAIConstants.getEmbeddingUrl())
                .defaultHeader("Authorization", "Bearer " + openAIConstants.getApiKey())
                .build();
    }

    @Override
    public TopicCreateResponse createTopic(TopicCreateRequest topicCreateRequest) {
        boolean isPresent = topicRepository.findByName(topicCreateRequest.getTopicName()).isPresent();

        if (isPresent) {
            throw new ResourceAlreadyExistsException("Topic already exists with this name");
        }

        Topic topic =  topicEntityConverter.apply(topicCreateRequest);
        topic = topicRepository.save(topic);

        List<TopicPost> topicPosts = getTopicPosts(topicCreateRequest, topic);
        saveTopicPosts(topicPosts);
        return new TopicCreateResponse(topic.getId());
    }

    /**
     * Retrieves the less related post for a given topic based on the average cosine similarity.
     * Calculates the average cosine similarity for each topic post related to the specified topic
     * and finds the post similarity record with the minimum cosine similarity average.
     * If the topic is not found with the provided ID, a ResourceNotFoundException is thrown.
     */
    @Override
    public LessRelatedPostResponse getLessRelatedPost(String topicId) {

        topicRepository.findById(topicId).orElseThrow(() -> new ResourceNotFoundException("Topic not found with this id"));
        List<TopicPost> topicPosts = topicPostRepository.findAllByTopicId(topicId);
        savePostSimilarities(topicPosts);


        PostSimilarityProjection minAvgPostSimilarity = getMinAvgPostSimilarity(topicPosts);

        TopicPost lessRelatedPost = topicPostRepository
                .findById(Objects.requireNonNull(minAvgPostSimilarity).getFirstPostId())
                .orElseThrow();

        return new LessRelatedPostResponse(lessRelatedPost.getPostContent());
    }


     /**
      * Calculates the average cosine similarity for each topic post related to a particular topic. Finds post similarity
      * record with minimum cosine similarity average.
      */
    private PostSimilarityProjection getMinAvgPostSimilarity(List<TopicPost> topicPosts) {
        return topicPosts.stream()
                .map(topicPost -> postSimilarityRepository
                        .findAvgCosSimilarityByFirstPostId(topicPost.getId()))
                .toList()
                .stream()
                .min(Comparator.comparingDouble(PostSimilarityProjection::getAvgCosSimilarity))
                .orElse(null);
    }


    /**
     * Calculates and saves the cosine similarity between each pair of topic posts.
     */
    private void savePostSimilarities(List<TopicPost> topicPosts) {
        topicPosts.forEach(first -> {
            topicPosts.forEach(second -> {
                postSimilarityRepository.insert(IdGenerator.generate(),
                        first.getId(),
                        second.getId(),
                        first.getEmbeddingVector(),
                        second.getEmbeddingVector());
            });
        });
    }

    private void saveTopicPosts(List<TopicPost> topicPosts) {
        topicPosts.forEach(topicPost -> {
            topicPostRepository.insert(IdGenerator.generate(), topicPost.getTopicId(), topicPost.getPostContent(), topicPost.getEmbeddingVector());
        });
    }


    private List<TopicPost> getTopicPosts(TopicCreateRequest topicCreateRequest, Topic topic) {
        final String topicId = topic.getId();
        return topicCreateRequest.getPosts().stream().map(post -> {

            EmbeddingResponse response = fetchEmbeddingResponse(post);

            try {
                return getTopicPost(topicId, post, response);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private TopicPost getTopicPost(String topicId, String post, EmbeddingResponse response) throws JsonProcessingException {
        return TopicPost.builder()
                .topicId(topicId)
                .postContent(post)
                .embeddingVector(objectMapper.writeValueAsString(Objects.requireNonNull(response)
                        .getData()
                        .get(0)
                        .getEmbedding()))
                .build();
    }

    /**
     *  Fetches the embedding response (embedding vector) for a given text using the OpenAI's embedding model
    */
    private EmbeddingResponse fetchEmbeddingResponse(String post) {
        return webClient
                .post()
                .uri(openAIConstants.getEmbeddingUrl())
                .bodyValue(new EmbeddingRequest(openAIConstants.getEmbeddingModel(), post))
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .block();
    }


}
