package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
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

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.OrderAlreadyExistsException;
import kitchenpos.table.application.TableService;
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
    private TableService tableService;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupService tableGroupService;


    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("단체지정 등록")
    void create_group1() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));
        TableGroup tableGroup = new TableGroup();
        OrderTable groupOrderTable1 = new OrderTable(3, true);
        OrderTable groupOrderTable2 = new OrderTable(3, true);
//        groupOrderTable1.setTableGroupId(null);
//        groupOrderTable2.setTableGroupId(null);
        groupOrderTable1.setTableGroup(null);
        groupOrderTable2.setTableGroup(null);
        given(tableService.findOrderTablesByIds(any(List.class))).willReturn(Arrays.asList(groupOrderTable1, groupOrderTable2));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(tableGroup);

        // when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(tableGroupResponse.getOrderTableResponses()).size().isEqualTo(2);
    }

    @TestFactory
    @DisplayName("단체지정 등록 오류")
    List<DynamicTest> group_exception1() {
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
                    given(tableService.findOrderTablesByIds(any(List.class))).willReturn(Arrays.asList(orderTable));

                    // then
                    assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                            .isInstanceOf(MisMatchedOrderTablesSizeException.class)
                            .hasMessage("입력된 항목과 조회결과가 일치하지 않습니다.");
                }),
                dynamicTest("단체지정 테이블 중 비어있지 않은 테이블이 존재할 경우 오류 발생.", () -> {
                    // given
                    TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));
                    TableGroup tableGroup = new TableGroup();
                    OrderTable orderTable1 = new OrderTable(3, true);
                    OrderTable orderTable2 = new OrderTable(3, false);
                    given(tableService.findOrderTablesByIds(any(List.class))).willReturn(Arrays.asList(orderTable1, orderTable2));
                    given(tableGroupRepository.save(any(TableGroup.class))).willReturn(tableGroup);

                    // then
                    assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessage("비어있지 않은 테이블은 정산 그룹에 포함시킬 수 없습니다.");
                }),
                dynamicTest("단체지정 테이블 중 테이블 그룹이 지정되어 있는 테이블이 존재할 경우 오류 발생.", () -> {
                    // given
                    TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(1L, 2L));
                    OrderTable orderTable1 = new OrderTable(3, true);
                    OrderTable orderTable2 = new OrderTable(3, true);
                    orderTable1.setTableGroup(new TableGroup());
                    given(tableService.findOrderTablesByIds(any(List.class))).willReturn(Arrays.asList(orderTable1, orderTable2));

                    // then
                    assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                            .isInstanceOf(IllegalArgumentException.class);
//                            .hasMessage("정산 그룹에 포함된 테이블을 새로운 정산그룹에 포함시킬 수 없습니다.");
                })
        );
    }

    @Test
    @DisplayName("단체지정 취소")
    void ungroup1() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable(3, true);
        OrderTable orderTable2 = new OrderTable(3, true);
        tableGroup.addOrderTable(orderTable1);
        tableGroup.addOrderTable(orderTable2);
        given(orderTableRepository.findByTableGroupId(anyLong())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(List.class), any(List.class))).willReturn(false);

        // when
        tableGroupService.ungroup(1L);

        // then
        assertAll(
                () -> assertThat(orderTable1.hasTableGroup()).isFalse(),
                () -> assertThat(orderTable2.hasTableGroup()).isFalse()
        );
    }

    @TestFactory
    @DisplayName("단체지정 취소 오류")
    List<DynamicTest> ungroup_exception1() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable1 = new OrderTable(3, true);
        OrderTable orderTable2 = new OrderTable(3, true);
        tableGroup.addOrderTable(orderTable1);
        tableGroup.addOrderTable(orderTable2);

        return Arrays.asList(
                dynamicTest("테이블들의 주문 상태가 COOKING이거나 MEAL인 상태가 존재하는 경우 오류 발생.", () -> {
                    // and
                    given(orderTableRepository.findByTableGroupId(anyLong())).willReturn(Arrays.asList(orderTable1, orderTable2));
                    given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(List.class), any(List.class))).willReturn(true);

                    // then
                    assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                            .isInstanceOf(OrderAlreadyExistsException.class)
                            .hasMessage("수정할 수 없는 주문이 존재합니다.");
                })
        );
    }
}
