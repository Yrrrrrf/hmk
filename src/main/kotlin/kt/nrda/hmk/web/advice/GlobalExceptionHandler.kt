package kt.nrda.hmk.web.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to (e.message ?: "Invalid request")))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf("error" to (e.message ?: "Internal error")))
    }
}
