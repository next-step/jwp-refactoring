package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;
import kitchenpos.tablegroup.domain.TableUnGroupedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableUnGroupedEventHandlerTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderTableValidator orderTableValidator;

    @InjectMocks
    private TableUnGroupedEventHandler tableUnGroupedEventHandler;

    private OrderTable tableA;
    private OrderTable tableB;
    private TableUnGroupedEvent event;

    @BeforeEach
    void setUp() {
        tableA = new OrderTable(0, true);
        tableB = new OrderTable(0, true);
        tableA.groupBy(1L);
        tableB.groupBy(1L);
        event = new TableUnGroupedEvent(1L);
    }

    @DisplayName("단체 테이블 지정을 해제할 수 있다.")
    @Test
    void ungroup() {
        given(orderTableRepository.findAllByTableGroupId(event.getTableGroupId())).willReturn(Arrays.asList(tableA, tableB));

        tableUnGroupedEventHandler.handle(event);

        assertThat(tableA.getTableGroupId()).isNull();
        assertThat(tableB.getTableGroupId()).isNull();
    }

    @DisplayName("주문이 완료되지 않은 테이블이 존재하는 경우 그룹을 해제할 수 없다.")
    @Test
    void ungroupWithException() {
        given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(Arrays.asList(tableA, tableB));
        doThrow(new IllegalArgumentException("완료되지 않은 주문이 존재합니다."))
                .when(orderTableValidator)
                .validateOrderStatus(tableA.getId());

        assertThatThrownBy(() -> tableUnGroupedEventHandler.handle(event))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료되지 않은 주문이 존재합니다.");
    }
}
