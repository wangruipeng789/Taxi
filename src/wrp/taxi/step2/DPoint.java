package wrp.taxi.step2;

public class DPoint {
     double lng;
     double lat;
     String time;
     String flag;//用来区别
	 public DPoint(double lng, double lat, String time) {
		super();
		this.lng  = lng;
		this.lat  = lat;
		this.time = time;
		this.flag ="D";
	 }
}
