package kitchenpos.order.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    private static final String NOT_EXISTS_ORDER_STATUS = "일치하는 주문 상태가 없습니다.";

    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        return orderStatus.orderStatus();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        return Stream.of(OrderStatus.values())
                .filter(orderStatus -> orderStatus.orderStatus().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_ORDER_STATUS));
    }
}
