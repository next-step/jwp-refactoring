package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

@ExtendWith(MockitoExtension.class)
public class TableValidatorTest {
    
    @Mock
    private OrderService orderService;
    
    @InjectMocks
    private TableValidator tableValidator;
    
    @DisplayName("조리중이거나 식사중일때는 주문 테이블을 해제 할 수 없다")
    @Test
    void 주문_테이블_해제_불가() {
        // given
        Long 메뉴_Id = 1L;
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴_Id, 3L);
        Long 테이블_Id = 1L;
        Order 주문 = Order.of(테이블_Id, OrderStatus.MEAL, Arrays.asList(주문_항목));
        given(orderService.findAllByOrderTableId(테이블_Id)).willReturn(Arrays.asList(주문));
        
        // when, then
        assertThatThrownBy(() -> {
            tableValidator.checkIsCookingOrMeal(테이블_Id);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("조리중, 식사중인 주문 테이블은 변경할 수 없습니다");
    }
}
