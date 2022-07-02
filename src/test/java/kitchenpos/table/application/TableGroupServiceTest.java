package kitchenpos.table.application;

import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("단체 지정 관련")
@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    TableGroupService tableGroupService;
    @MockBean
    TableGroupRepository tableGroupRepository;
    @MockBean
    OrderTableValidator orderTableValidator;

    Long orderTableId1;
    Long orderTableId2;
    OrderTable orderTable1;
    OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        setOrderTable();
    }

    void setOrderTable() {
        orderTableId1 = 1L;
        orderTableId2 = 2L;
        orderTable1 = new OrderTable(1, true);
        orderTable2 = new OrderTable(1, true);
        when(orderTableValidator.findExistsOrderTableByIdIn(singletonList(orderTableId1))).thenReturn(singletonList(orderTable1));
        when(orderTableValidator.findExistsOrderTableByIdIn(singletonList(orderTableId2))).thenReturn(singletonList(orderTable2));
        when(orderTableValidator.findExistsOrderTableByIdIn(Arrays.asList(orderTableId1, orderTableId2))).thenReturn(Arrays.asList(orderTable1, orderTable2));
    }

    @DisplayName("여러 주문 테이블을 단체 지정할 수 있다")
    @Test
    void create() {
        // when
        tableGroupService.create(Arrays.asList(orderTableId1, orderTableId2));

        // then
        verify(tableGroupRepository).save(any());
    }

    @DisplayName("없는 주문 테이블은 단체 지정할 수 없다")
    @Test
    void orderTable_is_exists() {
        // given
        List<Long> notExistsOrderTableIds = singletonList(1000L);

        // when
        assertThatThrownBy(() -> tableGroupService.create(notExistsOrderTableIds))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다")
    @Test
    void ungroup() {
        // given
        Long tableGroupId = 1L;
        OrderTable groupedTable1 = new OrderTable(1, true);
        OrderTable groupedTable2 = new OrderTable(1, true);
        TableGroup tableGroup = TableGroup.group(Arrays.asList(groupedTable1, groupedTable2));
        when(tableGroupRepository.findByIdWithOrderTable(tableGroupId)).thenReturn(Optional.of(tableGroup));

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(orderTableValidator).checkOrderStatusIn(anyList());
    }
}
