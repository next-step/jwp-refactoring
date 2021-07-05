package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    TableGroupDao tableGroupDao;


    @DisplayName("테이블 그룹을 만들어보자")
    @Test
    public void createTableGroup() throws Exception {
        //given
        OrderTable savedOrderTable1 = createOrderTable(4, true);
        OrderTable savedOrderTable2 = createOrderTable(4, true);

        OrderTableRequest orderTableRequest1 = new OrderTableRequest(savedOrderTable1.getId(), savedOrderTable1.getTableGroupId(), savedOrderTable1.getNumberOfGuests(), savedOrderTable1.isEmpty());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(savedOrderTable2.getId(), savedOrderTable2.getTableGroupId(), savedOrderTable2.getNumberOfGuests(), savedOrderTable2.isEmpty());
        TableGroupRequest tableGroup = new TableGroupRequest();
        tableGroup.setOrderTables(Arrays.asList(orderTableRequest1, orderTableRequest2));

        //when
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroup);

        //then
        assertNotNull(savedTableGroup.getId());
        assertThat(savedTableGroup.getOrderTables()).hasSize(2);
    }

    @DisplayName("주문테이블이 비어있지 않을 경우 테이블 그룹을 만들수없다.")
    @Test
    public void failCreateTableGroupNotEmptyTable() throws Exception {
        //given
        OrderTable savedOrderTable1 = createOrderTable(4, true);
        OrderTable savedOrderTable2 = createOrderTable(4, false);


        OrderTableRequest orderTableRequest1 = new OrderTableRequest(savedOrderTable1.getId(), savedOrderTable1.getTableGroupId(), savedOrderTable1.getNumberOfGuests(), savedOrderTable1.isEmpty());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(savedOrderTable2.getId(), savedOrderTable2.getTableGroupId(), savedOrderTable2.getNumberOfGuests(), savedOrderTable2.isEmpty());
        TableGroupRequest tableGroup = new TableGroupRequest();
        tableGroup.setOrderTables(Arrays.asList(orderTableRequest1, orderTableRequest2));

        //then
        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블이 테이블 그룹에 속해있을 경우 테이블 그룹을 만들수없다.")
    @Test
    public void failCreateTableGroupNotNullTableGroupId() throws Exception {
        //given
        int countOfPeople = 4;
        OrderTable savedOrderTable1 = createOrderTable(countOfPeople, true);
        OrderTable savedOrderTable2 = createOrderTable(countOfPeople, true);

        TableGroup anotherTableGroup = new TableGroup();
        anotherTableGroup.setCreatedDate(LocalDateTime.now());

        TableGroup savedAnotherTableGroup = tableGroupDao.save(anotherTableGroup);
        savedOrderTable2.setTableGroupId(savedAnotherTableGroup.getId());

        orderTableDao.save(savedOrderTable2);


        OrderTableRequest orderTableRequest1 = new OrderTableRequest(savedOrderTable1.getId(), savedOrderTable1.getTableGroupId(), savedOrderTable1.getNumberOfGuests(), savedOrderTable1.isEmpty());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(savedOrderTable2.getId(), savedOrderTable2.getTableGroupId(), savedOrderTable2.getNumberOfGuests(), savedOrderTable2.isEmpty());
        TableGroupRequest tableGroup = new TableGroupRequest();
        tableGroup.setOrderTables(Arrays.asList(orderTableRequest1, orderTableRequest2));

        //then
        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createOrderTable(int countOfPeople, boolean emptyFlag) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(countOfPeople);
        orderTable.setEmpty(emptyFlag);

        return orderTableDao.save(orderTable);
    }

    @DisplayName("테이블 그룹을 해지하자")
    @Test
    public void ungroup() throws Exception {
        //given
        // 테이블 추가

        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        OrderTable savedOrderTable1 = createOrderTable(4, savedTableGroup.getId());
        OrderTable savedOrderTable2 = createOrderTable(4, savedTableGroup.getId());
        //when
        tableGroupService.ungroup(savedTableGroup.getId());

        //then
        OrderTable orderTable1 = orderTableDao.findById(savedOrderTable1.getId()).get();
        assertNull(orderTable1.getTableGroupId());
        OrderTable orderTable2 = orderTableDao.findById(savedOrderTable2.getId()).get();
        assertNull(orderTable2.getTableGroupId());
    }

    @DisplayName("조리중과 식사중인 상태의 주문이 있을경우 테이블 그룹을 해지할수 없다.")
    @Test
    public void failUnGroupBecauseStatus() throws Exception {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        OrderTable savedOrderTable1 = createOrderTable(4, savedTableGroup.getId());
        OrderTable savedOrderTable2 = createOrderTable(4, savedTableGroup.getId());

        // 주문 상태 추가
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(savedOrderTable1.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        //then
        assertThatThrownBy(
                () -> tableGroupService.ungroup(savedTableGroup.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createOrderTable(int countOfPeople, Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(countOfPeople);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(id);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        return savedOrderTable;
    }
}
