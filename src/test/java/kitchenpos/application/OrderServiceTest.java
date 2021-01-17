package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends BaseTest{
    private Order expected;

    @Autowired
    OrderService orderService;

    @BeforeEach
    public void setUp() {
        super.setUp();
        List<Order> orders = orderService.list();
        expected = orders.get(0);
    }

    @DisplayName("주문 등록")
    @Test
    void create() {
        Order actual = orderService.create(expected);

        assertThat(actual.getOrderTableId()).isEqualTo(expected.getOrderTableId());
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        expected.setOrderStatus(OrderStatus.MEAL.name());
        Order actual = orderService.changeOrderStatus(expected.getId(), expected);

        assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
    }

    @DisplayName("주문 상태 변경 예외 - 이미 완료된 상태 변경할 경우")
    @Test
    void validCopletionOrderStatus() {
        expected.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(expected.getId(), expected);

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(expected.getId(), expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}