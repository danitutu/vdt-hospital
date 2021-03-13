package vdt.hospital.scheduling

import java.time.Duration

enum class Activity(val displayName: String, val specialty: Specialty, val duration: Duration) {
    DermatologyConsult("Dermatology consult", Specialty.DERMATOLOGY, Duration.ofMinutes(20)),
    MriHandScan("MRI Hand Scan", Specialty.RADIOLOGY, Duration.ofMinutes(30)),
    MriLumbarScan("MRI Lumbar Scan", Specialty.RADIOLOGY, Duration.ofMinutes(40)),
    MriCompleteSpineScan("MRI Complete Spine Scan", Specialty.RADIOLOGY, Duration.ofMinutes(90)),
    MedicalRecovery("Medical Recovery", Specialty.PHYSICAL_MEDICINE_AND_REHABILITATION, Duration.ofMinutes(50)),
    Appendectomy("Appendectomy", Specialty.GENERAL_SURGERY, Duration.ofMinutes(60)),
}
