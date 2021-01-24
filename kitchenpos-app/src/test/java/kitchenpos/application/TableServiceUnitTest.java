package kitchenpos.application;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.dto.ChangeEmptyTableRequest;
import kitchenpos.dto.NumberOfGuestsRequest;
import kitchenpos.exception.AlreadyGroupedTableException;
import kitchenpos.exception.NotEmptyTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static kitchenpos.common.Fixtures.aTableGroup;
import static kitchenpos.common.Fixtures.anEmptyOrderTable;
import static kitchenpos.common.Fixtures.numberOfGuestsRequest;
import static kitchenpos.common.Fixtures.toNotEmptyTableRequest;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("단체 지정된 주문 테이블을 업데이트한다.")
    @Test
    void testChangeEmptyNonExistentTableGroupId() {
        // given
        ChangeEmptyTableRequest changeEmptyTableRequest = toNotEmptyTableRequest();
        OrderTable orderTable = anEmptyOrderTable().build();
        // 단체_지정됨
        orderTable.grouping(aTableGroup());

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, changeEmptyTableRequest))
                .isInstanceOf(AlreadyGroupedTableException.class);
    }

    @DisplayName("특정 주문 상태인 주문 테이블을 업데이트한다")
    @Test
    void testChangeEmptyWithOrderStatus() {
        // given
        ChangeEmptyTableRequest changeEmptyTableRequest = toNotEmptyTableRequest();
        OrderTable orderTable = anEmptyOrderTable().build();

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Collections.singletonList(1L),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(true);

        // when & then
        assertThatIllegalStateException()
                .isThrownBy(() -> tableService.changeEmpty(1L, changeEmptyTableRequest));
    }

    @DisplayName("주문 테이블의 손님 수를 0 미만으로 업데이트한다")
    @Test
    void testChangeNumberOfGuestsWithLessThanZero() {
        // given
        NumberOfGuestsRequest numberOfGuestsRequest = new NumberOfGuestsRequest(-1);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(1L, numberOfGuestsRequest));
    }

    @DisplayName("주문 등록 가능한 상태가 아닌 주문 테이블의 손님 수를 업데이트한다")
    @Test
    void testChangeNumberOfGuestsWithEmptyTable() {
        // given
        OrderTable orderTable = anEmptyOrderTable().build();

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, numberOfGuestsRequest()))
                .isInstanceOf(NotEmptyTableException.class);
    }
}
