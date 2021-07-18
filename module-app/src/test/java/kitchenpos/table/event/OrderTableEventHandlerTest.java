package kitchenpos.table.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.common.event.GroupedTablesEvent;
import kitchenpos.common.event.UngroupedTablesEvent;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class OrderTableEventHandlerTest {
    @MockBean
    private OrderTableEventHandler orderTableEventHandler;

    @Autowired
    private ApplicationEventPublisher publisher;


    @Test
    @DisplayName("단체지정 취소 이벤트 핸들러 처리 결과 확인")
    void ungroup_events() {
        // when
        publisher.publishEvent(new UngroupedTablesEvent(Arrays.asList(1L, 2L)));

        // then
        verify(orderTableEventHandler).ungroupedTables(any());
    }

    @Test
    @DisplayName("단체지정 이벤트 핸들러 처리 결과 확인")
    void group_events() {
        // when
        publisher.publishEvent(new GroupedTablesEvent(Arrays.asList(1L, 2L), 1L));

        // then
        verify(orderTableEventHandler).groupedTables(any());
    }
}
