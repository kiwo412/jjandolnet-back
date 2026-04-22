package com.kin.jjandolnet.api.domain.post.controller;

import com.kin.jjandolnet.api.domain.auth.UserPrincipal;
import com.kin.jjandolnet.api.domain.post.dto.PostDto;
import com.kin.jjandolnet.api.domain.post.service.PostService;
import com.kin.jjandolnet.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostDto.Response>>> getPosts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostDto.Response> posts = postService.getPosts(pageable);
        return ResponseEntity.ok(ApiResponse.success("게시글 목록 조회가 완료되었습니다.", posts));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createPost(
            @Valid @RequestBody PostDto.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long postId = postService.createPost(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("게시글이 등록되었습니다.", postId));
    }

}
