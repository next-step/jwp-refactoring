package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.common.DefaultData.주문테이블_1번_ID;
import static kitchenpos.common.DefaultData.주문테이블_미존재_ID;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceUnitTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("존재하지 않는 주문 테이블을 업데이트한다")
    @Test
    void testChangeEmptyNonExistentOrderTable() {
        // given
        OrderTable orderTable = new OrderTable();
        given(orderTableDao.findById(주문테이블_미존재_ID)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문테이블_미존재_ID, orderTable));
    }

    @DisplayName("단체 지정된 주문 테이블을 업데이트한다.")
    @Test
    void testChangeEmptyNonExistentTableGroupId() {
        // given
        OrderTable orderTable = new OrderTable();
        OrderTable savedOrderTable = new OrderTable();
        // 단체_지정됨
        savedOrderTable.setTableGroupId(1L);
        given(orderTableDao.findById(주문테이블_1번_ID)).willReturn(Optional.of(savedOrderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문테이블_1번_ID, orderTable));
    }

    @DisplayName("특정 주문 상태인 주문 테이블을 업데이트한다")
    @Test
    void testChangeEmptyWithOrderStatus() {
        // given
        OrderTable orderTable = new OrderTable();
        OrderTable savedOrderTable = new OrderTable();
        given(orderTableDao.findById(주문테이블_1번_ID)).willReturn(Optional.of(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블_1번_ID,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문테이블_1번_ID, orderTable));
    }

    @DisplayName("주문 테이블의 손님 수를 0 미만으로 업데이트한다")
    @Test
    void testChangeNumberOfGuestsWithLessThanZero() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-10);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문테이블_1번_ID, orderTable));
    }

    @DisplayName("존재하지 않는 주문 테이블의 손님 수를 업데이트한다")
    @Test
    void testChangeNumberOfGuests_nonExistentOrderTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        given(orderTableDao.findById(주문테이블_미존재_ID)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_미존재_ID, orderTable));
    }

    @DisplayName("주문 등록 가능한 상태가 아닌 주문 테이블의 손님 수를 업데이트한다")
    @Test
    void testChangeNumberOfGuestsWithEmptyTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setEmpty(true);
        given(orderTableDao.findById(주문테이블_1번_ID)).willReturn(Optional.of(savedOrderTable));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_1번_ID, orderTable));
    }
}
