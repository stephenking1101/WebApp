# Detailed rules
## Packages
* group related classes into a package, so small change should not affect classes outside a package
* be aware of package dependencies
* if possible, avoid camel case strategy in package definition, use single words instead
## Methods
* reduce number of arguments
* avoid boolean arguments as they declare that the method does more than one thing
* return as soon as possible in order to reduce hard to read nested blocks
## Variables
* declare as late as possible
* all class constants (public/private static final) have names in capital letters separated by underscore
* enums are compared using ==
## Classes
* contain methods with the same level of abstraction
* use inner classes (promote static inner class)
* use lambdas instead of anonymous classes
## Enums
* enum name uses UpperCamelCase (like every other class)
* enum name should be singular
* name of enum's elements are in uppercase and words are separated by underscores
## Error Handling
* do not ignore exceptions with empty catch statement
* do not rethrow exceptions without providing additional context and exception cause (exception chanining)
* do not use exceptions for normal flow
* do not return/pass null, especially while operating on collections
## Interfaces
* promote interfaces for public API
* do not introduce interface if a class is supposed to be implemented only in one way, it's overdesign (Spring can handle this)
* do not introduce interfaces for domain/dto/java beans classes
* programme using interfaces (e.g. Set<> instead of HashSet<>)
## Comments
* avoid comments in favour of self-documented code
* use javadocs for public API (it does not mean every public method)
* remove javadoc if it does not provide more than method signature itself, eg. /** Creates profile */ createProfile(...)
* class should not have automatic class comment when it does not contain any meaningful class function descrption
## Tests
* provide tests for all changes implemented in production code (test important logic not POJOs)
* always use assertions to verify tests results (not java assert)
* make them fast
* test code is still code so not treat it worse than production code
* do not test the infrastructure, but the logic behind
* promote parameterized tests if possible
* JunitParams is recommended library for parameterized tests
* use mocked classes (Mockito) instead of building integration tests with Spring Contexts
* split them up into given/when/then sections (use comments //given // when // then)
* verify code coverage (e.g. EclEmma)

## Simplifying Boolean Conditions:
The below code is written in a bad way-
```
    boolean a = true;    
    if (a == true) {    ...    } 
```    
It can be simplified as-
```
    boolean a = true;    
    if (a) {    ...    } 
```    
## Using Magic Numbers:
A magic number is a direct usage of a number in the code.
```
    public class Foo {        
        public void setPassword(String password) {             
            // don't do this             
            if (password.length() > 7) {                  
                throw new InvalidArgumentException("password");             
            }        
        }    
    } 
```    
This should be refactored to:
```
    public class Foo {
        public static final int MAX_PASSWORD_SIZE = 7;      
        
        public void setPassword(String password) {      
            if (password.length() > MAX_PASSWORD_SIZE) {                 
                throw new InvalidArgumentException("password");              
            }       
        } 
    }
```

It improves readability of the code and it's easier to maintain. Imagine the case where you need to set the size of the password field in the GUI. If you use a magic number, whenever the max size changes, you have to change in two code locations. If you forget one, this will lead to inconsistencies.

## Avoiding NullPointerExceptions:
## Why NullPointerException occur:-

NullPointerException is a situation in code where you try to access/modify an object which has not been initialized yet. It essentially means that object reference variable is not pointing anywhere and refers to nothing or ‘null’. A simple example can be:
```
    package com.howtodoinjava.demo.npe;
    public class SampleNPE {    
        public static void main(String[] args) {        
            String s = null;        
            System.out.println(s.toString()); // s is un-initialized and is null    
        }
    }
```

## Common places where NullPointerException usually occur:-
## NullPointerException can occur anywhere in the code for various reasons but here is a list of the most frequent ones:
  1. Invoking methods on an object which is not initialized
  2. Parameters passed in a method are null
  3. Calling 'toString()' or 'equals()' method on object which is null
  4. Comparing object properties in if-block without checking null equality
  5. Incorrect configuration for frameworks like spring which works on dependency injection
  6. Using synchronized on an object which is null
  7. Chained statements i.e. multiple method calls in a single statement
  
  *This is not an exhaustive list. There are several other places and reasons also.*
  
## Best ways to avoid NullPointerException:-  
  
1. Ternary Operator
  
This operator results to the value on the left hand side if not null else right hand side is evaluated. It has syntax like :
  
```
  boolean expression ? value1 : value2;
```
  
If expression is evaluated as true then entire expression returns value1 otherwise value2. Its more like if-else construct but it is more effective and expressive. To prevent NullPointerException (NPE), use this operator like below code:
  
```
  String str = (param == null) ? "NA" : param;
```
     
2. Use Apache commons StringUtils for String operations
  
Apache commons lang package is a collection of several utility classes for various kind of operations. One of them is StringUtils.java. Use StringUtils.isNotEmpty() for verifying if string passed as parameter is null or empty string. If it is not null or empty; then use it further.Other similar methods are StringUtils. IsEmpty(), and StringUtils.equals().
     
```
    if (StringUtils.isNotEmpty(obj.getvalue())){
        String s = obj.getvalue();
        ....
    }
```
     
3. Check Method Arguments for null very early
  
You should always put input validation at the beginning of your method so that the rest of your code does not have to deal with the possibility of incorrect input. So if someone passes in a null, things will break early in the stack rather than in some deeper location where the root problem will be rather difficult to identify. Aiming for fail-fast behavior is a good choice in most situations.
     
4. Consider Primitives Rather than Objects
  
Null problem occurs where object references points to nothing. So it is always safe to use primitives as much as possible because they does not suffer with null references. All primitives must have some default values also attached so beware of it.

5. Carefully Consider Chained Method Calls
  
While chained statements are nice to look at in the code, they are not NPE friendly. A single statement spread over several lines will give you the line number of the first line in the stack trace regardless of where it occurs.

```
ref.method1().method2().method3().methods4();
```

6. Use String.valueOf() Rather than toString()

If you have to print the string representation of any object, the don’t use object.toString(). This is a very soft target for NPE. Instead use String.valueOf(object).Even if object is null in second method, it will not give exception and will prints ‘null’ to output stream.

7. Avoid returning null from your methods

An awesome tip to avoid NPE is to return empty strings or empty collections rather than a null. Do this consistently across your application. You will note that a bucket load of null checks become unneeded if you do so.An example could be:

```
List<string> data = null;
@SuppressWarnings("unchecked")
public List getDataDemo(){    
    if(data == null)    
    return Collections.EMPTY_LIST; //Returns unmodifiable list    
    return data;
}
```

Users of above method, even if they missed the null check, will not see ugly NPE.

8. Discourage Passing of Null Parameters

In some method declarations the method expects two or more parameters. If one of the parameters is passed as null, then also method works in some different manner. Avoid this.

In stead you should define two methods (overload); one with single parameter and second with two parameters. Make parameters passing mandatory. This helps a lot when writing application logic inside methods because you are sure that method parameters will not be null; so you don’t put unnecessary assumptions and assertions.

9. Call String.equals(String) on ‘Safe’ Non-Null String

In stead of writing below code for string comparison

```
public class SampleNPE {    
     public void demoEqualData(String param) {        
         if (param.equals("check me")) {            
             // some code        
         }    
     }
}
```

write above code like this. This will not cause in NPE even if param is passed as null.

```
public class SampleNPE {    
    public void demoEqualData(String param) {        
        if ("check me".equals(param)) // Do like this        
        {            
            // some code        
        }    
    }
}
```

FindBugs is a good tool which detects possible null pointers in the code.

## Properly Closing Resources:
In Java, memory bugs often appear as performance problems, because memory leaks usually cause performance degradation. Because Java manages the memory automatically, developers do not control when and how garbage is collected. To avoid memory leaks, check your applications to make sure they:

1. Release JDBC ResultSet, Statement, or Connection.
2. Release failures here are usually in error conditions. Use a 'finally' block to make sure these objects are released appropriately.
3. Release instance or resource objects that are stored in static tables.

Perform clean up on serially reusable objects.

An example is appending error messages to a Vector defined in a serially reusable object. The application never cleaned the Vector before it was given to the next user. As the object was reused over and over again, error messages accumulated, causing a memory leak that was difficult to track down.

## Using Generics:
Generics enable types (classes and interfaces) to be parameters when defining classes, interfaces and methods. Much like the more familiar formal parameters used in method declarations, type parameters provide a way for you to re-use the same code with different inputs. The difference is that the inputs to formal parameters are values, while the inputs to type parameters are types.

Code that uses generics has many benefits over non-generic code:
