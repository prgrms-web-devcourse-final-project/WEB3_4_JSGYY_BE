package com.ll.nbe344team7

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class Nbe344Team7Application

fun main(args: Array<String>) {
	runApplication<Nbe344Team7Application>(*args)
}

