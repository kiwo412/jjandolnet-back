package com.kin.jjandolnet.api.domain.post.repository;

import com.kin.jjandolnet.api.domain.post.dto.PostDto;
import com.kin.jjandolnet.api.domain.post.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.kin.jjandolnet.api.domain.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> searchPosts(PostDto.SearchRequest condition, Pageable pageable) {
        List<Post> content = queryFactory
                .selectFrom(post)
                .where(
                        filterEq(condition.getFilter(), condition.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        filterEq(condition.getFilter(), condition.getKeyword())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression filterEq(String filter, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        if(filter.equals("nickname")){
           return post.user.nickname.containsIgnoreCase(keyword);
        }else if(filter.equals("content")){
           return post.content.containsIgnoreCase(keyword);
        }else{
            return post.title.containsIgnoreCase(keyword);
        }
    }
}
