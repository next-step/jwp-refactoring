package kitchenpos.order.domain;

import kitchenpos.menu.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @DisplayName("주문 등록")
    @Test
    void create() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, false));
        Order expected = new Order(orderTable, OrderStatus.COOKING);
        Order actual = orderRepository.save(expected);
        assertThat(expected == actual).isTrue();
    }

    @DisplayName("주문 등록 예외 - 주문 테이블이 비어있는 경우")
    @Test
    void validateIsEmpty() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));

        assertThatThrownBy(() -> {
            Order expected = new Order(orderTable, OrderStatus.COOKING);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        // then
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, false));
        Order expected = orderRepository.save(new Order(orderTable, OrderStatus.COOKING));

        // when
        expected.changeOrderStatus(OrderStatus.COMPLETION);

        Order actual = orderRepository.findById(expected.getId()).get();
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("주문 상태 변경 예외 - 이미 완료된 상태 변경할 경우")
    @Test
    void validCopletionOrderStatus() {
        // then
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, false));
        Order expected = orderRepository.save(new Order(orderTable, OrderStatus.COOKING));
        expected.changeOrderStatus(OrderStatus.COMPLETION);

        // when
        assertThatThrownBy(() -> {
            expected.changeOrderStatus(OrderStatus.COMPLETION);
        }).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문항목 추가")
    @Test
    void addOrderLineItem() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, false));
        Order 주문 = orderRepository.save(new Order(orderTable, OrderStatus.COOKING));

        주문.add(Arrays.asList(new OrderLineItem(getMenu(), 1)));

        Order actual = orderRepository.findById(주문.getId()).get();
        assertThat(actual.getOrderLineItems()).extracting("orderId").containsExactly(주문.getId());
    }

    private Menu getMenu() {
        MenuGroup 세트_메뉴 = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        return menuRepository.save(new Menu("A세트",BigDecimal.valueOf(400000), 세트_메뉴));
    }
}
