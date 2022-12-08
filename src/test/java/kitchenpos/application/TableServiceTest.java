package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable firstTable;
    private OrderTable secondTable;

    @BeforeEach
    void setUp() {
        firstTable = new OrderTable(1L, null, 0, true);
        secondTable = new OrderTable(2L, null, 0, true);
    }

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        given(tableService.create(firstTable)).willReturn(firstTable);

        OrderTable savedOrderTable = tableService.create(firstTable);

        assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable.isEmpty()).isTrue(),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(0)
        );
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        given(orderTableDao.findAll()).willReturn(Arrays.asList(firstTable, secondTable));

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(2);
        assertThat(orderTables).contains(firstTable, secondTable);
    }

    @DisplayName("주문 테이블 이용 여부를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable expected = new OrderTable(1L, null, 0, false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(firstTable));
        given(orderTableDao.save(firstTable)).willReturn(firstTable);

        OrderTable changeOrderTable = tableService.changeEmpty(1L, expected);

        assertThat(changeOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("단체 테이블로 지정되어 있다면 주문 테이블 이용 여부를 변경할 수 없다.")
    @Test
    void changeEmptyWithTableGroup() {
        firstTable.setTableGroupId(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(firstTable));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리 또는 식사라면 주문 테이블 이용 여부를 변경할 수 없다.")
    @Test
    void changeEmptyWithOrderStatus() {
        List<String> orderStatus = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        given(orderTableDao.findById(1L)).willReturn(Optional.of(firstTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, orderStatus)).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable expected = new OrderTable(1L, null, 5, false);
        firstTable.setEmpty(false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(firstTable));
        given(orderTableDao.save(firstTable)).willReturn(firstTable);

        OrderTable changeOrderTable = tableService.changeNumberOfGuests(1L, expected);

        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
    }

    @DisplayName("주문 테이블의 손님 수를 음수로 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsWithNegative() {
        OrderTable expected = new OrderTable(1L, null, -1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 이용중이 아니라면 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsWithNotEmptyTable() {
        firstTable.setEmpty(true);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(firstTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
