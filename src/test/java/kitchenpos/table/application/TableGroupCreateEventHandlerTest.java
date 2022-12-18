package kitchenpos.table.application;

import static kitchenpos.table.domain.OrderTableFixture.*;
import static kitchenpos.tablegroup.domain.TableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupCreateEvent;

@DisplayName("단체 지정 생성 이벤트 헨들러")
@ExtendWith(MockitoExtension.class)
class TableGroupCreateEventHandlerTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableGroupCreateEventHandler tableGroupCreateEventHandler;

    @DisplayName("주문 테이블 없음")
    @Test
    void handle_empty_order_tables() {
        // given
        List<Long> orderTableIds = Collections.emptyList();
        TableGroupCreateEvent tableGroupCreateEvent = tableGroupCreateEvent(orderTableIds, 1L);

        // when, then
        assertThatThrownBy(() -> tableGroupCreateEventHandler.handle(tableGroupCreateEvent))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 2개 미만")
    @Test
    void handle_order_tables_less_than_2() {
        // given
        List<Long> orderTableIds = Collections.singletonList(1L);
        TableGroupCreateEvent tableGroupCreateEvent = tableGroupCreateEvent(orderTableIds, 1L);

        // when, then
        assertThatThrownBy(() -> tableGroupCreateEventHandler.handle(tableGroupCreateEvent))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어있지 않는 주문 테이블 존재")
    @Test
    void handle_order_tables_not_saved() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableGroupCreateEvent tableGroupCreateEvent = tableGroupCreateEvent(orderTableIds, 1L);
        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(Collections.singletonList(
            savedOrderTable(1L, true)
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupCreateEventHandler.handle(tableGroupCreateEvent))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 주문 테이블 존재")
    @Test
    void handle_order_table_not_empty() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableGroupCreateEvent tableGroupCreateEvent = tableGroupCreateEvent(orderTableIds, 1L);
        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(Arrays.asList(
            savedOrderTable(1L, true),
            savedOrderTable(2L, false)
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupCreateEventHandler.handle(tableGroupCreateEvent))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("다른 단체 지정에 연결되어있는 주문 테이블 존재")
    @Test
    void handle_order_table_table_group_id_exists() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableGroupCreateEvent tableGroupCreateEvent = tableGroupCreateEvent(orderTableIds, 1L);
        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(Arrays.asList(
            savedOrderTable(1L, true),
            savedOrderTable(2L, 1L, true)
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupCreateEventHandler.handle(tableGroupCreateEvent))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이벤트 핸들링 성공")
    @Test
    void handle() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableGroupCreateEvent tableGroupCreateEvent = tableGroupCreateEvent(orderTableIds, 1L);

        OrderTable orderTable1 = savedOrderTable(1L, true);
        OrderTable orderTable2 = savedOrderTable(2L, true);
        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        tableGroupCreateEventHandler.handle(tableGroupCreateEvent);

        // then
        assertAll(
            () -> assertThat(orderTable1.getTableGroupId()).isEqualTo(1L),
            () -> assertThat(orderTable1.isNotEmpty()).isTrue(),
            () -> assertThat(orderTable2.getTableGroupId()).isEqualTo(1L),
            () -> assertThat(orderTable2.isNotEmpty()).isTrue()
        );
    }
}
