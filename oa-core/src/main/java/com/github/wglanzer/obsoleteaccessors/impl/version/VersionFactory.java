package com.github.wglanzer.obsoleteaccessors.impl.version;

import com.github.wglanzer.annosave.api.*;
import com.github.wglanzer.annosave.api.containers.IMethodContainer;
import com.github.wglanzer.obsoleteaccessors.api.*;
import com.github.wglanzer.obsoleteaccessors.impl.attributes.*;
import com.github.wglanzer.obsoleteaccessors.impl.util.NotChangedType;
import com.github.wglanzer.obsoleteaccessors.spi.IAttributeConverter;
import com.google.common.base.Strings;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Factory creating IAccessorVersions
 *
 * @author W.Glanzer, 08.09.2017
 */
public class VersionFactory
{

  public static IAccessorVersion[] createVersion(IAnnotationContainer pRootContainer, IAnnotationContainer pContainer,
                                                 Function<IAccessorVersion, IAccessorVersion> pNextVersionSupplier)
  {
    try
    {
      String pkgName = pRootContainer.getAnnotation(ObsoleteVersionContainer.class).getParameterValue("pkgName", String.class);

      List<IAccessorAttributeDescription<?>> descriptions = null;
      if(pContainer instanceof IMethodContainer)
      {
        descriptions = Stream.of(((IMethodContainer) pContainer).getMethodParameters())
            .map(pParameter -> SimpleAccessorAttributeDescription.of(new OAAttribute(pParameter.getInstance(), null)))
            .collect(Collectors.toList());
      }

      return _createVersion(pkgName, pContainer.getName(), pContainer.getType().getInstance(), descriptions, pContainer.getAnnotations(), pNextVersionSupplier);
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public static IAccessorVersion createVersion(OAAccessor pAccessor)
  {
    return new _AccessorVersionWrapper(pAccessor);
  }

  private static IAccessorVersion[] _createVersion(String pRootPackageName, String pLatestName, Class<?> pLatestType,
                                                   List<IAccessorAttributeDescription<?>> pLatestDescriptions, IAnnotation[] pAnnotations,
                                                   Function<IAccessorVersion, IAccessorVersion> pNextVersionSupplier)
  {
    LatestAccessorVersion latest = new LatestAccessorVersion(UUID.randomUUID().toString(), pRootPackageName, pLatestName, pLatestType, pLatestDescriptions);
    IAccessorVersion[] obsoleteVersions = _createVersion(pRootPackageName, pAnnotations, latest, pNextVersionSupplier);
    IAccessorVersion[] versions = new IAccessorVersion[obsoleteVersions.length + 1];
    System.arraycopy(obsoleteVersions, 0, versions, 0, obsoleteVersions.length);
    versions[versions.length - 1] = latest;
    return versions;
  }

  private static IAccessorVersion[] _createVersion(String pRootPackageName, IAnnotation[] pAnnotations, LatestAccessorVersion pLatest,
                                                   Function<IAccessorVersion, IAccessorVersion> pNextVersionSupplier)
  {
    ArrayList<IAnnotation> obsoleteVersionsList = new ArrayList<>();
    for (IAnnotation annotation : pAnnotations)
    {
      Class<?> type = annotation.getType().getInstance();
      if(type != null) //type has to be resolvable, because the classes are ours o.O
      {
        if (ObsoleteVersions.class.isAssignableFrom(type))
          obsoleteVersionsList.addAll(Arrays.asList((IAnnotation[]) annotation.getParameters()[0].getValue()));
        else if (ObsoleteVersion.class.isAssignableFrom(type))
          obsoleteVersionsList.add(annotation);
      }
    }

    obsoleteVersionsList.sort(Comparator.comparingInt(VersionFactory::_getBranch)
                                  .thenComparingInt(VersionFactory::_getVersion));

    IAccessorVersion[] versions = new IAccessorVersion[obsoleteVersionsList.size()];
    for (int i = 0; i < obsoleteVersionsList.size(); i++)
    {
      IAnnotation version = obsoleteVersionsList.get(i);
      String pkgName = extract(version, "pkgName", obsoleteVersionsList, i + 1, pLatest::getPkgName);
      if(Strings.isNullOrEmpty(pkgName))
        pkgName = pRootPackageName;

      String id = extract(version, "id", obsoleteVersionsList, i + 1, pLatest::getId);
      Class<?> type = extract(version, "type", obsoleteVersionsList, i + 1, pLatest::getType);
      Class<?>[] parameters = extract(version, "parameters", obsoleteVersionsList, i + 1, () -> null);
      List<IAccessorAttributeDescription<?>> attributeDescriptions;
      if(parameters != null)
      {
        attributeDescriptions = new ArrayList<>();
        for (Class<?> clazz : parameters)
          attributeDescriptions.add(SimpleAccessorAttributeDescription.of(new OAAttribute(clazz, null)));
      }
      else
        attributeDescriptions = pLatest.getAttributeDescriptions();

      versions[i] = new ObsoleteAccessorVersion(UUID.randomUUID().toString(), _getBranch(version), _getVersion(version), pkgName, id,
                                                (Class<? extends IAttributeConverter>) version.getParameterValue("converter"),
                                                (String[]) version.getParameterValue("converterAttributes"),
                                                type, attributeDescriptions, pNextVersionSupplier);
    }

    return versions;
  }

  private static <T> T extract(IAnnotation pCurrentVersion, String pParameterName,
                               List<IAnnotation> pObsoleteVersions, int pStartIndex,
                               Supplier<T> pLatestValue)
  {
    // set in current version?
    T value = (T) pCurrentVersion.getParameterValue(pParameterName);
    if (_isDefaultValue(value))
    {
      // set in versions after in current branch?
      value = findFirstNonNull(pObsoleteVersions, pStartIndex, pAnno -> {
        int myBranch = _getBranch(pCurrentVersion);
        int thisBranch = _getBranch(pAnno);
        if(myBranch == thisBranch)
          return (T) pAnno.getParameterValue(pParameterName);
        return null;
      });

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
        pValue instanceof Class<?> && (pValue.equals(Void.class) || pValue.equals(IAttributeConverter.DEFAULT.class) || pValue.equals(NotChangedType.class)) ||
        (pValue != null && pValue.getClass().isArray() && Arrays.equals((Object[]) pValue, new Class<?>[]{NotChangedType.class})) ||
        pValue == null;
  }

  /**
   * @return The "version"-Parameter of the given annotation
   */
  private static int _getVersion(IAnnotation pAnnotation)
  {
    return Integer.parseInt(String.valueOf(pAnnotation.getParameterValue("version")));
  }

  /**
   * @return The "branch"-Parameter of the given annotation
   */
  private static int _getBranch(IAnnotation pAnnotation)
  {
    return Integer.parseInt(String.valueOf(pAnnotation.getParameterValue("branch")));
  }

  /**
   * AccessorVersion-Wrapper to wrap Functions in IAccessorVersions
   */
  private static class _AccessorVersionWrapper extends AbstractAccessorVersion
  {
    public _AccessorVersionWrapper(OAAccessor pAccessor)
    {
      super(pAccessor.getUUID(), -1, pAccessor.getPackageName(), pAccessor.getIdentifier(), pAccessor.getType(), pAccessor.getAttributes().stream()
          .map(SimpleAccessorAttributeDescription::of)
          .collect(Collectors.toList()));
    }

    @Override
    public int getBranch()
    {
      return -1;
    }

    @Override
    public int getVersion()
    {
      return -1;
    }
  }

}
