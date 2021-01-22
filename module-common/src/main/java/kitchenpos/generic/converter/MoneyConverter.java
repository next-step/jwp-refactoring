package kitchenpos.generic.converter;


import kitchenpos.generic.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, Long> {
    @Override
    public Long convertToDatabaseColumn(Money attribute) {
        return attribute.longValue();
    }

    @Override
    public Money convertToEntityAttribute(Long dbData) {
        return Money.price(dbData);
    }
}
