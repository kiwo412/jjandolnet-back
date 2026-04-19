package com.kin.jjandolnet.api.domain.rank.entity;

import com.kin.jjandolnet.api.domain.post.entity.Post;
import com.kin.jjandolnet.api.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "rank_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "change_point", nullable = false)
    private int changePoint;

    @Column(length = 255)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;

    public void setUser(User user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;

        if (post != null && post.getRankHistory() != this) {
            post.setRankHistory(this);
        }
    }

    @Builder
    public RankHistory(int changePoint, String reason, User user, Post post) {
        this.changePoint = changePoint;
        this.reason = reason;
        this.user = user;
        this.post = post;
    }
}
