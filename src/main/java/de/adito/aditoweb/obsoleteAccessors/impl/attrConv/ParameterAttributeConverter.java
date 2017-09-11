package de.adito.aditoweb.obsoleteAccessors.impl.attrConv;

import de.adito.aditoweb.obsoleteAccessors.api.Parameter;
import de.adito.aditoweb.obsoleteAccessors.impl.attrDescr.ImmutableAccessorAttributeDescription;
import de.adito.aditoweb.obsoleteAccessors.spi.IParameterConverter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author W.Glanzer, 11.09.2017
 */
public class ParameterAttributeConverter implements IAccessorAttributeConverter
{

  private final IParameterConverter parameterConverter;

  public ParameterAttributeConverter(IParameterConverter pParameterConverter)
  {
    parameterConverter = pParameterConverter;
  }

  @Override
  public List<IAccessorAttribute> convert(List<IAccessorAttribute> pAttributes)
  {
    List<Parameter> parameters = new ArrayList<>();
    for (IAccessorAttribute value : pAttributes)
      parameters.add(new Parameter(value.getValue().getClass(), value.getValue()));

    return parameterConverter.convert(parameters).stream()
        .map(pParameter -> new SimpleAccessorAttribute(new ImmutableAccessorAttributeDescription(pParameter.getType()), pParameter.getValue()))
        .collect(Collectors.toList());
  }

}
