package com.ll.nbe344team7.domain.post.repository;

import com.ll.nbe344team7.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:saleStatus IS NULL OR p.saleStatus = :saleStatus) AND " +
            "(:keyword IS NULL OR p.title LIKE %:keyword% OR p.content LIKE %:keyword%)")
    Page<Post> findBySearchCriteria(
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("saleStatus") Boolean saleStatus,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
