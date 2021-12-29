package kitchenpos.ordertable.domain;


import static kitchenpos.ordertable.application.fixture.OrderTableFixture.단체지정된_주문테이블;
import static kitchenpos.ordertable.application.fixture.OrderTableFixture.빈_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.ordertable.domain.event.TableGroupingEventListener;
import kitchenpos.ordertable.domain.event.TableUnGroupingEventListener;
import kitchenpos.tablegroup.domain.event.TableGroupingEvent;
import kitchenpos.tablegroup.domain.event.TableUnGroupingEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableGroupingEvenListenerTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    TableUnGroupingEventListener tableUnGroupingEventListener;

    @InjectMocks
    TableGroupingEventListener tableGroupingEventListener;

    @Test
    @DisplayName("주문테이블에서 단체지정을 할 수 있다.")
    void groupingHandle() {
        // given
        List<OrderTable> 주문테이블_목록 = Arrays.asList(빈_테이블(), 빈_테이블());

        // when
        when(orderTableRepository.findAllById(any())).thenReturn(주문테이블_목록);
        // then
        assertThat(주문테이블_목록).extracting("tableGroupId").containsExactly(null, null);

        // when
        tableGroupingEventListener.onApplicationEvent(
            new TableGroupingEvent(1L, Collections.singletonList(1L)));
        // then
        assertThat(주문테이블_목록).extracting("tableGroupId").containsExactly(1L, 1L);
    }

    @Test
    @DisplayName("주문테이블에서 단체지정을 해지한다.")
    void unGroupingHandle() {
        // given
        List<OrderTable> 주문테이블_목록 = Arrays.asList(단체지정된_주문테이블(), 단체지정된_주문테이블());

        // when
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(주문테이블_목록);
        // then
        assertThat(주문테이블_목록).extracting("tableGroupId").containsExactly(1L, 1L);

        // when
        tableUnGroupingEventListener.onApplicationEvent(new TableUnGroupingEvent(1L));
        // then
        assertThat(주문테이블_목록).extracting("tableGroupId").containsExactly(null, null);
    }
}
