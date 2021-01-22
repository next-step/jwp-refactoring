package kitchenpos.generic.converter;


import kitchenpos.domain.Quantity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class QuantityConverter implements AttributeConverter<Quantity, Long> {

    @Override
    public Long convertToDatabaseColumn(Quantity attribute) {
        return attribute.longValue();
    }

    @Override
    public Quantity convertToEntityAttribute(Long dbData) {
        return Quantity.of(dbData);
    }
}
