package de.egym.recruiting.codingtask;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class AbstractIntegrationTest {

	private static final Logger log = LoggerFactory.getLogger(AbstractIntegrationTest.class);

	protected Injector injector = Guice.createInjector(new RootModule());

	@Before
	public void setupInjector() {
		log.debug("Injecting test members.");
		injector.injectMembers(this);
	}

	/**
	 * Converts a string (format: "yyyy-MM-dd HH:mm:ss") into a timestamp.
	 *
	 * @param dateString
	 *            to convert
	 * @return converted date string
	 */
	@Nullable
	protected Date convertDate(@Nonnull final String dateString) {
		try {
			return DateUtils.parseDate(dateString, "yyyy-MM-dd'T'HH:mm:ss");
		} catch (ParseException e) {
			fail("Wrong format." + e.getMessage());
		}
		return null;
	}

	/**
	 * Asserts to dates.
	 *
	 * @param actual
	 *            date
	 * @param expected
	 *            date
	 */
	protected void assertDate(@Nullable final Date actual, @Nullable final Date expected) {
		final Long actualTimestamp = actual != null ? actual.getTime() : null;
		final Long expectedTimestamp = expected != null ? expected.getTime() : null;

		assertThat("Wrong date.", actualTimestamp, is(expectedTimestamp));
	}
}
