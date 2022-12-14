package kitchenpos.tablegroup.domain;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    private OrderTable tableA;
    private OrderTable tableB;

    @BeforeEach
    void setUp() {
        tableA = new OrderTable(0, true);
        tableB = new OrderTable(0, true);
    }

    @DisplayName("테이블 그룹을 지정할 수 있다.")
    @Test
    void groupingTable() {
        List<OrderTable> tables = Arrays.asList(tableA, tableB);
        List<Long> tableIds = Arrays.asList(1L, 2L);
        given(orderTableRepository.findAllByIdIn(tableIds)).willReturn(tables);

        tableGroupValidator.groupingTable(tableIds, 1L);

        assertThat(tableA.getTableGroupId()).isEqualTo(1L);
        assertThat(tableB.getTableGroupId()).isEqualTo(1L);
    }

    @DisplayName("테이블이 존재하지 않는 경우 테이블을 지정할 수 없다.")
    @Test
    void groupingTableNotExist() {
        List<Long> tableIds = Arrays.asList(1L, 2L);
        given(orderTableRepository.findAllByIdIn(tableIds)).willReturn(singletonList(tableA));

        assertThatThrownBy(() -> tableGroupValidator.groupingTable(tableIds, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 존재하지 않습니다.");
    }

    @DisplayName("이미 단체 테이블로 지정되어 있는 경우 테이블 그룹을 지정할 수 없다.")
    @Test
    void groupingTableAlreadyGrouped() {
        tableA.groupBy(1L);
        tableB.groupBy(1L);
        List<OrderTable> tables = Arrays.asList(tableA, tableB);
        List<Long> tableIds = Arrays.asList(1L, 2L);
        given(orderTableRepository.findAllByIdIn(tableIds)).willReturn(tables);

        assertThatThrownBy(() -> tableGroupValidator.groupingTable(tableIds, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블로 지정되어 있습니다.");
    }

    @DisplayName("테이블 그룹을 해제할 수 있다.")
    @Test
    void unGroup() {
        tableA.groupBy(1L);
        tableB.groupBy(1L);
        given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(Arrays.asList(tableA, tableB));
        given(orderRepository.findAllByOrderTableId(any())).willReturn(Collections.emptyList());

        tableGroupValidator.ungroup(1L);

        assertThat(tableA.isGrouped()).isFalse();
        assertThat(tableB.isGrouped()).isFalse();
    }

    @DisplayName("주문이 완료되지 않은 테이블이 존재하는 경우 그룹을 해제할 수 없다.")
    @Test
    void ungroupWithException() {
        tableA.groupBy(1L);
        tableB.groupBy(1L);
        given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(Arrays.asList(tableA, tableB));
        given(orderRepository.findAllByOrderTableId(any())).willReturn(Arrays.asList(new Order(1L)));

        assertThatThrownBy(() -> tableGroupValidator.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료되지 않은 주문이 존재합니다.");
    }
}
