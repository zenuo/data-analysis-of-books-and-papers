package edu.libsys.test;

public class GetPartOfDataTest {
    public static void main(String[] args) {
        int numerator = 5;
        int denominator = 5;
        int flag = 1;

        if (numerator > denominator) {
            System.out.printf("Your input '%d/%d' is invalid, please make sure numerator is less than denominator.\nNow exit.\n", numerator, denominator);
            return;
        }

        int line = 10;
        for (int i = 1; i <= line; i++) {
            if (flag <= numerator) {
                System.out.println(i);
                flag++;
            } else if (flag < denominator) {
                flag++;
            } else {
                flag = 1;
            }
        }
    }
}
