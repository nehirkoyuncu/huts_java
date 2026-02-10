package it.polito.po.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static it.polito.po.test.CollectionsAssertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mountainhuts.MountainHut;
import mountainhuts.Municipality;
import mountainhuts.Region;

class TestR3_ReadData {

	static String file;

	@BeforeAll
	static void createFile(){
		File outFile;

		try {
			outFile = File.createTempFile("mountain_huts", ".csv");
			outFile.deleteOnExit();
			System.out.println("Extracting data file: " + outFile.getAbsolutePath());
			URL resource = TestR3_ReadData.class.getResource("/it/polito/po/test/mountain_huts.csv");
			System.out.println("from: " + resource.toString());
			try (InputStream in = resource.openStream(); 
				FileOutputStream out = new FileOutputStream(outFile)) {
				byte[] b = new byte[2048];
				int n;
				while ((n = in.read(b)) != -1) {
					out.write(b, 0, n);
				}
				file = outFile.getCanonicalPath();
			}
		} catch (IOException e) {
			file = null;
			System.err.println(e.getMessage());
		}
	}

	private Region r;

	@BeforeEach
	void setUp() {
		assertNotNull(file, "Could not create temporary file");
		r = Region.fromFile("Piemonte", file);
	}

	@Test
	void testReadMunicipalities() {
		assertNotNull(r, "Missing region");

		Collection<Municipality> municipalities = r.getMunicipalities();
		assertNotNull(municipalities, "Missing municipalities");
		assertEquals(94, municipalities.size(), "Wrong number of municipalities");

		Map<String, Long> provinces = municipalities.stream()
				.collect(Collectors.groupingBy(Municipality::getProvince, Collectors.counting()));
		
		assertMapContains("Wrong number of municipalities in province ", provinces,
				entry("ALESSANDRIA",1L),
				entry( "CUNEO",25L),
				entry( "TORINO", 24L),
				entry( "VERCELLI", 13L),
				entry("BIELLA", 12L),
				entry("VERBANO-CUSIO-OSSOLA",19L));
	}

	@Test
	void testReadMountainHuts() {
		assertNotNull(r, "Missing region");

		Collection<MountainHut> mountainHuts = r.getMountainHuts();
		assertNotNull(mountainHuts, "Missing mountain huts");
		assertEquals(167, mountainHuts.size(), "Wrong number of mountain huts");

		Map<String, Long> categories = mountainHuts.stream()
				.collect(Collectors.groupingBy(MountainHut::getCategory, Collectors.counting()));
		
		assertMapContains("Wrong number of huts per category ", categories,
				entry("Bivacco Fisso", 27L),
				entry("Rifugio Alpino", 91L),
				entry("Rifugio Escursionistico", 33L),
				entry("Rifugio non gestito", 16L)
				);
	}

}
