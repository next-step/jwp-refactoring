package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;
    private OrderTable existedOrderTable;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        existedOrderTable = new OrderTable();
        orderTable = new OrderTable();
    }

    @Test
    @DisplayName("주문 테이블 등록시, 단체 지정(table group)은 빈 값으로 초기화되어진다.")
    void create() {
        OrderTable mock = new OrderTable();
        mock.setTableGroup(null);

        given(orderTableDao.save(mock)).willReturn(mock);

        OrderTable savedOrderTable = tableService.create(mock);
        assertThat(savedOrderTable.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 만들 수 있다.")
    void changeEmptyTable() {
        existedOrderTable.setId(1L);
        existedOrderTable.setEmpty(false);
        orderTable.setEmpty(true);

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(existedOrderTable));

        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(existedOrderTable)).willReturn(orderTable);

        OrderTable changedOrderTable = tableService.changeEmpty(1L, orderTable);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문의 상태가 조리이거나, 식사의 경우에는 빈 테이블로 만들 수 없다.")
    void exception_when_orderStatus_is_meal_or_cook() {
        existedOrderTable.setId(1L);
        existedOrderTable.setEmpty(false);

        given(orderTableDao.findById(1L))
                .willReturn(Optional.of(existedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
                Lists.list(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no exist order");
    }

    @Test
    @DisplayName("주문 테이블에는 방문한 손님 수를 변경할 수 있다.")
    void changeNumberOfGuestTest() {
        existedOrderTable.setId(1L);
        existedOrderTable.setEmpty(false);
        existedOrderTable.setNumberOfGuests(1);

        orderTable.setNumberOfGuests(10);

        given(orderTableDao.findById(1L))
                .willReturn(Optional.of(existedOrderTable));
        given(orderTableDao.save(existedOrderTable))
                .willReturn(orderTable);

        OrderTable savedOrderTable = tableService.changeNumberOfGuests(1L, orderTable);

        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    @DisplayName("만약 처음 방문한 손님의 수가 -1일 경우, 변경할 수 없다.")
    void exception_changeNumberOfGuestTest() {
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("numberOfGuests");
    }

    @Test
    @DisplayName("빈 주문 테이블의 경우에는 손님의 수를 변경할 수 없다.")
    void exception2_changeNumberOfGuestTest() {
        existedOrderTable.setId(1L);
        existedOrderTable.setEmpty(true);

        given(orderTableDao.findById(1L))
                .willReturn(Optional.of(existedOrderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("emptyTable");
    }

    @Test
    @DisplayName("주문 테이블의 전체 목록을 조회할 수 있다.")
    void getAllOrderTable() {
        given(orderTableDao.findAll())
                .willReturn(new ArrayList<>());

        assertThat(tableService.list()).isNotNull();
    }
}