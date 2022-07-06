package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.common.ServiceTestFactory.테이블생성;
import static kitchenpos.common.ServiceTestFactory.테이블요청생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
    }


    @Test
    void 테이블을_생성할_수_있다() {
        OrderTableRequest orderTableRequest = 테이블요청생성(3, true);
        OrderTable orderTable = 테이블생성(2L, 2L, 5, true);
        given(orderTableRepository.save(any())).willReturn(orderTable);

        OrderTableResponse createdOrderTable = tableService.create(orderTableRequest);

        assertThat(createdOrderTable.getId()).isNotNull();
    }

    @Test
    void 테이블을_조회할_수_있다() {
        OrderTable orderTable = 테이블생성(1L, null, 3, true);
        OrderTable orderTable2 = 테이블생성(2L, null, 5, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
        given(orderTableRepository.findAll()).willReturn(orderTables);

        List<OrderTableResponse> searchOrdertables = tableService.list();
//
        assertThat(searchOrdertables.size()).isEqualTo(orderTables.size());
    }

    @Test
    void 테이블을_비울_수_있다() {
        OrderTable orderTable = 테이블생성(1L, null, 3, false);
        OrderTableRequest request = 테이블요청생성(3, true);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(orderTable);

        OrderTableResponse changeEmptyTable = tableService.changeEmpty(orderTable.getId(), request);

        assertThat(changeEmptyTable.isEmpty()).isTrue();
    }

    @Test
    void 주문테이블이_존재하지않으면_테이블을_비울_수_없다() {
        OrderTable orderTable = 테이블생성(1L, null, 3, false);
        OrderTableRequest orderTableRequest = 테이블요청생성(3, false);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹이_존재하면_테이블을_비울_수_없다() {
        OrderTable orderTable = 테이블생성(1L, null, 3, false);
        OrderTableRequest orderTableRequest = 테이블요청생성(3, false);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문상태가_요리중이거나_식사중이면_테이블을_비울_수_없다() {
        OrderTable orderTable = 테이블생성(1L, null, 3, false);
        OrderTableRequest orderTableRequest = 테이블요청생성(3, false);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_인원수를_변경할_수_있다() {
        OrderTable orderTable = 테이블생성(1L, null, 3, false);
        OrderTableRequest 인원수변경_5명 = 테이블요청생성(5, false);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        OrderTableResponse result = tableService.changeNumberOfGuests(orderTable.getId(), 인원수변경_5명);

        assertThat(result.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 테이블의_인원수를_음수로_변경할_수_없다() {
        OrderTable orderTable = 테이블생성(1L, null, 3, false);
        OrderTableRequest 음수인원수로변경 = 테이블요청생성(-5, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), 음수인원수로변경))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지않는_테이블의_인원수를_변경할_수_없다() {
        OrderTable orderTable = 테이블생성(1L, null, 3, false);
        OrderTableRequest 인원수변경_5명 = 테이블요청생성(5, false);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), 인원수변경_5명))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
