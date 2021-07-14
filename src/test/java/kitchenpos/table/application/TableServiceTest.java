package kitchenpos.table.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
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
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private TableService tableService;

    private OrderTableRequest request;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        request = new OrderTableRequest(0, true);
        orderTable = new OrderTable(1L, null, 0, true);
    }

    @Test
    void 테이블_생성_기능() {
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(orderTable);
        OrderTableResponse expected = tableService.create(request);
        assertThat(expected.getId()).isEqualTo(orderTable.getId());
        assertThat(expected.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests().numberOfGuests());
        assertThat(expected.getTableGroupId()).isNull();
        assertThat(expected.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @Test
    void 테이블_조회_기능() {
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTable));
        List<OrderTableResponse> orderTables = tableService.list();
        assertThat(orderTables.size()).isEqualTo(1);
        OrderTableResponse expected = orderTables.get(0);
        assertThat(expected.getId()).isEqualTo(orderTable.getId());
        assertThat(expected.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests().numberOfGuests());
        assertThat(expected.getTableGroupId()).isNull();
        assertThat(expected.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @Test
    void 존재하지않는_테이블_상태_변경_시_에러_발생() {
        when(orderTableRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태가_조리중_식사중일때_테이블_상태_변경_시_에러발생() {
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, orderStatuses)).thenReturn(true);
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_인원수_변경() {
        orderTable.changeEmpty(false);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        OrderTableRequest request = new OrderTableRequest(2, false);
        OrderTableResponse expected = tableService.changeNumberOfGuests(orderTable.getId(), request);
        assertThat(expected.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }
}
