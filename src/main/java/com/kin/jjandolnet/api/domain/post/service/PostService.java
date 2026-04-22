package com.kin.jjandolnet.api.domain.post.service;

import com.kin.jjandolnet.api.domain.post.dto.PostDto;
import com.kin.jjandolnet.api.domain.post.entity.Post;
import com.kin.jjandolnet.api.domain.post.repository.PostRepository;
import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public Long createPost(PostDto.CreateRequest request, Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .build();

        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }

    @Transactional(readOnly = true)
    public Page<PostDto.Response> getPosts(Pageable pageable) {

        return postRepository.findAll(pageable)
                .map(PostDto.Response::from);
    }

}
