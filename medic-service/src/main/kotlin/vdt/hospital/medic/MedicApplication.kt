package vdt.hospital.medic

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MedicApplication

fun main(args: Array<String>) {
	runApplication<MedicApplication>(*args)
}
