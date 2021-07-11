package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("테이블 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 테이블;

    @BeforeEach
    void setUp() {
        테이블 = new OrderTable();
        테이블.setId(1L);
        테이블.setEmpty(true);
        테이블.setNumberOfGuests(0);
    }

    @Test
    void 테이블_생성_기능() {
        when(orderTableDao.save(테이블)).thenReturn(테이블);

        OrderTable expected = tableService.create(테이블);
        assertThat(expected.getId()).isEqualTo(테이블.getId());
        assertThat(expected.getNumberOfGuests()).isEqualTo(테이블.getNumberOfGuests());
        assertThat(expected.getTableGroupId()).isEqualTo(테이블.getTableGroupId());
        assertThat(expected.isEmpty()).isEqualTo(테이블.isEmpty());
    }

    @Test
    void 테이블_조회_기능() {
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(테이블));

        List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables.size()).isEqualTo(1);
        OrderTable expected = orderTables.get(0);
        assertThat(expected.getId()).isEqualTo(테이블.getId());
        assertThat(expected.getNumberOfGuests()).isEqualTo(테이블.getNumberOfGuests());
        assertThat(expected.getTableGroupId()).isEqualTo(테이블.getTableGroupId());
        assertThat(expected.isEmpty()).isEqualTo(테이블.isEmpty());
    }

    @ParameterizedTest
    @CsvSource(value = {"true:false", "false:true"}, delimiter = ':')
    void 테이블_착석_여부_상태_변경(boolean original, boolean expectedBoolean) {
        테이블.setEmpty(original);

        when(orderTableDao.findById(1L)).thenReturn(Optional.of(테이블));
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, orderStatuses)).thenReturn(false);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(테이블);

        테이블.setEmpty(expectedBoolean);
        OrderTable expected = tableService.changeEmpty(테이블.getId(), 테이블);
        assertThat(expected.isEmpty()).isEqualTo(expectedBoolean);
    }

    @Test
    void 존재하지않는_테이블_상태_변경_시_에러_발생() {
        when(orderTableDao.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> tableService.changeEmpty(테이블.getId(), 테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹으로_묶여있는_테이블_상태_변경시_에러발생() {
        테이블.setTableGroupId(1L);
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(테이블));
        assertThatThrownBy(() -> tableService.changeEmpty(테이블.getId(), 테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태가_조리중_식사중일때_테이블_상태_변경_시_에러발생() {
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(테이블));
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, orderStatuses)).thenReturn(true);
        assertThatThrownBy(() -> tableService.changeEmpty(테이블.getId(), 테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_인원수_변경() {
        테이블.setEmpty(false);
        when(orderTableDao.findById(테이블.getId())).thenReturn(Optional.of(테이블));
        when(orderTableDao.save(테이블)).thenReturn(테이블);
        테이블.setNumberOfGuests(2);
        OrderTable expected = tableService.changeNumberOfGuests(테이블.getId(), 테이블);
        assertThat(expected.getNumberOfGuests()).isEqualTo(테이블.getNumberOfGuests());
    }

    @Test
    void 테이블_인원수가_음수일때_에러발생() {
        테이블.setNumberOfGuests(-1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블.getId(), 테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지않는_테이블의_인원수_변경시_에러발생() {
        테이블.setEmpty(false);
        테이블.setNumberOfGuests(2);
        when(orderTableDao.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블.getId(), 테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 비어있는_테이블에_인원수_변경시_에러발생() {
        when(orderTableDao.findById(테이블.getId())).thenReturn(Optional.of(테이블));
        테이블.setNumberOfGuests(2);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블.getId(), 테이블)).isInstanceOf(IllegalArgumentException.class);
    }
}
