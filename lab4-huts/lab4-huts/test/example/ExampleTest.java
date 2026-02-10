package example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import mountainhuts.*;
import java.util.Collection;

class ExampleTest {

	@Test
	void testR1() {
		Region r = new Region("Piemonte");
		
		assertEquals("Piemonte", r.getName(), "Missing region name");
		assertEquals("0-INF", r.getAltitudeRange(1), "Missing range name");
		
		r.setAltitudeRanges("0-1000", "1000-2000", "2000-3000");
		assertEquals("1000-2000", r.getAltitudeRange(1500), "Wrong value in range");
	}

	@Test
	void testR2() {
		Region r = new Region("Piemonte");
		
		Municipality acceglio = r.createOrGetMunicipality("Acceglio", "Cuneo", 1200);
		assertNotNull(acceglio, "Missing municipality");
		assertEquals("Acceglio", acceglio.getName(), "Wrong municipality name");
		assertEquals("Cuneo", acceglio.getProvince(), "Wrong province province");

		r.createOrGetMunicipality("Bobbio Pellice", "Torino", 732);
		
		Collection<Municipality> ms = r.getMunicipalities();
		
		assertNotNull(ms, "Missing municipalities");
		assertEquals(2, ms.size(), "Wrong number of municipalities");
		
		MountainHut h = r.createOrGetMountainHut("Campo Base", 1660, "Rifugio Escursionistico",
										   32, acceglio);
		
		assertNotNull(h, "Missing hut");
		assertEquals("Campo Base", h.getName(), "Wrong hut name");
		assertSame(acceglio, h.getMunicipality(), "Wrong hut municipality");

		Collection<MountainHut> hs = r.getMountainHuts();
		
		assertNotNull(hs, "Missing huts");
		assertEquals(1, hs.size(), "Wrong number of huts");
	}


	@Test
	void testR3() {
		Region r = Region.fromFile("Piemonte", "data/mountain_huts.csv");

		assertNotNull(r, "No region from file");

		Collection<Municipality> municipalities = r.getMunicipalities();
		assertNotNull(municipalities, "Missing municipalities");
		assertEquals(94, municipalities.size(), "Wrong number of municipalities");

		Collection<MountainHut> mountainHuts = r.getMountainHuts();
		assertNotNull(mountainHuts, "Missing mountain huts");
		assertEquals(167, mountainHuts.size(), "Wrong number of mountain huts");
	}

}
