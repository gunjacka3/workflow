package cn.com.workflow.redis.impl;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import cn.com.workflow.redis.RedisService;

@Service(value = "redisServiceImpl")
public class RedisServiceImpl implements RedisService {
	
	private static Logger log =  LogManager.getLogger();

	private static String redisCode = "utf-8";
	
    @Autowired
    private RedisTemplate redisTemplate;
	 /**
     * @param key
     */
    public long del(final String... keys) {
        return (Long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    try {
						result = connection.del(keys[i].getBytes(redisCode));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
                }
                return result;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(final byte[] key, final byte[] value, final long liveTime) {
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }
    
    /**
     * 
     * @param key
     * @param value
     * @param liveTime
     */
    public Boolean setNx(final byte[] key, final byte[] value) {

        return (Boolean) redisTemplate.execute(new RedisCallback() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    return new Boolean(connection.setNX(key, value));
            }
        });  
        
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key, String value, long liveTime) {
        try {
			this.set(key.getBytes(redisCode), value.getBytes(redisCode), liveTime);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }
    
    public Boolean setNx(String key, String value) {
        try {
			return this.setNx(key.getBytes(redisCode), value.getBytes(redisCode));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
    }

    /**
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        this.set(key, value, 0L);
    }

    /**
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        this.set(key, value, 0L);
    }

    /**
     * @param key
     * @return
     */
    public String get(final String key) {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    return new String(connection.get(key.getBytes(redisCode)), redisCode);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return "";
            }
        });
    }
    
    
    /**
     * @param key
     * @return
     */
    public String getSet(final String key,final String value) {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    return new String(connection.getSet(key.getBytes(redisCode), value.getBytes(redisCode)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return "";
            }
        });
    }
    
    public byte[] gets(final String key) {
        return (byte[]) redisTemplate.execute(new RedisCallback() {
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    return connection.get(key.getBytes(redisCode));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    /**
     * @param pattern
     * @return
     */
    public Set keys(String pattern) {
        return redisTemplate.keys(pattern);

    }

    /**
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return (Boolean)redisTemplate.execute(new RedisCallback() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                try {
					return connection.exists(key.getBytes(redisCode));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
                return false;
            }
        });
    }

    /**
     * @return
     */
    public String flushDB() {
        return (String)redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    /**
     * @return
     */
    public long dbSize() {
        return (Long)redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    /**
     * @return
     */
    public String ping() {
        return (String)redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {

                return connection.ping();
            }
        });
    }

    private RedisServiceImpl() {

    }




}
