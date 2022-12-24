package kitchenpos.application;

import static kitchenpos.fixture.TableGroupFixture.*;
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

import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.tablegroup.application.OrderTableSupport;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupCreatedEvent;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.dto.TableGroupUngroupedEvent;

@DisplayName("단체 지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderTableSupport orderTableRepository;
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
        OrderTableResponse savedOrderTable1 = savedOrderTableResponse(orderTableId1, 1L, false);
        OrderTableResponse savedOrderTable2 = savedOrderTableResponse(orderTableId2, 1L, false);
        TableGroupRequest tableGroupRequest = tableGroupRequest(Arrays.asList(orderTableId1, orderTableId2));
        given(orderTableRepository.findOrderTables(Arrays.asList(orderTableId1, orderTableId2)))
            .willReturn(Arrays.asList(savedOrderTable1, savedOrderTable2));
        TableGroup savedTableGroup = savedTableGroup(1L);
        given(tableGroupRepository.save(any())).willReturn(savedTableGroup);
        doNothing().when(applicationEventPublisher).publishEvent(any(TableGroupCreatedEvent.class));

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
            () -> then(applicationEventPublisher).should().publishEvent(any(TableGroupCreatedEvent.class))
        );
    }

    @DisplayName("단체 지정 해제 API")
    @Test
    void ungroup() {
        // given
        long tableGroupId = 1L;
        doNothing().when(applicationEventPublisher).publishEvent(any(TableGroupUngroupedEvent.class));

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        then(applicationEventPublisher).should().publishEvent(any(TableGroupUngroupedEvent.class));
    }
}
