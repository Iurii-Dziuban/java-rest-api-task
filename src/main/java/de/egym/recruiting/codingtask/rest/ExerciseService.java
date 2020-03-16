package de.egym.recruiting.codingtask.rest;

import de.egym.recruiting.codingtask.jpa.domain.Enums;
import de.egym.recruiting.codingtask.jpa.domain.Exercise;
import de.egym.recruiting.codingtask.rest.validation.HasNoId;
import io.swagger.annotations.Api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/v1/exercise")
@Api(value = "Exercise Service")
public interface ExerciseService {

	/**
	 * Get the exercise for a given exerciseId.
	 *
	 * @param exerciseId
	 *            id to search
	 * @return the exercise for the given exerciseId
	 */
	@GET
	@Path("/{exerciseId}")
	@Nonnull
	@Produces(MediaType.APPLICATION_JSON)
	Exercise getExerciseById(@Nonnull @PathParam("exerciseId") Long exerciseId);

	/**
	 * Get the exercises with the given description.
	 *
	 * @param description
	 *            description to search
	 * @return the exercises for the given description
	 */
	@GET
	@Nonnull
	@Produces(MediaType.APPLICATION_JSON)
	List<Exercise> getExerciseByDescription(@Nonnull @QueryParam("description") String description);

	/**
	 * Create the exercise
	 *
	 * @param exercise
	 *            exercise to create
	 * @return the created exercise
	 */
	@POST
	@Nonnull
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Exercise create(@Nonnull @Valid @HasNoId Exercise exercise);

	/**
	 * Update the exercise
	 * @param exerciseId - id of the exercise
	 * @param exercise - exercise new data
	 * @return updated exercise
	 */
	@PUT
	@Path("/{exerciseId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @Nonnull
    Exercise update(@Nonnull @PathParam("exerciseId") Long exerciseId, @Nonnull @Valid @HasNoId Exercise exercise);

	/**
	 * Delete the exercise by exerciseId
	 * @param exerciseId id of the exercise to be delete
	 * @return HTTP 200 if the exercise deleted successfully
	 */
	@DELETE
	@Path("/{exerciseId}")
	@Nonnull
	Response deleteExerciseById(@Nonnull @PathParam("exerciseId") Long exerciseId);

	/**
	 * Search exercises by userId and optional exercise type and/or startTime of the exercise
	 * @param userId - id of the user
	 * @param type optional type of the exercise
	 * @param date - optional date in 'yyyy-MM-dd' format of the exercise, when it has startTime
	 * @return filtered list of user exercises
	 */
	@GET
	@Path("/exercises/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Nonnull
	List<Exercise> getExercises(@Nonnull @PathParam("userId") Long userId,
								@QueryParam("type") @Nullable Enums.ExerciseType type,
								@QueryParam("date") @Nullable String date);

	/**
	 * Returns list of user ids sorted in descending order of their rankings
	 * @param userIds - ids of users to calculate ranks for
	 * @return list of user ids sorted in descending order of their rankings
	 */
	@GET
	@Path("/getRankingExercises")
	@Produces(MediaType.APPLICATION_JSON)
	List<Long> getRanking(@Nonnull @QueryParam("id") List<Long> userIds);
}
