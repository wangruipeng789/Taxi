package wrp.taxi.step2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import wrp.taxi.step1.TaxiActivity;
import wrp.taxi.step1.TaxiUser;

public class ODprocessing {
	ArrayList<ChangePoint>  cpoints=new ArrayList<ChangePoint>();
    public static void main(String[] args){
    	ODprocessing odp=new ODprocessing();
    	for(int i=25;i<=31;i++){
    		odp.process(i);
    	}
    }
	private void process(int day) {
		String path="D:\\Fdisk\\taxi\\201312"+day+"_split\\";
		for(int j=0;j<=9;j++){
			try {
				BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path+"last_"+j+"_.csv")));
				TaxiUser  taxiUser;
				TaxiActivity ta;
				Hashtable<Long,TaxiUser> taxiUsers=new Hashtable<>();
				String recValues[];
				long   taxiID;
				String actLng;
				String actLat;
				String actSpeed;
				String actDirect;
				String actStatus;
				String actTime;
				for(String line=br.readLine();line!=null;line=br.readLine()){
					recValues=line.split(",");
					taxiID=Long.valueOf(recValues[0]);
					if((taxiUser=taxiUsers.get(taxiID))== null){
						taxiUser=new TaxiUser(taxiID);
						taxiUsers.put(taxiID, taxiUser);
					}//否则taxiUser就不为空
					actLng   =recValues[1];//经度
			    	actLat   =recValues[2];//维度
			    	actSpeed =recValues[3];//速度
			    	actDirect=recValues[4];//方向
			    	actStatus=recValues[5];//状态
			    	actTime  =recValues[6];//时间
			    	ta=new TaxiActivity(actLng, actLat, actSpeed, actDirect, actStatus, actTime);
			    	taxiUser.addTaxiActivity(ta);
				}
				br.close();
				//
				findODPoint(day,taxiUsers);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    
	}
	private void findODPoint(int day, Hashtable<Long, TaxiUser> taxiUsers) {
		TaxiUser  taxiUser;
		String recValues[];
		long   taxiID;
		String actLng1;
		String actLng2;
		String actLat1;
		String actLat2;
		String actSpeed1;
		String actSpeed2;
		String actDirect1;
		String actDirect2;
		String actStatus1;
		String actStatus2;//前后状态
		String actTime1;
		String actTime2;
		Enumeration<Long> taxiIDs=taxiUsers.keys();
		ArrayList<TaxiActivity>  tas=new ArrayList<TaxiActivity>();
		while(taxiIDs.hasMoreElements()){
		    taxiID=taxiIDs.nextElement();//针对同一个用户
		    taxiUser=taxiUsers.get(taxiID);
		    tas=taxiUser.getSortedActs();
		    for(int i=0;i<tas.size();i++){//同1个用户
		    	if(tas.size()<=1)continue;//活动数量>=2才能继续
		    	if(i==(tas.size()-1))break;
		        actStatus1=tas.get(i).actStatus;
		        actStatus2=tas.get(i+1).actStatus;
		        actLng1=tas.get(i).actLng;
		        actLng2=tas.get(i+1).actLng;
		        actLat1=tas.get(i).actLat;
		        actLat2=tas.get(i+1).actLat;
		        actTime1=tas.get(i).actTime;
		        actTime2=tas.get(i+1).actTime;
	            if(actStatus1.equals("1")&&actStatus2.equals("0")){
	            	ChangePoint cp=new ChangePoint(Double.valueOf(actLng2),Double.valueOf(actLat2),actTime2);
	            	cp.setFlag("D");//下车点
	            	cpoints.add(cp);
	            }else if(actStatus1.equals("0")&&actStatus2.equals("1")){
	            	ChangePoint cp=new ChangePoint(Double.valueOf(actLng2),Double.valueOf(actLat2),actTime2);
	            	cp.setFlag("O");//上车点
	            	cpoints.add(cp);
  	            }else{
	            	
	            }	        
		    }
		    generateOD(day,taxiID);
		}
	}
	private void generateOD(int day,long taxiID) {
		ArrayList<OD> ods=new ArrayList<OD>();
        String startFlag;
        String endFlag;
        String dFlag;
        if(cpoints.size()<=1){
			
		}else if(cpoints.size()==2){
			startFlag=cpoints.get(0).flag;
			endFlag  =cpoints.get(1).flag;
			if(startFlag.equals("O")&&endFlag.equals("D")){
				ChangePoint cp1=cpoints.get(0);
				ChangePoint cp2=cpoints.get(1);
				OD od=new OD(taxiID, cp1, cp2);
				ods.add(od);
		    }else{
		    	//D-O形式不做任何提取
		    }
		}else{//3个以上的上下车点
			startFlag=cpoints.get(0).flag;
			endFlag  =cpoints.get(cpoints.size()-1).flag;
			if(startFlag.equals("O")&&endFlag.equals("D")){
				for(int i=0;i<cpoints.size();i++){
					if(i==cpoints.size()-1)break;//最后一个不进行判断
					ChangePoint cp1=cpoints.get(i);
					ChangePoint cp2=cpoints.get(i+1);
					OD od=new OD(taxiID, cp1, cp2);
					ods.add(od);
				}
			}else if(startFlag.equals("O")&&endFlag.equals("O")){
				for(int i=0;i<cpoints.size()-1;i++){
					if(i==cpoints.size()-2)break;//最后一个不进行判断
					ChangePoint cp1=cpoints.get(i);
					ChangePoint cp2=cpoints.get(i+1);
					OD od=new OD(taxiID, cp1, cp2);
					ods.add(od);
				}
			}else if(startFlag.equals("D")&&endFlag.equals("D")){
				for(int i=1;i<cpoints.size();i++){
					if(i==cpoints.size()-1)break;//最后一个不进行判断
					ChangePoint cp1=cpoints.get(i);
					ChangePoint cp2=cpoints.get(i+1);
					OD od=new OD(taxiID, cp1, cp2);
					ods.add(od);
				}
			}else{//D-O形式
				for(int i=1;i<cpoints.size()-1;i++){
					if(i==cpoints.size()-2)break;//最后一个不进行判断
					ChangePoint cp1=cpoints.get(i);
					ChangePoint cp2=cpoints.get(i+1);
					OD od=new OD(taxiID, cp1, cp2);
					ods.add(od);
				}
			}
		}
		exportOD(day,ods);
		cpoints.clear();//清空
	}
	private void exportOD(int day, ArrayList<OD> ods) {
		String path="D:\\Fdisk\\taxi\\201312";
		try {
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path+day+"_OD\\od.csv"),true),"utf-8"));
			String originLng;
			String originLat;
			String destLng;
			String destLat;
			String originTime;
			String destTime;
			OD od;
			long taxiID;
			int id;
			for(int i=0;i<ods.size();i++){
				od=ods.get(i);
				id=od.id;
				taxiID=od.taxiID;
				originLng=od.op.lng+"";
				originLat=od.op.lat+"";
				originTime=od.op.time+"";
				destLng=od.dp.lng+"";
				destLat=od.dp.lat+"";
				destTime=od.dp.time+"";
				bw.append(id+","+taxiID+","+originLng+","+originLat+","+originTime+","+destLng+","+destLat+","+destTime+"\r\n");
			}
			bw.close();
			ods.clear();//qingk
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
