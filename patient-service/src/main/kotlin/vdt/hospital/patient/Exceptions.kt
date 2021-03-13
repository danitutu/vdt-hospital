package vdt.hospital.patient

sealed class AppException(message: String, val type: String) : RuntimeException(message)
class ValidationException(message: String) : AppException(message, "VALIDATION_ERROR")

inline fun validationWrapper(supplier: () -> Unit) = try {
    supplier.invoke()
} catch (ex: Exception) {
    when (ex) {
        is IllegalArgumentException, is IllegalStateException -> throw ValidationException(ex.message!!)
        else -> throw ex
    }
}
