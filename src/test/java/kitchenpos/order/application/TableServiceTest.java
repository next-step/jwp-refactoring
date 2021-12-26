package kitchenpos.order.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Table 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블을 생성한다.")
    void createTableTest() {
        OrderTable orderTable = mock(OrderTable.class);

        when(orderTable.getId())
                .thenReturn(1L);

        when(orderTableDao.save(any()))
                .thenReturn(orderTable);

        tableService.create(orderTable);
        assertThat(orderTable.getId()).isEqualTo(1L);

    }

    @Test
    @DisplayName("Table을 조회한다.")
    void findTableTest() {
        OrderTable orderTable = mock(OrderTable.class);

        when(orderTableDao.save(any()))
                .thenReturn(orderTable);

        tableService.create(orderTable);

        when(tableService.list())
                .thenReturn(Arrays.asList(orderTable));

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).contains(orderTable);
    }


    @Test
    @DisplayName("테이블상태를 변경한다.")
    void modifyTableStatusTest() {
        OrderTable orderTable = mock(OrderTable.class);

        when(orderTable.getId())
                .thenReturn(1L);

        when(orderTableDao.save(any()))
                .thenReturn(orderTable);

        tableService.create(orderTable);

        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(orderTable));

        when(orderTable.getTableGroupId())
                .thenReturn(null);

        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);

        orderTable.enterGuest();
        tableService.changeEmpty(orderTable.getId(), orderTable);

        assertThat(orderTable.getId()).isEqualTo(1L);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 인원수를 변경한다.")
    void changeMemberCountTest(){
        OrderTable orderTable = mock(OrderTable.class);

        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(orderTable));

        when(orderTable.getNumberOfGuests()).thenReturn(4);
        tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    @DisplayName("테이블 인원수를 음수로 변경하면 에러처리 테스트.")
    void changeWrongMemberCountTest(){
        OrderTable orderTable = mock(OrderTable.class);

        when(orderTable.getNumberOfGuests())
                .thenReturn(-4);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        }).isInstanceOf(InputTableDataException.class)
                .hasMessageContaining(InputTableDataErrorCode.THE_NUMBER_OF_GUESTS_IS_NOT_LESS_THAN_ZERO.errorMessage());

    }
}
