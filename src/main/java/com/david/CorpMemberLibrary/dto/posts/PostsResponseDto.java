package com.david.CorpMemberLibrary.dto.posts;

import com.david.CorpMemberLibrary.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }
}

