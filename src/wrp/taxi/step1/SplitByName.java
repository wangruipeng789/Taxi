package wrp.taxi.step1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Hashtable;

public class SplitByName {
	  int lineCount=10000000;
	  public static void main(String[] args) {
		  SplitByName sbn=new SplitByName();
		  sbn.integrate(25);//�ȴ���1���12��25��
//		  for(int i=26;i<=31;i++){
//			 sbn.integrate(i);//  
//			 //ds.finalSortData(i);
//		  }
	   }
	   /**�ۼ��ļ�*/
	   private void integrate(int day) {
		   Hashtable<Long, BufferedWriter> taxiBufferes=new Hashtable<Long, BufferedWriter>();
		   String path="D:\\Fdisk\\taxi\\201312"+day+"\\201312"+day;
		   String name;
		   String recValues[];
		   long   taxiID;
		   TaxiUser taxiUser;
		   String actLng;
		   String actLat;
		   String actSpeed;
		   String actDirect;
		   String actStatus;
		   String actTime;
		   String outString;
		   for(int i=1;i<=288;i++){  //�����ļ��ṹ�������ű���
			   if(i<=9){
				   name=path+"_00"+i+".txt"; 
			   }else if(i<=99){
				   name=path+"_0"+i+".txt"; 
			   }else{
				   name=path+"_"+i+".txt";
			   }
			   try {
				   BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(name)));
			       for(String line=br.readLine();line!=null;line=br.readLine()){
			    	  recValues=line.split(",");
			    	  //�õ�id
			    	  taxiID   =Long.valueOf(recValues[3]);
			    	  //ta�Ĳ���
			    	  actLng   =recValues[4];//����
			    	  actLat   =recValues[5];//ά��
			    	  actSpeed =recValues[6];//�ٶ�
			    	  actDirect=recValues[7];//����
			    	  actStatus=recValues[8];//״̬
			    	  actTime  =recValues[10].split(";")[0];//ʱ��ȥ��";"
			    	  //����ta
			    	  outString=taxiID+","+actLng+","+actLat+","+actSpeed+","+actDirect+","+actStatus+","+actTime+"\r\n";
			    	  if(taxiBufferes.get(taxiID) == null){//û�еĻ�
			    		 BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\Fdisk\\taxi\\201312"+day+"_splitByName\\"+taxiID+".csv"),true),"utf-8")); 
			    	     bw.append(outString);
			    		 taxiBufferes.put(taxiID, bw);
			    	  }else{//û�иó�����Ҫ�½�
			    		  taxiBufferes.get(taxiID).append(outString);
			    	  }
			       }
			       br.close();
			   } catch (FileNotFoundException e) {
				   e.printStackTrace();
			   } catch (IOException e) {
				   e.printStackTrace();
			   } 
		   }
		   try { 
			   Enumeration<BufferedWriter> bws=taxiBufferes.elements();
			   while(bws.hasMoreElements()){
			       BufferedWriter bw;
		    	   bw=bws.nextElement();
		    	   bw.close();
			   }
		   } catch (IOException e) {
				e.printStackTrace();
		   }
	   }
}
