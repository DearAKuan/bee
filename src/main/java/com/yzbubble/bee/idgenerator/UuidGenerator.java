package com.yzbubble.bee.idgenerator;

import java.util.UUID;

/**
 * @author yzbubble
 */
public class UuidGenerator implements IdGenerator{
    @Override
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}
