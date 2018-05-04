package com.yzbubble.bee.idgenerator;

/**
 * @author yzbubble
 */
@SuppressWarnings({"AlibabaAvoidCommentBehindStatement", "AlibabaCommentsMustBeJavadocFormat", "SpellCheckingInspection"})
public class SnowflakeGenerator implements IdGenerator {
    private final static long START_TIMESTAMP = 1514736000L; // 开始时间戳,这里设置为"2018-01-01 00:00:00"

    private final static long SEQUENCE_BIT_SIZE = 12; // 序列号占用的位数
    private final static long MACHINE_BIT_SIZE = 5; // 机器标识占用的位数
    private final static long DATACENTER_BIT_SIZE = 5; // 数据中心占用的位数

    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT_SIZE); // 数据中心序号数值的最大值
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT_SIZE); // 机器序号数值的最大值
    private final static long MAX_SEQUENCE_NUM = -1L ^ (-1L << SEQUENCE_BIT_SIZE); // 序列号数值的最大值

    private final static long DATACENTER_LEFT_OFFSET = SEQUENCE_BIT_SIZE + MACHINE_BIT_SIZE; // 数据中心序号数值向左的偏移量
    private final static long MACHINE_LEFT_OFFSET = SEQUENCE_BIT_SIZE; // 机器序号数值向左的偏移量
    private final static long TIMESTAMP_LEFT_OFFSET = DATACENTER_LEFT_OFFSET + DATACENTER_BIT_SIZE; // 时间戳数值向左的偏移量

    private long dataCenterId;  // 数据中心
    private long machineId;    // 机器标识
    private long sequence = 0L; // 序列号
    private long lastTimestamp = -1L; // 上一次时间戳

    public SnowflakeGenerator(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATACENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("argument \"dataCenterId\" can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("argument \"machineId\" can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    @Override
    public String nextId() {
        return internalNextId() + "";
    }

    private synchronized long internalNextId() {
        long currentTimestamp = getCurrentTimestamp();
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards.Refusing to generate id.");
        }
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE_NUM; // 相同毫秒内，序列号自增
            if (sequence == 0L) { // 同一毫秒的序列数已经达到最大
                currentTimestamp = getNextTimestamp();
            }
        } else {
            sequence = 0L; // 不同毫秒内，序列号置为0
        }
        lastTimestamp = currentTimestamp;
        return (currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_OFFSET // 时间戳部分
                | dataCenterId << DATACENTER_LEFT_OFFSET // 数据中心部分
                | machineId << MACHINE_LEFT_OFFSET // 机器标识部分
                | sequence; // 序列号部分
    }

    private long getNextTimestamp() {
        long currentTimestamp;
        do {
            currentTimestamp = getCurrentTimestamp();
        } while (currentTimestamp <= lastTimestamp);
        return currentTimestamp;
    }

    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
}

