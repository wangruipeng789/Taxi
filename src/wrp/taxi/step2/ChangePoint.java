package wrp.taxi.step2;

public class ChangePoint {
	double lng;
    double lat;
    String time;
    String flag;//用来区别
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public ChangePoint(double lng, double lat, String time) {
		super();
		this.lng  = lng;
		this.lat  = lat;
		this.time = time;
    }
	
}
