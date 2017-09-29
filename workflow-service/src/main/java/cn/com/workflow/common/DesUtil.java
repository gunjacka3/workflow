package cn.com.workflow.common;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DesUtil {

	private final static String DES = "DES";
	
	private static String key;
	
	private static String getKey(){
		if(null==key || "".equals(key)){
			key=PropertyUtil.getPropertis(WorkFlowContent.WF_SAVE_DATE_DES_KEY);
		}
		return key;
	}
	

	public static void main(String[] args) throws Exception {
		String data = "啊啊啊啊啊啊a6";
		String key = "quedaogurenxinyibian,buxiangyijianliaoenchou";
		System.err.println(encrypt(data, key));
		System.err.println(decrypt(encrypt(data, key), key));

	}

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		byte[] bt = encrypt(data.getBytes(), key.getBytes());
		byte[] strs = new Base64().encode(bt);
		return new String(strs);
	}
	
	public static String encrypt(String data) throws Exception {
		byte[] bt = encrypt(data.getBytes(), getKey().getBytes());
		byte[] strs = new Base64().encode(bt);
		return new String(strs);
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws Exception {
		if (data == null)
			return null;
		Base64 decoder = new Base64();
		byte[] buf = decoder.decode(data.getBytes());
		byte[] bt = decrypt(buf, key.getBytes());
		return new String(bt);
	}
	
	public static String decrypt(String data) throws Exception {
		if (data == null)
			return null;
		Base64 decoder = new Base64();
		byte[] buf = decoder.decode(data.getBytes());
		byte[] bt = decrypt(buf, getKey().getBytes());
		return new String(bt);
	}

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}
}
