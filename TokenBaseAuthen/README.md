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
