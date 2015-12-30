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
import java.text.ParseException;

public class OutOfBounds {
    
	public static void main(String[] args) {
		OutOfBounds ofb=new OutOfBounds();
		ofb.process();
    }
	private void process() {
		double totalTime = 0;//记录前后总间隔时间
		   int    count = 0;//记录总间隔个数
		   String path="D:\\Fdisk\\taxi\\20131225_splitByName\\";
		   File fileFolder=new File(path);
		   File[]    files=fileFolder.listFiles();
		   double lngMax=122+12.0/60;
		   double lngMin=120+51.0/60;
		   double latMax=31+53.0/60;
		   double latMin=30+40.0/60;
		   try {
			   BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\Fdisk\\taxi\\20131225_outOfBounds\\out.csv"))));
			   for(int i=0,len=files.length;i<len;i++){  //根据文件结构这样安排变量
				   String name=files[i].getName();
				   name=path+name;
				   double lng;
				   double lat;
				   String recValues[];
				   BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(name)));
				   for(String line=br.readLine();line!=null;line=br.readLine()){
				       recValues=line.split(",");
				    	  //拿到id
				       lng =Double.valueOf(recValues[1]);//经度
				       lat =Double.valueOf(recValues[2]);
				       if(lng>lngMax||lng<latMin||lat>latMax||lat<latMin){
				    	   bw.append(line+"\r\n");
				       }
				   }
				   br.close();
				}
			    bw.close();
            } catch (FileNotFoundException e) {
				   e.printStackTrace();
		    } catch (IOException e) {
				   e.printStackTrace();
		    }
	}
}
