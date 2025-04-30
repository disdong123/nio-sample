package kr.disdong.springboot.template.infrastructure.jpa.domain.user.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import kr.disdong.springboot.template.infrastructure.jpa.common.model.BaseEntity

@Entity(name = "user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(
        nullable = false,
        unique = false,
        length = 100,
    )
    val name: String,

    @Column(
        nullable = false,
        unique = false,
        length = 20,
    )
    val phone: String,
) : BaseEntity()
