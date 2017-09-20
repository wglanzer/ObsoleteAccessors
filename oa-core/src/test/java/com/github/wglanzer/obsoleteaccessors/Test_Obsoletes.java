package com.github.wglanzer.obsoleteaccessors;

import com.github.wglanzer.obsoleteaccessors.api.*;
import com.github.wglanzer.obsoleteaccessors.spi.IRegistryKey;
import org.junit.*;

import java.io.File;
import java.util.*;

/**
 * @author W.Glanzer, 04.09.2017
 */
public class Test_Obsoletes
{

  @Test
  public void test_convertSimpleFunction() throws Exception
  {
    OAAttribute attribute1 = new OAAttribute(double.class, 1.5D);
    OAAttribute attribute2 = new OAAttribute(int[].class, new int[]{42, 24, 13});
    OAAccessor accessor = new OAAccessor("obso", "getDoubleArr", Arrays.asList(attribute1, attribute2), double[].class);
    OAAccessor convertedAccessor = Obsoletes.convert(accessor, "js");
    Assert.assertNotNull(convertedAccessor);
    Assert.assertEquals("container", convertedAccessor.getPackageName());
    Assert.assertEquals("getIntArray", convertedAccessor.getIdentifier());
    Assert.assertEquals(int[].class, convertedAccessor.getType());
    Assert.assertEquals(Collections.singletonList(new OAAttribute(String.class, "1.5")), convertedAccessor.getAttributes());
  }

  @Test
  public void test_convertSimpleField() throws Exception
  {
    // CLASSIFICATION_PRIVATE with returnType INTEGER
    OAAccessor field = new OAAccessor("container", "CLASSIFICATION_PRIVATE", null, int.class);
    OAAccessor classField = Obsoletes.convert(field, "js");
    Assert.assertNotNull(classField);
    Assert.assertEquals("container", classField.getPackageName());
    Assert.assertEquals("CLASSIFICATION_PUBLIC", classField.getIdentifier());
    Assert.assertEquals(String.class, classField.getType());

    // CLASSIFICATION_PRIVATE with returnType STRING
    field = new OAAccessor("container", "CLASSIFICATION_PRIVATE", null, String.class);
    classField = Obsoletes.convert(field, "js");
    Assert.assertNotNull(classField);
    Assert.assertEquals("container", classField.getPackageName());
    Assert.assertEquals("CLASSIFICATION_PUBLIC_STRING", classField.getIdentifier());
    Assert.assertEquals(String.class, classField.getType());
  }

  @Test
  public void test_zip_convertSimpleField() throws Exception
  {
    IRegistryKey key = Obsoletes.register(new File("target/test-classes/annosave.zip"));

    // CLASSIFICATION_PRIVATE with returnType INTEGER
    OAAccessor field = new OAAccessor("container", "CLASSIFICATION_PRIVATE", null, int.class);
    OAAccessor classField = Obsoletes.convert(field, "js", key);
    Assert.assertNotNull(classField);
    Assert.assertEquals("container", classField.getPackageName());
    Assert.assertEquals("CLASSIFICATION_PUBLIC", classField.getIdentifier());
    Assert.assertEquals(String.class, classField.getType());

    // CLASSIFICATION_PRIVATE with returnType STRING
    field = new OAAccessor("container", "CLASSIFICATION_PRIVATE", null, String.class);
    classField = Obsoletes.convert(field, "js", key);
    Assert.assertNotNull(classField);
    Assert.assertEquals("container", classField.getPackageName());
    Assert.assertEquals("CLASSIFICATION_PUBLIC_STRING", classField.getIdentifier());
    Assert.assertEquals(String.class, classField.getType());
  }

}
