package com.github.wglanzer.obsoleteaccessors;

import com.github.wglanzer.obsoleteaccessors.api.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.util.*;

/**
 * @author W.Glanzer, 04.09.2017
 */
@RunWith(Parameterized.class)
public class Test_Obsoletes
{

  /**
   * Check it with the DEFAULT-Registry (Classloader) and the annosave.zip-Loader.
   * Both registrys have to return the same values.
   */
  @Parameterized.Parameter()
  public IRegistryKey key;

  @Test
  public void test_convertSimpleFunction() throws Exception
  {
    OAAttribute attribute1 = new OAAttribute(double.class, 1.5D);
    OAAttribute attribute2 = new OAAttribute(int[].class, new int[]{42, 24, 13});
    OAAccessor accessor = new OAAccessor("obso", "getDoubleArr", Arrays.asList(attribute1, attribute2), double[].class);
    OAAccessor convertedAccessor = Obsoletes.convert(accessor, "js", key);
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
    OAAccessor classField = Obsoletes.convert(field, "js", key);
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
  public void test_convertWithoutType() throws Exception
  {
    OAAttribute attribute1 = new OAAttribute(double.class, 1.5D);
    OAAttribute attribute2 = new OAAttribute(int[].class, new int[]{42, 24, 13});
    OAAccessor field = new OAAccessor("container", "getIntList", Arrays.asList(attribute1, attribute2), int[].class);
    OAAccessor classField = Obsoletes.convert(field, "js", key);
    Assert.assertNotNull(classField);
  }

  @Test
  public void test_idChangedInSubClass() throws Exception
  {
    OAAttribute attribute1 = new OAAttribute(TestVersionContainerImpl.class, null);
    OAAttribute attribute2 = new OAAttribute(int.class, null);
    OAAccessor field = new OAAccessor("innerContainer", "getMyContainer", Arrays.asList(attribute1, attribute2), TestVersionContainerImpl.InnerContainer.class);
    OAAccessor convert = Obsoletes.convert(field, "js", key);
    Assert.assertNotNull(convert);
  }

  @Test(expected = RuntimeException.class)
  public void test_convertFunctionNotAvailableInPackage() throws Exception
  {
    OAAttribute attribute1 = new OAAttribute(double.class, 1.5D);
    OAAttribute attribute2 = new OAAttribute(int[].class, new int[]{42, 24, 13});
    OAAccessor accessor = new OAAccessor("obso", "getDoubleArr", Arrays.asList(attribute1, attribute2), double[].class);
    Obsoletes.convert(accessor, "NO_PACKAGE_AVAILABLE", key);
  }

  @Test
  public void test_convertInnerContainer() throws Exception
  {
    OAAccessor accessor = new OAAccessor("innerContainer", "getContainer", Arrays.asList(new OAAttribute(TestVersionContainerImpl.class, null), new OAAttribute(int.class, null)), TestVersionContainerImpl.class);
    OAAccessor converted = Obsoletes.convert(accessor, "js", key);
    Assert.assertNotNull(converted);
    Assert.assertEquals(converted.getIdentifier(), "getMyInnerContainer");
    Assert.assertEquals(converted.getPackageName(), "innerContainer");
    Assert.assertEquals(converted.getType(), TestVersionContainerImpl.InnerContainer.class);
    Assert.assertEquals(converted.getAttributes(), Arrays.asList(new OAAttribute(TestVersionContainerImpl.class, null), new OAAttribute(int.class, null)));
  }

  @Test
  public void test_convertParameters() throws Exception
  {
    OAAccessor accessor = new OAAccessor("container", "getParameterConsumerConverter", Collections.singletonList(new OAAttribute(double.class, 5)), int[].class);
    Assert.assertEquals(5, accessor.getAttributes().get(0).getValue()); //pre-convert
    OAAccessor converted = Obsoletes.convert(accessor, "js", key);
    Assert.assertNotNull(converted);
    Assert.assertEquals(2, converted.getAttributes().size()); // post-convert
    Assert.assertEquals("my", converted.getAttributes().get(0).getValue());
    Assert.assertEquals("params", converted.getAttributes().get(1).getValue());
  }

  @Test
  public void test_branching() throws Exception
  {
    // testBranching_0 -> testBranch
    OAAccessor accessor = new OAAccessor("branching_pkg", "testBranching_0", Arrays.asList(new OAAttribute(String.class, "asdf"), new OAAttribute(int.class, 5)), void.class);
    OAAccessor converted = Obsoletes.convert(accessor, "js", key);
    Assert.assertNotNull(converted);
    Assert.assertEquals(2, converted.getAttributes().size());
    Assert.assertEquals("asdf", converted.getAttributes().get(0).getValue());
    Assert.assertEquals(null, converted.getAttributes().get(1).getValue());
    Assert.assertEquals("testBranch", converted.getIdentifier());
    Assert.assertEquals(void.class, converted.getType());

    // testBranch (int) : int -> testBranch (String) : void
    accessor = new OAAccessor("container", "testBranch", Collections.singletonList(new OAAttribute(int.class, 42)), int.class);
    converted = Obsoletes.convert(accessor, "js", key);
    Assert.assertNotNull(converted);
    Assert.assertEquals(2, converted.getAttributes().size());
    Assert.assertEquals("42", converted.getAttributes().get(0).getValue());
    Assert.assertEquals(null, converted.getAttributes().get(1).getValue());
    Assert.assertEquals("testBranch", converted.getIdentifier());
    Assert.assertEquals(void.class, converted.getType());
  }

  @Parameterized.Parameters(name = "{0}")
  public static Object[] data() throws Exception
  {
    IRegistryKey annosaveRegistryByFile = Obsoletes.register(new File("target/test-classes/annosave.zip"));
    IRegistryKey annosaveRegistryByStream = Obsoletes.register(new FileInputStream(new File("target/test-classes/annosave.zip")));
    return new IRegistryKey[]{IRegistryKey.DEFAULT_REGISTRY, annosaveRegistryByFile, annosaveRegistryByStream};
  }

}
