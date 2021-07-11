package kitchenpos.tablegroup.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableService orderTableService;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroupRequest tableGroupRequest;
    private OrderTableRequest tableRequest1;
    private OrderTableRequest tableRequest2;

    @BeforeEach
    void setUp() {
        tableRequest1 = new OrderTableRequest(1, true);
        tableRequest2 = new OrderTableRequest(2, true);
        tableGroupRequest = new TableGroupRequest(Arrays.asList(tableRequest1, tableRequest2));
    }



    @DisplayName("1개 이하 주문 테이블은 단체 지정 할 수 없다.")
    @Test
    void createTempFailBecauseOfOrderTablesSizeTest() {
        //given
        TableGroupRequest emptyRequest= new TableGroupRequest(new ArrayList<>());

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(emptyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 2개 이상이어야 합니다.");

        //given
        TableGroupRequest sizeOneRequest= new TableGroupRequest(Arrays.asList(tableRequest1));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(sizeOneRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 2개 이상이어야 합니다.");
    }

    @DisplayName("주문 테이블은 모두 등록 되어 있어야 한다.")
    @Test
    void createTempFailBecauseOfNotExistOrderTableTest() {
        //given
        given(orderTableService.findAllByIdIn(any())).willReturn(new ArrayList<>());

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 주문 테이블이 있습니다.");
    }

    @DisplayName("빈 테이블이어야 한다.")
    @Test
    void createTempFailBecauseOfNotEmptyTableTest() {
        //given
        OrderTable notEmptyOrderTable = new OrderTable(1L,null,1,false);
        OrderTable emptyOrderTable = new OrderTable(1L,null,1,true);

        given(orderTableService.findAllByIdIn(any())).willReturn(Arrays.asList(notEmptyOrderTable,emptyOrderTable));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 주문 테이블이 아닙니다.");
    }

    @DisplayName("이미 단체 지정된 테이블이어서는 안된다.")
    @Test
    void createTempFailBecauseOfHasTableGroupIdTest() {
        //given
        OrderTable hasNotOrderTable = new OrderTable(1L,null,1,true);
        OrderTable hasGroupOrderTable = new OrderTable(1L, new TableGroup(),1,true);
        given(orderTableService.findAllByIdIn(any())).willReturn(Arrays.asList(hasNotOrderTable,hasGroupOrderTable));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 단체 지정된 테이블이 있습니다.");
    }

    @DisplayName("단체 지정.")
    @Test
    void createTempTest() {
        //given
        OrderTable normalTableEntity = new OrderTable(1L,null,1,true);
        OrderTable normalTableEntity2 = new OrderTable(1L, null,2,true);
        TableGroup tableGroup =new TableGroup(1L, LocalDateTime.now(), new OrderTables(Arrays.asList(normalTableEntity,normalTableEntity2)));
        given(orderTableService.findAllByIdIn(any())).willReturn(Arrays.asList(normalTableEntity,normalTableEntity2));
        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        //when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(tableGroupResponse.getOrderTables().size()).isEqualTo(tableGroupRequest.getOrderTables().size());
    }


    @DisplayName("주문 테이블의 주문 상태가 `조리`, `식사` 이면 해제할 수 없다.")
    @Test
    void ungroupFailBecauseOfOrderStatusTest() {
        //given
        OrderTable normalTableEntity = new OrderTable(1L,null,1,true);
        OrderTable normalTableEntity2 = new OrderTable(1L, null,2,true);
        TableGroup tableGroup =new TableGroup(1L, LocalDateTime.now(), new OrderTables(Arrays.asList(normalTableEntity,normalTableEntity2)));
        given(tableGroupRepository.findById(any())).willReturn(java.util.Optional.of(tableGroup));
        given(orderService.existsByOrderTableIdInAndOrderStatusIn(any(),any())).willReturn(true);

        //when && then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 조리나 식사 상태에서는 변경할 수 없습니다.");
    }

    @DisplayName("단체 지정 해제.")
    @Test
    void ungroupTest() {
        //given
        OrderTable normalTableEntity = new OrderTable(1L,null,1,true);
        OrderTable normalTableEntity2 = new OrderTable(1L, null,2,true);
        TableGroup tableGroup =new TableGroup(1L, LocalDateTime.now(), new OrderTables(Arrays.asList(normalTableEntity,normalTableEntity2)));
        given(tableGroupRepository.findById(any())).willReturn(java.util.Optional.of(tableGroup));
        given(orderService.existsByOrderTableIdInAndOrderStatusIn(any(),any())).willReturn(false);

        //when
        tableGroupService.ungroup(any());
    }


}