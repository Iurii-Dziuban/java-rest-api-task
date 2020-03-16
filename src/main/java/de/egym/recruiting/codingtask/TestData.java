package de.egym.recruiting.codingtask;

import java.text.ParseException;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import de.egym.recruiting.codingtask.jpa.dao.ExerciseDao;
import de.egym.recruiting.codingtask.jpa.domain.Enums;
import de.egym.recruiting.codingtask.jpa.domain.Exercise;

@Transactional
public class TestData {

	private static final Logger log = LoggerFactory.getLogger(TestData.class);

	private static final String TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

	private final ExerciseDao exerciseDao;

	@Inject
	public TestData(final ExerciseDao exerciseDao) {
		this.exerciseDao = exerciseDao;

		insertTestData();
	}

	public void insertTestData() {
		// For convenience set the default time zone to UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		// Create the test exercise data for the user accounts
		insertTestExercises();
	}

	private void insertTestExercises() {
		log.debug("Inserting test exercises");

		final Long userId1 = 1L;

		final Exercise exercise11 = new Exercise();
		exercise11.setDescription("eGym");
		exercise11.setType(Enums.ExerciseType.CIRCUIT_TRAINING);
		exercise11.setDuration(1800);
		exercise11.setCalories(220);
		exercise11.setUserId(userId1);
		exercise11.setDistance(250000);
		try {
			exercise11.setStartTime(DateUtils.parseDate("2016-06-02T14:23:35", TIME_PATTERN));
		} catch (ParseException e) {
			// ignoring
		}
		exerciseDao.create(exercise11);

		final Exercise exercise12 = new Exercise();
		exercise12.setDescription("Bike");
		exercise12.setType(Enums.ExerciseType.CYCLING);
		exercise12.setDuration(2800);
		exercise12.setCalories(315);
		exercise12.setDistance(280000);
		exercise12.setUserId(userId1);
		try {
			exercise12.setStartTime(DateUtils.parseDate("2016-06-17T16:33:45", TIME_PATTERN));
		} catch (ParseException e) {
			// ignoring
		}
		exerciseDao.create(exercise12);

		final Long userId2 = 2L;

		final Exercise exercise21 = new Exercise();
		exercise21.setDescription("Running");
		exercise21.setType(Enums.ExerciseType.RUNNING);
		exercise21.setDuration(2000);
		exercise21.setCalories(234);
		exercise21.setDistance(92000);
		exercise21.setUserId(userId2);
		try {
			exercise21.setStartTime(DateUtils.parseDate("2016-05-12T19:13:07", TIME_PATTERN));
		} catch (ParseException e) {
			// ignoring
		}
		exerciseDao.create(exercise21);
	}
}
