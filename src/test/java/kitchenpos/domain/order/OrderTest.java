package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.order.OrderValidator;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class OrderTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderValidator orderValidator;

    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = OrderTableFixtureFactory.createWithGuests(1L, 1, false);

        주문테이블 = orderTableRepository.save(주문테이블);
    }

    @DisplayName("Oders 는 OrderTable 로 생성된다.")
    @Test
    void create1() {
        // when & then
        assertThatNoException().isThrownBy(() -> Order.createFromOrderTable(주문테이블.getId()));
    }

    @DisplayName("Oders 생성 시, OrderTable 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        Order order = Order.createFromOrderTable(0L);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> orderValidator.validateOrder(order));
    }

    @DisplayName("완료된 상태가 아니라면, Order 의 상태를 바꿀 수 있다.")
    @Test
    void changeOrderStatus1() {
        // given
        Order order = Order.createFromOrderTable(주문테이블.getId());

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertTrue(order.getOrderStatus().isMeal());
    }

    @DisplayName("완료된 Order 의 상태를 바꾸면 예외가 발생한다.")
    @Test
    void changeOrderStatus2() {
        // given
        Order order = Order.createFromOrderTable(주문테이블.getId());
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                                            .withMessageContaining("완료된 Order 는 상태를 바꿀 수 없습니다.");
    }
}