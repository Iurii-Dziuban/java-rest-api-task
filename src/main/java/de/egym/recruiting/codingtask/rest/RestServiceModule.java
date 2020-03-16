package de.egym.recruiting.codingtask.rest;

import com.google.inject.AbstractModule;

public class RestServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ExerciseService.class).to(ExerciseServiceImpl.class);
		bind(ObjectMapperProvider.class);
		bind(ValidationExceptionMapper.class);
		bind(RestExceptionMapper.class);
	}
}
