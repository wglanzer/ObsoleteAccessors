package de.adito.aditoweb.obsoleteAccessors;

import de.adito.aditoweb.obsoleteAccessors.api.*;
import de.adito.aditoweb.obsoleteAccessors.spi.*;
import org.junit.*;

/**
 * @author W.Glanzer, 04.09.2017
 */
public class Test_Obsoletes
{

  @Test
  public void test_convertSimpleFunction() throws Exception
  {
    // getDoubleArr -> getIntArray
    Function function = new Function("container", "getDoubleArr", new Object[0], double[].class);
    Function convertedFunction = Obsoletes.convert(function, "js");
    Assert.assertNotNull(convertedFunction);
    Assert.assertEquals("container", convertedFunction.getPackageName());
    Assert.assertEquals("getIntArray", convertedFunction.getIdentifier());
    Assert.assertEquals(int[].class, convertedFunction.getReturnType());

    // getIntList -> getIntArray
    function = new Function("container", "getIntList", new Object[0], int[].class);
    convertedFunction = Obsoletes.convert(function, "js");
    Assert.assertNotNull(convertedFunction);
    Assert.assertEquals("container", convertedFunction.getPackageName());
    Assert.assertEquals("getIntArray", convertedFunction.getIdentifier());
    Assert.assertEquals(int[].class, convertedFunction.getReturnType());
  }

  @ObsoleteVersionContainer(category = "js", pkgName = "container")
  public static class JSContainer
  {
    @ObsoleteVersions({
        @ObsoleteVersion(version = 0, id = "CLASSIFICATION_PRIVATE")
    })
    public final static String CLASSIFICATION_PUBLIC = "asdf";

    @ObsoleteVersions({
        @ObsoleteVersion(version = 0, id = "getDoubleArr", type = double[].class),
        @ObsoleteVersion(version = 1, id = "getIntList")
    })
    public int[] getIntArray()
    {
      return new int[0];
    }

  }

}
