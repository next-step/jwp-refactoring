package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.*;
import static kitchenpos.fixture.TableGroupFixture.*;
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

import kitchenpos.table.application.TableGroupCreatedEventHandler;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.dto.TableGroupCreatedEvent;

@DisplayName("단체 지정 생성 이벤트 헨들러")
@ExtendWith(MockitoExtension.class)
class TableGroupCreatedEventHandlerTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableGroupCreatedEventHandler tableGroupCreatedEventHandler;

    @DisplayName("주문 테이블 없음")
    @Test
    void handle_empty_order_tables() {
        // given
        List<Long> orderTableIds = Collections.emptyList();
        TableGroupCreatedEvent tableGroupCreatedEvent = tableGroupCreateEvent(orderTableIds, 1L);

        // when, then
        assertThatThrownBy(() -> tableGroupCreatedEventHandler.handle(tableGroupCreatedEvent))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 2개 미만")
    @Test
    void handle_order_tables_less_than_2() {
        // given
        List<Long> orderTableIds = Collections.singletonList(1L);
        TableGroupCreatedEvent tableGroupCreatedEvent = tableGroupCreateEvent(orderTableIds, 1L);

        // when, then
        assertThatThrownBy(() -> tableGroupCreatedEventHandler.handle(tableGroupCreatedEvent))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어있지 않는 주문 테이블 존재")
    @Test
    void handle_order_tables_not_saved() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableGroupCreatedEvent tableGroupCreatedEvent = tableGroupCreateEvent(orderTableIds, 1L);
        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(Collections.singletonList(
            savedOrderTable(1L, true)
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupCreatedEventHandler.handle(tableGroupCreatedEvent))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 생성 성공")
    @Test
    void handle() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        TableGroupCreatedEvent tableGroupCreatedEvent = tableGroupCreateEvent(orderTableIds, 1L);

        OrderTable orderTable1 = savedOrderTable(1L, true);
        OrderTable orderTable2 = savedOrderTable(2L, true);
        given(orderTableRepository.findAllByIdIn(orderTableIds)).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        tableGroupCreatedEventHandler.handle(tableGroupCreatedEvent);

        // then
        assertAll(
            () -> assertThat(orderTable1.getTableGroupId()).isEqualTo(1L),
            () -> assertThat(orderTable1.isNotEmpty()).isTrue(),
            () -> assertThat(orderTable2.getTableGroupId()).isEqualTo(1L),
            () -> assertThat(orderTable2.isNotEmpty()).isTrue()
        );
    }
}
