package com.example.maintenance.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
class calculatorTest{
   private calculator cal;

   @BeforeAll
   static void beforeAll()
   {
       System.out.println("Starting calculatoor test");
   }

   @BeforeEach
    void setUp()
   {
       cal =new calculator();
       System.out.println("Test Started");
   }

   @Test @DisplayName("Should add two numbers:") void AddTest() {assertEquals(10, cal.add(5,5));}

   @Test
    @DisplayName("Should return positive values")
    void valueCheck()
   {
       int result=cal.add(10,10);
       assertTrue(result>0);
   }

   @Test
    @DisplayName("Should throw exception")
    void throwException()
   {
       assertThrows(ArithmeticException.class,()->cal.div(10));
   }

@ParameterizedTest
    @CsvSource
            ({
"5,5,10","10,5,15","20,20,40"
            })
    void parametTest(int a, int b, int result)
{
    assertEquals(result,cal.add(a,b));
}

@AfterEach
    void TearDown()
{
    System.out.println("Test Finished");
}

@AfterAll
    static void afterAll()
{
    System.out.println("All calculator Test Completed..");
}

}