package org.danceden.bookings.repository;

import org.danceden.bookings.model.Offering;
import org.danceden.bookings.model.OfferingStatus;
import org.danceden.bookings.model.OfferingType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class OfferingSpecifications {

    private OfferingSpecifications() {
    }

    public static Specification<Offering> hasType(OfferingType type) {
        return (root, query, cb) -> type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<Offering> hasStatus(OfferingStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Offering> startDateAfter(LocalDate date) {
        return (root, query, cb) -> date == null ? null : cb.greaterThan(root.get("startDate"), date);
    }
}
