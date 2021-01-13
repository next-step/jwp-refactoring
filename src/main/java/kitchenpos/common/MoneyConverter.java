package kitchenpos.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money,BigDecimal>{

    @Override
    public BigDecimal convertToDatabaseColumn(final Money attribute) {
        return attribute.getAmount();
    }

    @Override
    public Money convertToEntityAttribute(final BigDecimal dbData) {
        return new Money(dbData);
    }
}
