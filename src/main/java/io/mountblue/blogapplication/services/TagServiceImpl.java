package io.mountblue.blogapplication.services;

import io.mountblue.blogapplication.entity.Tag;
import io.mountblue.blogapplication.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements  TagService{

    @Autowired
    private TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }
}
