package cn.com.workflow.ext.idgenerator;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.engine.common.impl.cfg.IdGenerator;

import cn.com.workflow.redis.RedisService;
public class CusIdGenerator implements IdGenerator {

	static Logger logger = LogManager.getLogger();
	
	private RedisService redisService;

	private String key;
	private String lockKey;
	private long idBlockSize;
	private int maxAttempts;
	//锁持有超时，防止线程在入锁以后，无限的执行下去，让锁无法释放 
	private int expireMsecs;
	//锁等待超时，防止线程饥饿，永远没有入锁执行代码的机会 
	private int timeoutMsecs;
	
	
	protected long nextId = 0;
	protected long lastId = -1;
	private static Random random = new Random();

	public synchronized String getNextId() {

		long rm = (long) random.nextInt(3);
		nextId = nextId + rm;
		if (lastId < nextId) {
			logger.debug("last id " + lastId + " was consumed.  acquiring new block...");
			lastId = -2;
			nextId = -1;
			try {
				acquireDbidBlock();
			} catch (Exception e) {
				e.printStackTrace();
//				throw new Exception("couldn't acquire block of ids", e);
			}
		}
		long _nextId = nextId++;
		return Long.toString(_nextId);
	}

	protected void acquireDbidBlock() throws Exception {
		for (int attempts = maxAttempts; (attempts > 0) && (nextId == -1); attempts--) {
			try {
				logger.debug("acquire block ...");
				long size = idBlockSize + random.nextInt(10);
				nextId = getIdFromRedis(size);
				lastId = nextId + size - 1;

				logger.debug("acquired new id block [" + nextId + "-" + lastId + "]");

			} catch (Exception e) {
				e.printStackTrace();
				attempts--;
				// if no attempts left
				if (attempts == 0) {
					// fail the surrounding transaction
					throw new Exception("couldn't acquire block of ids, tried " + maxAttempts + " times");
				}
				// if there are still attempts left, first wait a bit
				int millis = 20 + random.nextInt(100);
				logger.debug("optimistic locking failure while trying to acquire id block.  retrying in " + millis
						+ " millis");
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e1) {
					logger.debug("waiting after id block locking failure got interrupted");
				}
			}
		}
	}

	private long getIdFromRedis(long size) throws Exception{
		long lid=0;
			if(acquire()){
				String id=redisService.get(key);
				if(null==id || "".equals(id)){
					lid=1;
				}else{
					lid=Long.parseLong(id);
				}
				String nid=new Long(size+lid).toString();
				redisService.set(key, nid);
			}else{
				throw new Exception("redis lock acquire return false");
			}
		return lid;
	}
	
	public synchronized boolean acquire() throws InterruptedException {
        int timeout = timeoutMsecs;
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireMsecs + 1;
            String expiresStr = String.valueOf(expires); //锁到期时间
            logger.debug("acquire() ....expiresStr:"+expiresStr);
 
//            if (redisService.setNx("jbpm.key.lockkey", expiresStr)) {
                // lock acquired
//                locked = true;
//                return true;
//            }
 
            String currentValueStr = redisService.get(lockKey); //redis里的时间
            logger.debug("acquire() ....currentValueStr:"+currentValueStr);
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
                // lock is expired
 
                String oldValueStr = redisService.getSet(lockKey, expiresStr);
                //获取上一个锁到期时间，并设置现在的锁到期时间，
                //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    //如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                    // lock acquired
//                    locked = true;
                    return true;
                }
            }
            timeout -= 100;
            logger.debug("acquire() ....Thread.sleep:"+timeout);
            Thread.sleep(100);
        }
        return false;
    }

	public RedisService getRedisService() {
		return redisService;
	}

	public void setRedisService(RedisService redisService) {
		this.redisService = redisService;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLockKey() {
		return lockKey;
	}

	public void setLockKey(String lockKey) {
		this.lockKey = lockKey;
	}

	public long getIdBlockSize() {
		return idBlockSize;
	}

	public void setIdBlockSize(long idBlockSize) {
		this.idBlockSize = idBlockSize;
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public int getExpireMsecs() {
		return expireMsecs;
	}

	public void setExpireMsecs(int expireMsecs) {
		this.expireMsecs = expireMsecs;
	}

	public int getTimeoutMsecs() {
		return timeoutMsecs;
	}

	public void setTimeoutMsecs(int timeoutMsecs) {
		this.timeoutMsecs = timeoutMsecs;
	}

}
