package kitchenpos.tablegroup.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroupUnGroupEvent;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.domain.TableGroupCreatedEvent;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
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


    @DisplayName("단체를 지정한다.")
    @Test
    void createTest() {

        // given
        OrderTable orderTable1 = mock(OrderTable.class);
        OrderTable orderTable2 = mock(OrderTable.class);
        when(orderTable1.getId()).thenReturn(1L);
        when(orderTable2.getId()).thenReturn(2L);

        TableGroup tableGroup = new TableGroup();

        TableGroup savedTableGroup = mock(TableGroup.class);
        when(savedTableGroup.getId()).thenReturn(1L);

        when(tableGroupRepository.save(tableGroup)).thenReturn(savedTableGroup);

        TableGroupService tableGroupService = new TableGroupService(tableGroupRepository, eventPublisher);

        // when
        TableGroupResponse createdTableGroup = tableGroupService.create(new TableGroupRequest(Arrays.asList(orderTable2.getId(), orderTable1.getId())));

        // then
        verify(eventPublisher).publishEvent(any(TableGroupCreatedEvent.class));
        assertThat(createdTableGroup.getId()).isEqualTo(savedTableGroup.getId());
    }


    @DisplayName("단체를 해제한다.")
   @Test
    void ungroupTest(){

        // given
        OrderTable orderTable1 = mock(OrderTable.class);
        OrderTable orderTable2 = mock(OrderTable.class);
        when(orderTable1.getId()).thenReturn(1L);
        when(orderTable2.getId()).thenReturn(2L);

        TableGroup tableGroup = new TableGroup();

        TableGroup savedTableGroup = mock(TableGroup.class);
        when(savedTableGroup.getId()).thenReturn(1L);

        when(tableGroupRepository.save(tableGroup)).thenReturn(savedTableGroup);
        when(tableGroupRepository.findById(savedTableGroup.getId())).thenReturn(Optional.of(savedTableGroup));

        TableGroupService tableGroupService = new TableGroupService(tableGroupRepository, eventPublisher);
        TableGroupResponse createdTableGroup = tableGroupService.create(new TableGroupRequest(Arrays.asList(orderTable2.getId(), orderTable1.getId())));

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        verify(eventPublisher).publishEvent(any(TableGroupUnGroupEvent.class));
    }
}
