package dataproviders;

import org.testng.annotations.DataProvider;


public final class BookDataProviders {
    private BookDataProviders() {}
    @DataProvider(name = "putInvalidPayloads")
    public static Object[][] putInvalidPayloads() {
        return new Object[][]{
            {
         
                """
                {
                  "title": "string",
                  "description": "string",
                  "pageCount": 3849,
                  "excerpt": "string",
                  "publishDate": "a"
                }
                """,
                "$.publishDate",
                "The JSON value could not be converted to System.DateTime. Path: $.publishDate"
            },
            {
         
                """
                {
                  "title": "string",
                  "description": "string",
                  "pageCount": 3849,
                  "excerpt": 1,
                  "publishDate": "1957-03-04T04:21:06.126Z"
                }
                """,
                "$.excerpt",
                "The JSON value could not be converted to System.String. Path: $.excerpt"
            },
            {
              
                """
                {
                  "title": "string",
                  "description": "string",
                  "pageCount": "a",
                  "excerpt": "string",
                  "publishDate": "1957-03-04T04:21:06.126Z"
                }
                """,
                "$.pageCount",
                "The JSON value could not be converted to System.Int32. Path: $.pageCount"
            },
            {
              
                """
                {
                  "title": "string",
                  "description": 1,
                  "pageCount": 3849,
                  "excerpt": "string",
                  "publishDate": "1957-03-04T04:21:06.126Z"
                }
                """,
                "$.description",
                "The JSON value could not be converted to System.String. Path: $.description"
            }
        };
    }
    
    @DataProvider(name = "postInvalidPayloads")
    public static Object[][] postInvalidPayloads() {
        return new Object[][]{
            {
                """
                {
                  "id": 3842,
                  "title": "string",
                  "description": "string",
                  "pageCount": 3849,
                  "excerpt": "string",
                  "publishDate": "not-a-date"
                }
                """,
                "$.publishDate",
                "The JSON value could not be converted to System.DateTime. Path: $.publishDate"
            },
            {

                """
                {
                  "id": 3842,
                  "title": "string",
                  "description": "string",
                  "pageCount": 3849,
                  "excerpt": 123,
                  "publishDate": "1957-03-04T04:21:06.126Z"
                }
                """,
                "$.excerpt",
                "The JSON value could not be converted to System.String. Path: $.excerpt"
            },
            {
  
                """
                {
                  "id": 3842,
                  "title": "string",
                  "description": "string",
                  "pageCount": "aa",
                  "excerpt": "string",
                  "publishDate": "1957-03-04T04:21:06.126Z"
                }
                """,
                "$.pageCount",
                "The JSON value could not be converted to System.Int32. Path: $.pageCount"
            },
            {
 
                """
                {
                  "id": 3842,
                  "title": "string",
                  "description": 12345,
                  "pageCount": 3849,
                  "excerpt": "string",
                  "publishDate": "1957-03-04T04:21:06.126Z"
                }
                """,
                "$.description",
                "The JSON value could not be converted to System.String. Path: $.description"
            }
        };
    }
    

    @DataProvider(name = "bookIdNumericCases")
    public static Object[][] bookIdNumericCases() {
        return new Object[][]{
            { 1,   200 }, 
            { -1,  404 }, 
            { 201, 404 }  
        };
    }

    @DataProvider(name = "bookIdStringCases")
    public static Object[][] bookIdStringCases() {
        return new Object[][]{
            { "a", "id", "The value 'a' is not valid." },
            { "999999999999999999999", "id", "The value '999999999999999999999' is not valid." }
        };
    }
    
    @DataProvider(name = "deleteBookNumericCases")
    public static Object[][] deleteBookNumericCases() {
        return new Object[][]{
            { 1,   200 },
            { -1,  400 },
            { 0,   404 } 
        };
    }


    @DataProvider(name = "deleteBookStringCases")
    public static Object[][] deleteBookStringCases() {
        return new Object[][]{
            { "abc", "The value 'abc' is not valid." },
            { "999999999999999999999", "The value '999999999999999999999' is not valid." }
        };
    }
    
    
}
