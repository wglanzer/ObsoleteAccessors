package com.github.wglanzer.obsoleteaccessors.api;

import com.github.wglanzer.obsoleteaccessors.impl.registry.RegistryDirectory;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.List;

/**
 * Contains all API-Methods for ObsoleteAccessors
 *
 * @author W.Glanzer, 04.09.2017
 */
public abstract class Obsoletes
{

  private Obsoletes()
  {
  }

  /**
   * Converts an old OAAcessor to the newest version.
   * If no ObsoleteVersion is found, <tt>null</tt> will be returned.
   *
   * @param pOldAccessor Accessor which should be converted to newest version
   * @return the newest Accessor available or <tt>null</tt>
   */
  @Nullable
  public static OAAccessor convert(OAAccessor pOldAccessor) throws Exception
  {
    return convert(pOldAccessor, null);
  }

  /**
   * Converts an old OAAcessor to the newest version.
   * If no ObsoleteVersion is found in this category, <tt>null</tt> will be returned.
   *
   * @param pOldAccessor Accessor which should be converted to newest version
   * @param pCategory    Category, or <tt>null</tt> if no filtering should be done
   * @return the newest Accessor available or <tt>null</tt>
   */
  @Nullable
  public static OAAccessor convert(OAAccessor pOldAccessor, @Nullable String pCategory) throws Exception
  {
    return convert(pOldAccessor, pCategory, IRegistryKey.DEFAULT_REGISTRY);
  }

  /**
   * Converts an old OAAcessor to the newest version.
   * If no ObsoleteVersion is found in this category, <tt>null</tt> will be returned.
   *
   * @param pOldAccessor Accessor which should be converted to newest version
   * @param pCategory    Category, or <tt>null</tt> if no filtering should be done
   * @param pRegistryKey Registry to use. Registerable with {@link #register(File)}
   * @return the newest Accessor available or <tt>null</tt>
   */
  @Nullable
  public static OAAccessor convert(OAAccessor pOldAccessor, @Nullable String pCategory, @NotNull IRegistryKey pRegistryKey) throws Exception
  {
    return RegistryDirectory.get(pRegistryKey).convert(pOldAccessor, pCategory);
  }

  /**
   * Returns all registered accessors with the given category, packagename and identifier
   *
   * @param pCategory   Category to search for
   * @param pPkgName    Package to search in
   * @param pIdentifier ID searched
   * @param pKey        Registry to search in
   * @return a list of all found accessors, not <tt>null</tt>
   */
  @NotNull
  public static List<OAAccessor> findAccessors(@Nullable String pCategory, @NotNull String pPkgName, @NotNull String pIdentifier, IRegistryKey pKey)
  {
    return RegistryDirectory.get(pKey).findAccessors(pCategory, pPkgName, pIdentifier);
  }

  /**
   * Registers a new File to the directory.
   * This file has to be a zip-file generated with AnnoSave.
   *
   * @param pRegistryFile File which should be registered
   * @return A key-object to reference the registered file
   */
  @NotNull
  public static IRegistryKey register(@NotNull File pRegistryFile) throws Exception
  {
    return RegistryDirectory.register(pRegistryFile);
  }

  /**
   * Registers a new File to the directory.
   * This file has to be a zip-file generated with AnnoSave.
   *
   * @param pStream Stream which should be registered as registry
   * @return A key-object to reference the registered file
   */
  @NotNull
  public static IRegistryKey register(@NotNull InputStream pStream) throws Exception
  {
    return RegistryDirectory.register(pStream);
  }

  /**
   * Returns a multimap containing all packages in one category.
   * Key = Name of the package
   *
   * @param pCategory Category to search for, <tt>null</tt> = All Categories
   * @param pKey      Key for the registry to search in
   * @return the multimap containing all packages, not <tt>null</tt>
   */
  @NotNull
  public static Multimap<String, OAAccessor> getPackages(@Nullable String pCategory, @NotNull IRegistryKey pKey)
  {
    return RegistryDirectory.get(pKey).getPackages(pCategory);
  }

  /**
   * Unregister a file which was registered before.
   * If it was not registered, nothing will be done.
   *
   * @param pKey Key which holds the reference to the file
   * @return <tt>true</tt> if something was removed
   */
  public static boolean unregister(@NotNull IRegistryKey pKey)
  {
    return RegistryDirectory.unregister(pKey);
  }

}
