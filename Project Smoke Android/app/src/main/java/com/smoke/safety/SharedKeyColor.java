package com.smoke.safety;

/**
 * Created by Jose Carlos Garcia.
 */
public class SharedKeyColor {
    private String sharedKey;
    private Integer intervalSize;

    public SharedKeyColor(String sharedKey) {
        this.sharedKey = sharedKey;
        this.intervalSize = this.sharedKey.length() / 6;
    }

    public String generateHexColor() {
        long seed, generator;

        int length, i;

        StringBuilder builder = new StringBuilder();
        builder.append('#');

        seed = this.sharedKey.hashCode();
        generator = seed = (seed ^ 0x5DEECE66DL) & ((1L << 48) - 1);

        length = this.sharedKey.length();

        for(i = 0;i < length;i++) {
            seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
            generator = generator + (int)(seed >>> 16)*this.sharedKey.charAt(i);

            /* Trivial 0 mod intervalSize = 0 */
            if((i != 0 || length % 6 == 0) && i > 0 && i % this.intervalSize == 0) builder.append(String.format("%X", generator % 16));
        }
        return builder.toString();
    }


}
