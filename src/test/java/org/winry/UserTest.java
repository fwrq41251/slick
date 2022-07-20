package org.winry;

import org.junit.Test;

public class UserTest {

    @Test
    public void getUsers() {
        Slick.userManager().getAllUsers();
    }
}
