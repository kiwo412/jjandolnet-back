package com.kin.jjandolnet.api.domain.post.entity;

import com.kin.jjandolnet.api.domain.rank.entity.RankHistory;
import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    @Column(length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    //어차피 한개(1:1)라 굳이 지연로딩 안해도 됨.
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private RankHistory rankHistory;

    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        if (attachment.getPost() != this) {
            attachment.setPost(this);
        }
    }

    public void setRankHistory(RankHistory rankHistory) {
        this.rankHistory = rankHistory;
        if (rankHistory != null && rankHistory.getPost() != this) {
            rankHistory.setPost(this);
        }
    }

    @Builder
    public Post(String category, String title, String content, int viewCount, String status, User user,
                List<Attachment> attachments) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.status = status;
        this.user = user;

        if (attachments != null) {
            attachments.forEach(this::addAttachment);
        }
    }

    public void updatePost(String title, String content){
        this.title = title;
        this.content = content;
    }

}
