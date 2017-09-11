package de.adito.aditoweb.obsoleteAccessors.impl.attributes.conversion;

import de.adito.aditoweb.obsoleteAccessors.api.*;
import de.adito.aditoweb.obsoleteAccessors.impl.attributes.*;
import de.adito.aditoweb.obsoleteAccessors.spi.IAttributeConverter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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

  @NotNull
  @Override
  public List<IAccessorAttribute> convert(@NotNull List<IAccessorAttribute> pAttributes) throws AttributeConversionException
  {
    // Wrap IAA -> OAA
    List<OAAttribute> attributes = pAttributes.stream()
        .map(pAttribute -> new OAAttribute(pAttribute.getDescription().getType(), pAttribute.getValue()))
        .collect(Collectors.toList());

    // Convert
    List<OAAttribute> converted = parameterConverter.convert(attributes);

    // Wrap OAA -> IAA
    return converted.stream()
        .map(SimpleAccessorAttribute::of)
        .collect(Collectors.toList());
  }

}
