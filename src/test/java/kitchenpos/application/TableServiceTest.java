package kitchenpos.application;

import static kitchenpos.fixture.DomainFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
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
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = createOrderTable(1L, null, 5, false);
    }

    @Test
    void 테이블_생성() {
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        assertThat(tableService.create(orderTable)).isEqualTo(orderTable);
    }

    @Test
    void 테이블_목록_조회() {
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable));

        List<OrderTable> result = tableService.list();
        assertThat(toIdList(result)).containsExactlyElementsOf(toIdList(Arrays.asList(orderTable)));
    }

    @Test
    void 빈_상태_변경_테이블_없음_예외() {
        when(orderTableDao.findById(orderTable.getId())).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_상태_변경_단체_지정_테이블_예외() {
        orderTable.setTableGroupId(1L);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));

        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_상태_변경_주문_상태_계산완료_아닌경우_예외() {

        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_상태_변경() {
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        assertThat(tableService.changeEmpty(orderTable.getId(), orderTable)).isEqualTo(orderTable);
    }

    @Test
    void 손님_수_변경_0_미만_예외() {
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(
                () -> assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수_변경_존재하지_않는_테이블_예외() {
        orderTable.setNumberOfGuests(10);
        when(orderTableDao.findById(orderTable.getId())).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(
                () -> assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수_변경_빈_테이블_예외() {
        orderTable.setNumberOfGuests(10);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));
        orderTable.setEmpty(true);

        assertThatThrownBy(
                () -> assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수_변경() {
        orderTable.setNumberOfGuests(10);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);

        assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTable)).isEqualTo(orderTable);
    }

    private List<Long> toIdList(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}