package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.tablegroup.domain.UngroupingTableEvent;

@ExtendWith(MockitoExtension.class)
class UngroupingTableEventHandlerTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private UngroupingTableEventHandler ungroupingTableEventHandler;

    @DisplayName("테이블 그룹 제거 이벤트")
    @Test
    void handle() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, 1L, 4, true),
            new OrderTable(2L, 1L, 2, true));
        테이블_그룹_ID로_조회_결과_반환(orderTables);

        UngroupingTableEvent ungroupingTableEvent = new UngroupingTableEvent(1L);

        // when and then
        assertThatCode(() -> ungroupingTableEventHandler.handle(ungroupingTableEvent))
            .doesNotThrowAnyException();
    }

    private void 테이블_그룹_ID로_조회_결과_반환(List<OrderTable> orderTables) {
        Mockito.when(orderTableRepository.findAllByTableGroupId(Mockito.anyLong()))
            .thenReturn(orderTables);
    }
}