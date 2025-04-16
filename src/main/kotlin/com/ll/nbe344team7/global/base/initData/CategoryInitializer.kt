package com.ll.nbe344team7.global.base.initData

import com.ll.nbe344team7.domain.category.entity.Category
import com.ll.nbe344team7.domain.category.repository.CategoryRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class CategoryInitializer(
    private val categoryRepository: CategoryRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (categoryRepository.count() == 0L) {
            val categories = listOf(
                Category(name = "남성의류"),
                Category(name = "여성의류"),
                Category(name = "디지털/가전"),
                Category(name = "가구/인테리어"),
                Category(name = "패션/잡화"),
                Category(name = "뷰티/미용"),
                Category(name = "도서/음반"),
                Category(name = "스포츠/레저"),
                Category(name = "취미/게임"),
                Category(name = "유아동/출산"),
                Category(name = "반려동물용품"),
                Category(name = "식품"),
                Category(name = "식물"),
                Category(name = "기타")
            )
            categoryRepository.saveAll(categories)
        }
    }
}