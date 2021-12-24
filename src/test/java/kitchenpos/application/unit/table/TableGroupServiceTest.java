package kitchenpos.application.unit.table;

import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.application.table.TableService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroupUnGroupEvent;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.domain.tablegroup.TableGroupCreatedEvent;
import kitchenpos.repository.tablegroup.TableGroupRepository;
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
        TableGroup createdTableGroup = tableGroupService.create(new TableGroupRequest(Arrays.asList(orderTable2.getId(), orderTable1.getId())));

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
        TableGroup createdTableGroup = tableGroupService.create(new TableGroupRequest(Arrays.asList(orderTable2.getId(), orderTable1.getId())));

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        verify(eventPublisher).publishEvent(any(TableGroupUnGroupEvent.class));
    }
}
