package vdt.hospital.scheduling

import arrow.core.*
import arrow.core.extensions.applicativeNel
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

@Service
class SchedulingService(
    private val patientService: PatientService,
    private val medicService: MedicService,
    private val appointmentRepository: AppointmentRepository,
    private val notificationService: NotificationService,

) {
    suspend fun createNewAppointment(input: CreateAppointmentDto): Validated<NonEmptyList<ValidationError>, Appointment> {
        return Validated.applicativeNel<ValidationError>()
            .tupledN(
                validateAndGetPatient(input).toValidatedNel(),
                validateAndGetMedic(input).toValidatedNel(),
                validateStartTime(input).toValidatedNel(),
                validateSlotIsFree(input).toValidatedNel(),
            )
            .fix()
            .map { (patient, medic, start, _) ->
                val appointment = Appointment(
                    UUID.randomUUID(),
                    medic,
                    patient,
                    input.activity,
                    start.toInstant(),
                    calculateEndTime(start, input.activity.duration).toInstant()
                )

                appointmentRepository.save(appointment).awaitSingle()

                notificationService.sendAppointmentCreatedNotification(appointment)

                appointment
            }
    }

    private fun validateStartTime(input: CreateAppointmentDto): Validated<ValidationError.StartDateInThePast, ZonedDateTime> =
        if (input.start.toInstant().isAfter(Instant.now())) input.start.valid()
        else ValidationError.StartDateInThePast.invalid()

    private suspend fun validateAndGetPatient(input: CreateAppointmentDto): Validated<ValidationError.PatientNotFound, Patient> =
        patientService.findById(input.patientId)?.valid() ?: ValidationError.PatientNotFound.invalid()

    private suspend fun validateAndGetMedic(input: CreateAppointmentDto): Validated<ValidationError.MedicNotFound, Medic> =
        medicService.findById(input.medicId)?.valid() ?: ValidationError.MedicNotFound.invalid()

    private suspend fun validateSlotIsFree(input: CreateAppointmentDto): Validated<ValidationError.SlotUnavailable, Unit> {
        val exists = appointmentRepository.existsByStartBeforeOrEndAfter(
            input.start.toInstant(), calculateEndTime(
                input.start,
                input.activity.duration
            ).toInstant()
        )
        return if (exists) ValidationError.SlotUnavailable.invalid() else Valid(Unit)
    }

    private fun calculateEndTime(start: ZonedDateTime, duration: Duration) = start.plus(duration)

}

sealed class ValidationError {
    object MedicNotFound : ValidationError()
    object StartDateInThePast : ValidationError()
    object SlotUnavailable : ValidationError()
    object PatientNotFound : ValidationError()
}
