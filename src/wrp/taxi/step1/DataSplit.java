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
   private Hashtable<Long,TaxiUser> ht=new Hashtable();//车号跟车的对应
   private int count=1;//该文件夹下所有文件总行数的第几行
   public static void main(String[] args) {
	  DataSplit ds=new DataSplit();
	  //ds.integrate(25);//先处理1天的12月25的
	  for(int i=26;i<=31;i++){
		 ds.integrate(i);//  
		 ds.finalSortData(i);
	  }
   }
   /**聚集文件*/
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
	   for(int i=1;i<=288;i++){  //根据文件结构这样安排变量
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
		    	  //拿到id
		    	  taxiID   =Long.valueOf(recValues[3]);
		    	  //ta的参数
		    	  actLng   =recValues[4];//经度
		    	  actLat   =recValues[5];//维度
		    	  actSpeed =recValues[6];//速度
		    	  actDirect=recValues[7];//方向
		    	  actStatus=recValues[8];//状态
		    	  actTime  =recValues[10].split(";")[0];//时间去除";"
		    	  //创建ta
		    	  ta=new TaxiActivity(actLng, actLat, actSpeed, actDirect, actStatus, actTime);
		    	  if((taxiUser=ht.get(taxiID)) != null){//该已经有了
		    		 taxiUser.addTaxiActivity(ta);  
		    	  }else{//没有该车，需要新建
		    		 taxiUser=new TaxiUser(taxiID);//创建该出租车
		    		 taxiUser.addTaxiActivity(ta);//给该车添加这一活动
		    		 ht.put(taxiID, taxiUser);     //把车号跟车放入字典
		    	  }
		    	  if(count%10000000==0){//每1000万行输出一次
		    		  System.out.println("正在输出新的："+count+"行记录");
		    		  exportData(day);
		    		  count=1;//输出之后重新计数
		    	  }
		       }
		       exportData(day);//最后剩下的可能不到1000万行
		       count=1;//输出之后重新赋值
		       br.close();
		   } catch (FileNotFoundException e) {
			   e.printStackTrace();
		   } catch (IOException e) {
			   e.printStackTrace();
		   } 
	   }
   }
   /**分散文件*/
   private void exportData(int day) {
	   try {
		   //BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\Fdisk\\taxi\\20131225_split\\all.csv"),true),"utf-8"));
		   BufferedWriter[] bws=new BufferedWriter[10];//创建10个文件的缓冲输出流
		   for(int i=0;i<10;i++){
			   bws[i]=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\Fdisk\\taxi\\201312"+day+"_split\\last_"+i+".csv"),true),"utf-8"));
		   }
		   //bw.append("车辆ID，经度，纬度，速度，方向，状态，时间\r\n");
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
		   //判断该段数据往哪个输出流
		   int fileID = 0;
		   while(taxiIDs.hasMoreElements()){
			   taxiID=taxiIDs.nextElement();
			   fileID=(int) (taxiID%10);//看余数
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
			   taxiUser.clearActs();//输出完之后先清空
		   }
		   for(int i=0;i<bws.length;i++){//防止最后一次的时候之刷新一部分，剩下一部分bw没有刷新
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
   /**重新整理文件*/
   private void finalSortData(int day){//重新大排列
	   ht.clear();//先清空前面留下的再利用
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
				      actLng   =recValues[1];//经度
			    	  actLat   =recValues[2];//维度
			    	  actSpeed =recValues[3];//速度
			    	  actDirect=recValues[4];//方向
			    	  actStatus=recValues[5];//状态
			    	  actTime  =recValues[6];//时间
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
   /**输出文件*/
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
