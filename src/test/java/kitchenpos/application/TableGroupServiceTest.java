package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    private List<OrderTable> orderTables = new ArrayList<>();
    private TableGroup tableGroup;
    private TableGroupRequest tableGroupRequest;
    private TableGroupService tableGroupService;

    @Mock
    private  OrderRepository orderRepository;

    @Mock
    private  OrderTableRepository orderTableRepository;

    @Mock
    private  TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        orderTables.add(new OrderTable(1L, 1, true));
        orderTables.add(new OrderTable(2L, 2, true));
        orderTables.add(new OrderTable(3L, 3, true));
        tableGroup = new TableGroup(new OrderTables(orderTables));
        tableGroupRequest = new TableGroupRequest(orderTables);

        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
    }

    @DisplayName("테이블그룹 생성 테스트")
    @Test
    void createTest() {

        when(tableGroupRepository.save(any())).thenReturn(tableGroup);
        when(orderTableRepository.save(orderTables.get(0))).thenReturn(orderTables.get(0));
        when(orderTableRepository.save(orderTables.get(1))).thenReturn(orderTables.get(1));
        when(orderTableRepository.save(orderTables.get(2))).thenReturn(orderTables.get(2));

        TableGroupResponse resultGroupResponse = tableGroupService.create(tableGroupRequest);

        assertThat(resultGroupResponse.getOrderTables().size()).isEqualTo(tableGroup.getOrderTables().getSize());
        List<Long> resultTableIds = resultGroupResponse.getOrderTables()
                .stream()
                .map(orderTableResponse -> orderTableResponse.getId())
                .collect(Collectors.toList());
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertThat(resultTableIds).containsExactlyElementsOf(orderTableIds);
    }


    @DisplayName("테이블그룹 생성 예외테스트: 테이블이 2개이하인 경우")
    @Test
    void notEnoughTable() {
        orderTables.remove(0);
        orderTables.remove(1);
        tableGroupRequest = new TableGroupRequest(orderTables);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(tableGroupRequest)
        );

        assertThat(exception.getMessage()).isEqualTo("그룹핑할 테이블이 부족합니다.");
    }


    @DisplayName("테이블그룹 생성 예외테스트: 테이블이 비어있지 않은 경우")
    @Test
    void notEmptyTable() {
        orderTables.get(0).changeStatus(false);
        //when(tableGroupRepository.save(any())).thenReturn(orderTables);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(tableGroupRequest)
        );

        assertThat(exception.getMessage()).isEqualTo("테이블이 사용중이거나 이미 그룹핑되어 있습니다.");
    }

    @DisplayName("테이블그룹 생성 예외테스트: 테이블에 그룹아이디가 있는경우")
    @Test
    void existGroupId() {
        orderTables.add(new OrderTable(new TableGroup()));
        //when(orderTableDao.findAllByIdIn(any())).thenReturn(orderTables);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.create(tableGroupRequest)
        );

        assertThat(exception.getMessage()).isEqualTo("테이블이 사용중이거나 이미 그룹핑되어 있습니다.");
    }

    @DisplayName("테이블그룹 해제 예외테스트: 테이블상태가 유효하지 않은 경우")
    @Test
    void invalidTableStatus() {
        when(tableGroupRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(tableGroup));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                any(), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        )).thenReturn(true);

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.ungroup(1L)
        );

        assertThat(exception.getMessage()).isEqualTo("주문이 완료되지 않았습니다.");

    }

}
