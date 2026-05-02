package com.kin.jjandolnet.api.domain.post.controller;

import com.kin.jjandolnet.api.domain.auth.UserPrincipal;
import com.kin.jjandolnet.api.domain.post.dto.PostDto;
import com.kin.jjandolnet.api.domain.post.service.PostService;
import com.kin.jjandolnet.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    @GetMapping
    @RequestMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDto.Response>> getPost(@PathVariable Long id) {

        PostDto.Response post = postService.getPost(id);
        return ResponseEntity.ok(ApiResponse.success("게시글 조회가 완료되었습니다.", post));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createPost(
            @Valid @RequestBody PostDto.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long postId = postService.createPost(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("게시글이 등록되었습니다.", postId));
    }

    @PutMapping
    @PreAuthorize("@postService.isAuthorUuid(#request.getId(), authentication.principal.uuid)")
    public ResponseEntity<ApiResponse<Long>> updatePost(
            @Valid @RequestBody PostDto.updateRequest request) {
        Long postId = postService.updatePost(request);
        return ResponseEntity.ok(ApiResponse.success("게시글이 수정되었습니다.", postId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@postService.isAuthorUuid(#id, authentication.principal.uuid)")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("게시글 삭제가 완료되었습니다."));
    }

}
