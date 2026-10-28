package nl.expertlycoded.coursemcp;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpTool.McpAnnotations;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import nl.expertlycoded.coursemcp.Course.Level;

/**
 * The MCP tools that an AI model can call. Spring AI generates the JSON schema
 * from the method parameters; the descriptions help the model decide which
 * tool to use when.
 */
@Component
public class CourseTools {

    private final CourseRepository repository;

    public CourseTools(CourseRepository repository) {
        this.repository = repository;
    }

    @McpTool(name = "search_courses",
            description = """
                    Searches the course catalog. All filters are optional; without filters \
                    all courses are returned. Includes price, start date, location, instructor \
                    and the number of available seats.""",
            annotations = @McpAnnotations(readOnlyHint = true, destructiveHint = false, idempotentHint = true))
    public List<Course> searchCourses(
            @McpToolParam(required = false, description = "Category, e.g. 'AI', 'Programming', 'DevOps', 'Web Development'")
            @Nullable String category,
            @McpToolParam(required = false, description = "Level: BEGINNER, INTERMEDIATE or ADVANCED")
            @Nullable Level level,
            @McpToolParam(required = false, description = "Free-text search on title, description or instructor")
            @Nullable String query) {
        return repository.search(blankToNull(category), level, blankToNull(query));
    }

    @McpTool(name = "get_course_details",
            description = "Returns all details of a single course by its course code, e.g. 'SPRING-201'.",
            annotations = @McpAnnotations(readOnlyHint = true, destructiveHint = false, idempotentHint = true))
    public Course getCourseDetails(@McpToolParam(description = "The course code, e.g. 'AI-301'") String courseId) {
        return repository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("No course found with code " + courseId));
    }

    @McpTool(name = "list_course_categories",
            description = "Returns the list of available course categories.",
            annotations = @McpAnnotations(readOnlyHint = true, destructiveHint = false, idempotentHint = true))
    public List<String> listCourseCategories() {
        return repository.categories();
    }

    private static @Nullable String blankToNull(@Nullable String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
