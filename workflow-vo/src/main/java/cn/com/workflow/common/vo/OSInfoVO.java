package cn.com.workflow.common.vo;

public class OSInfoVO  extends BaseVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ipAddress;
	
	private String hostName;
	
	private String mACAddress;
	
	private String systemAccount;
	
	private String osName;
	
	private String osArch;
	
	private String osVersion;
	
	private String javaVersion;
	
	private String javaVendor;
	
	private String javaVendorUrl;
	
	private String javaHome;
	
	private String computerName;
	
	private String userDomain;
	
	private String userName;

	private long totalMemory;
	
	private long freeMemory;
	
	private int availableProcessors;
	
	private String serverName;
	
	
	
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getAvailableProcessors() {
		return availableProcessors;
	}

	public void setAvailableProcessors(int availableProcessors) {
		this.availableProcessors = availableProcessors;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getmACAddress() {
		return mACAddress;
	}

	public void setmACAddress(String mACAddress) {
		this.mACAddress = mACAddress;
	}

	public String getSystemAccount() {
		return systemAccount;
	}

	public void setSystemAccount(String systemAccount) {
		this.systemAccount = systemAccount;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsArch() {
		return osArch;
	}

	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getJavaVendor() {
		return javaVendor;
	}

	public void setJavaVendor(String javaVendor) {
		this.javaVendor = javaVendor;
	}

	public String getJavaVendorUrl() {
		return javaVendorUrl;
	}

	public void setJavaVendorUrl(String javaVendorUrl) {
		this.javaVendorUrl = javaVendorUrl;
	}

	public String getJavaHome() {
		return javaHome;
	}

	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}

	public String getComputerName() {
		return computerName;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	public String getUserDomain() {
		return userDomain;
	}

	public void setUserDomain(String userDomain) {
		this.userDomain = userDomain;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "OSInfoVO [ipAddress=" + ipAddress + ", hostName=" + hostName + ", mACAddress=" + mACAddress
				+ ", systemAccount=" + systemAccount + ", osName=" + osName + ", osArch=" + osArch + ", osVersion="
				+ osVersion + ", javaVersion=" + javaVersion + ", javaVendor=" + javaVendor + ", javaVendorUrl="
				+ javaVendorUrl + ", javaHome=" + javaHome + ", computerName=" + computerName + ", userDomain="
				+ userDomain + ", userName=" + userName + ", totalMemory=" + totalMemory + ", freeMemory=" + freeMemory
				+ ", availableProcessors=" + availableProcessors + ", serverName=" + serverName + "]";
	}
	
}
