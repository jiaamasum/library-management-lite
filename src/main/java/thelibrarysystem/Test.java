package thelibrarysystem;

import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(10, 25, 30, 45, 50);

        nums.stream()
                .filter(num -> num > 20)
                .forEach(num -> System.out.println(num));
    }

}
