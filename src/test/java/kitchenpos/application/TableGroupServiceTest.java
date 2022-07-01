package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable 주문_테이블1;
    private OrderTable 주문_테이블2;

    private TableGroup 단체;

    @BeforeEach
    void before() {
        주문_테이블1 = orderTableRepository.save(new OrderTable(2, true));
        주문_테이블2 = orderTableRepository.save(new OrderTable(3, true));
    }

    @Test
    @DisplayName("주문 테이블들이 시스템에 등록 되어 있지 않으면 테이블 그룹은 지정 할 수 없다.")
    void createFailWithOrderTableNotExistTest() {

        //when & then
        assertThatThrownBy(
                () -> tableGroupService.create(
                        TableGroupRequest.from(Arrays.asList(10L, 12L)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 지정 할 수 있다.")
    void createTest() {
        //when
        TableGroupResponse response = tableGroupService.create(
                TableGroupRequest.from(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId())));
        //then
        assertThat(response.getId()).isNotNull();
    }


    @Test
    @DisplayName("주문 상태가 조리중(COOKING), 식사중(MEAL)인 경우에는 해제 할 수 없다.")
    void ungroupFailWithStatusTest(@Autowired MenuRepository menuRepository,
                                   @Autowired MenuGroupRepository menuGroupRepository) {
        //given : 주문 생성
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("중식"));
        Menu menu = menuRepository.save(new Menu("볶음밥", BigDecimal.valueOf(1000L), menuGroup.getId()));

        Order order = new Order(주문_테이블1.getId(), new OrderLineItem(menu.getId(), 1));
        orderRepository.save(order);
        TableGroupResponse response = tableGroupService.create(
                TableGroupRequest.from(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId())));

        //when & then
        assertThatThrownBy(
                () -> tableGroupService.ungroup(response.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("지정된 테이블 그룹을 해제 할 수 있다.")
    @Transactional(readOnly = true)
    void ungroupTest() {
        //given
        TableGroupResponse response = tableGroupService.create(
                TableGroupRequest.from(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId())));

        //when
        tableGroupService.ungroup(response.getId());

        //then
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(response.getId());
        long count = orderTables.stream().filter(orderTable -> !orderTable.isInTableGroup()).count();

        assertThat(count).isZero();
    }
}
