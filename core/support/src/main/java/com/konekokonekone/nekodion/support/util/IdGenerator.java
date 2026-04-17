package com.konekokonekone.nekodion.support.util;

/**
 * 単一JVM内のスレッドセーフを保証
 */
public class IdGenerator {

    private static final int MAX_SEQUENCE = 999;
    private static long lastTimestamp = 0L;
    private static long sequence = 0L;

    // ID = タイムスタンプ(ms) * 1000 + シーケンス番号(0〜999)
    public static synchronized long generate() {
        long now = System.currentTimeMillis();

        if (now < lastTimestamp) {
            // 時刻逆行（NTP補正等）: lastTimestamp に追いつくまで待機
            while (now < lastTimestamp) {
                now = System.currentTimeMillis();
            }
        }

        if (now == lastTimestamp) {
            sequence++;
            if (sequence > MAX_SEQUENCE) {
                while (now <= lastTimestamp) {
                    now = System.currentTimeMillis();
                }
                lastTimestamp = now;
                sequence = 0;
            }
        } else {
            lastTimestamp = now;
            sequence = 0;
        }

        return now * 1000 + sequence;
    }
}

