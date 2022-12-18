package kitchenpos.table.application;

import static kitchenpos.table.domain.OrderTableFixture.*;
import static kitchenpos.tablegroup.domain.TableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupUngroupEvent;

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
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupUngroupEventHandler.handle(tableGroupUngroupEvent))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
