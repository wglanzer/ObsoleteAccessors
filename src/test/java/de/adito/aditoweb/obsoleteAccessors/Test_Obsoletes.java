package de.adito.aditoweb.obsoleteAccessors;

import de.adito.aditoweb.obsoleteAccessors.api.*;
import de.adito.aditoweb.obsoleteAccessors.spi.*;
import org.junit.*;

import java.util.*;

/**
 * @author W.Glanzer, 04.09.2017
 */
public class Test_Obsoletes
{

  @Test
  public void test_convertSimpleFunction() throws Exception
  {
    Parameter parameter1 = new Parameter(double.class, 1.5D);
    Parameter parameter2 = new Parameter(int[].class, new int[]{42, 24, 13});
    Function function = new Function("container", "getDoubleArr", Arrays.asList(parameter1, parameter2), double[].class);
    Function convertedFunction = Obsoletes.convert(function, "js");
    Assert.assertNotNull(convertedFunction);
    Assert.assertEquals("container", convertedFunction.getPackageName());
    Assert.assertEquals("getIntArray", convertedFunction.getIdentifier());
    Assert.assertEquals(int[].class, convertedFunction.getReturnType());
    Assert.assertEquals(Collections.singletonList(new Parameter(String.class, null)), convertedFunction.getParameters());
  }

  @Test
  public void test_convertSimpleField() throws Exception
  {
    // CLASSIFICATION_PRIVATE with returnType INTEGER
    Function field = new Function("container", "CLASSIFICATION_PRIVATE", null, int.class);
    Function classField = Obsoletes.convert(field, "js");
    Assert.assertNotNull(classField);
    Assert.assertEquals("container", classField.getPackageName());
    Assert.assertEquals("CLASSIFICATION_PUBLIC", classField.getIdentifier());
    Assert.assertEquals(String.class, classField.getReturnType());

    // CLASSIFICATION_PRIVATE with returnType STRING
    field = new Function("container", "CLASSIFICATION_PRIVATE", null, String.class);
    classField = Obsoletes.convert(field, "js");
    Assert.assertNotNull(classField);
    Assert.assertEquals("container", classField.getPackageName());
    Assert.assertEquals("CLASSIFICATION_PUBLIC_STRING", classField.getIdentifier());
    Assert.assertEquals(String.class, classField.getReturnType());
  }

  @ObsoleteVersionContainer(category = "js", pkgName = "container")
  public static class JSContainer
  {
    @ObsoleteVersions({
        @ObsoleteVersion(version = 0, id = "CLASSIFICATION_PRIVATE", type = int.class)
    })
    public final static String CLASSIFICATION_PUBLIC = "asdf";

    @ObsoleteVersions({
        @ObsoleteVersion(version = 0, id = "CLASSIFICATION_PRIVATE", type = String.class)
    })
    public final static String CLASSIFICATION_PUBLIC_STRING = "asdf";

    @ObsoleteVersions({
        @ObsoleteVersion(version = 0, id = "getDoubleArr", type = double[].class),
        @ObsoleteVersion(version = 1, id = "getIntList", parameters = {double.class, int[].class})
    })
    public int[] getIntArray(String pParam)
    {
      return new int[0];
    }

  }

}
