package cn.com.workflow.service;

import org.springframework.stereotype.Service;

import cn.com.workflow.common.vo.OSInfoVO;
@Service("sysInfoService")
public interface SysInfoService {
	
	public OSInfoVO getOSInfo() throws Exception;

}
