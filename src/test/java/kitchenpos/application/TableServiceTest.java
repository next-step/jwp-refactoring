package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private TableService tableService;

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable orderTable;

    @BeforeEach
    public void init() {
        tableService = new TableService(orderDao, orderTableDao);
        orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
    }


    @Test
    @DisplayName("TableGroup에 속해있지않고, 주문중에 요리중이거나, 먹고있는중이 아닌 테이블은 Empty설정이 가능하다")
    void changeEmpty() {
        //given
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
            1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(
            false);

        //when & then
        assertDoesNotThrow(() -> tableService.changeEmpty(1L, orderTable));
    }

    @Test
    @DisplayName("TableGroup에 속해있는 테이블은 empty설정이 불가능하다.")
    void changeEmptyAssignedWithTableGroupThrowError() {
        //given
        orderTable.setTableGroupId(1L);
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장되지않은 테이블은 empty설정이불가능하다.")
    void changeEmptyWithNotSavedTableThrowError() {
        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 요청한 주문중에 먹고있거나 요리중이면 empty설정 불가")
    void changeEmptyDuringCookingOrMealThrowError() {
        //given
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
            1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(
            true);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("음수의 손님 수 등록시 에러발생")
    void changeNumberOfGuestsWithMinusNumberThrowError() {
        //given
        orderTable.setNumberOfGuests(-1);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장되지 않은 테이블 수정시 에러발생")
    void changeNumberOfGuestsWithNotSavedTableThrowError() {
        //given
        orderTable.setNumberOfGuests(3);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 테이블에 손님 수 설정시 에러발생")
    void changeNumberOfGuestsToEmptyTableThrowError() {
        //given
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(true);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}