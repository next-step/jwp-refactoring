package kitchenpos.converter;


import kitchenpos.infra.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
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
