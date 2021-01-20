package kitchenpos.domain.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class QuantityConverter implements AttributeConverter<Quantity, Long> {
    @Override
    public Long convertToDatabaseColumn(Quantity attribute) {
        return attribute.getQuantity();
    }

    @Override
    public Quantity convertToEntityAttribute(Long dbData) {
        return new Quantity(dbData);
    }
}
