package com.blog.backend.repositories.post;



import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.blog.backend.models.post.Post;
import com.blog.backend.models.user.User;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
  @EntityGraph(attributePaths = { "user", "comments", "votes" })
  Page<Post> findAllByUserId(@Nonnull Long userId, Pageable pageable);
  @EntityGraph(attributePaths = { "user" })
  Page<Post> findAll(Pageable pageable);
  List<Post> findByTitleContaining(String keyword);
  Optional<Post> findByIdAndUserId(@Nonnull Long id, Long userId);
  @Query("SELECT p.user FROM Post p WHERE p.id = :postId")
  Optional<User> findUserByPostId(@Param("postId") Long postId);
}

/*
 * At the first glance the post repository looks empty but the spring data jpa
 * provides a lot of functionality out of the box, including the ability to
 * query by entity attributes, pagination, and sorting.
 * all you have to do is define method signatures following spring data jpa
 * naming conventions.
 * for example the findByUser method above will generate a query that finds all
 * posts
 * associated with a specific user.
 * here are a list of all the words you can use to create query methods:
 */
/*
 * ================= Spring Data JPA Query Method Cheat Sheet =================
 * 
 * 1️⃣ Basics: Keywords for simple field queries
 * ------------------------------------------------
 * findBy / getBy / readBy -> Start a query
 * And -> Combine conditions (AND)
 * Or -> Combine conditions (OR)
 * Is / Equals -> Equality (optional)
 * 
 * Examples:
 * findByTitle(String title)
 * findByTitleAndUser(String title, User user)
 * findByTitleOrContent(String title, String content)
 * findByTitleIs(String title)
 * 
 * 2️⃣ Comparisons
 * ------------------------------------------------
 * GreaterThan -> >
 * GreaterThanEqual -> >=
 * LessThan -> <
 * LessThanEqual -> <=
 * Between -> BETWEEN
 * 
 * Examples:
 * findByLikesGreaterThan(int likes)
 * findByCreatedAtLessThan(LocalDate date)
 * findByCreatedAtBetween(LocalDate start, LocalDate end)
 * 
 * 3️⃣ Strings / Patterns
 * ------------------------------------------------
 * Containing -> LIKE %value%
 * Like -> LIKE
 * StartingWith / StartsWith -> LIKE value%
 * EndingWith / EndsWith -> LIKE %value
 * IgnoreCase -> Case-insensitive
 * 
 * Examples:
 * findByTitleContaining(String keyword)
 * findByTitleLike(String pattern)
 * findByTitleStartingWith("Hello")
 * findByTitleEndingWith("World")
 * findByTitleContainingIgnoreCase("spring")
 * 
 * 4️⃣ Collections / Null checks
 * ------------------------------------------------
 * In -> Field value in collection
 * NotIn -> Field value NOT in collection
 * IsNull -> Field is null
 * IsNotNull -> Field is not null
 * 
 * Examples:
 * findByStatusIn(List<Status> statuses)
 * findByStatusNotIn(List<Status> statuses)
 * findByDeletedAtIsNull()
 * findByDeletedAtIsNotNull()
 * 
 * 5️⃣ Ordering / Limiting
 * ------------------------------------------------
 * OrderBy -> Sort results
 * Top / First -> Limit number of results
 * 
 * Examples:
 * findByUserOrderByCreatedAtDesc(User user)
 * findTop5ByOrderByLikesDesc()
 * 
 * 6️⃣ Combining multiple keywords
 * ------------------------------------------------
 * You can chain keywords to form complex queries:
 * 
 * Example:
 * findTop10ByUserAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(User user,
 * String keyword)
 * 
 * Explanation:
 * - Top10 -> limit 10 results
 * - ByUser -> filter by user
 * - AndTitleContainingIgnoreCase -> filter title containing keyword ignoring
 * case
 * - OrderByCreatedAtDesc -> sort by creation date descending
 * 
 * 7️⃣ Notes / Tips
 * ------------------------------------------------
 * - CamelCase matters: CreatedAt must match the entity field.
 * - Keyword order matters: findByTitleContainingAndUser works,
 * findByAndUserTitleContaining does not.
 * - Boolean fields: use IsTrue / IsFalse
 * e.g., findByPublishedIsTrue()
 * - Relationships: you can traverse nested objects
 * e.g., findByUserEmail(String email) // Post has User user, User has String
 * email
 * 
 * ============================================================================
 */
