package kitchenpos.order.domain;

import kitchenpos.OrderApplication;
import kitchenpos.OrderTestSupport;
import kitchenpos.menu.domain.Menu;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = OrderApplication.class)
@DataJpaTest
public class OrderTest extends OrderTestSupport {
    @DisplayName("주문상태 변경 예외 - 주문상태가 계산완료인 경우")
    @Test
    public void 주문상태가계산완료인경우_주문상태변경_예외() throws Exception {
        //given
        Menu menu = 메뉴_등록되어있음("치킨후라이드", BigDecimal.valueOf(15_000));
        OrderTable orderTable = 테이블_등록되어있음(3, false);
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 2L);
        Order order = 주문_등록되어있음(orderTable, Arrays.asList(orderLineItem));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when
        //then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
