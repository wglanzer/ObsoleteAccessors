# Obsolete Accessors 
[![Build Status](https://travis-ci.org/wglanzer/obsoleteaccessors.svg?branch=master)](https://travis-ci.org/wglanzer/obsoleteaccessors)

ObsoleteAccessors is a small framework for method versioning via annotation.

The latest dependency is available in maven-central:
 ```xml
 <dependency>
     <groupId>com.github.wglanzer.obsoleteaccessors</groupId>
     <artifactId>oa-core</artifactId>
     <version>1.0.0</version>
 </dependency>
 ````
 
 ## How to use - Easy example
 
```java
@ObsoleteVersionContainer(pkgName = "myPackage", category = "myCategory", serialize = true)
class TestClass 
{
  @ObsoleteVersion(version = 0, pkgName = "branching_pkg")
  public void testMethod(String pParam, String pParam2)
  {
  } 
}
```

This small code fragment shows how to annotate a method/class for ObsoleteVersions-Framework correctly.

Description: The "TestClass" is available under the package "myPackage" in the category "myCategory" with a method named "testMethod" which was in the "branching_pkg"-package before.

A **category** is an identifier that shows which ObsoleteVersionContainers belong together so the framework can find the right accessors quicker. 
It does not have to be set - no category = no quick search.

The **serialize** flag indicates, if the framework should create an annosave.zip-File in the target folder while compiling your class (annotation processor).
This is necessary if your annotated classes are not available at runtime.

A simple query example could look like the following:

```java
Obsoletes.convert(new OAAccessor("branching_pkg", "testMethod", 
                                 Arrays.asList(new OAAttribute(String.class, "myStringValue"), new OAAttribute(String.class, "mySecondValue")), 
                                 void.class), "js");
```

Here you describe your "old" method, which was in package "branching_pkg", and reconstruct it with the given type and parameters. The parameters have to be in the correct order
and can contain a value. After this "convert()"-call the OAAccessor is converted to the newest version of the method. So after this your resulting OAAccessor is in the package "myPackage".