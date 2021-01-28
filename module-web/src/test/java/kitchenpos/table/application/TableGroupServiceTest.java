package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    private List<OrderTable> orderTables = new ArrayList<>();
    private List<Long> orderTableRequests = new ArrayList<>();
    private TableGroup tableGroup;
    private TableGroupRequest tableGroupRequest;
    private TableGroupService tableGroupService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        orderTables.add(new OrderTable(1L, 1, true));
        orderTables.add(new OrderTable(2L, 2, true));
        orderTables.add(new OrderTable(3L, 3, true));
        tableGroup = new TableGroup(new OrderTables(orderTables));

        orderTableRequests.add(1L);
        orderTableRequests.add(2L);
        orderTableRequests.add(3L);
        tableGroupRequest = new TableGroupRequest(orderTableRequests);

        tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);
    }

    @DisplayName("테이블그룹 생성 테스트")
    @Test
    void createTest() {
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(orderTables);

        TableGroupResponse resultGroupResponse = tableGroupService.create(tableGroupRequest);

        assertThat(resultGroupResponse.getOrderTables().size()).isEqualTo(3);
    }


    @DisplayName("테이블그룹 생성 예외테스트: 테이블이 2개이하인 경우")
    @Test
    void notEnoughTable() {
        orderTables.remove(0);
        orderTables.remove(1);
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(orderTables);
        tableGroupRequest = new TableGroupRequest(orderTableRequests);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(tableGroupRequest)
        );

        assertThat(exception.getMessage()).isEqualTo("그룹핑할 테이블이 부족합니다.");
    }


    @DisplayName("테이블그룹 생성 예외테스트: 테이블이 비어있지 않은 경우")
    @Test
    void notEmptyTable() {
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(orderTables);
        orderTables.get(0).changeStatus(false);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(tableGroupRequest)
        );

        assertThat(exception.getMessage()).isEqualTo("테이블이 사용중입니다.");
    }

    @DisplayName("테이블그룹 생성 예외테스트: 테이블에 그룹아이디가 있는경우")
    @Test
    void existGroupId() {
        orderTables.add(new OrderTable(2L));
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(orderTables);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(tableGroupRequest)
        );

        assertThat(exception.getMessage()).isEqualTo("그룹핑된 상태입니다.");
    }

    @DisplayName("테이블그룹 해제 예외테스트: 테이블상태가 유효하지 않은 경우")
    @Test
    void invalidTableStatus() {
        tableGroup.getOrderTables().getOrderTables().get(0).addOrder(Order.empty());
        when(tableGroupRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(tableGroup));

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.ungroup(1L)
        );

        assertThat(exception.getMessage()).isEqualTo("주문이 완료되지 않았습니다.");

    }

}
