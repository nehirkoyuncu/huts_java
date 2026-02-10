package it.polito.po.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mountainhuts.Region;

class TestR1_AltitudeRanges {

	private Region r;

	@BeforeEach
	void setUp() {
		r = new Region("Piemonte");
	}

	@Test
	void testGetName() {
		assertNotNull(r.getName(), "Missing region name");
		assertEquals("Piemonte", r.getName(), "Wrong region name");
	}

	@Test
	void testGetAltitudeRangeDefault() {
		assertNotNull(r.getAltitudeRange(0), "Missing range name");
		assertEquals("0-INF", r.getAltitudeRange(0), "Wrong empty range name");
	}

	@Test
	void testGetAltitudeRange() {
		r.setAltitudeRanges("0-1000", "1000-2000", "2000-3000");
		assertEquals("1000-2000", r.getAltitudeRange(1001), "Wrong left value in range");
		assertEquals("1000-2000", r.getAltitudeRange(2000), "Wrong right value in range");
		assertEquals("2000-3000", r.getAltitudeRange(2500), "Wrong middle value in range");
		assertEquals("0-INF", r.getAltitudeRange(3001), "Wrong default range name");
	}

}
