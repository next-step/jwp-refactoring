package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.event.TableGroupCreatedEvent;
import kitchenpos.event.TableGroupUnGroupEvent;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    TableGroupRepository tableGroupRepository;
    @Mock
    ApplicationEventPublisher eventPublisher;
    @Mock
    TableService tableService;


    @DisplayName("단체를 지정한다.")
    @Test
    void createTest() {

        // given
        OrderTable orderTable1 = mock(OrderTable.class);
        OrderTable orderTable2 = mock(OrderTable.class);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setTableGroupToOrderTables(Arrays.asList(orderTable1, orderTable2));

        TableGroup savedTableGroup = mock(TableGroup.class);
        when(savedTableGroup.getId()).thenReturn(1L);

        when(tableGroupRepository.save(tableGroup)).thenReturn(savedTableGroup);

        OrderTable expectedOrderTable1 = mock(OrderTable.class);
        OrderTable expectedOrderTable2 = mock(OrderTable.class);

        when(savedTableGroup.getOrderTables()).thenReturn(Arrays.asList(expectedOrderTable1, expectedOrderTable2));

        TableGroupService tableGroupService = new TableGroupService(tableGroupRepository, tableService, eventPublisher);

        // when
        TableGroup createdTableGroup = tableGroupService.create(TableGroupRequest.from(tableGroup));

        // then
        verify(eventPublisher).publishEvent(any(TableGroupCreatedEvent.class));
        assertThat(createdTableGroup.getId()).isNotNull();
        assertThat(createdTableGroup.getOrderTables()).containsExactly(expectedOrderTable1, expectedOrderTable2);
    }


    @DisplayName("단체를 해제한다.")
   @Test
    void ungroupTest(){

        // given
        Long tableGroupId = 1L;

        OrderTable orderTable1 = mock(OrderTable.class);
        OrderTable orderTable2 = mock(OrderTable.class);
        TableGroup tableGroup = mock(TableGroup.class);

        when(tableService.getOrderTablesByTableGroup(tableGroup)).thenReturn(Arrays.asList(orderTable1, orderTable2));

        OrderTable expectedOrderTable1 = mock(OrderTable.class);
        when(expectedOrderTable1.getTableGroup()).thenReturn(null);
        OrderTable expectedOrderTable2 = mock(OrderTable.class);
        when(expectedOrderTable2.getTableGroup()).thenReturn(null);

        TableGroupService tableGroupService = new TableGroupService(tableGroupRepository, tableService, eventPublisher);

        when(tableGroupRepository.findById(tableGroupId)).thenReturn(Optional.of(tableGroup));
        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(eventPublisher).publishEvent(any(TableGroupUnGroupEvent.class));
        assertThat(expectedOrderTable1.getTableGroup()).isNull();
        assertThat(expectedOrderTable2.getTableGroup()).isNull();
    }
}
