package it.polito.po.test;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mountainhuts.MountainHut;
import mountainhuts.Municipality;
import mountainhuts.Region;

import static org.junit.jupiter.api.Assertions.*;

class TestR2_MountainHuts {

	private Region r;

	@BeforeEach
	void setUp() {
		r = new Region("Piemonte");
	}

	@Test
	void testGetMunicipalities() {
		Municipality m1 = r.createOrGetMunicipality("Torino", "TO", 245);
		Municipality m2 = r.createOrGetMunicipality("Cuneo", "CN", 534);

		Collection<Municipality> m = r.getMunicipalities();

		assertNotNull(m, "Missing municipalities");
		assertEquals(2, m.size(), "Wrong number of municipalities");
		assertTrue(m.contains(m1), "Missing municipality Torino");
		assertTrue(m.contains(m2), "Missing municipality Cuneo");
	}

	@Test
	void testGetMountainHut() {
		Municipality m = r.createOrGetMunicipality("Torino", "TO", 245);
		MountainHut h1 = r.createOrGetMountainHut("Alpe", "Rifugio", 10, m);
		MountainHut h2 = r.createOrGetMountainHut("Tappa", "Bivacco", 0, m);

		Collection<MountainHut> h = r.getMountainHuts();

		assertNotNull(h, "Missing mountain huts");
		assertEquals(2, h.size(), "Wrong number of mountain huts");
		assertTrue(h.contains(h1), "Missing mountain hut Alpe");
		assertTrue(h.contains(h2), "Missing mountain hut Tappa");
	}

	@Test
	void testCreateOrGetMunicipality() {
		Municipality m1 = r.createOrGetMunicipality("Torino", "TO", 245);
		Municipality m2 = r.createOrGetMunicipality("Torino", "TO", 245);

		assertNotNull(m1, "Missing municipality");
		assertSame(m1, m2, "Duplicate municipality");
		assertEquals("Torino", m1.getName(), "Wrong municipality name");
		assertEquals("TO", m1.getProvince(), "Wrong municipality province");
		assertEquals(Integer.valueOf(245), m1.getAltitude(), "Wrong municipality altitude");
	}

	@Test
	void testCreateOrGetMountainHut() {
		Municipality m = r.createOrGetMunicipality("Torino", "TO", 245);
		MountainHut h1 = r.createOrGetMountainHut("Alpe", "Rifugio", 10, m);
		MountainHut h2 = r.createOrGetMountainHut("Alpe", "Rifugio", 10, m);

		assertNotNull(h1, "Missing mountain hut");
		assertSame(h1, h2, "Duplicate mountain hut");
		assertEquals("Alpe", h1.getName(), "Wrong mountain hut name");
		assertEquals("Rifugio", h1.getCategory(), "Wrong mountain hut category");
		assertEquals(Optional.empty(), h1.getAltitude(), "Wrong mountain hut altitude");
		assertEquals(Integer.valueOf(10), h1.getBedsNumber(), "Wrong mountain hut beds number");
		assertEquals(m, h1.getMunicipality(), "Wrong mountain municipality");

		MountainHut h3 = r.createOrGetMountainHut("Tappa", 1250, "Bivacco", 0, m);
		assertEquals(Integer.valueOf(1250), h3.getAltitude().orElse(-1), "Wrong mountain hut altitude");
	}

}
