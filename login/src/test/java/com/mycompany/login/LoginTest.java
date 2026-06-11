package com.mycompany.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    
    private Login login;

    /**
     * Initializes a fresh Login instance before each individual test execution
     * to keep tests fully isolated and independent.
     */
    @BeforeEach
    public void setUp() {
        login = new Login();
    }

    // =========================================================================
    // 1. REGISTER USER FLOW TESTS
    // =========================================================================

    @Test
    public void testRegisterUser_AllValid_Success() {
        String result = login.registerUser("ValidUser1", "P@ssword1", "+27123456789");
        assertEquals("Registration successful!", result);
    }

    @Test
    public void testRegisterUser_FailsOnUsername() {
        String result = login.registerUser("abc", "P@ssword1", "+27123456789");
        assertTrue(result.contains("Username is not correctly formatted"));
    }

    @Test
    public void testRegisterUser_FailsOnPassword() {
        String result = login.registerUser("ValidUser1", "weakpass", "+27123456789");
        assertTrue(result.contains("Password must be at least 8 characters long"));
    }

    @Test
    public void testRegisterUser_FailsOnPhone() {
        String result = login.registerUser("ValidUser1", "P@ssword1", "0821234567");
        assertTrue(result.contains("Cell phone number incorrectly formatted"));
    }

    // =========================================================================
    // 2. LOGIN USER FLOW TESTS
    // =========================================================================

    @Test
    public void testLoginUser_Success() {
        login.registerUser("GoodUser", "Secr3t!99", "+27821234567");
        boolean loginPassed = login.loginUser("GoodUser", "Secr3t!99");
        assertTrue(loginPassed, "Login must return true for correct matching credentials.");
    }

    @Test
    public void testLoginUser_Failure_WrongPassword() {
        login.registerUser("GoodUser", "Secr3t!99", "+27821234567");
        boolean loginPassed = login.loginUser("GoodUser", "WrongPassword!");
        assertFalse(loginPassed, "Login must return false for a mismatched password.");
    }

    @Test
    public void testLoginUser_Failure_WrongUsername() {
        login.registerUser("GoodUser", "Secr3t!99", "+27821234567");
        boolean loginPassed = login.loginUser("WrongUser", "Secr3t!99");
        assertFalse(loginPassed, "Login must return false for a mismatched username.");
    }

    @Test
    public void testLoginUser_Failure_NullInputs() {
        // Triggers the safety guard clause where data is completely missing
        assertFalse(login.loginUser(null, "Secr3t!99"));
        assertFalse(login.loginUser("GoodUser", null));
        
        login.registerUser("GoodUser", "Secr3t!99", "+27821234567");
        assertFalse(login.loginUser(null, null));
    }

    // =========================================================================
    // 3. CELLPHONE VALIDATION GRANULAR METHOD TESTS
    // =========================================================================

    @Test
    public void testCheckCellphoneNumber_Valid() {
        assertTrue(login.checkCellphoneNumber("+27123456789"));
        assertTrue(login.checkCellphoneNumber("+27000000000"));
    }

    @Test
    public void testCheckCellphoneNumber_Invalid_NullOrPrefix() {
        assertFalse(login.checkCellphoneNumber(null));         // Null boundary
        assertFalse(login.checkCellphoneNumber("027123456789")); // Doesn't start with +27
        assertFalse(login.checkCellphoneNumber("+28123456789")); // Wrong prefix digits
    }

    @Test
    public void testCheckCellphoneNumber_Invalid_Length() {
        assertFalse(login.checkCellphoneNumber("+2712345"));      // Too short
        assertFalse(login.checkCellphoneNumber("+2712345678901")); // Too long
    }

    @Test
    public void testCheckCellphoneNumber_Invalid_NonDigits() {
        assertFalse(login.checkCellphoneNumber("+2712345678a")); // Contains alpha character
        assertFalse(login.checkCellphoneNumber("+271234567-9")); // Contains punctuation symbol
    }

    // =========================================================================
    // 4. USERNAME VALIDATION GRANULAR METHOD TESTS
    // =========================================================================

    @Test
    public void testCheckUsername_Valid() {
        assertTrue(login.checkUsername("User123"));
        assertTrue(login.checkUsername("ALPHANUMERIC99"));
    }

    @Test
    public void testCheckUsername_Invalid_NullOrShort() {
        assertFalse(login.checkUsername(null));  // Null boundary
        assertFalse(login.checkUsername("abc"));   // Under 5 characters
        assertFalse(login.checkUsername(""));      // Empty string boundary
    }

    @Test
    public void testCheckUsername_Invalid_SpecialCharacters() {
        assertFalse(login.checkUsername("user_name")); // Underscore is invalid
        assertFalse(login.checkUsername("user!123"));  // Symbol is invalid
        assertFalse(login.checkUsername("user name")); // Spaces are invalid
    }

    // =========================================================================
    // 5. PASSWORD COMPLEXITY GRANULAR METHOD TESTS
    // =========================================================================

    @Test
    public void testCheckPasswordComplexity_Valid() {
        assertTrue(login.checkPasswordComplexity("Ab1!cdef")); // Meets all 4 conditions
        assertTrue(login.checkPasswordComplexity("PASSWORD99low#")); 
    }

    @Test
    public void testCheckPasswordComplexity_Invalid_NullOrShort() {
        assertFalse(login.checkPasswordComplexity(null));       // Null boundary
        assertFalse(login.checkPasswordComplexity("Ab1!"));     // Under 8 characters
    }

    @Test
    public void testCheckPasswordComplexity_Missing_Components() {
        assertFalse(login.checkPasswordComplexity("nolowercase1!")); // Missing Uppercase
        assertFalse(login.checkPasswordComplexity("NOUPPERCASE1!")); // Missing Lowercase
        assertFalse(login.checkPasswordComplexity("NoDigitsHere!")); // Missing Digits
        assertFalse(login.checkPasswordComplexity("NoSpecialChar1"));// Missing Special character
    }
}
