package vdt.hospital.patient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class PatientApplication

fun main(args: Array<String>) {
	runApplication<PatientApplication>(*args)
}

