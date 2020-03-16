package de.egym.recruiting.codingtask.rest;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.egym.recruiting.codingtask.jpa.dao.ExerciseDao;
import de.egym.recruiting.codingtask.jpa.domain.Enums;
import de.egym.recruiting.codingtask.jpa.domain.Exercise;
import de.egym.recruiting.codingtask.jpa.domain.IdRank;
import de.egym.recruiting.codingtask.rest.exception.ConflictException;
import de.egym.recruiting.codingtask.rest.validation.HasNoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class ExerciseServiceImpl implements ExerciseService {

    private static final Logger log = LoggerFactory.getLogger(ExerciseServiceImpl.class);

    private static final Map<Enums.ExerciseType, Integer> typeToMultiplicationFactor = initializeTypeToMultiplicationFactor();

    private static Map<Enums.ExerciseType, Integer> initializeTypeToMultiplicationFactor() {
        ;
        Map<Enums.ExerciseType, Integer> map = new EnumMap<>(Enums.ExerciseType.class);
        map.put(Enums.ExerciseType.RUNNING, 2);
        map.put(Enums.ExerciseType.CYCLING, 2);
        map.put(Enums.ExerciseType.SWIMMING, 3);
        map.put(Enums.ExerciseType.ROWING, 2);
        map.put(Enums.ExerciseType.WALKING, 1);
        map.put(Enums.ExerciseType.CIRCUIT_TRAINING, 4);
        map.put(Enums.ExerciseType.STRENGTH_TRAINING, 3);
        map.put(Enums.ExerciseType.FITNESS_COURSE, 2);
        map.put(Enums.ExerciseType.SPORTS, 3);
        map.put(Enums.ExerciseType.OTHER, 1);
        return Collections.unmodifiableMap(map);
    }

    private final ExerciseDao exerciseDao;

    @Inject
    ExerciseServiceImpl(final ExerciseDao exerciseDao) {
        this.exerciseDao = exerciseDao;
    }

    @Nonnull
    @Override
    public Exercise getExerciseById(@Nonnull final Long exerciseId) {
        log.debug("Get exercise by id.");

        final Exercise exercise = exerciseDao.findById(exerciseId);
        if (exercise == null) {
            throw new NotFoundException("Exercise with id = " + exerciseId + " could not be found.");
        }

        return exercise;
    }

    @Nonnull
    @Override
    public List<Exercise> getExerciseByDescription(@Nonnull final String description) {
        log.debug("Get exercise by description.");

        return exerciseDao.findByDescription(description);
    }

    @Nonnull
    @Override
    public Exercise create(@Nonnull @Valid @HasNoId Exercise exercise) {
        log.debug("Creating new exercise");
        List<Long> conflictExerciseIds = exerciseDao.findConflictExerciseIds(exercise);
        if (conflictExerciseIds.isEmpty()) {
            return exerciseDao.create(exercise);
        } else {
            throw new ConflictException("Time conflict with other user exercises with ids " + conflictExerciseIds);
        }

    }

    @Nonnull
    @Override
    public Exercise update(@Nonnull Long exerciseId, @Nonnull @Valid @HasNoId Exercise exercise) {
        log.debug("Updating exercise by id.");
        exercise.setId(exerciseId);
        Exercise exerciseById = getExerciseById(exerciseId);
        if (!exercise.getUserId().equals(exerciseById.getUserId())) {
            throw new IllegalStateException("Exercise userId can not be changed from "
                    + exerciseById.getUserId() + " to " + exercise.getUserId());
        }
        if (!exercise.getType().equals(exerciseById.getType())) {
            throw new IllegalStateException("Exercise type can not be changed from "
                    + exerciseById.getType() + " to " + exercise.getType());
        }
        List<Long> conflictExerciseIds = exerciseDao.findConflictExerciseIds(exercise);
        if (conflictExerciseIds.size() > 1 || conflictExerciseIds.size() == 1 && !conflictExerciseIds.contains(exerciseId)) {
            throw new ConflictException("Time conflict with other user exercises with ids " + conflictExerciseIds);
        }
        return exerciseDao.update(exercise);
    }

    @Nonnull
    @Override
    public Response deleteExerciseById(@Nonnull final Long exerciseId) {
        log.debug("Deleting exercise by id.");
        try {
            exerciseDao.deleteById(exerciseId);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Exercise with id = " + exerciseId + " could not be found.");
        }
    }

    @Nonnull
    @Override
    public List<Exercise> getExercises(@Nonnull Long userId, @Nullable Enums.ExerciseType type, @Nullable String date) {
        log.debug("Getting exercises by userId and optional type and date");
        return exerciseDao.findExercises(userId, type, date);
    }

    @Nonnull
    @Override
    public List<Long> getRanking(@Nonnull List<Long> userIds) {
        log.debug("Calculating ranking for the userIds");
        List<Exercise> exercisesToRank = exerciseDao.getRankingExercises(userIds);

        Map<Long, List<Exercise>> userIdExercises = exercisesToRank.stream().collect(Collectors.groupingBy(Exercise::getUserId));
        PriorityQueue<IdRank> idRanks = new PriorityQueue<>();

        for (Long userId : userIds) {
            long sum = 0;
            for (Enums.ExerciseType type : Enums.ExerciseType.values()) {
                List<Exercise> exerciseByUserSpecificType = userIdExercises.get(userId).stream()
                        .filter(exercise -> type.equals(exercise.getType())).limit(10).collect(Collectors.toList());
                int percent = 100;
                for (Exercise exercise : exerciseByUserSpecificType) {
                    sum += percent * typeToMultiplicationFactor.get(type) * (exercise.getDuration() / 60
                            + exercise.getCalories()) / 100;
                    percent -= 10;
                }
            }
            idRanks.add(new IdRank(userId, sum));
        }
        return Stream.generate(idRanks::poll).limit(idRanks.size()).map(idRank -> idRank.id).collect(Collectors.toList());
    }

    @Nonnull
    public List<Long> getRankingWithStreamingApi(@Nonnull List<Long> userIds) {
        log.debug("Calculating ranking for the userIds");
        List<Exercise> exercisesToRank = exerciseDao.getRankingExercises(userIds);

        return userIds.stream().parallel().map(userId -> {
                    long sumByUserId = Arrays.stream(Enums.ExerciseType.values()).parallel()
                            .mapToLong(type -> {
                                 List<Exercise> exerciseByUserAndSpecificType = exercisesToRank.stream()
                                    .filter(exercise -> exercise.getUserId().equals(userId))
                                    .filter(exercise -> exercise.getType().equals(type)).limit(10).collect(Collectors.toList());
                                 int percent = 100;
                                 long sumPerUserAndType = 0L;
                                 for (Exercise exercise : exerciseByUserAndSpecificType) {
                                     sumPerUserAndType += percent * typeToMultiplicationFactor.get(type)
                                             * (exercise.getDuration() / 60 + exercise.getCalories()) / 100;
                                     percent -= 10;
                                 }
                        return sumPerUserAndType;
                    }).sum();
                    return new IdRank(userId, sumByUserId);
                }
            ).sorted().map(idRank -> idRank.id).collect(Collectors.toList());
    }
}
