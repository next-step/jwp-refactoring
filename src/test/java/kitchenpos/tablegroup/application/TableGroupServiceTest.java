package kitchenpos.tablegroup.application;

import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private TableGroupValidator tableGroupValidator;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    void create() {
        // given
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
            Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));
        final OrderTable orderTable = new OrderTable(2);
        orderTable.changeEmpty(true);
        final OrderTable orderTable2 = new OrderTable(3);
        orderTable2.changeEmpty(true);
        final TableGroup tableGroup = new TableGroup();
        given(tableGroupRepository.save(tableGroup)).willReturn(new TableGroup());

        // when
        tableGroupService.create(tableGroupRequest);

        // then
        verify(tableGroupRepository).save(any(TableGroup.class));
    }

    @Test
    void ungroup() {
        // given
        final Long tableGroupId = 1L;
        given(tableGroupRepository.findById(tableGroupId)).willReturn(Optional.of(new TableGroup()));

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(tableGroupRepository).findById(tableGroupId);
    }
}
