package kitchenpos.tablegroup.application;

import static kitchenpos.table.domain.OrderTableFixture.*;
import static kitchenpos.tablegroup.domain.TableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreateEvent;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupUngroupEvent;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@DisplayName("단체 지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정 등록 API")
    @Test
    void create() {
        // given
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        OrderTable savedOrderTable1 = savedOrderTable(orderTableId1, 1L, false);
        OrderTable savedOrderTable2 = savedOrderTable(orderTableId2, 1L, false);
        TableGroupRequest tableGroupRequest = tableGroupRequest(Arrays.asList(orderTableId1, orderTableId2));
        given(orderTableRepository.findAllByIdIn(Arrays.asList(orderTableId1, orderTableId2)))
            .willReturn(Arrays.asList(savedOrderTable1, savedOrderTable2));
        TableGroup savedTableGroup = savedTableGroup(1L);
        given(tableGroupRepository.save(any())).willReturn(savedTableGroup);
        doNothing().when(applicationEventPublisher).publishEvent(any(TableGroupCreateEvent.class));

        // when
        TableGroupResponse actual = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getOrderTables()).hasSize(2),
            () -> assertThat(savedOrderTable1.isEmpty()).isFalse(),
            () -> assertThat(savedOrderTable1.getTableGroupId()).isEqualTo(1L),
            () -> assertThat(savedOrderTable2.isEmpty()).isFalse(),
            () -> assertThat(savedOrderTable2.getTableGroupId()).isEqualTo(1L),
            () -> then(applicationEventPublisher).should().publishEvent(any(TableGroupCreateEvent.class))
        );
    }

    @DisplayName("단체 지정 해제 API")
    @Test
    void ungroup() {
        // given
        long tableGroupId = 1L;
        doNothing().when(applicationEventPublisher).publishEvent(any(TableGroupUngroupEvent.class));

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        then(applicationEventPublisher).should().publishEvent(any(TableGroupUngroupEvent.class));
    }
}
