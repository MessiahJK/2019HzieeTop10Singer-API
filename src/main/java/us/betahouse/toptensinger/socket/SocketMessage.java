/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.socket;

import com.alibaba.fastjson.JSON;
import lombok.Data;


import java.util.HashMap;
import java.util.Map;

/**
 * @author MessiahJK
 * @version : SocketMessage.java 2019/03/26 15:56 MessiahJK
 */
@Data
public class SocketMessage {

    private String name;

    private Map<String, Object> data = new HashMap<>(4);

    public SocketMessage add(String key, Object value) {
        this.getData().put(key, value);
        return this;
    }

    public String toJSON(){
        return JSON.toJSONString(this);
    }
}
