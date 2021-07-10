package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.constant.OrderStatus;
import kitchenpos.table.OrderTable;

public class OrderTest {

    private OrderTable 테이블;

    @BeforeEach
    void setUp() {
        테이블 = new OrderTable(1L, 1L, 10, false, null);

    }

    @Test
    @DisplayName("정상 생성 케스트 테스트")
    void creat() {
        Order order = Order.create(테이블.getId());
        assertThat(order.getOrderTableId()).isEqualTo(테이블.getId());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

}
