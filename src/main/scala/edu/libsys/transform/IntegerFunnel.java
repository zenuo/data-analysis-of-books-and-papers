package edu.libsys.transform;

import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

//for create BloomFilter of Integer
public class IntegerFunnel implements Funnel<Integer> {
    public void funnel(Integer integer, PrimitiveSink primitiveSink) {
        primitiveSink.putInt(integer);
    }
}