package com.github.wglanzer.obsoleteaccessors.impl.registry;

import com.github.wglanzer.obsoleteaccessors.spi.IRegistryKey;
import com.google.common.base.Objects;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author W.Glanzer, 20.09.2017
 */
public class RegistryKeyImpl implements IRegistryKey
{

  private String id;

  public RegistryKeyImpl(@Nullable String pId)
  {
    id = pId == null ? UUID.randomUUID().toString() : pId;
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO) return true;
    if (pO == null || getClass() != pO.getClass()) return false;
    RegistryKeyImpl that = (RegistryKeyImpl) pO;
    return Objects.equal(id, that.id);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(id);
  }

  @Override
  public String toString()
  {
    return String.valueOf(id);
  }
}
