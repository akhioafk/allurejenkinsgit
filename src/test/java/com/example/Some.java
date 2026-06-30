package com.example;

import org.testng.Assert;
import org.testng.annotations.Test;

public class Some {
    @Test(description = "Test method")
    public void testMethod() {
        Assert.assertTrue(true, "This is a test assertion");
    }

    @Test(description = "Another test method")
    public void anotherTestMethod() {
        Assert.assertEquals(1, 2, "This is another test assertion");
    }
}
