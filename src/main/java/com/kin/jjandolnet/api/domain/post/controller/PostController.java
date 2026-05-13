package com.kin.jjandolnet.api.domain.post.controller;

import com.kin.jjandolnet.api.domain.auth.UserPrincipal;
import com.kin.jjandolnet.api.domain.post.dto.CommentDto;
import com.kin.jjandolnet.api.domain.post.dto.PostDto;
import com.kin.jjandolnet.api.domain.post.service.PostService;
import com.kin.jjandolnet.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "05. PostController", description = " 게시판 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 목록")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostDto.Response>>> getPosts(
            @ModelAttribute PostDto.SearchRequest searchRequest,
            @Parameter(description = "페이지 설정 (page, size, sort)",
                    example = "{\"page\": 0, \"size\": 10, \"sort\": [\"createdAt,desc\"]}")
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostDto.Response> posts = postService.getPosts(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("게시글 목록 조회가 완료되었습니다.", posts));
    }

    @Operation(summary = "게시글 상세보기")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDto.Response>> getPost(@PathVariable Long id) {

        PostDto.Response post = postService.getPost(id);
        return ResponseEntity.ok(ApiResponse.success("게시글 조회가 완료되었습니다.", post));
    }

    @Operation(summary = "해당 게시글의 댓글 목록")
    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<Page<CommentDto.Response>>> getComments(
            @Parameter(example = "1")
            @PathVariable Long postId,
            @ParameterObject
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<CommentDto.Response> comments = postService.getComments(postId, pageable);

        return ResponseEntity.ok(ApiResponse.success("댓글 조회가 완료되었습니다.", comments));
    }

    @Operation(summary = "게시글 쓰기", security = @SecurityRequirement(name = "accessToken"))
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createPost(
            @Valid @RequestBody PostDto.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long postId = postService.createPost(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("게시글이 등록되었습니다.", postId));
    }

    @Operation(summary = "해당 게시글의 댓글 쓰기", security = @SecurityRequirement(name = "accessToken"))
    @PostMapping("/{postId}/comment")
    public ResponseEntity<ApiResponse<Void>> createComment(
            @Valid @RequestBody CommentDto.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        postService.createComment(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("댓글이 등록되었습니다."));
    }

    @Operation(summary = "게시글 수정", security = @SecurityRequirement(name = "accessToken"))
    @PutMapping
    @PreAuthorize("@postService.isAuthorUuid(#request.getId(), authentication.principal.uuid)")
    public ResponseEntity<ApiResponse<Long>> updatePost(
            @Valid @RequestBody PostDto.updateRequest request) {
        Long postId = postService.updatePost(request);
        return ResponseEntity.ok(ApiResponse.success("게시글이 수정되었습니다.", postId));
    }

    @Operation(summary = "해당 게시글의 댓글 수정", security = @SecurityRequirement(name = "accessToken"))
    @PutMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(
            @Valid @RequestBody CommentDto.UpdateRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        postService.updateComment(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("댓글이 수정되었습니다."));
    }

    @Operation(summary = "게시글 삭제", security = @SecurityRequirement(name = "accessToken"))
    @DeleteMapping("/{id}")
    @PreAuthorize("@postService.isAuthorUuid(#id, authentication.principal.uuid)")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("게시글 삭제가 완료되었습니다."));
    }

    @Operation(summary = "댓글 삭제", security = @SecurityRequirement(name = "accessToken"))
    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        postService.deleteComment(commentId, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("게시글 삭제가 완료되었습니다."));
    }

}
