package kitchenpos.tablegroup.application;

import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
class TableGroupServiceTest {

    private List<OrderTableRequest> orderTableRequests;
    private OrderTable 테이블_1번;
    private OrderTable 테이블_2번;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @MockBean
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        테이블_1번 = 테이블을_생성한다(0, true);
        테이블_2번 = 테이블을_생성한다(0, true);
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(테이블_1번);
        orderTables.add(테이블_2번);
        orderTableRequests = orderTables.stream()
                .map(OrderTableRequest::of)
                .collect(Collectors.toList());
    }

    @DisplayName("테이블 그룹을 생성한다 : 각 테이블들의 테이블 그룹 id를 등록하고 상태를 비어있지않음으로 변경한다")
    @Test
    void create() {
        TableGroup tableGroup = 테이블_그룹을_생성한다(new TableGroupRequest(orderTableRequests));

        List<OrderTable> orderTables = tableGroup.getOrderTables();

        orderTables.forEach(group -> {
            assertThat(group.getTableGroupId()).isEqualTo(tableGroup.getId());
            assertThat(group.isEmpty()).isEqualTo(false);
        });
    }

    @DisplayName("테이블 그룹을 생성한다 : 테이블 사이즈가 2미만이면 익셉션 발생")
    @Test
    void createException() {
        assertThatThrownBy(() -> 테이블_그룹을_생성한다(new TableGroupRequest()))
                .isInstanceOf(OrderTableException.class);
    }


    @DisplayName("각 주문 테이블의 테이블 그룹 id를 지운다")
    @Test
    void ungroup() {
        TableGroup tableGroup = 테이블_그룹을_생성한다(new TableGroupRequest(orderTableRequests));
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        System.out.println(">>>>>>>>>" + tableGroup);

        테이블_그룹을_비운다(tableGroup.getId());

        orderTableIds.forEach(id -> {
            assertThat(orderService.findOrderTableById(id).getTableGroupId()).isNull();
        });
    }

    @DisplayName("테이블 그룹을 제거한다 : OrderStatus가 COOKING, MEAL이면 익셉션 발생")
    @Test
    void ungroupException() {
        TableGroup tableGroup = 테이블_그룹을_생성한다(new TableGroupRequest(orderTableRequests));

        when(orderRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(OrderTableException.class);
    }

    private OrderTable 테이블을_생성한다(int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTableRequest(numberOfGuest, empty));
    }

    private void 테이블_그룹을_비운다(Long id) {
        tableGroupService.ungroup(id);
    }

    private TableGroup 테이블_그룹을_생성한다(TableGroupRequest tableGroupRequest) {
        return tableGroupService.create(tableGroupRequest);
    }

}