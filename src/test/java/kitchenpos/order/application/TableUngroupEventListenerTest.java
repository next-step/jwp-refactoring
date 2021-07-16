package kitchenpos.order.application;

import kitchenpos.table.domain.TableUngroupedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableUngroupEventListenerTest {

    @Mock
    private OrderService orderService;

    private TableUngroupEventListener tableUngroupEventListener;

    @BeforeEach
    void setUp() {
        tableUngroupEventListener = new TableUngroupEventListener(orderService);
    }

    @Test
    void ungroupTable() {
        TableUngroupedEvent tableUngroupedEvent = new TableUngroupedEvent(Arrays.asList(1L, 2L));

        when(orderService.isUpgroupTableStatus(anyList()))
                .thenReturn(true);

        assertThatThrownBy(() -> tableUngroupEventListener.ungroupTable(tableUngroupedEvent))
                .isInstanceOf(IllegalStateException.class);
    }
}
