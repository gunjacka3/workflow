package cn.com.workflow.service.impl;

import java.net.InetAddress;
import java.util.Map;
import java.util.Properties;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.liferay.portal.kernel.util.ServerDetector;

import cn.com.workflow.common.vo.OSInfoVO;
import cn.com.workflow.service.SysInfoService;

@Service("sysInfoServiceImpl")
public class SysInfoServiceImpl implements SysInfoService {
    private static final Logger LOGGER = LogManager.getLogger(SysInfoServiceImpl.class);

	@Override
	public OSInfoVO getOSInfo() throws Exception{
		OSInfoVO os=new OSInfoVO();
		Runtime r = Runtime.getRuntime();
		Properties props = System.getProperties();
		InetAddress addr;
		addr = InetAddress.getLocalHost();
		os.setHostName(addr.getHostName());
		String ip = addr.getHostAddress();
		os.setIpAddress(ip);
//		os.setmACAddress(getMACAddress());
		Map<String, String> map = System.getenv();
		String userName = map.get("USERNAME");// 获取用户名
		os.setUserName(userName);
		String computerName = map.get("COMPUTERNAME");// 获取计算机名
		os.setComputerName(computerName);
		String userDomain = map.get("USERDOMAIN");// 获取计算机域名
		os.setUserDomain(userDomain);
		os.setTotalMemory(r.totalMemory());
		os.setFreeMemory(r.freeMemory());
		os.setAvailableProcessors(r.availableProcessors());
		os.setJavaVersion(props.getProperty("java.version"));
		os.setJavaVendor(props.getProperty("java.vendor"));
		os.setJavaVendorUrl(props.getProperty("java.vendor.url"));
		os.setJavaHome(props.getProperty("java.home"));
		os.setOsName( props.getProperty("os.name"));
		os.setOsArch(props.getProperty("os.arch"));
		os.setOsVersion(props.getProperty("os.version"));
		os.setServerName(ServerName());
		LOGGER.debug("os===={}",os);
		return os;
	}
	
//	private static String getMACAddress(){  
//        String address = "";  
//        String os = System.getProperty("os.name");  
//        if (os != null && os.startsWith("Windows")) {  
//            try {  
//                String command = "cmd.exe /c ipconfig /all";  
//                Process p = Runtime.getRuntime().exec(command);  
//                BufferedReader br =new BufferedReader(new InputStreamReader(p.getInputStream()));  
//                String line;  
//                while ((line = br.readLine()) != null) {  
//                    if (line.indexOf("Physical Address") > 0) {  
//                        int index = line.indexOf(":");  
//                        index += 2;  
//                        address = line.substring(index);  
//                        break;  
//                    }  
//                }  
//                br.close();  
//                return address.trim();  
//            }  catch (IOException e) {  
//            	e.printStackTrace();
//            }  
//  
//        }  
//        return address;  
//  
//    }  
	
	
	
	private void webInfo() throws MalformedObjectNameException, NamingException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException{
		
		if (ServerDetector.isTomcat()) {  
		      
		}else if (ServerDetector.isWebLogic()) {  
		}
		ObjectName service = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");  
		InitialContext ctx = new InitialContext();  
		MBeanServer server = (MBeanServer)ctx.lookup("java:comp/env/jmx/runtime");  
		ObjectName rt =  (ObjectName)server.getAttribute(service,"ServerRuntime");  
//		System.out.println("Server Name  : "+server.getAttribute(rt,"Name"));  
//		System.out.println("Server Address : "+server.getAttribute(rt,"ListenAddress"));  
//		System.out.println("Server Port : "+server.getAttribute(rt,"ListenPort"));  
		ctx.close();
	}
	
