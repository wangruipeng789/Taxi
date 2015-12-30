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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
public class DataSplit {
   private Hashtable<Long,TaxiUser> ht=new Hashtable();//���Ÿ����Ķ�Ӧ
   private int count=1;//���ļ����������ļ��������ĵڼ���
   public static void main(String[] args) {
	  DataSplit ds=new DataSplit();
	  //ds.integrate(25);//�ȴ���1���12��25��
	  for(int i=26;i<=31;i++){
		 ds.integrate(i);//  
		 ds.finalSortData(i);
	  }
   }
   /**�ۼ��ļ�*/
   private void integrate(int day) {
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
	   TaxiActivity ta;
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
		    	  ta=new TaxiActivity(actLng, actLat, actSpeed, actDirect, actStatus, actTime);
		    	  if((taxiUser=ht.get(taxiID)) != null){//���Ѿ�����
		    		 taxiUser.addTaxiActivity(ta);  
		    	  }else{//û�иó�����Ҫ�½�
		    		 taxiUser=new TaxiUser(taxiID);//�����ó��⳵
		    		 taxiUser.addTaxiActivity(ta);//���ó������һ�
		    		 ht.put(taxiID, taxiUser);     //�ѳ��Ÿ��������ֵ�
		    	  }
		    	  if(count%10000000==0){//ÿ1000�������һ��
		    		  System.out.println("��������µģ�"+count+"�м�¼");
		    		  exportData(day);
		    		  count=1;//���֮�����¼���
		    	  }
		       }
		       exportData(day);//���ʣ�µĿ��ܲ���1000����
		       count=1;//���֮�����¸�ֵ
		       br.close();
		   } catch (FileNotFoundException e) {
			   e.printStackTrace();
		   } catch (IOException e) {
			   e.printStackTrace();
		   } 
	   }
   }
   /**��ɢ�ļ�*/
   private void exportData(int day) {
	   try {
		   //BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\Fdisk\\taxi\\20131225_split\\all.csv"),true),"utf-8"));
		   BufferedWriter[] bws=new BufferedWriter[10];//����10���ļ��Ļ��������
		   for(int i=0;i<10;i++){
			   bws[i]=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\Fdisk\\taxi\\201312"+day+"_split\\last_"+i+".csv"),true),"utf-8"));
		   }
		   //bw.append("����ID�����ȣ�γ�ȣ��ٶȣ�����״̬��ʱ��\r\n");
		   Enumeration<Long> taxiIDs=ht.keys();
		   Long        taxiID;
		   TaxiUser    taxiUser;
		   String actLng;
		   String actLat;
		   String actSpeed;
		   String actDirect;
		   String actStatus;
		   String actTime;
		   ArrayList<TaxiActivity> tas=new ArrayList<>();
		   //�жϸö��������ĸ������
		   int fileID = 0;
		   while(taxiIDs.hasMoreElements()){
			   taxiID=taxiIDs.nextElement();
			   fileID=(int) (taxiID%10);//������
			   taxiUser=ht.get(taxiID);
			   tas=taxiUser.getSortedActs();
			   for(int i=0;i<tas.size();i++){
				   actLng=tas.get(i).actLng;
				   actLat=tas.get(i).actLat;
				   actSpeed=tas.get(i).actSpeed;
				   actDirect=tas.get(i).actDirect;
				   actStatus=tas.get(i).actStatus;
				   actTime=tas.get(i).actTime;
			       bws[fileID].append(taxiID+","+actLng+","+actLat+","+actSpeed+","+actDirect+","+actStatus+","+actTime+"\r\n");
			   }
			   taxiUser.clearActs();//�����֮�������
		   }
		   for(int i=0;i<bws.length;i++){//��ֹ���һ�ε�ʱ��֮ˢ��һ���֣�ʣ��һ����bwû��ˢ��
			   bws[i].close();
		   }
	   } catch (FileNotFoundException e) {
		  e.printStackTrace();
	   } catch (UnsupportedEncodingException e) {
		   
		  e.printStackTrace();
	   } catch (IOException e) {
		e.printStackTrace();
	   }
   }
   /**���������ļ�*/
   private void finalSortData(int day){//���´�����
	   ht.clear();//�����ǰ�����µ�������
	   try{
		   for(int i=0;i<10;i++){
	    	   BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\Fdisk\\taxi\\201312"+day+"_split\\last_"+i+".csv")));
	    	   Long        taxiID;
			   TaxiUser    taxiUser;
			   TaxiActivity ta;
			   String actLng;
			   String actLat;
			   String actSpeed;
			   String actDirect;
			   String actStatus;
			   String actTime;
			   String recValues[];
			   for(String line=br.readLine();line!=null;line=br.readLine()){
				      recValues=line.split(",");
				      taxiID=Long.valueOf(recValues[0]);
				      if(ht.get(taxiID)==null){
				    	  taxiUser=new TaxiUser(taxiID);
				    	  ht.put(taxiID, taxiUser);
				      }
				      actLng   =recValues[1];//����
			    	  actLat   =recValues[2];//ά��
			    	  actSpeed =recValues[3];//�ٶ�
			    	  actDirect=recValues[4];//����
			    	  actStatus=recValues[5];//״̬
			    	  actTime  =recValues[6];//ʱ��
			    	  ta=new TaxiActivity(actLng, actLat, actSpeed, actDirect, actStatus, actTime);
			    	  ht.get(taxiID).addTaxiActivity(ta);
			   }
			   finalExportData(day,i);
			   br.close();
	       }
	   }catch (Exception e) {
		  e.printStackTrace();
	   }
   }
   /**����ļ�*/
   private void finalExportData(int day,int fileID) {
	   try{
		   Enumeration<Long> ids=ht.keys();
		   TaxiUser taxiUser;
		   ArrayList<TaxiActivity> tas;
		   String actLng;
		   String actLat;
		   String actSpeed;
		   String actDirect;
		   String actStatus;
		   String actTime;
		   BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\Fdisk\\taxi\\201312"+day+"_split\\last_"+fileID+"_.csv"),true),"utf-8"));
		   while(ids.hasMoreElements()){
			   taxiUser=ht.get(ids.nextElement());   
		       tas=taxiUser.getSortedActs();
		       for(int j=0;j<tas.size();j++){
				   actLng=tas.get(j).actLng;
				   actLat=tas.get(j).actLat;
				   actSpeed=tas.get(j).actSpeed;
				   actDirect=tas.get(j).actDirect;
				   actStatus=tas.get(j).actStatus;
				   actTime=tas.get(j).actTime;
				   bw.append(taxiUser.taxiID+","+actLng+","+actLat+","+actSpeed+","+actDirect+","+actStatus+","+actTime+"\r\n");
			   }
		   }
		   bw.close();
		   ht.clear();
	   }catch (Exception e) {
	      e.printStackTrace(); 
 	   }
   }
}
