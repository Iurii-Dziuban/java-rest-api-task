package de.egym.recruiting.codingtask.jpa.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import de.egym.recruiting.codingtask.jpa.domain.Enums;
import de.egym.recruiting.codingtask.jpa.domain.Exercise;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Transactional
public class ExerciseDaoImpl extends AbstractBaseDao<Exercise> implements ExerciseDao {

    private static final int MILLIS_IN_SECOND = 1000;

    @Inject
    ExerciseDaoImpl(final Provider<EntityManager> entityManagerProvider) {
        super(entityManagerProvider, Exercise.class);
    }

    @Nonnull
    @Override
    public List<Exercise> findByDescription(@Nullable String description) {
        if (description == null) {
            return Collections.emptyList();
        }

        description = description.toLowerCase();

        try {
            return getEntityManager()
                    .createQuery("SELECT e FROM Exercise e WHERE LOWER(e.description) = :description")
                    .setParameter("description", description)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Nonnull
    @Override
    public List<Long> findConflictExerciseIds(@Nullable Exercise exercise) {
        Date tillTime = new Date(exercise.getStartTime().getTime() + exercise.getDuration() * MILLIS_IN_SECOND);
        try {
            return getEntityManager()
                    .createQuery("SELECT e.id FROM Exercise e WHERE e.userId = :userId" +
                            " AND e.startTime < :tillTime AND " +
                            "DATEADD('ss', e.duration, e.startTime) > :startTime")
                    .setParameter("userId", exercise.getUserId())
                    .setParameter("startTime", exercise.getStartTime())
                    .setParameter("tillTime", tillTime)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Nonnull
    @Override
    public List<Exercise> findExercises(@Nullable Long userId, @Nullable Enums.ExerciseType type, @Nullable String date) {
        String query = "SELECT e FROM Exercise e WHERE e.userId = :userId";
        if (type != null) {
            query += " AND e.type = :type";
        }
        Date dateToSearch = null;
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                dateToSearch = simpleDateFormat.parse(date);
            } catch (ParseException e) {
                throw new IllegalArgumentException("date should have format yyyy-MM-dd");
            }
            query += " AND e.startTime >= :date AND e.startTime < DATEADD('dd', 1, :date)";
        }
        try {
            Query queryForSearch = getEntityManager()
                    .createQuery(query);
            queryForSearch = queryForSearch.setParameter("userId", userId);
            if (dateToSearch != null) {
                queryForSearch = queryForSearch.setParameter("date", dateToSearch);
            }
            if (type != null) {
                queryForSearch = queryForSearch.setParameter("type", type);
            }
            return queryForSearch.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Nonnull
    @Override
    public List<Exercise> getRankingExercises(@Nonnull List<Long> userIds) {
        Date tillTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tillTime);
        calendar.add(Calendar.WEEK_OF_MONTH, -4);
        Date fourWeeksAgoDate = calendar.getTime();
        try {
            return getEntityManager()
                    .createQuery("SELECT e FROM Exercise e WHERE e.userId in :userIds" +
                            " AND e.startTime >= :startTime AND e.startTime <= :tillTime ORDER BY e.startTime desc")
                    .setParameter("userIds", userIds)
                    .setParameter("tillTime", tillTime)
                    .setParameter("startTime", fourWeeksAgoDate)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }


}
