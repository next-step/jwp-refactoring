package kitchenpos.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test
    void 주문_항목이_비어있을_경우_등록할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                orderService.create(new Order(1L, null, orderedTime()))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 없습니다.");
    }

    @Test
    void 중복된_메뉴가_있을_경우_등록할_수_없다() {
        // given
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 1), new OrderLineItem(1L, 1));
        Order order = new Order(1L, orderLineItems, orderedTime());

        // when & then
        assertThatThrownBy(() ->
                orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 메뉴가 있습니다.");
    }

    @Test
    void 주문_테이블이_빈_테이블이면_등록할_수_없다() {
        // given
        Order order = new Order(1L, createOrderLineItems(), orderedTime());

        // when & then
        assertThatThrownBy(() ->
                orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블은 주문을 할 수 없습니다.");
    }

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
        return Arrays.asList(new OrderLineItem(1L, 1), new OrderLineItem(2L, 1));
    }

    public static LocalDateTime orderedTime() {
        return LocalDateTime.now();
    }
}
