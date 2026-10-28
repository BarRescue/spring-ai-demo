package nl.expertlycoded.coursemcp;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Course(
        String id,
        String title,
        String category,
        Level level,
        String instructor,
        int durationInDays,
        BigDecimal priceEurExclVat,
        LocalDate startDate,
        String location,
        int availableSeats,
        String description) {

    public enum Level { BEGINNER, INTERMEDIATE, ADVANCED }
}
