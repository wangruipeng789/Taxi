package wrp.taxi.step2;

public class OD {
    int id;
    int maxID=0;
    long taxiID;
    ChangePoint op;
    ChangePoint dp;
	public OD(long taxiID, ChangePoint op, ChangePoint dp) {
		super();
		this.id = (maxID++);
		this.taxiID = taxiID;
		this.op = op;
		this.dp = dp;
	}
    
}
