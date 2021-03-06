package com.reckue.post.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class Comment is responsible for model that works with comments.
 *
 * @author Artur Magomedov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Comment implements Cloneable {

    @Id
    private String id;

    private String userId;
    private String postId;
    private String commentId;
    private List<Node> nodes;

    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modificationDate;

    @Override
    public Comment clone() {
        try {
            return (Comment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
