package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.*;
import static kitchenpos.fixture.TableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.Arrays;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import kitchenpos.dao.*;
import kitchenpos.domain.*;

@DisplayName("주문 테이블 관련 테스트")
class TableServiceTest {
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        orderDao = mock(OrderDao.class);
        orderTableDao = mock(OrderTableDao.class);
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("주문 테이블 생성하기")
    @Test
    void createTest() {
        when(orderTableDao.save(any())).thenReturn(orderTable1);
        assertThat(tableService.create(orderTable1)).isEqualTo(orderTable1);
    }

    @DisplayName("주문 테이블 조회시 저장된 주문 테이블 목록 얻기")
    @Test
    void listTest() {
        when(orderTableDao.findAll()).thenReturn(Lists.newArrayList(orderTable1, orderTable2));
        assertThat(tableService.list()).isEqualTo(Lists.newArrayList(orderTable1, orderTable2));
    }

    @DisplayName("주문 테이블 공백 상태 변경")
    @Test
    void changeEmptyTest() {
        OrderTable targetOrderTable = OrderTable.of(orderTable1.getId(), orderTable1.getTableGroupId(), orderTable1.getNumberOfGuests(), true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable1));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        assertThat(tableService.changeEmpty(orderTable1.getId(), targetOrderTable).isEmpty()).isEqualTo(targetOrderTable.isEmpty());
    }

    @DisplayName("주문 테이블 공백 상태 변경시 주문 테이블에 단체 지정이 되어 있다면 예외 발생")
    @Test
    void exceptionTest1() {
        orderTable1.setTableGroupId(tableGroup.getId());
        OrderTable targetOrderTable = OrderTable.of(orderTable1.getId(), orderTable1.getTableGroupId(), orderTable1.getNumberOfGuests(), true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable1));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        assertThatThrownBy(
            () -> tableService.changeEmpty(orderTable1.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 공백 상태 변경시 주문테이블의 상태가 조리 또는 식사중이면 예외 발생")
    @Test
    void exceptionTest2() {
        orderTable1.setEmpty(false);
        OrderTable targetOrderTable = OrderTable.of(orderTable1.getId(), orderTable1.getTableGroupId(), orderTable1.getNumberOfGuests(), true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable1));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTable1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        assertThatThrownBy(
            () -> tableService.changeEmpty(orderTable1.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 변경")
    @Test
    void changeNumberOfGuestsTest() {
        OrderTable targetOrderTable = OrderTable.of(orderTable1.getId(), orderTable1.getTableGroupId(), 6, true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable1));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        assertThat(tableService.changeEmpty(orderTable1.getId(), targetOrderTable).getNumberOfGuests()).isEqualTo(
            targetOrderTable.getNumberOfGuests()
        );
    }

    @DisplayName("방문한 손님 수 변경시 음수이면 예외 발생")
    @Test
    void exceptionTest3() {
        OrderTable targetOrderTable = OrderTable.of(orderTable1.getId(), orderTable1.getTableGroupId(), -1, true);

        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(orderTable1.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 변경시 저장된 주문 테이블이 없다면 예외 발생")
    @Test
    void exceptionTest4() {
        OrderTable targetOrderTable = OrderTable.of(orderTable1.getId(), orderTable1.getTableGroupId(), 6, true);

        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(orderTable1.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 변경시 주문 테이블의 주문이 조리 또는 식사중이면 예외 발생")
    @Test
    void exceptionTest5() {
        orderTable1.setEmpty(true);
        OrderTable targetOrderTable = OrderTable.of(orderTable1.getId(), orderTable1.getTableGroupId(), 6, true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable1));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTable1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(orderTable1.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
