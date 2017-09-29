package cn.com.workflow.redis.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import cn.com.workflow.redis.ProcessDataHandle;

@Service(value = "processDataHandleLocalImpl")
public class ProcessDataHandleLocalImpl implements ProcessDataHandle {

    private static Map<String, String> map = new HashMap<String, String>();

    /**
     * 
     * @param _key
     * @return
     */
    public String getStringData(String _key) {
        return map.get(_key);
    }

    /**
     * 
     * @param keys
     * @param Data
     */
    public void extendStringData(String keys, String Data) throws Exception {
        String track = getStringData(keys);
        if (null != track && !"".equals(track)) {
            map.put(keys, track + "," + Data);
        } else {
            map.put(keys, Data);
        }
    }

    /**
     * 
     * @param keys
     * @param _data
     */
    public void addData(String keys, String _data) throws Exception {
        map.put(keys, _data);
    }

    @Override
    public boolean existsData(String keys) {
        return map.containsKey(keys);
    }

    @Override
    public Set keys(String str) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void del(String... str) {
        // TODO Auto-generated method stub

    }
}
