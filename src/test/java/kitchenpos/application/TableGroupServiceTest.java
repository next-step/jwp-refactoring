package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 생성한다.")
    @Test
    public void createTableGroup() {

        //given
        TableGroupRequest given = TableGroupRequest.from(createOrderTableIds());
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(createOrderTables());
        when(tableGroupRepository.save(any())).thenReturn(TableGroup.of(createOrderTables()));
        //when
        TableGroupResponse result = tableGroupService.create(given);
        //then
        assertThat(result).isNotNull();
    }

    @DisplayName("주문 테이블이 2개 보다 적으면 에러")
    @Test
    public void createTableGroupWithLessOrderTables() {
        //given
        TableGroupRequest given = TableGroupRequest.from(Arrays.asList(1l));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(given)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 모두 등록되어 있지 않으면 에러")
    @Test
    public void createTableGroupWithNoExistOrderTables() {
        //given
        TableGroupRequest given = TableGroupRequest.from(Arrays.asList(1l, 2l));
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(new OrderTable(1l,  1, true)));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(given)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 테이블이 빈값 아니면 에러")
    @Test
    public void createTableGroupWithEmptyOrderTables() {
        //given
        TableGroupRequest given = TableGroupRequest.from(Arrays.asList(3l));
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(createNotEmptyOrderTables());
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(given)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 테이블이 단체 지정이 되어 있으면 에러")
    @Test
    public void createTableGroupExist() {
        //given
        TableGroupRequest given = TableGroupRequest.from(Arrays.asList(3l));
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(createHasTableGroupOrderTables());
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(given)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 지정을 해제한다.")
    @Test
    public void ungroup() {
        //given
        OrderTable orderTable = new OrderTable(1l, 1, true);
        orderTable.groupBy(new TableGroup(1l, LocalDateTime.now()));
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Arrays.asList(orderTable));
        //when
        tableGroupService.ungroup(1l);
        //then
        assertThat(orderTable.isGrouped()).isFalse();
    }

    @DisplayName("조리, 식사 중인 상태인 경우에는 해제할 수 없다.")
    @Test
    public void ungroupWithStatus(){
        //given
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(createOrderTables());
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);
        //when
        //then
        assertThatThrownBy(() ->  tableGroupService.ungroup(1l)).isInstanceOf(IllegalArgumentException.class);

    }

    private List<Long> createOrderTableIds() {
        return Arrays.asList(1l,2l);
    }

    private List<OrderTable> createOrderTables() {
        return Arrays.asList(new OrderTable(1l,  1, true), new OrderTable(2l,  1, true));
    }

    private List<OrderTable> createNotEmptyOrderTables() {
        return Arrays.asList(new OrderTable(3l,  1, true));
    }

    private List<OrderTable> createHasTableGroupOrderTables() {
        OrderTable orderTable = new OrderTable(3l, 1, true);
        orderTable.groupBy(new TableGroup(3l, LocalDateTime.now()));
        return Arrays.asList(orderTable, new OrderTable(3l,  1, true));
    }
}
