package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TagService {
    List<Tag> findAllTags();
}
