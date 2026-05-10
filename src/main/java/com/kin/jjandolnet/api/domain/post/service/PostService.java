package com.kin.jjandolnet.api.domain.post.service;

import com.kin.jjandolnet.api.domain.post.dto.CommentDto;
import com.kin.jjandolnet.api.domain.post.dto.PostDto;
import com.kin.jjandolnet.api.domain.post.entity.Comment;
import com.kin.jjandolnet.api.domain.post.entity.Post;
import com.kin.jjandolnet.api.domain.post.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

    public boolean isAuthorUuid(Long postId, String currentUserId) {
        return postRepository.findById(postId)
                .map(post -> post.getUser().getUuid().equals(currentUserId))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Page<PostDto.Response> getPosts(PostDto.SearchRequest searchRequest, Pageable pageable) {

        return postRepository.searchPosts(searchRequest, pageable)
                .map(PostDto.Response::from);
    }

    @Transactional
    public PostDto.Response getPost(Long id) {

        Post post =postRepository.findById(id).orElseThrow(
                ()-> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.updateViewCount();

        return PostDto.Response.from(post);
    }

    @Transactional(readOnly = true)
    public Page<CommentDto.Response> getComments(Long postId, Pageable pageable) {

        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

        return commentPage.map(CommentDto.Response::from);
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
    public void createComment(CommentDto.CreateRequest request, Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(request.getPostId()).orElseThrow(
                ()-> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    public Long updatePost(PostDto.updateRequest request){

        Post post = postRepository.findById(request.getId()).orElseThrow(
                ()-> new BusinessException(ErrorCode.POST_NOT_FOUND));
        post.updatePost(request.getTitle(), request.getContent());

        return post.getId();
    }

    @Transactional
    public void updateComment(CommentDto.UpdateRequest request, Long userId){

        Comment comment = commentRepository.findByIdAndUser_Id(request.getId(),userId).orElseThrow(
                ()-> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        comment.updateComment(request.getContent());
    }

    @Transactional
    public void deletePost(Long id){
        postRepository.deleteById(id);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId){
        commentRepository.deleteByIdAndUser_Id(commentId, userId);
    }

}
