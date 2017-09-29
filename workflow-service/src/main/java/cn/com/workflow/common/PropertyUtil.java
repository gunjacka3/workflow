package cn.com.workflow.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

	private static Properties props=null;
	
	public PropertyUtil(){

	}
	
	public  static String getPropertis(String key){
		if(null==props){
			readProperty();
		}
		return props.getProperty(key);
	}
	
	
	private static void readProperty(){
		try {
			InputStream is = PropertyUtil.class.getResourceAsStream(WorkFlowContent.WF_DEFINITION_FILE_NAME);
			props=new Properties();
			props.load(is);
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
