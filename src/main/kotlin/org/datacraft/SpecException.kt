package org.datacraft

import java.time.format.DateTimeParseException

/**
 * Exception thrown when a specification cannot be found or processed.
 */
class SpecException : Exception {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    constructor(message: String) : super(message)

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method).
     *        (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    constructor(message: String, cause: Throwable) : super(message, cause)
}
