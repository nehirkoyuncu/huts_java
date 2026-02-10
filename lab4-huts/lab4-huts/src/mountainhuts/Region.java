package mountainhuts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Region {
	
	private String name;
	private Map<String, Municipality> municipalities = new HashMap<>();
	private Map<String, MountainHut> mountainHuts = new HashMap<>();
	private List<String> altitudeRanges = new ArrayList<>();
	
	private static class Range {
		int min;
		int max;
		String definition;
		public Range(int min, int max, String definition) {
			this.min = min;
			this.max = max;
			this.definition = definition;
		}
	}
	private List<Range> parsedRanges = new ArrayList<>();

	public Region(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setAltitudeRanges(String... ranges) {
		altitudeRanges.clear();
		parsedRanges.clear();
		
		Pattern p = Pattern.compile("(\\d+)-(\\d+)");
		
		for(String rangeDef : ranges) {
			altitudeRanges.add(rangeDef);
			Matcher m = p.matcher(rangeDef);
			if (m.matches()) {
				int min = Integer.parseInt(m.group(1));
				int max = Integer.parseInt(m.group(2));
				parsedRanges.add(new Range(min, max, rangeDef));
			}
		}
	}

	public String getAltitudeRange(int altitude) {
		for (Range r : parsedRanges) {
			if (altitude > r.min && altitude <= r.max) {
				return r.definition;
			}
		}
		return "0-INF";
	}
	
	public Municipality createOrGetMunicipality(String name, String province, Integer altitude) {
		if (municipalities.containsKey(name)) {
			return municipalities.get(name);
		}
		
		Municipality m = new Municipality(name, province, altitude);
		municipalities.put(name, m);
		return m;
	}

	public Collection<Municipality> getMunicipalities() {
		return municipalities.values();
	}

	public MountainHut createOrGetMountainHut(String name, Integer altitude, String category, int beds,
			Municipality municipality) {
		if (mountainHuts.containsKey(name)) {
			return mountainHuts.get(name);
		}

		MountainHut h;
		if (altitude == null) {
			h = new MountainHut(name, category, beds, municipality);
		} else {
			h = new MountainHut(name, altitude, category, beds, municipality);
		}
		
		mountainHuts.put(name, h);
		return h;
	}

	public MountainHut createOrGetMountainHut(String name, String category, int beds, Municipality municipality) {
		return createOrGetMountainHut(name, null, category, beds, municipality);
	}

	public Collection<MountainHut> getMountainHuts() {
		return mountainHuts.values();
	}
	
	public static Region fromFile(String regionName, String file) {
		Region region = new Region(regionName);
		
		List<String> data = readData(file);

		data.stream()
			.skip(1)
			.map(line -> line.split(";")) 
			.forEach(parts -> {
				String province = parts[0].trim();
				String municipalityName = parts[1].trim();
				int municipalityAltitude = Integer.parseInt(parts[2].trim());
				String hutName = parts[3].trim();
				String hutAltitudeStr = parts[4].trim();
				String category = parts[5].trim();
				int bedsNumber = Integer.parseInt(parts[6].trim());
				Municipality m = region.createOrGetMunicipality(municipalityName, province, municipalityAltitude);
				Integer hutAltitude = hutAltitudeStr.isEmpty() ? null : Integer.parseInt(hutAltitudeStr);
				region.createOrGetMountainHut(hutName, hutAltitude, category, bedsNumber, m);
			});

		return region;
	}

	public static List<String> readData(String file) {
		List<String> result = new ArrayList<>();
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			String line = null;
			while ((line = in.readLine()) != null) {
				result.add(line);
			}
		} catch (IOException e) {
			System.err.println("Problem reading from file: " + file);
			e.printStackTrace();
		}
		return result;
	}
	
	public Map<String, Long> countMunicipalitiesPerProvince() {
		return municipalities.values().stream()
				.collect(Collectors.groupingBy(
						Municipality::getProvince,
						Collectors.counting()
				));
	}

	public Map<String, Map<String, Long>> countMountainHutsPerMunicipalityPerProvince() {
		return mountainHuts.values().stream()
				.collect(Collectors.groupingBy(
						h -> h.getMunicipality().getProvince(),
						Collectors.groupingBy(
								h -> h.getMunicipality().getName(), 
								Collectors.counting() 
						)
				));
	}

	public Map<String, Long> countMountainHutsPerAltitudeRange() {
		return mountainHuts.values().stream()
				.collect(Collectors.groupingBy(
						h -> {
							int effectiveAltitude = h.getAltitude().orElse(h.getMunicipality().getAltitude());
							return getAltitudeRange(effectiveAltitude);
						},
						Collectors.counting()
				));
	}

	public Map<String, Integer> totalBedsNumberPerProvince() {
		return mountainHuts.values().stream()
				.collect(Collectors.groupingBy(
						h -> h.getMunicipality().getProvince(),
						Collectors.summingInt(MountainHut::getBedsNumber)
				));
	}

	public Map<String, Optional<Integer>> maximumBedsNumberPerAltitudeRange() {
		Map<String, List<MountainHut>> hutsPerRange = mountainHuts.values().stream()
				.collect(Collectors.groupingBy(
						h -> {
							int effectiveAltitude = h.getAltitude().orElse(h.getMunicipality().getAltitude());
							return getAltitudeRange(effectiveAltitude);
						}
				));
		return hutsPerRange.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						e -> e.getValue().stream()
							.map(MountainHut::getBedsNumber)
							.max(Comparator.naturalOrder())
				));
	}
	
	public Map<Long, List<String>> municipalityNamesPerCountOfMountainHuts() {
		Map<Municipality, Long> hutsCountPerMunicipality = mountainHuts.values().stream()
				.collect(Collectors.groupingBy(
						MountainHut::getMunicipality,
						Collectors.counting()
				));
		return hutsCountPerMunicipality.entrySet().stream()
				.collect(Collectors.groupingBy(
						Map.Entry::getValue, 
						Collectors.mapping(
								e -> e.getKey().getName(),
								Collectors.collectingAndThen(
									Collectors.toList(),
									l -> l.stream().sorted().collect(Collectors.toList()) 
								)
						)
				));
	}

}