package kitchenpos.core;

import kitchenpos.core.domain.OrderTable;
import kitchenpos.core.domain.OrderTableGroupingEventHandler;
import kitchenpos.core.domain.OrderTableRepository;
import kitchenpos.core.domain.TableGroup;
import kitchenpos.core.event.TableGroupedEvent;
import kitchenpos.core.event.TableUnGroupedEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.core.OrderTableFixture.getOrderTable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderTableGroupingEventHandlerTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderTableGroupingEventHandler orderTableGroupingEventHandler;

    @Test
    @DisplayName("주문테이블에서 단체지정을 할 수 있다.")
    void groupingHandle() {
        // given
        List<OrderTable> 주문테이블_목록 = Arrays.asList(getOrderTable(1L, false, 3),
                getOrderTable(2L, false, 3));
        given(orderTableRepository.findAllById(any())).willReturn(주문테이블_목록);
        final long tableGroupId = 1L;
        // when
        orderTableGroupingEventHandler.groupingHandle(TableGroupedEvent.of(TableGroup.generate(tableGroupId,
                Arrays.asList(1L, 2L))));
        // then
        Assertions.assertThat(주문테이블_목록).extracting("tableGroupId").containsExactly(tableGroupId, tableGroupId);
    }

    @Test
    @DisplayName("주문테이블에서 단체지정을 해지한다.")
    void unGroupingHandle() {
        // given
        List<OrderTable> 주문테이블_목록 = Arrays.asList(getOrderTable(1L, false, 3),
                getOrderTable(2L, false, 3));
        // when
        orderTableGroupingEventHandler.unGroupingHandle(TableUnGroupedEvent.of(TableGroup.generate(1L,
                Arrays.asList(1L, 2L))));
        // then
        Assertions.assertThat(주문테이블_목록).extracting("tableGroupId").containsExactly(null, null);
    }
}
