package org.danceden.bookings.controller;

import org.danceden.bookings.exception.OfferingNotFoundException;
import org.danceden.bookings.model.Offering;
import org.danceden.bookings.model.OfferingStatus;
import org.danceden.bookings.model.OfferingType;
import org.danceden.bookings.repository.OfferingRepository;
import org.danceden.bookings.repository.OfferingSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class OfferingController {

    private static final int MAX_PAGE_SIZE = 100;

    private final OfferingRepository offeringRepository;

    public OfferingController(OfferingRepository offeringRepository) {
        this.offeringRepository = offeringRepository;
    }

    @GetMapping("/api/offerings")
    public Page<Offering> getOfferings(
            @RequestParam(required = false) OfferingType type,
            @RequestParam(required = false) OfferingStatus status,
            @RequestParam(required = false) LocalDate startDateAfter,
            @PageableDefault(size = 20, sort = "startDate", direction = Sort.Direction.ASC) Pageable pageable) {

        Specification<Offering> spec = Specification
                .where(OfferingSpecifications.hasType(type))
                .and(OfferingSpecifications.hasStatus(status))
                .and(OfferingSpecifications.startDateAfter(startDateAfter));

        Pageable boundedPageable = pageable.getPageSize() > MAX_PAGE_SIZE
                ? PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE, pageable.getSort())
                : pageable;

        return offeringRepository.findAll(spec, boundedPageable);
    }

    @GetMapping("/api/offerings/{id}")
    public Offering getOffering(@PathVariable Long id) {
        return offeringRepository.findById(id)
                .orElseThrow(() -> new OfferingNotFoundException(id));
    }
}
