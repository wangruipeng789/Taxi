package wrp.taxi.step1;
import java.util.ArrayList;
import java.util.Collections;


public class TaxiUser implements Comparable{
    long  taxiID;
    public TaxiUser(long taxiID) {
		super();
		this.taxiID = taxiID;
	}
	ArrayList<TaxiActivity> tas=new ArrayList<>();
    public void addTaxiActivity(TaxiActivity ta){
    	tas.add(ta);
    }
    public void removeTaxiActivity(TaxiActivity ta){
    	tas.remove(ta);
    }
    public ArrayList<TaxiActivity> getSortedActs(){
		Collections.sort(this.tas);
    	return tas;
    }
    public void clearActs(){//È«²¿Çå³ý
    	this.tas.clear();
    }
	@Override
	public int compareTo(Object o) {
		if(this.taxiID>((TaxiUser)o).taxiID){
			return 1;
		}else if(this.taxiID<((TaxiUser)o).taxiID){
			return -1;
		}else{
			return 0;
		}
	}
}
