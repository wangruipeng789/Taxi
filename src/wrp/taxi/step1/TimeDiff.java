package wrp.taxi.step1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeDiff {
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String[] args) {
		TimeDiff td=new TimeDiff();
		td.process();
	}
	private void process() {
		   double totalTime = 0;//记录前后总间隔时间
		   int    count = 0;//记录总间隔个数
		   String path="D:\\Fdisk\\taxi\\20131225_splitByName\\";
		   File fileFolder=new File(path);
		   File[]    files=fileFolder.listFiles();
		   for(int i=0,len=files.length;i<len;i++){  //根据文件结构这样安排变量
			   String name;
			   String recValues[];
			   String actTime;
			   String currTime = null;
			   name=files[i].getName();
			   name=path+name;
			   try {
				   BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(name)));
			       for(String line=br.readLine();line!=null;line=br.readLine()){
			    	  recValues=line.split(",");
			    	  //拿到id
			    	  actTime  =recValues[6];
			    	  if(currTime==null){//过滤掉第一个
			    		  currTime=actTime;
			    		  continue;
			    	  }
			    	  totalTime+=(sdf.parse(actTime).getTime()-sdf.parse(currTime).getTime());
			    	  //System.out.println("总时间变为："+totalTime);
		    		  count++;
		    		  //System.out.println("总数目变为："+count);
		    		  currTime=actTime;	  
			       }
			       br.close();
			   } catch (FileNotFoundException e) {
				   e.printStackTrace();
			   } catch (IOException e) {
				   e.printStackTrace();
			   } catch (ParseException e) {
				e.printStackTrace();
			   }
		   }
		   System.out.println("平均时间为:"+(totalTime/1000)/count+"秒");//平均时间为:10.401778976184941秒
		   
	}
}
