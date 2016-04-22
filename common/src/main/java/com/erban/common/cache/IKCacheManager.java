
package com.erban.common.cache;

interface IKCacheManager {

    public Object get(String key);

    public boolean put(String key,
                       Object o);

    public boolean put(String key,
                       Object o,
                       long expired);

    public boolean delete(String key);

    public boolean clear();

    public boolean contains(String key);

    public boolean isExpired(String key);

    public void destroy();

    public byte[] getByteArray(String key);

}
