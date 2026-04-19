package com.kin.jjandolnet.api.domain.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "attachment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public void setPost(Post post) {
        this.post = post;

        if (post != null && !post.getAttachments().contains(this)) {
            post.getAttachments().add(this);
        }
    }

    @Builder
    public Attachment(String fileName, String filePath, Long fileSize, Post post) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.post = post;
    }
}
