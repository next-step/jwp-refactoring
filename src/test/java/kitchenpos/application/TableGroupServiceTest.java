package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.TableGroup;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            orderTableService.findById(1L),
            orderTableService.findById(2L)
        );
        TableGroup tableGroup = new TableGroup(orderTables);

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(2);
    }

    @DisplayName("테이블 그룹 등록 예외 - 테이블 목록이 2개 이상이어야 한다.")
    @Test
    void create_exception1() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            orderTableService.findById(1L)
        );
        TableGroup tableGroup = new TableGroup(orderTables);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableGroupService.create(tableGroup))
            .withMessage("테이블 목록이 2개 이상이어야 한다.");
    }

    @DisplayName("테이블 그룹 등록 예외 - 테이블 목록이 등록되어 있어야 한다.")
    @Test
    void create_exception2() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            orderTableService.findById(1L),
            orderTableService.findById(2L),
            new OrderTable()
        );
        TableGroup tableGroup = new TableGroup(orderTables);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableGroupService.create(tableGroup))
            .withMessage("테이블 목록이 등록되어 있어야 한다.");
    }

    @DisplayName("테이블 그룹 등록 예외 - 테이블 목록이 이미 다른 테이블 그룹에 속해있지 않아야 한다.")
    @Test
    void create_exception3() {
        // given
        OrderTable orderTable = orderTableService.findById(1L);
        orderTable.setTableGroupId(1L);
        List<OrderTable> orderTables = Arrays.asList(
            orderTable,
            orderTableService.findById(2L)
        );
        TableGroup tableGroup = new TableGroup(orderTables);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableGroupService.create(tableGroup))
            .withMessage("테이블 목록이 이미 다른 테이블 그룹에 속해있지 않아야 한다.");
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroup tableGroup = tableGroupDao.findById(2L).get();
        Long orderTableId = orderTableService.findAllByTableGroupId(tableGroup.getId()).get(0).getId();
        Order order = new Order(orderTableId);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(order);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        OrderTable updatedOrderTable = orderTableService.findById(orderTableId);
        assertThat(updatedOrderTable.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 그룹 해제 예외 - `조리중`과 `식사중`에는 변경할 수 없다.")
    @Test
    void ungroup_exception1() {
        // given
        TableGroup tableGroup = tableGroupDao.findById(2L).get();
        Long orderTableId = orderTableService.findAllByTableGroupId(tableGroup.getId()).get(0).getId();
        Order order = new Order(orderTableId);
        order.setOrderStatus(OrderStatus.MEAL.name());
        orderDao.save(order);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .withMessage("`조리중`과 `식사중`에는 변경할 수 없다.");
    }
}
