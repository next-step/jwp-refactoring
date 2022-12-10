package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    private OrderTable 주문테이블_비어있음;
    private OrderTable 주문테이블_2명;
    private OrderTable 주문테이블_비어있음_단체1;
    private OrderTable 주문테이블_5명_단체2;

    @BeforeEach
    void setUp() {
        주문테이블_비어있음 = OrderTable.of(1L, null, 0, true);
        주문테이블_2명 = OrderTable.of(2L, null, 2, false);
        주문테이블_비어있음_단체1 = OrderTable.of(3L, 1L, 0, true);
        주문테이블_5명_단체2 = OrderTable.of(4L, 1L, 5, false);
        TableGroup 단체지정 = TableGroup.of(1L, LocalDateTime.MIN, Arrays.asList(주문테이블_비어있음_단체1, 주문테이블_5명_단체2));
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void 주문테이블_생성() {
        // given
        when(tableService.create(주문테이블_비어있음)).thenReturn(주문테이블_비어있음);

        // when
        OrderTable savedOrderTable = tableService.create(주문테이블_비어있음);

        // then
        assertAll(
                () -> assertThat(savedOrderTable.getTableGroupId()).isEqualTo(주문테이블_비어있음.getTableGroupId()),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(주문테이블_비어있음.getNumberOfGuests()),
                () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(주문테이블_비어있음.isEmpty())
        );
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void 주문테이블_목록_조회() {
        // given
        List<OrderTable> orderTables = Arrays.asList(주문테이블_비어있음, 주문테이블_비어있음_단체1, 주문테이블_5명_단체2);
        when(tableService.list()).thenReturn(orderTables);

        // when
        List<OrderTable> selectOrderTables = tableService.list();

        // then
        assertAll(
                () -> assertThat(selectOrderTables).hasSize(orderTables.size()),
                () -> assertThat(selectOrderTables).isEqualTo(orderTables)
        );
    }

    @DisplayName("주문 테이블 빈자리 여부를 변경한다.")
    @Test
    void 주문테이블_빈자리_여부_변경() {
        // given
        OrderTable 주문테이블_자리있음 = OrderTable.of(주문테이블_비어있음.getId(), 주문테이블_비어있음.getTableGroupId(), 주문테이블_비어있음.getNumberOfGuests(), false);
        when(orderTableDao.findById(주문테이블_비어있음.getId())).thenReturn(Optional.of(주문테이블_비어있음));
        when(tableService.changeEmpty(주문테이블_비어있음.getId(), 주문테이블_자리있음)).thenReturn(주문테이블_자리있음);

        // when
        OrderTable updateTable = tableService.changeEmpty(주문테이블_비어있음.getId(), 주문테이블_자리있음);

        // then
        assertThat(updateTable.isEmpty()).isEqualTo(주문테이블_자리있음.isEmpty());
    }

    @DisplayName("등록되지 않은 주문 테이블은 빈자리 여부를 변경할 수 없다.")
    @Test
    void 등록되지_않은_주문테이블_빈자리_여부_변경() {
        // given
        OrderTable 주문테이블_자리있음 = OrderTable.of(주문테이블_비어있음.getId(), 주문테이블_비어있음.getTableGroupId(), 주문테이블_비어있음.getNumberOfGuests(), false);

        // when/then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문테이블_비어있음.getId(), 주문테이블_자리있음));
    }

    @DisplayName("단체 지정이 된 주문 테이블은 빈자리 여부를 변경할 수 없다.")
    @Test
    void 단체_주문테이블_빈자리_여부_변경() {
        // given
        OrderTable 주문테이블_자리있음 = OrderTable.of(주문테이블_비어있음_단체1.getId(), 주문테이블_비어있음_단체1.getTableGroupId(), 주문테이블_비어있음_단체1.getNumberOfGuests(), false);
        when(orderTableDao.findById(주문테이블_비어있음_단체1.getId())).thenReturn(Optional.of(주문테이블_비어있음_단체1));

        // when/then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문테이블_비어있음_단체1.getId(), 주문테이블_자리있음));
    }

    @DisplayName("조리중/식사중인 주문 테이블은 빈자리 여부를 변경할 수 없다.")
    @Test
    void 조리중_식사중인_주문테이블_빈자리_여부_변경() {
        // given
        OrderTable orderTable = OrderTable.of(4L, null, 2, true);
        OrderTable orderTable_isNotEmpty = OrderTable.of(4L, null, 2, false);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        // when/then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable_isNotEmpty));
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void 주문테이블_손님수_변경() {
        // given
        OrderTable 주문테이블_5명 = OrderTable.of(주문테이블_2명.getId(), 주문테이블_2명.getTableGroupId(), 5, 주문테이블_2명.isEmpty());
        when(orderTableDao.findById(주문테이블_2명.getId())).thenReturn(Optional.of(주문테이블_2명));
        when(tableService.changeNumberOfGuests(주문테이블_2명.getId(), 주문테이블_5명)).thenReturn(주문테이블_5명);

        // when
        OrderTable updateOrderTable = tableService.changeNumberOfGuests(주문테이블_2명.getId(), 주문테이블_5명);

        // then
        assertThat(updateOrderTable.getNumberOfGuests()).isEqualTo(주문테이블_5명.getNumberOfGuests());
    }

    @DisplayName("주문 테이블의 손님 수를 음수로 변경할 수 없다.")
    @Test
    void 음수_주문테이블_손님수_변경() {
        // given
        OrderTable 주문테이블_음수 = OrderTable.of(주문테이블_2명.getId(), 주문테이블_2명.getTableGroupId(), -1, 주문테이블_2명.isEmpty());

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_2명.getId(), 주문테이블_음수));
    }

    @DisplayName("등록되지 않은 주문 테이블은 손님 수를 변경할 수 없다.")
    @Test
    void 등록되지_않은_주문테이블_손님수_변경() {
        // given
        OrderTable 주문테이블_5명 = OrderTable.of(주문테이블_2명.getId(), 주문테이블_2명.getTableGroupId(), 5, 주문테이블_2명.isEmpty());

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_2명.getId(), 주문테이블_5명));
    }

    @DisplayName("빈 주문 테이블은 손님 수를 변경할 수 없다.")
    @Test
    void 빈_주문테이블_손님수_변경() {
        // given
        OrderTable 주문테이블_5명 = OrderTable.of(주문테이블_비어있음.getId(), 주문테이블_비어있음.getTableGroupId(), 5, 주문테이블_비어있음.isEmpty());
        when(orderTableDao.findById(주문테이블_비어있음.getId())).thenReturn(Optional.of(주문테이블_비어있음));

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_비어있음.getId(), 주문테이블_5명));
    }
}
