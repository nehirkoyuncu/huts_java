package mountainhuts;

public class Municipality {
	private String name;
	private String province;
	private int altitude;
	
	public Municipality(String name, String province, int altitude) {
		this.name = name;
		this.province = province;
		this.altitude = altitude;
	}

	public String getName() {
		return name;
	}

	public String getProvince() {
		return province;
	}

	public int getAltitude() {
		return altitude;
	}
}