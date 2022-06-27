package kitchenpos.tablegroup.application;


import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.table.application.TableServiceTest.테이블_등록;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 관련 기능")
public class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = 테이블_등록(2, true);
        orderTable2 = 테이블_등록(4, true);
    }

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        // when
        TableGroupResponse tableGroup = 단체_지정_등록();

        // then
        assertThat(tableGroup).isNotNull();
    }

    @Test
    @DisplayName("테이블 그룹을 등록시, 테이블 갯수가 2보다 작으면 실패한다.")
    void createWithUnderTwoOrderTable() {
        // when-then
        assertThatThrownBy(() -> 단체_지정(Arrays.asList())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹으로 등록하려는 테이블이 등록되어 있지 않으면 실패한다.")
    void createWithDifferentOrderTable() {
        // given
        TableGroupRequest tableGroupRequest = 단체_지정_요청(Arrays.asList(OrderTableIdRequest.of(1L), OrderTableIdRequest.of(2L)));
        OrderTables orderTables = 단체_지정(Arrays.asList(orderTable1, orderTable2));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1));

        // when-then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹으로 등록하려는 테이블이 비어있지 않거나 다른 그룹에 등록되어 있으면 실패한다.")
    void createWithNotEmptyOrderTableOrNonNullTableGroupId() {
        // given
        TableGroupRequest tableGroupRequest = 단체_지정_요청(Arrays.asList(OrderTableIdRequest.of(1L), OrderTableIdRequest.of(2L)));
        orderTable1.changeEmpty(false);
        OrderTables orderTables = 단체_지정(Arrays.asList(orderTable1, orderTable2));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when-then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제한다.")
    void ungroup() {
        // given
        TableGroupResponse tableGroup = 단체_지정_등록();
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertThat(orderTable1.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("단체 지정을 해제할 때 식사중이거나 조리중인 테이블이 있으면 실패한다.")
    void ungroupWithCookingOrMealOrderTable() {
        // given
        TableGroupResponse tableGroup = 단체_지정_등록();
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when-then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    public TableGroupResponse 단체_지정_등록() {
        TableGroupRequest tableGroupRequest = 단체_지정_요청(Arrays.asList(OrderTableIdRequest.of(1L), OrderTableIdRequest.of(2L)));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(tableGroupRepository.save(any())).willReturn(TableGroup.of(1L));
        return tableGroupService.create(tableGroupRequest);
    }

    public static OrderTables 단체_지정(List<OrderTable> orderTables) {
        return OrderTables.of(orderTables);
    }

    public static TableGroupRequest 단체_지정_요청(List<OrderTableIdRequest> orderTables) {
        return TableGroupRequest.of(orderTables);
    }
}
