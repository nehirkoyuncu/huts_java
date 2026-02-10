package example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import mountainhuts.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class ExampleQueriesTest {

	private Region r;
	
	@BeforeEach
	void setUp() {
		r = Region.fromFile("Piemonte", "data/mountain_huts.csv");
		assertNotNull(r,"Cannot set up region from file");
		r.setAltitudeRanges("0-1000", "1000-2000", "2000-3000");
	}
	
	@Test
	void testR4() {

		Map<String, Long> res1 = r.countMunicipalitiesPerProvince();
		assertNotNull(res1, "Missing count of municipalities per province");
		assertEquals(Long.valueOf(24), res1.get("TORINO"), "Wrong number of municipalities in province of Torino");

		Map<String, Map<String, Long>> res2 = r.countMountainHutsPerMunicipalityPerProvince();
		assertNotNull(res2, "Missing count of mountain huts per municipality per province");
		Map<String, Long> resTo = res2.get("TORINO");
		assertNotNull(resTo, "Missing count of mountain huts in province of Torino");

		Map<String, Long> res3 = r.countMountainHutsPerAltitudeRange();
		assertNotNull(res3, "Missing count of mountain huts per altitude range");
		assertEquals(Long.valueOf(22), res3.get("0-1000"), "Wrong number of mountain huts in altitude range 0-1000");

		Map<String, Integer> res4 = r.totalBedsNumberPerProvince();
		assertNotNull(res4, "Missing total beds number per province");
		assertEquals(Integer.valueOf(953), res4.get("TORINO"), "Wrong number of beds number in province of Torino");

		Map<String, Optional<Integer>> res5 = r.maximumBedsNumberPerAltitudeRange();
		assertNotNull(res5, "Missing maximum beds number per altitude range");
		assertEquals(Integer.valueOf(27),
				res5.get("0-1000").orElse(-1), "Wrong number of maximum beds number in altitude range 0-1000");

		Map<Long, List<String>> res6 = r.municipalityNamesPerCountOfMountainHuts();
		assertNotNull(res6, "Missing set of municipality names per count of mountain huts");
		assertEquals(61, res6.get(1L).size(), "Wrong number of municipalities per count 1");
	}
}
