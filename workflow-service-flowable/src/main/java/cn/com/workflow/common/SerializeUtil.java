package cn.com.workflow.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * Package : cn.com.workflow.common
 * 
 * @author wangzhiyin
 *		   2017年9月27日 上午11:20:28
 *
 */
public class SerializeUtil {
    /**
     * 序列化对象
     * 
     * @param object
     * @return 
     * @author wangzhiyin
     *	       2017年9月27日 上午11:20:56
     */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return 
	 * @author wangzhiyin
	 *	       2017年9月27日 上午11:20:36
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
	}
}
