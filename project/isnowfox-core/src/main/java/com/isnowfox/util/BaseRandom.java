package com.isnowfox.util;

/**
 * @author zuoge85 on 2015/1/5.
 */
public class BaseRandom {
    private int seed;

    private static final int multiplier = 0x1315D;
    private static final int addend = 0xB;
    private static final int mask = (1 << 24) - 1;


    public static void main(String[] args) {
        BaseRandom r = new BaseRandom(1);
        int trueNums = 0;
        for (int i = 0; i < 100000; i++) {
            if(r.nextMax(2048) >(2048 / 2)){
                trueNums++;
            }
        }
        System.out.println("sb" + trueNums);

//        System.out.println(10790047 * multiplier);
//        System.out.println(Integer.toBinaryString(10790047 * multiplier + addend));
//        System.out.println(Integer.toBinaryString(10790047));
//        System.out.println((10790047 * multiplier + addend) & mask);
////        System.out.println(isPrime(3602129131L));
//
//        int n =1;
//        if ((n & -n) == n){
//
//        }
//        System.out.println((n & -n) );
    }

    public BaseRandom(int seed) {
        this.seed = initialScramble(seed);
    }

    private static int initialScramble(int seed) {
        return (seed ^ multiplier) & mask;
    }


    public  Boolean randOdds(double value) {
        int i = nextInt16();
        return i < value * 0xFFFF;
    }

    public void setSeed(int seed) {
        this.seed = (initialScramble(seed));
//        haveNextNextGaussian = false;
    }

    protected int next(int bits) {
        this.seed = (this.seed * multiplier + addend) & mask;
        return this.seed >>> (24 - bits);
    }

    public int nextInt16() {
        return next(16);
    }

    public int nextInt() {
        return (next(16) << 16) + next(16);
    }

    public int nextInt31() {
        return (next(15) << 15) + next(16);
    }

    public int nextBits(int bits) {
        if (bits > 16) {
            int highBits = bits - 16;
            return (next(highBits) << highBits) + next(16);
        } else {
            return next(bits);
        }
    }

    public int nextMax(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive");


        int bits, val;
        do {
            bits = nextInt31();
            val = bits % n;
        } while (bits - val + (n - 1) < 0);
        return val;
    }


    public boolean nextBoolean() {
        return next(1) != 0;
    }

//    public double nextDouble() {
//        return (((long) (nextBits(26)) << 27) + nextBits(27))
//                / (double) (1L << 53);
//    }

//    private double nextNextGaussian;
//    private boolean haveNextNextGaussian = false;
//
//    synchronized public double nextGaussian() {
//        // See Knuth, ACP, Section 3.4.1 Algorithm C.
//        if (haveNextNextGaussian) {
//            haveNextNextGaussian = false;
//            return nextNextGaussian;
//        } else {
//            double v1, v2, s;
//            do {
//                v1 = 2 * nextDouble() - 1; // between -1 and 1
//                v2 = 2 * nextDouble() - 1; // between -1 and 1
//                s = v1 * v1 + v2 * v2;
//            } while (s >= 1 || s == 0);
//            double multiplier = Math.sqrt(-2 * StrictMath.log(s) / s);
//            nextNextGaussian = v2 * multiplier;
//            haveNextNextGaussian = true;
//            return v1 * multiplier;
//        }
//    }
}
