package com.github.wglanzer.obsoleteaccessors;

import com.github.wglanzer.obsoleteaccessors.api.*;
import com.github.wglanzer.obsoleteaccessors.api.converters.AttributeConverters;
import com.github.wglanzer.obsoleteaccessors.spi.IAttributeConverter;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

/**
 * @author W.Glanzer, 11.09.2017
 */
@SuppressWarnings("unused")
@ObsoleteVersionContainer(category = "js", pkgName = "container", serialize = true)
public class TestVersionContainerImpl
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
      @ObsoleteVersion(version = 0, pkgName = "obso", id = "getDoubleArr", type = double[].class),
      @ObsoleteVersion(version = 1, id = "getIntList", parameters = {double.class, int[].class}, converter = _IntListToIntArrayConverter.class)
  })
  public int[] getIntArray(String pParam)
  {
    return new int[0];
  }

  @ObsoleteVersions({
      @ObsoleteVersion(version = 0, parameters = double.class, converter = _ParameterConsumerConverter.class, converterAttributes = {"my", "params"})
  })
  public int[] getParameterConsumerConverter(String pParam, String pParam2)
  {
    return new int[0];
  }

  @ObsoleteVersions({
      @ObsoleteVersion(branch = 0, version = 0, pkgName = "branching_pkg"),
      @ObsoleteVersion(branch = 0, version = 1, id = "testBranching_0", parameters = {String.class, int.class}, converter = AttributeConverters.DynamicSizeConverter.class),
      @ObsoleteVersion(branch = 0, version = 2, parameters = {String.class}, converter = AttributeConverters.DynamicSizeConverter.class),
      @ObsoleteVersion(branch = 1, version = 1, type = int.class),
      @ObsoleteVersion(branch = 1, version = 0, parameters = {int.class}, converter = _IntListToIntArrayConverter.class),
      @ObsoleteVersion(branch = 1, version = 2, parameters = {String.class}, converter = AttributeConverters.DynamicSizeConverter.class)
  })
  public void testBranch(String pParam, String pParam2)
  {
  }

  @ObsoleteVersionContainer(category = "js", pkgName = "innerContainer", serialize = true)
  public static class InnerContainer
  {
    @ObsoleteVersions({
        @ObsoleteVersion(version = 0, id = "getContainer", type = TestVersionContainerImpl.class),
        @ObsoleteVersion(version = 1, id = "getMyContainer")
    })
    @Nullable
    public InnerContainer getMyInnerContainer(@NotNull TestVersionContainerImpl pContainer, int pID)
    {
      return null;
    }
  }

  public static class _IntListToIntArrayConverter implements IAttributeConverter
  {
    @NotNull
    @Override
    public List<OAAttribute> convert(@NotNull List<OAAttribute> pAttributes, @NotNull OAAccessor pNextAccessor)
    {
      return Collections.singletonList(new OAAttribute(String.class, String.valueOf(pAttributes.get(0).getValue())));
    }
  }

  public static class _ParameterConsumerConverter implements IAttributeConverter
  {
    private final String[] strings;

    public _ParameterConsumerConverter(String[] pStrings)
    {
      strings = pStrings;
    }

    @NotNull
    @Override
    public List<OAAttribute> convert(@NotNull List<OAAttribute> pAttributes, @NotNull OAAccessor pNextAccessor)
    {
      return Stream.of(strings).map(pString -> new OAAttribute(String.class, pString)).collect(Collectors.toList());
    }
  }

}
