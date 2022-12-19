package kitchenpos.table.domain;

import static kitchenpos.order.domain.OrderLineItemTestFixture.*;
import static kitchenpos.order.domain.OrderTestFixture.order;
import static kitchenpos.table.domain.OrderTableTestFixture.orderTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 묶음 유효성 검사 테스트")
@ExtendWith(MockitoExtension.class)
class OrderTablesValidatorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderTablesValidator orderTablesValidator;

    @Test
    @DisplayName("단체 테이블 해제시 조리 중이거나 식사중인 테이블은 해제할 수 없다.")
    void ungroupTableByOrderStatusCookingOrMeal() {
        // given
        OrderTable orderTable = orderTable(1L, null, 4, true);
        OrderTable orderTable2 = orderTable(2L, null, 3, true);
        Order order = order(1L, orderTable.id(), Collections.singletonList(짜장_탕수육_주문_항목));
        Order order2 = order(2L, orderTable2.id(), Collections.singletonList(짬뽕2_탕수육_주문_항목));
        OrderTables orderTables = OrderTables.from(Arrays.asList(orderTable, orderTable2));
        given(orderRepository.findAllByOrderTableIdIn(Arrays.asList(orderTable.id(), orderTable2.id())))
                .willReturn(Arrays.asList(order, order2));

        // when
        assertThatThrownBy(() -> orderTables.validateCookingAndMeal(orderTablesValidator))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("주문 상태가 조리 중 입니다.");
    }
}
