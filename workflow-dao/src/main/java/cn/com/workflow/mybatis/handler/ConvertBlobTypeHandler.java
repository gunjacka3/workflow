package cn.com.workflow.mybatis.handler;

import java.io.ByteArrayInputStream;  
import java.io.UnsupportedEncodingException;  
import java.sql.Blob;  
import java.sql.CallableStatement;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
  


import org.apache.ibatis.type.BaseTypeHandler;  
import org.apache.ibatis.type.JdbcType;  
  
/** 
 * className:ConvertBlobTypeHandler 
 *  
 * 自定义typehandler，解决mybatis存储blob字段后，出现乱码的问题 
 * 配置mapper.xml：  
 *  
 * @author  
 * @version
 * @date
 *  
 */  
public class ConvertBlobTypeHandler extends BaseTypeHandler<String> {    
    //###指定字符集    
    private static final String DEFAULT_CHARSET = "utf-8";    
    
    @Override    
    public void setNonNullParameter(PreparedStatement ps, int i,    
            String parameter, JdbcType jdbcType) throws SQLException {    
        ByteArrayInputStream bis;    
        try {    
            //###把String转化成byte流    
        	byte[] data = parameter.getBytes(); 
        	String str2 = new String(data,DEFAULT_CHARSET);
        	ps.setString(i, str2);
//            bis = new ByteArrayInputStream(parameter.getBytes(DEFAULT_CHARSET));    
        } catch (UnsupportedEncodingException e) {    
            throw new RuntimeException("Blob Encoding Error!");    
        }       
//        ps.setBinaryStream(i, bis, parameter.length());
//        ps.setBlob(i, bis);
    }    
    
    @Override    
    public String getNullableResult(ResultSet rs, String columnName)    
            throws SQLException {    
        Blob blob = rs.getBlob(columnName);    
        byte[] returnValue = null;    
        if (null != blob) {    
            returnValue = blob.getBytes(1, (int) blob.length()); 
            try {    
                //###把byte转化成string    
                return new String(returnValue, DEFAULT_CHARSET);    
            } catch (UnsupportedEncodingException e) {    
                throw new RuntimeException("Blob Encoding Error!");    
            }
        }else{
        	return "";    
        }
            
    }    
    
    @Override    
    public String getNullableResult(CallableStatement cs, int columnIndex)    
            throws SQLException {    
        Blob blob = cs.getBlob(columnIndex);    
        byte[] returnValue = null;    
        if (null != blob) {    
            returnValue = blob.getBytes(1, (int) blob.length());    
        }    
        if(returnValue!=null){
        	try {    
        		return new String(returnValue, DEFAULT_CHARSET);    
        	} catch (UnsupportedEncodingException e) {    
        		throw new RuntimeException("Blob Encoding Error!");    
        	} 
        }else{
        	return null;
        }
    }  
  
    @Override  
    public String getNullableResult(ResultSet arg0, int arg1)  
            throws SQLException {  
        // TODO Auto-generated method stub  
        return null;  
    }    
}   