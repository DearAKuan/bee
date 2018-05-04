package com.yzbubble.bee.idgenerator;

/**
 * @author yzbubble
 */
public class IdGeneratorFactory {
    public static IdGenerator getGenerator(IdGeneratorStrategy strategy) {
        switch (strategy) {
            case SNOWFLAKE:
                return getDefaultSnowflakeGenerator();
            case UUID:
                return getUuidGenerator();
        }
        throw new IllegalArgumentException();
    }

    private static IdGenerator getDefaultSnowflakeGenerator() {
        return new SnowflakeGenerator(0, 0);
    }

    public static IdGenerator getSnowflakeGenerator(long dataCenterId, long machineId) {
        return new SnowflakeGenerator(dataCenterId, machineId);
    }

    public static IdGenerator getUuidGenerator() {
        return new UuidGenerator();
    }
}
