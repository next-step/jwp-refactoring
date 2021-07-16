package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.common.event.GroupedTablesEvent;
import kitchenpos.common.event.UngroupedTablesEvent;
import kitchenpos.order.application.OrderTableGroupService;
import kitchenpos.order.domain.Order;
import kitchenpos.table.application.TableTableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@DisplayName("단체 지정 서비스")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupOrderValidator tableGroupOrderValidator;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableGroupOrderTableValidator tableGroupOrderTableValidator;
    @Mock
    private TableGroupValidator tableGroupValidator;
    @Mock
    private OrderTableGroupService orderTableGroupService;
    @Mock
    private TableTableGroupService tableTableGroupService;

    @InjectMocks
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("단체지정 등록")
    void create_group() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));
        TableGroup tableGroup = new TableGroup();
        given(tableTableGroupService.findOrderTableByIds(any(List.class))).willReturn(Arrays.asList(new OrderTable(3, false)));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(tableGroup);

        // when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // then
        verify(tableGroupValidator).validateOrderTablesConditionForCreatingTableGroup(any(List.class), anyInt());
        verify(publisher).publishEvent(any(GroupedTablesEvent.class));
    }

    @Test
    @DisplayName("단체지정 취소")
    void ungroup() {
        // given
        given(tableTableGroupService.findOrderTableIdsByTableGroupId(anyLong())).willReturn(Arrays.asList(1L, 2L));
        given(orderTableGroupService.findOrdersByOrderTableIdIn(any(List.class))).willReturn(Arrays.asList(new Order(LocalDateTime.now(), 1L)));

        // when
        tableGroupService.ungroup(1L);

        // then
        verify(tableGroupValidator).validateExistsOrdersStatusIsCookingOrMeal(any(List.class));
        verify(publisher).publishEvent(any(UngroupedTablesEvent.class));
    }

    @TestFactory
    @DisplayName("단체지정 취소 오류")
    List<DynamicTest> ungroup_exception() {
        return Arrays.asList(
                dynamicTest("테이블들의 주문 상태가 COOKING이거나 MEAL인 상태가 존재하는 경우 오류 발생.", () -> {
                    // and
                    given(tableTableGroupService.findOrderTableIdsByTableGroupId(anyLong())).willReturn(Arrays.asList(1L, 2L));
                    given(orderTableGroupService.findOrdersByOrderTableIdIn(any(List.class))).willReturn(Arrays.asList(new Order(LocalDateTime.now(), 1L)));
                    doThrow(RuntimeException.class).when(tableGroupValidator).validateExistsOrdersStatusIsCookingOrMeal(any(List.class));

                    // then
                    assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                            .isInstanceOf(RuntimeException.class);
                })
        );
    }
}
