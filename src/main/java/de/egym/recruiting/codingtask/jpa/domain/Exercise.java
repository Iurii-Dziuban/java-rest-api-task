package de.egym.recruiting.codingtask.jpa.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import javax.annotation.RegEx;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Exercise extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@NotNull
	private Long userId;

	@NotNull
	@Pattern(regexp="[a-zA-Z0-9 ]*", message="Only Alphanumeric and space characters are allowed")
	private String description;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Enums.ExerciseType type;

	/**
	 * format: yyyy-MM-dd'T'HH:mm:ss
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@NotNull
	private Date startTime;

	/**
	 * in seconds
	 */
	@NotNull
	private Integer duration;

	/**
	 * in meters
	 */
	@NotNull
	private Integer distance;

	/**
	 * in kcal
	 */
	@NotNull
	private Integer calories;

	public Integer getCalories() {
		return calories;
	}

	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Enums.ExerciseType getType() {
		return type;
	}

	public void setType(Enums.ExerciseType type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
