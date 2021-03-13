package vdt.hospital.scheduling

data class Email(
    val to: List<String>,
    val contentType: ContentType,
    val body: String,
) {
    enum class ContentType {
        TEXT
    }
}