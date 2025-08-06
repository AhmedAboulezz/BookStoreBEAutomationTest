package dataproviders;

import org.testng.annotations.DataProvider;

public final class AuthorDataProviders {
    private AuthorDataProviders() {}

    @DataProvider(name = "deleteAuthorNumericCases")
    public static Object[][] deleteAuthorNumericCases() {
        return new Object[][]{
            { 1,   200 }, 
            { -1,  400 },
            { 0,   404 }  
        };
    }

    @DataProvider(name = "deleteAuthorStringCases")
    public static Object[][] deleteAuthorStringCases() {
        return new Object[][]{
            { "abc", "The value 'abc' is not valid." },
            { "999999999999999999999", "The value '999999999999999999999' is not valid." }
        };
    }
    
    @DataProvider(name = "authorHappyIds")
    public static Object[][] authorHappyIds() {
        return new Object[][]{
            { 1 },
            { 2 },
            { 10 }
        };
    }

    @DataProvider(name = "authorBadStringIds")
    public static Object[][] authorBadStringIds() {
        return new Object[][]{
            { "a", "The value 'a' is not valid." },
            { "999999999999999999999", "The value '999999999999999999999' is not valid." }
        };
    }

    @DataProvider(name = "authorNumeric404Ids")
    public static Object[][] authorNumeric404Ids() {
        return new Object[][]{
            { -1 },
            { 0 },
            { 10000 }
        };
    }
}
