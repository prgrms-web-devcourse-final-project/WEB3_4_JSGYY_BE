package com.ll.nbe344team7.global.imageFIle.repository;

import com.ll.nbe344team7.global.imageFIle.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    List<ImageFile> findByPostId(Long postId);
}