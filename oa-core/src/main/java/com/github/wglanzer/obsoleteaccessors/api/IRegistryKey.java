package com.github.wglanzer.obsoleteaccessors.api;

import com.github.wglanzer.obsoleteaccessors.impl.registry.RegistryKeyImpl;

/**
 * @author W.Glanzer, 20.09.2017
 */
public interface IRegistryKey
{

  IRegistryKey DEFAULT_REGISTRY = new RegistryKeyImpl("DEFAULT");

}
