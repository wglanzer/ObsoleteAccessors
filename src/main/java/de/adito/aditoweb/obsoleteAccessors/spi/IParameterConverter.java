package de.adito.aditoweb.obsoleteAccessors.spi;

import de.adito.aditoweb.obsoleteAccessors.api.Parameter;

import java.util.List;

/**
 * @author W.Glanzer, 04.09.2017
 */
public interface IParameterConverter
{
  List<Parameter> convert(List<Parameter> pParameters);

  class DEFAULT implements IParameterConverter
  {
    @Override
    public List<Parameter> convert(List<Parameter> pParameters)
    {
      return pParameters;
    }
  }

}
