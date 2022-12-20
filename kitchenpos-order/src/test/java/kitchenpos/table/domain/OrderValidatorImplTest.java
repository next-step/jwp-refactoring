package kitchenpos.table.domain;

import static kitchenpos.order.domain.OrderLineItemTestFixture.orderLineItem;
import static kitchenpos.order.domain.OrderMenuTestFixture.*;
import static kitchenpos.order.domain.OrderTestFixture.*;
import static kitchenpos.table.domain.OrderTableTestFixture.orderTable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 유효성 검사 테스트")
@ExtendWith(MockitoExtension.class)
class OrderValidatorImplTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderValidatorImpl orderValidatorImpl;

    @Test
    @DisplayName("주문 등록시 주문 테이블은 등록된 테이블이어야 한다.")
    void createOrderByCreatedOrderTable() {
        // given
        OrderTable orderTable = orderTable(1L, null, 3, false);
        OrderLineItem orderLineItem = orderLineItem(1L, 짜장_탕수육_주문_세트, 1L);
        OrderLineItem orderLineItem2 = orderLineItem(2L, 짬뽕2_탕수육_주문_세트, 1L);
        given(orderTableRepository.findById(orderTable.id())).willReturn(Optional.empty());
        Order order = order(null, orderTable.id(), Arrays.asList(orderLineItem, orderLineItem2));

        // when & then
        assertThatThrownBy(() -> order.validate(orderValidatorImpl))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다. ID : 1");
    }

    @Test
    @DisplayName("주문 등록시 주문 테이블은 비어있는 테이블일 수 없다.")
    void createOrderByEmptyOrderTable() {
        // given
        OrderLineItem orderLineItem = orderLineItem(1L, 짜장_탕수육_주문_세트, 1L);
        OrderLineItem orderLineItem2 = orderLineItem(2L, 짬뽕2_탕수육_주문_세트, 1L);
        OrderTable emptyTable = orderTable(2L, null, 0, true);
        given(orderTableRepository.findById(emptyTable.id())).willReturn(Optional.of(emptyTable));
        Order order = order(null, emptyTable.id(), Arrays.asList(orderLineItem, orderLineItem2));

        // when & then
        assertThatThrownBy(() -> order.validate(orderValidatorImpl))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("비어있는 테이블입니다.");
    }
}
