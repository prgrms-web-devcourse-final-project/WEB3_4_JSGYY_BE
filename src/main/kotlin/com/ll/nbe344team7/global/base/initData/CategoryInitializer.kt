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
                Category(name = "여성의류"),
                Category(name = "남성의류"),
                Category(name = "유아복"),
                Category(name = "패션잡화"),
                Category(name = "가구"),
                Category(name = "디지털"),
                Category(name = "생활가전"),
                Category(name = "생활/주방"),
                Category(name = "뷰티/미용"),
                Category(name = "취미/게임/음반"),
                Category(name = "스포츠/레저"),
            )
            categoryRepository.saveAll(categories)
        }
    }
}