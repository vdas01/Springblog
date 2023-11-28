package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TagService {
    List<Tag> findAllTags();

    Tag getTagByIdRest(int tagId);

    String createTagRest(int postId,Tag tag);

    List<Tag> getTagsByPostIdRest(int postId);

    String updateTagRest(int tagId, Tag updatedTag);

    String deleteTagRest(int tagId);
}
