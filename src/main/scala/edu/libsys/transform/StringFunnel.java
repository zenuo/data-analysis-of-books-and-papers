package edu.libsys.transform;

import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

//for create BloomFilter of String
public class StringFunnel implements Funnel<String> {
    public void funnel(String string, PrimitiveSink primitiveSink) {
        primitiveSink.putUnencodedChars(string);
    }
}