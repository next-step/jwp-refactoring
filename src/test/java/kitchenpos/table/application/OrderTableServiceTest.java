package kitchenpos.table.application;

import kitchenpos.exception.OrderTableException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.publisher.TableEventPublisher;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("테이블 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
class OrderTableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    public TableEventPublisher eventPublisher;

    @InjectMocks
    private TableService tableService;

    private OrderTableRequest request;
    private OrderTable orderTable;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup(1L, LocalDateTime.now());
        request = new OrderTableRequest(0, true);
        orderTable = new OrderTable(1L, 0, true);
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
    void 테이블_상태_변경() {
        OrderTableRequest request = new OrderTableRequest(0, false);
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
        OrderTableResponse orderTableResponse = tableService.changeEmpty(1L, request);
        assertThat(orderTableResponse.isEmpty()).isEqualTo(request.isEmpty());
    }

    @Test
    void 존재하지않는_테이블_상태_변경_시_에러_발생() {
        when(orderTableRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request)).isInstanceOf(OrderTableException.class);
    }

    @Test
    void 테이블_인원수_변경() {
        orderTable.changeEmpty(false);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        OrderTableRequest request = new OrderTableRequest(2, false);
        OrderTableResponse expected = tableService.changeNumberOfGuests(orderTable.getId(), request);
        assertThat(expected.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @Test
    void 주문_테이블_아이디_리스트_기준_조회() {
        OrderTable orderTableNo1 = new OrderTable( 1L, 0, true);
        OrderTable orderTableNo2 = new OrderTable( 2L, 0, true);
        List<OrderTable> orderTables = Arrays.asList(orderTableNo1, orderTableNo2);
        when(orderTableRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(orderTables);

        OrderTables expected = tableService.findAllByIds(Arrays.asList(1L, 2L));
        assertThat(expected).isEqualTo(new OrderTables(orderTables));
    }

    @Test
    void 테이블_그룹_아이디_기준_으로_주문_테이블_조회() {
        OrderTable orderTableNo1 = new OrderTable( 1L, 0, true);
        orderTableNo1.withTableGroup(tableGroup);
        OrderTable orderTableNo2 = new OrderTable( 2L, 0, true);
        orderTableNo2.withTableGroup(tableGroup);
        List<OrderTable> orderTables = Arrays.asList(orderTableNo1, orderTableNo2);

        when(orderTableRepository.findAllByTableGroup(1L)).thenReturn(orderTables);

        OrderTables expected = tableService.findAllByTableGroupId(1L);
        assertThat(expected).isEqualTo(new OrderTables(orderTables));
    }
}
