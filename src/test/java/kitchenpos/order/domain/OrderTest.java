package kitchenpos.order.domain;

import kitchenpos.order.constant.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("주문기능")
public class OrderTest {

    @Test
    @DisplayName("주문의 상태변경 기능 (완료된 주문은 변경할 수 없다.)")
    void orderTest2() {
        Order order = new Order(null, OrderStatus.COMPLETION.name(), LocalDateTime.now());

        assertThatThrownBy(() -> order.changeStatus(OrderStatus.COMPLETION.name())).isInstanceOf(IllegalArgumentException.class);
    }

}
