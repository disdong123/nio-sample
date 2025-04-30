package kr.disdong.springboot.template.infrastructure.jpa.domain.user.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.disdong.springboot.template.infrastructure.jpa.domain.user.model.QUserEntity
import kr.disdong.springboot.template.infrastructure.jpa.domain.user.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long>, UserCustomRepository

interface UserCustomRepository {
    fun findByUserId(id: Long): UserEntity?
}

class UserRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : UserCustomRepository {

    private val userEntity = QUserEntity.userEntity
    override fun findByUserId(id: Long): UserEntity? {
        return jpaQueryFactory
            .selectFrom(userEntity)
            .where(
                userEntity.id.eq(id),
                userEntity.isDeleted.isFalse
            )
            .fetchOne()
    }
}
