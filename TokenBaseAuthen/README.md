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

    public class Foo {        
        public void setPassword(String password) {             
            // don't do this             
            if (password.length() > 7) {                  
                throw new InvalidArgumentException("password");             
            }        
        }    
    } 
    
This should be refactored to:
