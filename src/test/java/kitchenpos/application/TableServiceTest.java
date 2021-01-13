package kitchenpos.application;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private OrderTable orderTable3;
    private List<OrderTable> orderTables = new ArrayList<>();

    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1, true);
        orderTable2 = new OrderTable(2, true);
        orderTable3 = new OrderTable(3, true);
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);
        orderTables.add(orderTable3);

        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("주문테이블 등록 테스트")
    @Test
    void createOrderTable() {
        mockSaveOrderTable(orderTable1);
        mockSaveOrderTable(orderTable2);
        mockSaveOrderTable(orderTable3);
        checkOrderTable(tableService.create(orderTable1), orderTable1);
        checkOrderTable(tableService.create(orderTable2), orderTable2);
        checkOrderTable(tableService.create(orderTable3), orderTable3);
    }

    @DisplayName("주문테이블 목록 조회 테스트")
    @Test
    void findOrderTableList() {
        when(orderTableDao.findAll()).thenReturn(orderTables);
        List<OrderTable> resultOrderTables = tableService.list();

        List<Integer> resultOrderTableNumOfGuests = resultOrderTables.stream()
                .map(resultOrderTable ->resultOrderTable.getNumberOfGuests())
                .collect(Collectors.toList());
        List<Integer> OrderTableNumOfGuests = orderTables.stream()
                .map(orderTable -> orderTable.getNumberOfGuests())
                .collect(Collectors.toList());
        assertThat(resultOrderTableNumOfGuests).containsExactlyElementsOf(OrderTableNumOfGuests);
    }

    @DisplayName("손님 숫자 변경 테스트")
    @Test
    void changeNumberOfGuestTest() {
        mockSaveOrderTable(orderTable1);
        when(orderTableDao.findById(any())).thenReturn(java.util.Optional.ofNullable(orderTable1));

        orderTable1.setNumberOfGuests(10);
        orderTable1.setEmpty(false);
        OrderTable resultOrderTable = tableService.changeNumberOfGuests(1L, orderTable1);

        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
    }

    @DisplayName("테이블 상태 변경 테스트")
    @Test
    void changeEmptyTest() {
        mockSaveOrderTable(orderTable1);
        when(orderTableDao.findById(any())).thenReturn(java.util.Optional.ofNullable(orderTable1));
        List<String> orderStatus = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), eq(orderStatus)))
                .thenReturn(false);

        OrderTable resultTable = tableService.changeEmpty(1L, orderTable1);

        assertThat(resultTable.isEmpty()).isEqualTo(orderTable1.isEmpty());
    }

    private void checkOrderTable(OrderTable resultOrderTable, OrderTable orderTable) {
        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(resultOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    private void mockSaveOrderTable(OrderTable orderTable) {
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);
    }


}
