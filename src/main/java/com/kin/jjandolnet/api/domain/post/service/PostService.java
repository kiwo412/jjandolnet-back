package com.kin.jjandolnet.api.domain.post.service;

import com.kin.jjandolnet.api.domain.post.dto.PostDto;
import com.kin.jjandolnet.api.domain.post.entity.Post;
import com.kin.jjandolnet.api.domain.post.repository.PostRepository;
import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.api.domain.user.repository.UserRepository;
import com.kin.jjandolnet.global.error.exception.BusinessException;
import com.kin.jjandolnet.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("postService")
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public boolean isAuthorUuid(Long postId, String currentUserId) {
        return postRepository.findById(postId)
                .map(post -> post.getUser().getUuid().equals(currentUserId))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Page<PostDto.Response> getPosts(Pageable pageable) {

        return postRepository.findAll(pageable)
                .map(PostDto.Response::from);
    }

    @Transactional(readOnly = true)
    public PostDto.Response getPost(Long id) {

        return PostDto.Response.from(
                postRepository.findById(id).orElseThrow(
                        ()-> new BusinessException(ErrorCode.POST_NOT_FOUND)));
    }

    @Transactional
    public Long createPost(PostDto.CreateRequest request, Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .build();

        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }

    @Transactional
    public Long updatePost(PostDto.updateRequest request){

        Post post = postRepository.findById(request.getId()).orElseThrow(
                ()-> new BusinessException(ErrorCode.POST_NOT_FOUND));
        post.updatePost(request.getTitle(), request.getContent());

        return post.getId();
    }

    public void deletePost(Long id){
        postRepository.deleteById(id);
    }

}
