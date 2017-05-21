package edu.libsys.test;

public class StringSplitTest {
    public static void main(String[] args) {
        String string = "1DE2DE3";

        String[] tokens = string.split("DE");

        if (tokens.length != 1) throw new AssertionError("数组长度出错");

        for (String token : tokens) {
            System.out.println(token);
        }
    }
}
