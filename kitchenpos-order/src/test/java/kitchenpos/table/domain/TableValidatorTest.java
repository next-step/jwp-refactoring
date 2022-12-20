package kitchenpos.table.domain;

import static kitchenpos.order.domain.OrderLineItemTestFixture.*;
import static kitchenpos.order.domain.OrderTestFixture.*;
import static kitchenpos.table.domain.OrderTableTestFixture.orderTable;
import static kitchenpos.table.domain.OrderTableTestFixture.주문테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 유효성 검사 테스트")
@ExtendWith(MockitoExtension.class)
class TableValidatorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private TableValidator tableValidator;

    @Test
    @DisplayName("빈 테이블로 수정시 주문 테이블은 단체 테이블이 아니어야 한다.")
    void updateEmptyTableByNoneTableGroup() {
        // given
        OrderTable 단체테이블 = orderTable(2L, 1L, 8, false);

        // when & then
        assertThatThrownBy(() -> tableValidator.validateChangeEmpty(단체테이블))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("단체 테이블이 존재합니다.");
    }

    @Test
    @DisplayName("빈 테이블로 수정시 주문 상태가 조리 중 또는 식사 중이면 안된다.")
    void updateEmptyTableByOrderStatusIsCookingOrMeal() {
        // given
        Order order = order(1L, 주문테이블.id(), Collections.singletonList(짬뽕2_탕수육_주문_항목));
        given(orderRepository.findByOrderTableId(주문테이블.id())).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> tableValidator.validateChangeEmpty(주문테이블))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("주문 상태가 조리 중 입니다.");
    }
}
