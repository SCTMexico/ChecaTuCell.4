package gob.sct.checatucell.utils;

public class Point {

	private int intensity;
	private String lat;
	private String lon;
	private String alt;
	private String id;
	private String bandWidth;
	private int connectionType;

	
	public int getIntensity() {
		return intensity;
	}
	public void setIntensity(String intensity) {
		this.intensity = (int)Double.parseDouble(intensity);
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public double getLat() {
		return Double.parseDouble(lat);
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public double getLon() {
		return Double.parseDouble(lon);
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public int getId() {
		return (int)Double.parseDouble(id);
		
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public int getBandWidth() {
		return (int)Double.parseDouble(bandWidth);
		
	}
	public void setBandWidth(String bandWidth) {
		this.bandWidth = bandWidth;
	}
	
	public int getConnectionType() {
		return connectionType;
		
	}
	public void setConnectionType(String connectionType) {
		this.connectionType = (int)Double.parseDouble(connectionType);
	}
}
