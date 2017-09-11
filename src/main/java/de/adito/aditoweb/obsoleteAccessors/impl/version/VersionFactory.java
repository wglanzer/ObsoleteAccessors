package de.adito.aditoweb.obsoleteAccessors.impl.version;

import com.google.common.base.Strings;
import de.adito.aditoweb.obsoleteAccessors.api.*;
import de.adito.aditoweb.obsoleteAccessors.api.Parameter;
import de.adito.aditoweb.obsoleteAccessors.impl.attrDescr.*;
import de.adito.aditoweb.obsoleteAccessors.spi.*;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

/**
 * Factory creating IAccessorVersions
 *
 * @author W.Glanzer, 08.09.2017
 */
public class VersionFactory
{

  public static IAccessorVersion[] createVersion(ObsoleteVersionContainer pContainer, Method pReflMethod)
  {
    List<IAccessorAttributeDescription<?>> descriptions = Stream.of(pReflMethod.getParameters())
        .map(pParameter -> new ImmutableAccessorAttributeDescription<>(pParameter.getType()))
        .collect(Collectors.toList());

    return _createVersion(pContainer, pReflMethod.getName(), pReflMethod.getReturnType(), descriptions, pReflMethod.getDeclaredAnnotations());
  }

  public static IAccessorVersion[] createVersion(ObsoleteVersionContainer pContainer, Field pReflField)
  {
    return _createVersion(pContainer, pReflField.getName(), pReflField.getType(), null, pReflField.getDeclaredAnnotations());
  }

  public static IAccessorVersion createVersion(Function pFunction)
  {
    return new _AccessorVersionWrapper(pFunction);
  }

  private static IAccessorVersion[] _createVersion(ObsoleteVersionContainer pContainer, String pLatestName, Class<?> pLatestType,
                                                   List<IAccessorAttributeDescription<?>> pLatestDescriptions, Annotation[] pAnnotations)
  {
    LatestAccessorVersion latest = new LatestAccessorVersion(pContainer.pkgName(), pLatestName, pLatestType, pLatestDescriptions);
    IAccessorVersion[] obsoleteVersions = _createVersion(pAnnotations, latest);
    IAccessorVersion[] versions = new IAccessorVersion[obsoleteVersions.length + 1];
    System.arraycopy(obsoleteVersions, 0, versions, 0, obsoleteVersions.length);
    versions[versions.length - 1] = latest;
    return versions;
  }

  private static IAccessorVersion[] _createVersion(Annotation[] pAnnotations, LatestAccessorVersion pLatest)
  {
    ArrayList<ObsoleteVersion> obsoleteVersionsList = new ArrayList<>();
    for (Annotation annotation : pAnnotations)
    {
      if (annotation instanceof ObsoleteVersions)
        obsoleteVersionsList.addAll(Arrays.asList(((ObsoleteVersions) annotation).value()));
      else if (annotation instanceof ObsoleteVersion)
        obsoleteVersionsList.add((ObsoleteVersion) annotation);
    }

    obsoleteVersionsList.sort(Comparator.comparingInt(ObsoleteVersion::version));

    IAccessorVersion[] versions = new IAccessorVersion[obsoleteVersionsList.size()];
    for (int i = 0; i < obsoleteVersionsList.size(); i++)
    {
      ObsoleteVersion version = obsoleteVersionsList.get(i);
      String pkgName = extract(version, ObsoleteVersion::pkgName, obsoleteVersionsList, i + 1, pLatest::getPkgName);
      String id = extract(version, ObsoleteVersion::id, obsoleteVersionsList, i + 1, pLatest::getId);
      Class<?> type = extract(version, ObsoleteVersion::type, obsoleteVersionsList, i + 1, pLatest::getType);
      Class<?>[] parameters = extract(version, ObsoleteVersion::parameters, obsoleteVersionsList, i + 1, () -> null);
      List<IAccessorAttributeDescription<?>> attributeDescriptions;
      if(parameters != null)
      {
        attributeDescriptions = new ArrayList<>();
        for (Class<?> parameter : parameters)
          attributeDescriptions.add(new ImmutableAccessorAttributeDescription<>(parameter));
      }
      else
        attributeDescriptions = pLatest.getAttributeDescriptions();

      versions[i] = new ObsoleteAccessorVersion(version.version(), pkgName, id, version.converter(), type, attributeDescriptions);
    }

    return versions;
  }

  private static <T> T extract(ObsoleteVersion pCurrentVersion, java.util.function.Function<ObsoleteVersion, T> pExtractor,
                               List<ObsoleteVersion> pObsoleteVersions, int pStartIndex,
                               Supplier<T> pLatestValue)
  {
    // set in current version?
    T value = pExtractor.apply(pCurrentVersion);
    if (_isDefaultValue(value))
    {
      // set in versions after?
      value = findFirstNonNull(pObsoleteVersions, pStartIndex, pExtractor);
      if (_isDefaultValue(value))

        // set in latest version!
        value = pLatestValue.get();
    }

    return value;
  }

  /**
   * Finds the first for which the function does not return null
   *
   * @param pList       List of items
   * @param pStartIndex Optional start-index, default -1
   * @param pFunction   Fucntion to extract items from the lists objects
   * @return the first item not null, or <tt>null</tt> if no item was found
   */
  @Nullable
  private static <T, O> T findFirstNonNull(List<O> pList, int pStartIndex, java.util.function.Function<O, T> pFunction)
  {
    if (pStartIndex == -1)
      pStartIndex = 0;

    for (int i = pStartIndex; i < pList.size(); i++)
    {
      O item = pList.get(i);
      T res = pFunction.apply(item);
      if (res != null)
        return res;
    }

    return null;
  }

  /**
   * @return Returns <tt>true</tt> if the given value is a default-value (empty string, null, or Default-ParameterConverter)
   */
  private static boolean _isDefaultValue(Object pValue)
  {
    return (pValue instanceof String && Strings.isNullOrEmpty((String) pValue)) ||
        pValue instanceof Class<?> && (pValue.equals(Void.class) || pValue.equals(IParameterConverter.DEFAULT.class)) ||
        (pValue != null && pValue.getClass().isArray() && Arrays.equals((Object[]) pValue, new Class<?>[]{Void.class})) ||
        pValue == null;
  }

  public static List<IAccessorAttributeDescription<?>> createAttributes(List<Parameter> pParameters) //todo
  {
    if(pParameters == null || pParameters.isEmpty())
      return Collections.emptyList();
    ArrayList<IAccessorAttributeDescription<?>> attributes = new ArrayList<>(pParameters.size());
    for (Parameter parameter : pParameters)
      attributes.add(new ImmutableAccessorAttributeDescription<>(parameter.getType()));
    return attributes;
  }

  /**
   * AccessorVersion-Wrapper to wrap Functions in IAccessorVersions
   */
  private static class _AccessorVersionWrapper extends AbstractAccessorVersion
  {
    public _AccessorVersionWrapper(Function pFunction)
    {
      super(-1, pFunction.getPackageName(), pFunction.getIdentifier(), pFunction.getReturnType(), createAttributes(pFunction.getParameters()));
    }
  }

}
