package org.danceden.bookings.exception;

public class OfferingNotFoundException extends RuntimeException {

    public OfferingNotFoundException(Long id) {
        super("Offering not found: " + id);
    }
}
