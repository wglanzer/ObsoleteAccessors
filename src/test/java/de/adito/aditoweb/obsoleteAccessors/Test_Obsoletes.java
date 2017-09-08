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
  public void test_convertSimple() throws Exception
  {
    Function function = new Function("container", "getDoubleList", new Object[0], double[].class);
    Function convertedFunction = Obsoletes.convert(function, "js");
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
        @ObsoleteVersion(version = 0, pkgName = "container", id = "getDoubleList", returnType = double[].class),
        @ObsoleteVersion(version = 1, pkgName = "container", id = "getIntList", returnType = double[].class)
    })
    public int[] getIntArray()
    {
      return new int[0];
    }

  }

}