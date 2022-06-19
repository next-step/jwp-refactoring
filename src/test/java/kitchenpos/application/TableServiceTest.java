package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 관련 Service 기능 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        //given
        long generateTableId = 1;
        OrderTable request = new OrderTable(null, 1L, 5, false);

        doAnswer(invocation -> new OrderTable(generateTableId, request.getTableGroupId(), request.getNumberOfGuests(),
                request.isEmpty()))
                .when(orderTableDao).save(request);

        //when
        OrderTable result = tableService.create(request);

        //then
        assertThat(result.getId()).isEqualTo(generateTableId);
        assertThat(result.getTableGroupId()).isEqualTo(request.getTableGroupId());
        assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        assertThat(result.isEmpty()).isEqualTo(request.isEmpty());
    }

    @DisplayName("빈 테이블 여부를 업데이트 한다.")
    @Test
    void changeEmpty() {
        //given
        long requestTableId = 1L;
        OrderTable request = new OrderTable(null, null, 0, true);
        OrderTable orderTable = new OrderTable(1L, null, 5, false);

        when(orderTableDao.findById(requestTableId)).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(requestTableId,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        //when
        OrderTable result = tableService.changeEmpty(requestTableId, request);

        //then
        assertThat(result.isEmpty()).isEqualTo(request.isEmpty());
        assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("단체 지정이 되어있는 경우 빈 테이블 여부 업데이트 할 수 없다.")
    @Test
    void changeEmpty_table_group() {
        //given
        long requestTableId = 1;
        OrderTable request = new OrderTable(null, null, 0, true);
        OrderTable orderTable = new OrderTable(1L, 1L, 5, false);

        when(orderTableDao.findById(requestTableId)).thenReturn(Optional.of(orderTable));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(requestTableId, request));
    }

    @DisplayName("주문 상태가 조리, 식사 단계인 경우 빈 테이블 여부 업데이트 할 수 없다.")
    @Test
    void changeEmpty_order_stats_cooking_or_meal() {
        //given
        long requestTableId = 1;
        OrderTable request = new OrderTable(null, null, 0, true);
        OrderTable orderTable = new OrderTable(1L, null, 5, false);

        when(orderTableDao.findById(requestTableId)).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(requestTableId,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(requestTableId, request));
    }

    @DisplayName("방문 손님 수를 업데이트 한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        long requestTableId = 1;
        OrderTable request = new OrderTable(null, null, 10, true);
        OrderTable orderTable = new OrderTable(1L, null, 5, false);

        when(orderTableDao.findById(requestTableId)).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        //when
        OrderTable result = tableService.changeNumberOfGuests(requestTableId, request);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        assertThat(result.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @DisplayName("방문 손님 수가 0명 미만인 경우 업데이트 할 수 없다.")
    @Test
    void changeNumberOfGuests_less_than_zero() {
        //given
        long requestTableId = 1;
        OrderTable request = new OrderTable(null, null, -1, true);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request));
    }

    @DisplayName("빈 테이블인 경우 방문 손님 수를 업데이트 할 수 없다.")
    @Test
    void changeNumberOfGuests_empty_table() {
        //given
        long requestTableId = 1;
        OrderTable request = new OrderTable(null, null, 10, true);
        OrderTable orderTable = new OrderTable(1L, null, 0, true);

        when(orderTableDao.findById(requestTableId)).thenReturn(Optional.of(orderTable));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request));
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 0, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, false);
        OrderTable orderTable3 = new OrderTable(3L, null, 3, false);

        when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable1, orderTable2, orderTable3));

        //when
        List<OrderTable> results = tableService.list();

        //then
        assertThat(results).containsExactlyInAnyOrderElementsOf(Arrays.asList(orderTable1, orderTable2, orderTable3));
    }

}
