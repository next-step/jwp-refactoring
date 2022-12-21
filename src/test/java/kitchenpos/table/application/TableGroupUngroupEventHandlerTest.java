package kitchenpos.table.application;

import static kitchenpos.table.domain.OrderTableFixture.*;
import static kitchenpos.tablegroup.domain.TableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.dto.TableGroupUngroupEvent;

@DisplayName("단체 지정 해제 이벤트 헨들러")
@ExtendWith(MockitoExtension.class)
class TableGroupUngroupEventHandlerTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableGroupUngroupEventHandler tableGroupUngroupEventHandler;

    @DisplayName("주문 상태 조리중 또는 식사중")
    @Test
    void handle_cooking_or_meal() {
        TableGroupUngroupEvent tableGroupUngroupEvent = tableGroupUngroupEvent(1L);
        given(orderTableRepository.findAllByTableGroupId(anyLong())).willReturn(
            Collections.singletonList(savedOrderTable(1L, 1L)));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Collections.singletonList(1L),
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupUngroupEventHandler.handle(tableGroupUngroupEvent))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 해제 성공")
    @Test
    void handle() {
        TableGroupUngroupEvent tableGroupUngroupEvent = tableGroupUngroupEvent(1L);

        OrderTable orderTable1 = savedOrderTable(1L, 1L);
        OrderTable orderTable2 = savedOrderTable(2L, 1L);

        given(orderTableRepository.findAllByTableGroupId(anyLong()))
            .willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(false);

        // when
        tableGroupUngroupEventHandler.handle(tableGroupUngroupEvent);

        // then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }
}
