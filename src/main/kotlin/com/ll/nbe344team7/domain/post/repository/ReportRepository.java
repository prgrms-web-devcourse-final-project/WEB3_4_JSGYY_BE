package com.ll.nbe344team7.domain.post.repository;

import com.ll.nbe344team7.domain.post.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
