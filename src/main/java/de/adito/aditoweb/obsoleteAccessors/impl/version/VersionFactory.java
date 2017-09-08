package de.adito.aditoweb.obsoleteAccessors.impl.version;

import de.adito.aditoweb.obsoleteAccessors.api.Function;
import de.adito.aditoweb.obsoleteAccessors.spi.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author W.Glanzer, 08.09.2017
 */
public class VersionFactory
{

  public static IAccessorVersion[] createVersion(ObsoleteVersionContainer pContainer, Method pReflMethod)
  {
    IAccessorVersion[] obsoleteVersions = createVersion(pReflMethod.getDeclaredAnnotations());
    IAccessorVersion latest = new LatestAccessorVersion(pContainer.pkgName(), pReflMethod.getName(), pReflMethod.getReturnType());
    IAccessorVersion[] versions = new IAccessorVersion[obsoleteVersions.length + 1];
    System.arraycopy(obsoleteVersions, 0, versions, 0, obsoleteVersions.length);
    versions[versions.length - 1] = latest;
    return versions;
  }

  public static IAccessorVersion[] createVersion(ObsoleteVersionContainer pContainer, Field pReflField)
  {
    IAccessorVersion[] obsoleteVersions = createVersion(pReflField.getDeclaredAnnotations());
    IAccessorVersion latest = new LatestAccessorVersion(pContainer.pkgName(), pReflField.getName(), pReflField.getType());
    IAccessorVersion[] versions = new IAccessorVersion[obsoleteVersions.length + 1];
    System.arraycopy(obsoleteVersions, 0, versions, 0, obsoleteVersions.length);
    versions[versions.length - 1] = latest;
    return versions;
  }

  public static IAccessorVersion[] createVersion(Annotation[] pAnnotations)
  {
    ArrayList<ObsoleteVersion> obsoleteVersions = new ArrayList<>();
    for (Annotation annotation : pAnnotations)
    {
      if(annotation instanceof ObsoleteVersions)
        obsoleteVersions.addAll(Arrays.asList(((ObsoleteVersions) annotation).value()));
      else if(annotation instanceof ObsoleteVersion)
        obsoleteVersions.add((ObsoleteVersion) annotation);
    }

    return obsoleteVersions.stream()
        .sorted(Comparator.comparingInt(ObsoleteVersion::version))
        .map(pVersion -> new ObsoleteAccessorVersion(pVersion.version(), pVersion.pkgName(), pVersion.id(), pVersion.converter(), pVersion.returnType()))
        .toArray(IAccessorVersion[]::new);
  }

  public static IAccessorVersion createVersion(Function pFunction)
  {
    return new _AccessorVersionWrapper(pFunction);
  }

  private static class _AccessorVersionWrapper extends AbstractAccessorVersion
  {
    public _AccessorVersionWrapper(Function pFunction)
    {
      super(-1, pFunction.getPackageName(), pFunction.getIdentifier(), pFunction.getReturnType());
    }
  }

}
