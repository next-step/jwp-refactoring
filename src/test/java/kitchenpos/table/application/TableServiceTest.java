package kitchenpos.table.application;

import kitchenpos.application.TableService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    private final OrderTable 주문테이블 = new OrderTable(1L, 1L, 4, false);
    private final OrderTable 빈테이블 = new OrderTable(2L, null, 0, true);


    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("테이블생성테스트")
    @Test
    void createTableTest() {
        //given
        when(orderTableDao.save(주문테이블)).thenReturn(주문테이블);

        //when
        //then
        assertThat(tableService.create(주문테이블)).isEqualTo(주문테이블);
    }

    @DisplayName("빈 상태로 변경테스트")
    @Test
    void changeEmptyTableTest() {
        //given
        when(orderTableDao.findById(빈테이블.getId())).thenReturn(Optional.ofNullable(빈테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                빈테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);
        when(orderTableDao.save(빈테이블)).thenReturn(빈테이블);

        //when
        OrderTable result = tableService.changeEmpty(빈테이블.getId(), 빈테이블);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("존재하지 않는 id로 빈 테이블 변경 오류 테스트")
    @Test
    void notExistIdChangeEmptyTableExceptionTest() {
        //given
        when(orderTableDao.findById(빈테이블.getId())).thenReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> tableService.changeEmpty(빈테이블.getId(), 빈테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리중이거나 식사중인 테이블을 빈 테이블로 변경 오류 테스트")
    @Test
    void cookingOrMealChangeEmptyTableExceptionTest() {
        //given
        when(orderTableDao.findById(빈테이블.getId())).thenReturn(Optional.ofNullable(빈테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                빈테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(빈테이블.getId(), 빈테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("게스트숫자 변경 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        //given
        int changeGuestNumber = 2;
        when(orderTableDao.findById(주문테이블.getId()))
                .thenReturn(Optional.ofNullable(주문테이블));
        when(orderTableDao.save(주문테이블)).thenReturn(주문테이블);
        OrderTable 변경주문테이블 =
                new OrderTable(주문테이블.getId(), 주문테이블.getTableGroupId(), changeGuestNumber, false);

        //when
        OrderTable result = tableService.changeNumberOfGuests(주문테이블.getId(), 변경주문테이블);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(changeGuestNumber);
    }

    @DisplayName("게스트숫자 0보다 작은 값으로 변경 오류 테스트")
    @Test
    void changeNumberOfGuestsUnderZeroExceptionTest() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(),
                new OrderTable(주문테이블.getId(),
                        주문테이블.getTableGroupId(),
                        -1,
                        주문테이블.isEmpty())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 id로 게스트 숫자 변경 오류 테스트")
    @Test
    void changeNumberOfGuestNotExistTableIdExceptionTest() {
        when(orderTableDao.findById(4L)).thenReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(),
                new OrderTable(주문테이블.getId(), 주문테이블.getTableGroupId(), 3, 주문테이블.isEmpty())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈테이블에서 게스트 숫자 변경 오류 테스트")
    @Test
    void changeNumberOfGuestEmptyTableIdExceptionTest() {
        when(orderTableDao.findById(빈테이블.getId())).thenReturn(Optional.ofNullable(빈테이블));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(빈테이블.getId(), 빈테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
