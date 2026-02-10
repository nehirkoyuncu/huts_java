package mountainhuts;

import java.util.Optional;

public class MountainHut {
	private String name;
	private Optional<Integer> altitude;
	private String category;
	private int bedsNumber;
	private Municipality municipality;
	
	public MountainHut(String name, Optional<Integer> altitude, String category, int bedsNumber,
			Municipality municipality) {
		this.name = name;
		this.altitude = altitude;
		this.category = category;
		this.bedsNumber = bedsNumber;
		this.municipality = municipality;
	}
	
	public MountainHut(String name, String category, int bedsNumber, Municipality municipality) {
		this(name, Optional.empty(), category, bedsNumber, municipality);
	}

	public MountainHut(String name, int altitude, String category, int bedsNumber, Municipality municipality) {
		this(name, Optional.of(altitude), category, bedsNumber, municipality);
	}

	public String getName() {
		return name;
	}

	public Optional<Integer> getAltitude() {
		return altitude;
	}

	public String getCategory() {
		return category;
	}

	public int getBedsNumber() {
		return bedsNumber;
	}

	public Municipality getMunicipality() {
		return municipality;
	}
}