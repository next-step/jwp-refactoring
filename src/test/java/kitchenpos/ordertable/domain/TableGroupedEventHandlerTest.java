package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TableGroupedEventHandlerTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableGroupedEventHandler tableGroupedEventHandler;

    private OrderTable tableA;
    private OrderTable tableB;
    private TableGroup tableGroup;
    private TableGroupedEvent event;

    @BeforeEach
    void setUp() {
        tableA = new OrderTable(0, true);
        tableB = new OrderTable(0, true);
        tableGroup = new TableGroup(Arrays.asList(1L, 2L));
        ReflectionTestUtils.setField(tableGroup, "id", 1L);
        event = new TableGroupedEvent(tableGroup, Arrays.asList(1L, 2L));
    }

    @DisplayName("주문 테이블을 단체 테이블로 지정할 수 있다.")
    @Test
    void group() {
        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(tableA, tableB));

        tableGroupedEventHandler.handle(event);

        assertThat(tableA.getTableGroupId()).isNotNull();
        assertThat(tableB.getTableGroupId()).isNotNull();
    }

    @DisplayName("테이블이 존재하지 않는 경우 단체 테이블로 지정할 수 없다.")
    @Test
    void groupingTableNotExist() {
        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupedEventHandler.handle(event))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 존재하지 않습니다.");
    }

    @DisplayName("이미 단체 테이블로 지정되어 있는 경우 단체 테이블로 지정할 수 없다.")
    @Test
    void groupingTableAlreadyGrouped() {
        tableA.groupBy(tableGroup.getId());
        tableB.groupBy(tableGroup.getId());
        given(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(tableA, tableB));

        assertThatThrownBy(() -> tableGroupedEventHandler.handle(event))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블로 지정되어 있습니다.");
    }
}
