package nl.expertlycoded.coursemcp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import nl.expertlycoded.coursemcp.Course.Level;

/**
 * Hardcoded course data for the demo. No database required.
 */
@Repository
public class CourseRepository {

    private static final List<Course> COURSES = List.of(
            new Course("JAVA-101", "Java Fundamentals", "Programming", Level.BEGINNER,
                    "Sanne de Vries", 3, new BigDecimal("1195.00"), LocalDate.of(2026, 11, 16),
                    "Utrecht", 8,
                    "Learn the basics of Java: syntax, OOP, collections and records."),
            new Course("SPRING-201", "Spring Boot 4 in Practice", "Programming", Level.INTERMEDIATE,
                    "Rens Manders", 2, new BigDecimal("1495.00"), LocalDate.of(2026, 11, 24),
                    "Den Bosch", 4,
                    "Build production-ready REST APIs with Spring Boot 4, observability and testing."),
            new Course("AI-301", "Building AI Agents with MCP", "AI", Level.ADVANCED,
                    "Rens Manders", 1, new BigDecimal("895.00"), LocalDate.of(2026, 12, 3),
                    "Online", 12,
                    "Connect your own systems to LLMs using the Model Context Protocol."),
            new Course("AI-101", "Prompt Engineering for Professionals", "AI", Level.BEGINNER,
                    "Lotte Jansen", 1, new BigDecimal("495.00"), LocalDate.of(2026, 11, 20),
                    "Online", 20,
                    "Get more out of Claude and other LLMs with structured prompts."),
            new Course("DEVOPS-201", "Docker & Kubernetes", "DevOps", Level.INTERMEDIATE,
                    "Mehmet Yilmaz", 2, new BigDecimal("1295.00"), LocalDate.of(2026, 12, 8),
                    "Amsterdam", 0,
                    "Containerize applications and deploy them to Kubernetes."),
            new Course("WEB-101", "Modern Web Development with Next.js", "Web Development", Level.BEGINNER,
                    "Sanne de Vries", 3, new BigDecimal("1095.00"), LocalDate.of(2027, 1, 12),
                    "Eindhoven", 10,
                    "From React basics to a deployed Next.js application."));

    public List<Course> search(@Nullable String category, @Nullable Level level, @Nullable String query) {
        return COURSES.stream()
                .filter(c -> category == null || c.category().equalsIgnoreCase(category))
                .filter(c -> level == null || c.level() == level)
                .filter(c -> query == null || matches(c, query))
                .toList();
    }

    public Optional<Course> findById(String id) {
        return COURSES.stream().filter(c -> c.id().equalsIgnoreCase(id)).findFirst();
    }

    public List<String> categories() {
        return COURSES.stream().map(Course::category).distinct().sorted().toList();
    }

    private static boolean matches(Course c, String query) {
        var term = query.toLowerCase(Locale.ROOT);
        return (c.title() + " " + c.description() + " " + c.instructor()).toLowerCase(Locale.ROOT).contains(term);
    }
}
