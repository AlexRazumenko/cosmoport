package com.space.utils;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.Objects;

public class SpecificationFactory {
    public static Specification<Ship> getSpecification (String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                                   Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                   Double minRating, Double maxRating) {

        Specification<Ship> specification = Specification.where(filterByNameSpecification(name))
                .and(filterByPlanetSpecification(planet))
                .and(filterByShipTypeSpecification(shipType))
                .and(filterByDateSpecification(after, before))
                .and(filterByUsageSpecification(isUsed))
                .and(filterBySpeedSpecification(minSpeed, maxSpeed))
                .and(filterByCrewSizeSpecification(minCrewSize, maxCrewSize))
                .and(filterByRatingSpecification(minRating, maxRating));
        return specification;
    }

    public static Specification<Ship> filterByNameSpecification(String name) {
        return (r, q, cb) -> Objects.nonNull(name) ? cb.like(r.get("name"), "%" + name + "%") : null;
    }

    public static Specification<Ship> filterByPlanetSpecification(String planet) {
        return (r, q, cb) -> Objects.nonNull(planet) ? cb.like(r.get("planet"), "%" + planet + "%") : null;
    }

    public static Specification<Ship> filterByShipTypeSpecification(ShipType shipType) {
        return (r, q, cb) -> Objects.nonNull(shipType) ? cb.equal(r.get("shipType"), shipType) : null;
    }

    public static Specification<Ship> filterByDateSpecification(Long after, Long before) {
        return (r, q, cb) -> {
            if (Objects.isNull(after) & Objects.isNull(before)) return null;
            if (Objects.isNull(after)) return cb.lessThanOrEqualTo(r.get("prodDate"), new Date(before));
            if (Objects.isNull(before)) return cb.greaterThanOrEqualTo(r.get("prodDate"), new Date(after));
            return cb.between(r.get("prodDate"), new Date(after), new Date(before));
        };
    }

    public static Specification<Ship> filterByUsageSpecification(Boolean isUsed) {
        return (r, q, cb) -> {
            if (Objects.isNull(isUsed)) return null;
            if (isUsed) return cb.isTrue(r.get("isUsed"));
            else return cb.isFalse(r.get("isUsed"));
        };
    }

    public static Specification<Ship> filterBySpeedSpecification(Double min, Double max) {
        return (r, q, cb) -> {
            if (Objects.isNull(min) & Objects.isNull(max)) return null;
            if (Objects.isNull(min)) return cb.lessThanOrEqualTo(r.get("speed"), max);
            if (Objects.isNull(max)) return cb.greaterThanOrEqualTo(r.get("speed"), min);
            return cb.between(r.get("speed"), min, max);
        };
    }

    public static Specification<Ship> filterByCrewSizeSpecification(Integer min, Integer max) {
        return (r, q, cb) -> {
            if (Objects.isNull(min) & Objects.isNull(max)) return null;
            if (Objects.isNull(min)) return cb.lessThanOrEqualTo(r.get("crewSize"), max);
            if (Objects.isNull(max)) return cb.greaterThanOrEqualTo(r.get("crewSize"), min);
            return cb.between(r.get("crewSize"), min, max);
        };
    }

    public static Specification<Ship> filterByRatingSpecification(Double min, Double max) {
        return (r, q, cb) -> {
            if (Objects.isNull(min) & Objects.isNull(max)) return null;
            if (Objects.isNull(min)) return cb.lessThanOrEqualTo(r.get("rating"), max);
            if (Objects.isNull(max)) return cb.greaterThanOrEqualTo(r.get("rating"), min);
            return cb.between(r.get("rating"), min, max);
        };
    }
}
