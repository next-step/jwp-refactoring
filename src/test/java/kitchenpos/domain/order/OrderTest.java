package kitchenpos.domain.order;

import kitchenpos.domain.order.exceptions.InvalidTryOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    @DisplayName("1개 미만의 주문 항목으로 주문 오브젝트를 생성할 수 없다.")
    @Test
    void createFailTest() {
        List<OrderLineItem> emptyOrderLineItems = new ArrayList<>();

        assertThatThrownBy(() -> new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), emptyOrderLineItems))
                .isInstanceOf(InvalidTryOrderException.class)
                .hasMessage("주문하기 위해서는 1개 이상의 주문 항목이 필요합니다.");
    }
}