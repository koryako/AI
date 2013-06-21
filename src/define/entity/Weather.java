package define.entity;

public class Weather {
	String city;
	String temp1;
	String temp2;
	String weather;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return city + ",ÎÂ¶È£º" + temp1 + "-" + temp2 + "," + weather;
	}
}
