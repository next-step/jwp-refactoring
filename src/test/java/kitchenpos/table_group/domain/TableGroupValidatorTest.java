package kitchenpos.table_group.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@DisplayName("단체 지정 유효성검사 관련")
@SpringBootTest
class TableGroupValidatorTest {
    @Autowired
    TableGroupValidator tableGroupValidator;
    @MockBean
    OrderTableRepository orderTableRepository;
    @MockBean
    TableUngroupValidator tableUngroupValidator;

    @DisplayName("주문 테이블이 존재하는 지 확인한다")
    @Test
    void checkOrderTableIds() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        List<OrderTable> savedOrderTables = Arrays.asList(new OrderTable(0, true), new OrderTable(0, true));
        when(orderTableRepository.findByIdIn(orderTableIds)).thenReturn(savedOrderTables);

        // when
        List<GroupTable> groupTables = tableGroupValidator.checkOrderTableIds(orderTableIds);

        // then
        assertThat(groupTables).hasSize(2);
    }

    @DisplayName("주문 테이블은 존재해야 한다")
    @Test
    void orderTableNotExist() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        List<OrderTable> savedOrderTables = singletonList(new OrderTable(0, true));
        when(orderTableRepository.findByIdIn(orderTableIds)).thenReturn(savedOrderTables);

        // when then
        assertThatThrownBy(() -> tableGroupValidator.checkOrderTableIds(orderTableIds))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제 불가능")
    @Test
    void invalidUngroup() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> groupedOrderTables = Arrays.asList(new OrderTable(0, true), new OrderTable(0, true));
        when(orderTableRepository.findByTableGroupId(anyLong())).thenReturn(groupedOrderTables);
        doThrow(IllegalArgumentException.class).when(tableUngroupValidator).validate(anyList());

        // when then
        assertThatThrownBy(() -> tableGroupValidator.checkUngroupTables(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
