package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.service.OrderValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class OrderTest {
    private OrderValidator orderValidator;
    @Autowired
    private MenuRepository menuRepository;

    private Menu menu;

    @BeforeEach
    public void setUp() {
        orderValidator = new OrderValidator(menuRepository);
        menu = menuRepository.save(new Menu("치킨후라이드", BigDecimal.valueOf(15_000)));
    }

    @DisplayName("주문상태 변경 예외 - 주문상태가 계산완료인 경우")
    @Test
    public void 주문상태가계산완료인경우_주문상태변경_예외() throws Exception {
        //given
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 2L);
        Order order = new Order(new OrderTable(3, false),
                Arrays.asList(orderLineItem), orderValidator);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when
        //then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
