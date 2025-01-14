package de.beuth.knabe.spring_ddd_bank.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

/** Test driver for the entity object {@link Account} */
public class AccountNoTest {

	@Before
	public void setUp() {
		Locale.setDefault(Locale.GERMANY);
	}

	@Test
	public void constructExtract() {
		{
			final AccountNo result = new AccountNo(0L);
			assertEquals(0L, result.toLong());
			assertEquals("0", result.toString());
		}
		{
			final AccountNo result = new AccountNo(Long.MAX_VALUE);
			assertEquals(Long.MAX_VALUE, result.toLong());
			assertEquals(Long.toString(Long.MAX_VALUE), result.toString());
		}
	}

	@Test
	public void constructIllegals() {
		try {
			new AccountNo((Long)null);
			fail("AccountNo.IllegalExc expected");
		} catch (AccountNo.IllegalExc expected) {
		}
		try {
			new AccountNo((String)null);
			fail("AccountNo.IllegalExc expected");
		} catch (AccountNo.IllegalExc expected) {
		}
		try {
			new AccountNo("");
			fail("AccountNo.IllegalExc expected");
		} catch (AccountNo.IllegalExc expected) {
		}
		try {
			new AccountNo("A");
			fail("AccountNo.IllegalExc expected");
		} catch (AccountNo.IllegalExc expected) {
		}
		try {
			new AccountNo(".");
			fail("AccountNo.IllegalExc expected");
		} catch (AccountNo.IllegalExc expected) {
		}
	}
	
}
