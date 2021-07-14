package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
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
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.exception.OrderAlreadyExistsException;
import kitchenpos.table.application.OrderTableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.MisMatchedOrderTablesSizeException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@DisplayName("단체 지정 서비스")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private ApplicationEventPublisher publisher;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderTableValidator orderTableValidator;

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
        OrderTable groupOrderTable1 = new OrderTable(3, true);
        OrderTable groupOrderTable2 = new OrderTable(3, true);
        groupOrderTable1.ungroup();
        groupOrderTable2.ungroup();
//        groupOrderTable1.setTableGroup(null);
//        groupOrderTable2.setTableGroup(null);
        given(orderTableRepository.countByIdIn(any(List.class))).willReturn(2L);
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(tableGroup);


        // when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // then
        verify(orderTableValidator).validateOrderTableIsEmptyOrHasTableGroups(any(List.class));
        verify(publisher).publishEvent(any(GroupedTablesEvent.class));
    }

    @TestFactory
    @DisplayName("단체지정 등록 오류")
    List<DynamicTest> group_exception() {
        return Arrays.asList(
                dynamicTest("단체지정 테이블이 없는 경우 오류 발생.", () -> {
                    // given
                    TableGroupRequest tableGroupRequest = new TableGroupRequest(new ArrayList<>());

                    // then
                    assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("단체지정 테이블이 2개 이상이 아닐 경우 오류 발생.", () -> {
                    // given
                    TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L));

                    // then
                    assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                            .isInstanceOf(IllegalArgumentException.class);
                }),
                dynamicTest("단체지정 테이블 중 등록되지 않은 테이블이 존재할 경우 오류 발생.", () -> {
                    // given
                    TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));
                    OrderTable orderTable = new OrderTable(3, true);
                    given(orderTableRepository.countByIdIn(any(List.class))).willReturn(1L);

                    // then
                    assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                            .isInstanceOf(MisMatchedOrderTablesSizeException.class)
                            .hasMessage("입력된 항목과 조회결과가 일치하지 않습니다.");
                })
        );
    }

    @Test
    @DisplayName("단체지정 취소")
    void ungroup() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable(3, true);
        OrderTable orderTable2 = new OrderTable(3, true);
        orderTable1.groupBy(1L);
        orderTable2.groupBy(1L);
        given(orderTableRepository.findByTableGroupId(anyLong())).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        tableGroupService.ungroup(1L);

        // then
        verify(orderValidator).validateExistsOrdersStatusIsCookingOrMeal(any(List.class));
        verify(publisher).publishEvent(any(UngroupedTablesEvent.class));
    }

    @TestFactory
    @DisplayName("단체지정 취소 오류")
    List<DynamicTest> ungroup_exception() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable(3, true);
        OrderTable orderTable2 = new OrderTable(3, true);
        orderTable1.groupBy(1L);
        orderTable2.groupBy(1L);

        return Arrays.asList(
                dynamicTest("테이블들의 주문 상태가 COOKING이거나 MEAL인 상태가 존재하는 경우 오류 발생.", () -> {
                    // and
                    given(orderTableRepository.findByTableGroupId(anyLong())).willReturn(Arrays.asList(orderTable1, orderTable2));
                    doThrow(OrderAlreadyExistsException.class).when(orderValidator).validateExistsOrdersStatusIsCookingOrMeal(any(List.class));

                    // then
                    assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                            .isInstanceOf(OrderAlreadyExistsException.class);
                })
        );
    }
}
