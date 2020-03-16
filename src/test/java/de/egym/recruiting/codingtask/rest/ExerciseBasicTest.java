package de.egym.recruiting.codingtask.rest;

import com.google.inject.Inject;
import de.egym.recruiting.codingtask.AbstractIntegrationTest;
import de.egym.recruiting.codingtask.TestClientService;
import de.egym.recruiting.codingtask.jpa.domain.Enums;
import de.egym.recruiting.codingtask.jpa.domain.Exercise;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ExerciseBasicTest extends AbstractIntegrationTest {

	@Inject
	private TestClientService testClientService;

	@Test(expected = RuntimeException.class)
	public void testNotValidExerciseId() {
		testClientService.getExercise(-1L);
	}

	@Test
	public void testInsert() {
		final long userId = 10L;
		final String date = "2016-06-01T14:23:35";

		final Exercise exerciseToInsert = new Exercise();
		exerciseToInsert.setDescription("Coding Task");
		exerciseToInsert.setDuration(14400);
		exerciseToInsert.setDistance(0);
		exerciseToInsert.setCalories(500);
		exerciseToInsert.setStartTime(convertDate(date));
		exerciseToInsert.setType(Enums.ExerciseType.OTHER);
		exerciseToInsert.setUserId(userId);

		final Exercise persistedExercise = testClientService.createExercise(exerciseToInsert);
		assertNotNull(persistedExercise);
		assertNotNull(persistedExercise.getId());
		assertThat(persistedExercise.getDescription(), is("Coding Task"));
		assertThat(persistedExercise.getDuration(), is(14400));
		assertThat(persistedExercise.getType(), is(Enums.ExerciseType.OTHER));
		assertThat(persistedExercise.getCalories(), is(500));
		assertThat(persistedExercise.getDistance(), is(0));
		assertThat(persistedExercise.getUserId(), is(userId));
		assertDate(persistedExercise.getStartTime(), convertDate(date));

		final Exercise selectedExercise = testClientService.getExercise(persistedExercise.getId());
		assertNotNull(selectedExercise);
		assertThat(selectedExercise.getId(), is(persistedExercise.getId()));
		assertThat(selectedExercise.getDescription(), is("Coding Task"));
		assertThat(selectedExercise.getDuration(), is(14400));
		assertThat(selectedExercise.getType(), is(Enums.ExerciseType.OTHER));
		assertThat(selectedExercise.getCalories(), is(500));
		assertThat(selectedExercise.getDistance(), is(0));
		assertThat(selectedExercise.getUserId(), is(userId));
		assertDate(selectedExercise.getStartTime(), convertDate(date));
	}

	@Test
	public void testUpdate() {
		final long userId = 11L;
		final String date = "2016-06-02T17:03:15";

		final Exercise exerciseToInsert = new Exercise();
		exerciseToInsert.setDescription("Coding Task");
		exerciseToInsert.setDuration(14400);
		exerciseToInsert.setDistance(0);
		exerciseToInsert.setCalories(500);
		exerciseToInsert.setStartTime(convertDate(date));
		exerciseToInsert.setType(Enums.ExerciseType.OTHER);
		exerciseToInsert.setUserId(userId);

		final Exercise persistedExercise = testClientService.createExercise(exerciseToInsert);
		assertNotNull(persistedExercise);
		assertNotNull(persistedExercise.getId());

		final Exercise exerciseToUpdate = new Exercise();
		exerciseToUpdate.setId(persistedExercise.getId());
		exerciseToUpdate.setDescription("Onsite Interview");
		exerciseToUpdate.setDuration(7200);
		exerciseToUpdate.setDistance(1500);
		exerciseToUpdate.setCalories(700);
		exerciseToUpdate.setStartTime(convertDate(date));
		exerciseToUpdate.setType(Enums.ExerciseType.OTHER);
		exerciseToUpdate.setUserId(userId);

		final Exercise updatedExercise = testClientService.updateExercise(exerciseToUpdate);
		assertNotNull(updatedExercise);
		assertThat(updatedExercise.getId(), is(persistedExercise.getId()));
		assertThat(updatedExercise.getDescription(), is("Onsite Interview"));
		assertThat(updatedExercise.getDuration(), is(7200));
		assertThat(updatedExercise.getType(), is(Enums.ExerciseType.OTHER));
		assertThat(updatedExercise.getCalories(), is(700));
		assertThat(updatedExercise.getDistance(), is(1500));
		assertThat(updatedExercise.getUserId(), is(userId));
		assertDate(updatedExercise.getStartTime(), convertDate(date));

		final Exercise selectedExercise = testClientService.getExercise(persistedExercise.getId());
		assertNotNull(selectedExercise);
		assertThat(selectedExercise.getId(), is(persistedExercise.getId()));
		assertThat(selectedExercise.getDescription(), is("Onsite Interview"));
		assertThat(selectedExercise.getDuration(), is(7200));
		assertThat(selectedExercise.getType(), is(Enums.ExerciseType.OTHER));
		assertThat(selectedExercise.getCalories(), is(700));
		assertThat(selectedExercise.getDistance(), is(1500));
		assertThat(selectedExercise.getUserId(), is(userId));
		assertDate(selectedExercise.getStartTime(), convertDate(date));
	}

	@Test
	public void testDelete() {
		final long userId = 12L;
		final String date = "2016-06-15T12:00:00";

		final Exercise exerciseToInsert = new Exercise();
		exerciseToInsert.setDescription("Coding Task");
		exerciseToInsert.setDuration(14400);
		exerciseToInsert.setDistance(0);
		exerciseToInsert.setCalories(500);
		exerciseToInsert.setStartTime(convertDate(date));
		exerciseToInsert.setType(Enums.ExerciseType.OTHER);
		exerciseToInsert.setUserId(userId);

		final Exercise persistedExercise = testClientService.createExercise(exerciseToInsert);
		assertNotNull(persistedExercise);
		assertNotNull(persistedExercise.getId());

		testClientService.deleteExercise(persistedExercise.getId());

		try {
			testClientService.getExercise(persistedExercise.getId());
			fail("'testClientService.getExercise(persistedExercise.getId())' should have thrown an exception.");
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testSelectByDescription() {
		final long userId = 13L;
		final String date = "2016-06-01T14:23:35";

		final Exercise exerciseToInsert = new Exercise();
		exerciseToInsert.setDescription("Coding Task testSelectByDescription");
		exerciseToInsert.setDuration(14400);
		exerciseToInsert.setDistance(0);
		exerciseToInsert.setCalories(500);
		exerciseToInsert.setStartTime(convertDate(date));
		exerciseToInsert.setType(Enums.ExerciseType.OTHER);
		exerciseToInsert.setUserId(userId);

		final Exercise persistedExercise = testClientService.createExercise(exerciseToInsert);
		assertNotNull(persistedExercise);
		assertNotNull(persistedExercise.getId());

		final List<Exercise> selectedExercises = testClientService.getExerciseByDescription("Coding Task testSelectByDescription");
		assertNotNull(selectedExercises);

		assertThat("timestamp", selectedExercises.size(), greaterThanOrEqualTo(1));
		assertThat(selectedExercises, contains(hasProperty("id", is(persistedExercise.getId()))));
	}

	@Test
	public void testSelectByTypeAndDate() {
		final long userId = 14L;
		final String date = "2016-06-19T11:00:00";

		final Exercise exerciseToInsert = new Exercise();
		exerciseToInsert.setDescription("Coding Task");
		exerciseToInsert.setDuration(14400);
		exerciseToInsert.setDistance(0);
		exerciseToInsert.setCalories(500);
		exerciseToInsert.setStartTime(convertDate(date));
		exerciseToInsert.setType(Enums.ExerciseType.OTHER);
		exerciseToInsert.setUserId(userId);

		final Exercise persistedExercise = testClientService.createExercise(exerciseToInsert);
		assertNotNull(persistedExercise);
		assertNotNull(persistedExercise.getId());

		final List<Exercise> selectedExercises = testClientService.getExercises(userId, Enums.ExerciseType.OTHER, "2016-06-19");
		assertNotNull(selectedExercises);
		assertThat(selectedExercises.size(), is(1));

		final Exercise selectedExercise = selectedExercises.get(0);
		assertThat(selectedExercise.getId(), is(persistedExercise.getId()));
		assertThat(selectedExercise.getDescription(), is("Coding Task"));
		assertThat(selectedExercise.getDuration(), is(14400));
		assertThat(selectedExercise.getType(), is(Enums.ExerciseType.OTHER));
		assertThat(selectedExercise.getCalories(), is(500));
		assertThat(selectedExercise.getDistance(), is(0));
		assertThat(selectedExercise.getUserId(), is(userId));
		assertDate(selectedExercise.getStartTime(), convertDate(date));
	}
}
