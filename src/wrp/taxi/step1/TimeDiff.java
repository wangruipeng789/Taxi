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
		   double totalTime = 0;//��¼ǰ���ܼ��ʱ��
		   int    count = 0;//��¼�ܼ������
		   String path="D:\\Fdisk\\taxi\\20131225_splitByName\\";
		   File fileFolder=new File(path);
		   File[]    files=fileFolder.listFiles();
		   for(int i=0,len=files.length;i<len;i++){  //�����ļ��ṹ�������ű���
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
			    	  //�õ�id
			    	  actTime  =recValues[6];
			    	  if(currTime==null){//���˵���һ��
			    		  currTime=actTime;
			    		  continue;
			    	  }
			    	  totalTime+=(sdf.parse(actTime).getTime()-sdf.parse(currTime).getTime());
			    	  //System.out.println("��ʱ���Ϊ��"+totalTime);
		    		  count++;
		    		  //System.out.println("����Ŀ��Ϊ��"+count);
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
		   System.out.println("ƽ��ʱ��Ϊ:"+(totalTime/1000)/count+"��");//ƽ��ʱ��Ϊ:10.401778976184941��
		   
	}
}
