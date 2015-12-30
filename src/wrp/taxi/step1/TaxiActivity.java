package wrp.taxi.step1;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TaxiActivity implements Comparable{
	public SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String actLng;
	public String actLat;
	public String actSpeed;
	public String actDirect;
	public String actStatus;
	public String actTime;
	public TaxiActivity(String actLng, String actLat, String actSpeed,
			String actDirect, String actStatus, String actTime) {
		super();
		this.actLng = actLng;
		this.actLat = actLat;
		this.actSpeed = actSpeed;
		this.actDirect = actDirect;
		this.actStatus = actStatus;
		this.actTime = actTime;
	}
	@Override
	public int compareTo(Object o) {
		long t1 = 0;
		long t2 = 0;
		Date d1 = null;
		try {
			d1 = sdf.parse(this.actTime);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		t1=d1.getTime();
		Date d2 = null;
		try {
			d2 = sdf.parse(((TaxiActivity)o).actTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		t2=d2.getTime();
		if(t1>t2){
			return 1;
		}else if(t1==t2){
			return 0;
		}else{
			return -1;
		}
	}
   
}
