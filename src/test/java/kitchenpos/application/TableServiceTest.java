package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable table1;
    private OrderTable 비어있는_주문_테이블;

    @BeforeEach
    void setUp() {
        table1 = OrderTable.of(null, 2, false);
        비어있는_주문_테이블 = OrderTable.of(null, 0, true);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        when(orderTableDao.save(any())).thenReturn(table1);

        OrderTable result = tableService.create(table1);

        assertThat(result).isEqualTo(table1);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(table1, 비어있는_주문_테이블));

        List<OrderTable> results = tableService.list();

        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(results).containsExactly(table1, 비어있는_주문_테이블)
        );
    }

    @DisplayName("등록되지 않은 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException() {
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, table1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException2() {
        table1.setTableGroupId(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(table1));

        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, table1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 식사 또는 조리이면 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException3() {
        when(orderTableDao.findById(any())).thenReturn(Optional.of(table1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, table1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 빈 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        boolean isEmpty = table1.isEmpty();
        when(orderTableDao.findById(any())).thenReturn(Optional.of(table1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(OrderTable.of(1L, table1.getNumberOfGuests(), !isEmpty));

        OrderTable result = tableService.changeEmpty(1L,
                OrderTable.of(1L, table1.getNumberOfGuests(), !table1.isEmpty()));

        Assertions.assertThat(result.isEmpty()).isEqualTo(!isEmpty);
    }

    @DisplayName("방문한 손님 수가 0보다 작은경우 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException() {
        OrderTable orderTable = OrderTable.of(1L, -1, true);

        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException2() {
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        OrderTable orderTable = OrderTable.of(table1.getId(), 4, table1.isEmpty());
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(table1.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블이 빈 상태이면 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException3() {
        when(orderTableDao.findById(any())).thenReturn(Optional.of(비어있는_주문_테이블));

        OrderTable orderTable = OrderTable.of(비어있는_주문_테이블.getId(), 2, 비어있는_주문_테이블.isEmpty());
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(비어있는_주문_테이블.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = OrderTable.of(table1.getId(), 4, table1.isEmpty());

        when(orderTableDao.findById(any())).thenReturn(Optional.of(table1));
        when(orderTableDao.save(any())).thenReturn(orderTable);

        OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        Assertions.assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }
}
