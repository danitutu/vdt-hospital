package vdt.hospital.patient

import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class PatientController(
    private val patientService: PatientService,
) {
    @PostMapping
    suspend fun createNewPatient(@RequestBody body: CreatePatientDto): ResponseEntity<Patient> {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createNewUser(body.toPatient()))
    }

    @PutMapping("/{patientId}")
    suspend fun updatePatient(
        @PathVariable patientId: UUID,
        @RequestBody body: UpdatePatientDto
    ): ResponseEntity<Patient> {
        validationWrapper { require(patientId == body.id) { "Body ID does not match the path ID" } }
        return ResponseEntity.status(HttpStatus.OK).body(patientService.updatePatient(patientId, body.toPatient()))
    }

    @GetMapping("/{patientId}")
    suspend fun findPatientById(
        @PathVariable patientId: UUID,
    ): ResponseEntity<Patient> {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.findPatientById(patientId))
    }

    @GetMapping
    suspend fun searchPatients(): ResponseEntity<Flow<Patient>> {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.searchPatients())
    }

}