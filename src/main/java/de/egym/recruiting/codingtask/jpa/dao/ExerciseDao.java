package de.egym.recruiting.codingtask.jpa.dao;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.egym.recruiting.codingtask.jpa.domain.Enums;
import de.egym.recruiting.codingtask.jpa.domain.Exercise;

public interface ExerciseDao extends BaseDao<Exercise> {

	/**
	 * Returns a list of exercises with the given description
	 *
	 * @param description of the exercise
	 * @return filters list of exercise
	 */
	@Nonnull
	List<Exercise> findByDescription(@Nullable String description);

	/**
	 * Returns a list of exercise ids that conflict with the exercise for the user based on
	 * overlap between startTime + Duration window, excluding both time ends
	 *
	 * @param exercise - exercise to find conflicts related to
	 * @return conflict exercise ids
	 */
	@Nonnull
	List<Long> findConflictExerciseIds(@Nonnull Exercise exercise);

	/**
	 * Returns list of exercises that satisfies search criteria:
	 * (userId - mandatory, type and date are optional parameters)
	 *
	 * @param userId - id of the user for which to search the exercises
	 * @param type   - optional type of exercise
	 * @param date   - optional date in 'yyyy-MM-dd' format on which startTime of exercise happens
	 * @return list of exercises that satisfies search criteria
	 */
	@Nonnull
	List<Exercise> findExercises(@Nonnull Long userId, @Nullable Enums.ExerciseType type, @Nullable String date);

	/**
	 * Returns list of exercises for users with userIds that will take part in the ranking
	 * which startTime is in the last 4 weeks (ignoring the duration of the exercise for the tests)
	 * ordered by startTime
	 * @param userIds ids of the users to calculate the ranking
	 * @return exercises for users with userIds that will take part in the ranking ordered by startTime
	 */
	@Nonnull
    List<Exercise> getRankingExercises(@Nonnull List<Long> userIds);
}
