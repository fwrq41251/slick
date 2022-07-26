package org.winry;

import org.junit.Test;

import java.util.function.Function;

public class UserTest {

    @Test
    public void getUsers() {
        Slick.userManager().getAllUsers();
        Function<Integer, String> function = i -> "";
        Runnable runnable = () -> System.out.println("");
    }
}
