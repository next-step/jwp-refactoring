package kitchenpos.infrastructure.jpa;

import java.math.BigDecimal;
import javax.persistence.AttributeConverter;
import kitchenpos.domain.Money;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-13
 */
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Money attribute) {
        return attribute.amount;
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal dbData) {
        return Money.won(dbData.longValue());
    }
}