	public static String ServerName() {
		String serverName = null;
		if (ServerDetector.isWebLogic()) {
			serverName = "WebLogic";
		} else if (ServerDetector.isTomcat()) {
			serverName = "Tomcat";
		} else if (ServerDetector.isWebSphere()) {
			serverName = "WebSphere";
		} else if (ServerDetector.isSupportsComet()) {
			serverName = "SupportsComet";
		} else if (ServerDetector.isResin()) {
			serverName = "Resin";
		} else if (ServerDetector.isOC4J()) {
			serverName = "OC4J";
		} else if (ServerDetector.isJOnAS()) {
			serverName = "JOnAS";
		} else if (ServerDetector.isJetty()) {
			serverName = "Jetty";
		} else if (ServerDetector.isJBoss()) {
			serverName = "JBoss";
		} else if (ServerDetector.isGlassfish()) {
			serverName = "Glassfish";
		}
		return serverName;
	}
	

//	private static void memory() throws SigarException {
//		 Sigar sigar = new Sigar();
//		 Mem mem = sigar.getMem();
//		 // 内存总量
//		 System.out.println("内存总量:"+ mem.getTotal() / 1024L +"K av");
//		 // 当前内存使用量
//		 System.out.println("当前内存使用量:"+ mem.getUsed() / 1024L +"K used");
//		 // 当前内存剩余量
//		 System.out.println("当前内存剩余量:"+ mem.getFree() / 1024L +"K free");
//		 Swap swap = sigar.getSwap();
//		 // 交换区总量
//		 System.out.println("交换区总量:"+ swap.getTotal() / 1024L +"K av");
//		 // 当前交换区使用量
//		 System.out.println("当前交换区使用量:"+ swap.getUsed() / 1024L +"K used");
//		 // 当前交换区剩余量
//		 System.out.println("当前交换区剩余量:"+ swap.getFree() / 1024L +"K free");
//		}
//	
//	
//	private static void cpu() throws SigarException {
//		Sigar sigar = new Sigar();
//		CpuInfo infos[] = sigar.getCpuInfoList();
//		CpuPerc cpuList[] = null;
//		cpuList = sigar.getCpuPercList();
//		for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
//			CpuInfo info = infos[i];
//			System.out.println("第" + (i + 1) + "块CPU信息");
//			System.out.println("CPU的总量MHz:" + info.getMhz());// CPU的总量MHz
//			System.out.println("CPU生产商:" + info.getVendor());// 获得CPU的卖主，如：Intel
//			System.out.println("CPU类别:" + info.getModel());// 获得CPU的类别，如：Celeron
//			System.out.println("CPU缓存数量:" + info.getCacheSize());// 缓冲存储器数量
//			printCpuPerc(cpuList[i]);
//		}
//	}
//	
//	private static void printCpuPerc(CpuPerc cpu) {
//		 System.out.println("CPU用户使用率:"+ CpuPerc.format(cpu.getUser()));// 用户使用率
//		 System.out.println("CPU系统使用率:"+ CpuPerc.format(cpu.getSys()));// 系统使用率
//		 System.out.println("CPU当前等待率:"+ CpuPerc.format(cpu.getWait()));// 当前等待率
//		 System.out.println("CPU当前错误率:"+ CpuPerc.format(cpu.getNice()));//
//		 System.out.println("CPU当前空闲率:"+ CpuPerc.format(cpu.getIdle()));// 当前空闲率
//		 System.out.println("CPU总的使用率:"+ CpuPerc.format(cpu.getCombined()));// 总的使用率
//	}
//	
//	
//	private static void net() throws Exception {
//		Sigar sigar = new Sigar();
//		String ifNames[] = sigar.getNetInterfaceList();
//		for (int i = 0; i < ifNames.length; i++) {
//			String name = ifNames[i];
//			NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
//			System.out.println("网络设备名:" + name);// 网络设备名
//			System.out.println("IP地址:" + ifconfig.getAddress());// IP地址
//			System.out.println("子网掩码:" + ifconfig.getNetmask());// 子网掩码
//			if ((ifconfig.getFlags() & 1L) <= 0L) {
//				System.out.println("!IFF_UP...skipping getNetInterfaceStat");
//				continue;
//			}
//			NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
//			System.out.println(name + "接收的总包裹数:" + ifstat.getRxPackets());// 接收的总包裹数
//			System.out.println(name + "发送的总包裹数:" + ifstat.getTxPackets());// 发送的总包裹数
//			System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes());// 接收到的总字节数
//			System.out.println(name + "发送的总字节数:" + ifstat.getTxBytes());// 发送的总字节数
//			System.out.println(name + "接收到的错误包数:" + ifstat.getRxErrors());// 接收到的错误包数
//			System.out.println(name + "发送数据包时的错误数:" + ifstat.getTxErrors());// 发送数据包时的错误数
//			System.out.println(name + "接收时丢弃的包数:" + ifstat.getRxDropped());// 接收时丢弃的包数
//			System.out.println(name + "发送时丢弃的包数:" + ifstat.getTxDropped());// 发送时丢弃的包数
//		}
//	}
//	
//	private static void ethernet() throws SigarException {
//		Sigar sigar = null;
//		sigar = new Sigar();
//		String[] ifaces = sigar.getNetInterfaceList();
//		for (int i = 0; i < ifaces.length; i++) {
//			NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
//			if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
//					|| NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
//				continue;
//			}
//			System.out.println(cfg.getName() + "IP地址:" + cfg.getAddress());// IP地址
//			System.out.println(cfg.getName() + "网关广播地址:" + cfg.getBroadcast());// 网关广播地址
//			System.out.println(cfg.getName() + "网卡MAC地址:" + cfg.getHwaddr());// 网卡MAC地址
//			System.out.println(cfg.getName() + "子网掩码:" + cfg.getNetmask());// 子网掩码
//			System.out.println(cfg.getName() + "网卡描述信息:" + cfg.getDescription());// 网卡描述信息
//			System.out.println(cfg.getName() + "网卡类型" + cfg.getType());//
//		}
//	}
}
