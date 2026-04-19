package com.kin.jjandolnet.api.domain.post.dto;

import com.kin.jjandolnet.api.domain.post.entity.Attachment;
import lombok.Builder;
import lombok.Getter;

public class AttachmentDto {

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String fileName;
        private String filePath;
        private Long fileSize;

        public static Response from(Attachment attachment) {
            return Response.builder()
                    .id(attachment.getId())
                    .fileName(attachment.getFileName())
                    .filePath(attachment.getFilePath())
                    .fileSize(attachment.getFileSize())
                    .build();
        }
    }
}
