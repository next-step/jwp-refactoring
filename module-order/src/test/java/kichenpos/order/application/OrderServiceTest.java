package kichenpos.order.application;

import kichenpos.order.domain.Order;
import kichenpos.order.domain.OrderLineItem;
import kichenpos.order.domain.OrderLineItemsTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kichenpos.order.domain.OrderStatus.COMPLETION;
import static kichenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test
    void 주문을_등록한다() {
        // when
        Order result = orderService.create(new Order(8L, createOrderLineItems(), orderedTime()));

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 주문_목록을_조회한다() {
        // given
        Order saved = orderService.create(new Order(8L, createOrderLineItems(), orderedTime()));

        // when
        List<Order> result = orderService.list();

        // then
        assertAll(
                () -> assertThat(result).hasSizeGreaterThan(0),
                () -> assertThat(result).contains(saved)
        );
    }

    @Test
    void 계산_완료_상태이면_상태를_변경할_수_없다() {
        // given
        Order saved = orderService.create(new Order(8L, createOrderLineItems(), orderedTime()));
        saved.changeOrderStatus(COMPLETION);

        // when & then
        assertThatThrownBy(() ->
                orderService.changeOrderStatus(saved.getId(), saved)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("계산 완료한 주문은 상태를 변경할 수 없습니다.");
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        Order saved = orderService.create(new Order(8L, createOrderLineItems(), orderedTime()));
        saved.changeOrderStatus(MEAL);

        // when
        Order result = orderService.changeOrderStatus(saved.getId(), saved);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(MEAL);
    }

    private List<OrderLineItem> createOrderLineItems() {
        return Arrays.asList(new OrderLineItem(OrderLineItemsTest.createOrderMenu(), 1), new OrderLineItem(OrderLineItemsTest.createOrderMenu2(), 1));
    }

    public static LocalDateTime orderedTime() {
        return LocalDateTime.now();
    }
}
