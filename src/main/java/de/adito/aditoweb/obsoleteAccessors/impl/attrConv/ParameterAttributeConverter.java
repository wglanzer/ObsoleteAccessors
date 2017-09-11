package de.adito.aditoweb.obsoleteAccessors.impl.attrConv;

import de.adito.aditoweb.obsoleteAccessors.api.OAAttribute;
import de.adito.aditoweb.obsoleteAccessors.impl.attrDescr.ImmutableAccessorAttributeDescription;
import de.adito.aditoweb.obsoleteAccessors.spi.IAttributeConverter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author W.Glanzer, 11.09.2017
 */
public class ParameterAttributeConverter implements IAccessorAttributeConverter
{

  private final IAttributeConverter parameterConverter;

  public ParameterAttributeConverter(IAttributeConverter pParameterConverter)
  {
    parameterConverter = pParameterConverter;
  }

  @Override
  public List<IAccessorAttribute> convert(List<IAccessorAttribute> pAttributes)
  {
    List<OAAttribute> attributes = new ArrayList<>();
    for (IAccessorAttribute value : pAttributes)
      attributes.add(new OAAttribute(value.getValue().getClass(), value.getValue()));

    return parameterConverter.convert(attributes).stream()
        .map(pParameter -> new SimpleAccessorAttribute(new ImmutableAccessorAttributeDescription(pParameter.getType()), pParameter.getValue()))
        .collect(Collectors.toList());
  }

}
