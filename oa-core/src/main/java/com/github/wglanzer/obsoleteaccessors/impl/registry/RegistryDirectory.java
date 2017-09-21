package com.github.wglanzer.obsoleteaccessors.impl.registry;

import com.github.wglanzer.annosave.api.*;
import com.github.wglanzer.obsoleteaccessors.api.*;
import de.adito.picoservice.IPicoRegistry;
import org.apache.commons.io.output.NullOutputStream;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

/**
 * @author W.Glanzer, 20.09.2017
 */
public class RegistryDirectory
{

  private static IConvertableRegistry _DEFAULT_REGISTRY;
  private static Map<IRegistryKey, IConvertableRegistry> directory = new HashMap<>();

  @Nullable
  public static IConvertableRegistry find(IRegistryKey pKey)
  {
    try
    {
      return get(pKey);
    }
    catch (Exception e)
    {
      return null;
    }
  }

  @NotNull
  public static IConvertableRegistry get(IRegistryKey pKey)
  {
    if (Objects.equals(pKey, IRegistryKey.DEFAULT_REGISTRY))
      return _setAndGetDefaultRegistry();

    IConvertableRegistry registry = directory.get(pKey);
    if (registry == null)
      throw new NullPointerException("Key not registered in this directory (key=\"" + pKey + "\")");
    return registry;
  }

  @NotNull
  public static IRegistryKey register(@NotNull File pRegistryFile) throws Exception
  {
    if(!pRegistryFile.exists() || !pRegistryFile.canRead())
      throw new IllegalArgumentException("File is not readable (" + pRegistryFile + ")");

    if(!pRegistryFile.getName().endsWith(".zip"))
      throw new IllegalArgumentException("Not a valid file format (" + pRegistryFile.getName() + ")");

    try
    {
      return register(new FileInputStream(pRegistryFile));
    }
    catch (Exception e)
    {
      // Collect all exceptions here (+RuntimeExceptions) and rethrow
      throw new Exception(e);
    }
  }

  @NotNull
  public static IRegistryKey register(@NotNull InputStream pStream) throws Exception
  {
    try
    {
      IAnnotationContainer[] containers = AnnoSaveGZip.read(pStream);
      RegistryKeyImpl key = new RegistryKeyImpl(null);
      directory.put(key, new ConvertableRegistry(containers));
      return key;
    }
    catch (Exception e)
    {
      // Collect all exceptions here (+RuntimeExceptions) and rethrow
      throw new Exception(e);
    }
  }

  public static boolean unregister(@NotNull IRegistryKey pKey)
  {
    return directory.remove(pKey) != null;
  }

  @NotNull
  private static IConvertableRegistry _setAndGetDefaultRegistry()
  {
    if (_DEFAULT_REGISTRY == null)
    {
      IAnnotationContainer[] containersOnClasspath = IPicoRegistry.INSTANCE.find(Object.class, ObsoleteVersionContainer.class).entrySet().stream()
          .map(pEntry -> AnnoSave.write(pEntry.getKey(), new NullOutputStream()))
          .toArray(IAnnotationContainer[]::new);
      _DEFAULT_REGISTRY = new ConvertableRegistry(containersOnClasspath);
    }
    return _DEFAULT_REGISTRY;
  }

}
