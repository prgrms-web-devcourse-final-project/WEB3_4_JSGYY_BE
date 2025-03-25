package com.ll.nbe344team7.global.base

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 *
 *
 * @author kjm72
 * @since 2025-03-25
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @Column(name = "created_at")
    @CreatedDate
    var createdAt: LocalDateTime? = null
        protected set

    @Column(name = "modified_at")
    @LastModifiedDate
    var modifiedAt: LocalDateTime? = null
        protected set
}