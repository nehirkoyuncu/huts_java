package it.polito.po.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static it.polito.po.test.CollectionsAssertions.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mountainhuts.Region;

class TestR4_Queries {

	private Region r;

	@BeforeAll
	static void createFile(){
		TestR3_ReadData.createFile();
	}

	@BeforeEach
	void setUp() {
		r = Region.fromFile("Piemonte", TestR3_ReadData.file);
	}
	
	@Test
	void testCountMunicipalitiesPerProvince() {
		assertNotNull(r, "Missing region");

		Map<String, Long> res = r.countMunicipalitiesPerProvince();
		
		assertNotNull(res, "Missing count of municipalities per province");

		assertMapContains("Wrong number of municipalities",res,
							entry("ALESSANDRIA",1L),
							entry("CUNEO", 25L),
							entry("TORINO", 24L),
							entry("VERCELLI", 13L),
							entry("BIELLA", 12L),
							entry("VERBANO-CUSIO-OSSOLA",19L)
							);
	}
	
	@Test
	void testCountMountainHutsPerMunicipalityPerProvince() {
		assertNotNull(r, "Missing region");

		Map<String, Map<String, Long>> res = r.countMountainHutsPerMunicipalityPerProvince();

		assertNotNull(res, "Missing count of mountain huts per municipality per province");
		
		Map<String, Long> resTo = res.get("TORINO");
		assertNotNull(resTo, "Missing count of mountain huts in province of Torino");
		assertEquals(24, resTo.size(), "Wrong number of municipalities in province of Torino");
		assertNotNull(resTo.get("BUSSOLENO"), "Missing count of mountain huts in municipality of Bussoleno");
		assertEquals(Long.valueOf(2), resTo.get("BUSSOLENO"), "Wrong number of mountain huts in municipality of Bussoleno");
		
		Map<String, Long> resAl = res.get("ALESSANDRIA");
		assertNotNull(resAl, "Missing count of mountain huts in province of Alessandria");
		assertEquals(1, resAl.size(), "Wrong number of municipalities in province of Alessandria");
		assertNotNull(resAl.get("BOSIO"), "Missing count of mountain huts in municipality of Bosio");
		assertEquals(Long.valueOf(1), resAl.get("BOSIO"), "Wrong number of mountain huts in municipality of Bosio");
	}
	
	@Test
	void testCountMountainHutsPerAltitudeRange() {
		assertNotNull(r, "Missing region");

		r.setAltitudeRanges("0-1000", "1000-1500", "1500-2000","2000-3000","3000-4000","4000-5000");
		Map<String, Long> res = r.countMountainHutsPerAltitudeRange();
		
		assertNotNull(res, "Missing count of mountain huts per altitude range");

		assertMapContains("Wrong number of mountain huts in altitude range ", res,
				entry("0-1000", 22L), 
				entry("1000-1500", 36L),  
				entry("1500-2000", 52L),  
				entry("2000-3000", 51L),
				entry("3000-4000", 4L),
				entry("4000-5000",2L));
	}
	
	@Test
	void testTotalBedsNumberPerProvince() {
		assertNotNull(r, "Missing region");

		Map<String, Integer> res = r.totalBedsNumberPerProvince();

		assertNotNull(res, "Missing total beds number per province");
		
		assertMapContains("Wrong number of beds number in province ", res,
				entry("ALESSANDRIA",10),
				entry("CUNEO", 1046),
				entry("TORINO", 953),
				entry("VERCELLI", 534),
				entry("BIELLA", 237),
				entry("VERBANO-CUSIO-OSSOLA",852)
				);
	}
	
	@Test
	void testMaximumBedsNumberPerAltitudeRange() {
		assertNotNull(r, "Missing region");

		r.setAltitudeRanges("0-1000", "1000-2000", "2000-3000", "3000-4000", "4000-5000");
		Map<String, Optional<Integer>> res = r.maximumBedsNumberPerAltitudeRange();
		
		assertNotNull(res, "Missing maximum beds number per altitude range");
		
		assertMapContains("Wrong number of maximum beds number in altitude range ", res,
				entry("0-1000", Optional.of(27)), 
				entry("1000-2000", Optional.of(95)),  
				entry("2000-3000", Optional.of(96)),
				entry("3000-4000", Optional.of(16)),
				entry("4000-5000", Optional.of(70))
				);
	}
	
	@Test
	void testMunicipalityNamesPerCountOfMountainHuts() {
		assertNotNull(r, "Missing region");

		Map<Long, List<String>> res = r.municipalityNamesPerCountOfMountainHuts();
		
		assertNotNull(res, "Missing set of municipality names per count of mountain huts");
		
		assertMapContains("Wrong number of municipality names per count of mountain huts ", res,
				entry(1L, List::size, 61), 
				entry(2L, List::size, 15), 
				entry(3L, List::size, 11), 
				entry(4L, List::size, 3), 
				entry(5L, List::size, 2), 
				entry(10L, List::size, 1), 
				entry(11L, List::size, 1) 
		);


		assertMapContains("Wrong first municipality name per count of mountain huts ", res,
				entry(1L, l->l.get(0), "ANDORNO MICCA"),
				entry(1L, l->l.get(0), "ANDORNO MICCA"),
				entry(2L, l->l.get(0), "ARGENTERA"),
				entry(3L, l->l.get(0), "ACCEGLIO"),
				entry(4L, l->l.get(0), "BOGNANCO"),
				entry(5L, l->l.get(0), "ENTRACQUE"),
				entry(10L, l->l.get(0), "MACUGNAGA"),
				entry(11L, l->l.get(0), "ALAGNA VALSESIA")
		);
	}

}
