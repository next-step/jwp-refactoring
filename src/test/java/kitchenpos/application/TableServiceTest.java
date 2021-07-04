package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderTableRequest;
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
    private OrderTable orderTable;
    private long orderTableId;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.of(10, false);
    }

    @Test
    @DisplayName("주문 테이블 등록시, 단체 지정(table group)은 빈 값으로 초기화되어진다.")
    void create() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(10, false);

        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        OrderTable savedOrderTable = tableService.create(orderTableRequest);
        assertThat(savedOrderTable.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 만들 수 있다.")
    void changeEmptyTable() {
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        OrderTable changedOrderTable = tableService.changeEmpty(1L);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문의 상태가 조리이거나, 식사의 경우에는 빈 테이블로 만들 수 없다.")
    void exception_when_orderStatus_is_meal_or_cook() {

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

        orderTableId = 1L;

        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Lists.list(OrderStatus.COOKING, OrderStatus.MEAL)))
                .willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no exist order");
    }

    @Test
    @DisplayName("주문 테이블에는 방문한 손님 수를 변경할 수 있다.")
    void changeNumberOfGuestTest() {

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
        given(orderTableDao.save(orderTable))
                .willReturn(orderTable);

        OrderTable savedOrderTable = tableService.changeNumberOfGuests(1L, 10);

        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    @DisplayName("빈 주문 테이블의 경우에는 손님의 수를 변경할 수 없다.")
    void exception2_changeNumberOfGuestTest() {

        orderTable.changeEmptyTable();

        given(orderTableDao.findById(1L))
                .willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 10))
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