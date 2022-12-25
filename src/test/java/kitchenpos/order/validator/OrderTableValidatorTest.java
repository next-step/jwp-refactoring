package kitchenpos.order.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Optional;
import kitchenpos.order.OrderFixture;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderTableValidator validator;

    @Test
    @DisplayName("조리중이거나 식사중일떄는 테이블을 빈 상태로 변경 불가능")
    void cantEmptyWhenCookingOrMeal() {
        //given
        OrderTable 식사중테이블 = new OrderTable(1L, 0, false);
        BDDMockito.when(orderRepository.findByOrderTableId(eq(1L))).thenReturn(Optional.of(
            OrderFixture.조리중주문));

        //when && then

        //when & then
        assertThatThrownBy(() -> validator.validateChangeEmpty(식사중테이블))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중 이거나 식사중에는 테이블을 비울 수 없습니다.");

        //given
        OrderTable 조리중테이블 = new OrderTable(2L, 0, false);
        BDDMockito.when(orderRepository.findByOrderTableId(eq(2L))).thenReturn(Optional.of(
            OrderFixture.식사중));

        //when & then
        assertThatThrownBy(() -> validator.validateChangeEmpty(조리중테이블))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중 이거나 식사중에는 테이블을 비울 수 없습니다.");
    }

    @Test
    @DisplayName("단체 지정된 테이블은 빈 상태로 변경 불가능")
    void cantEmptyWhenHasGroup() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, false);
        orderTable.setTableGroupId(1L);

        //when & then
        assertThatThrownBy(() -> validator.validateChangeEmpty(orderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("단체 지정된 테이블은 비울 수 없습니다.");
    }

}