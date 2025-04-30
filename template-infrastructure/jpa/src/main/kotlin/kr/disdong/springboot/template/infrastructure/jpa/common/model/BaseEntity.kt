package kr.disdong.springboot.template.infrastructure.jpa.common.model

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.ZonedDateTime

@MappedSuperclass
abstract class BaseEntity(
    @Column
    val isDeleted: Boolean = false,

    @Column
    @CreatedDate
    val createdAt: ZonedDateTime = ZonedDateTime.now(),

    @Column
    @LastModifiedDate
    val updatedAt: ZonedDateTime = ZonedDateTime.now(),
)
