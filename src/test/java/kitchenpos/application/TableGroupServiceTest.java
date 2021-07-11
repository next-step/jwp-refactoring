package kitchenpos.application;

import kitchenpos.application.order.OrderTableGroupService;
import kitchenpos.domain.order.*;
import kitchenpos.ui.dto.order.OrderTableGroupRequest;
import kitchenpos.ui.dto.order.OrderTableGroupResponse;
import kitchenpos.ui.dto.order.OrderTableRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 그룹 관리")
class TableGroupServiceTest extends DataBaseCleanSupport {

    @Autowired
    private OrderTableGroupService orderTableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderTableGroupRepository orderTableGroupRepository;

    @Autowired
    EntityManager entityManager;

    @DisplayName("테이블 그룹을 추가한다.")
    @Test
    void create() {
        //given
        OrderTable orderTable1 = saveOrderTable(4, true);
        OrderTable orderTable2 = saveOrderTable(2, true);
        OrderTableGroupRequest tableGroup = OrderTableGroupRequest.of(LocalDateTime.now(), Lists.list(
                OrderTableRequest.of(orderTable1.getId(), null, orderTable1.getNumberOfGuests(), orderTable1.isEmpty()),
                OrderTableRequest.of(orderTable2.getId(), null, orderTable2.getNumberOfGuests(), orderTable2.isEmpty())));

        //when
        OrderTableGroupResponse actual = orderTableGroupService.create(tableGroup);

        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getCreatedDate()).isNotNull();
        assertThat(actual.getOrderTables().size()).isNotZero();
    }

    @DisplayName("추가할 땐 주문 테이블을 지정해야한다.")
    @Test
    void createTableGroupExceptionIfOrderTableIsNull() {
        //given
        OrderTableGroupRequest tableGroup = OrderTableGroupRequest.of(LocalDateTime.now(), Lists.list());

        //when
        assertThatThrownBy(() -> orderTableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("주문 테이블이 2개 이상이어야 한다.")
    @Test
    void createTableGroupExceptionIfOrderTableIsNotBiggerTwo() {
        //given
        OrderTable orderTable1 = saveOrderTable(4, true);
        OrderTableGroupRequest tableGroup = OrderTableGroupRequest.of(LocalDateTime.now(), Lists.list(
                OrderTableRequest.of(orderTable1.getId(), null, orderTable1.getNumberOfGuests(), orderTable1.isEmpty())));

        //when
        assertThatThrownBy(() -> orderTableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("주문 테이블이 존재해야만 한다.")
    @Test
    void createTableGroupExceptionIfOrderTableIsNotExist() {
        //given
        OrderTableGroupRequest tableGroup = OrderTableGroupRequest.of(LocalDateTime.now(), Lists.list(
                OrderTableRequest.of(0L, null, 4, true)));

        //when
        assertThatThrownBy(() -> orderTableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("테이블 그룹을 지정할 때 주문 테이블은 주문 불가능 상태여야만 한다.")
    @Test
    void createTableGroupExceptionIfOrderTableEmptyIsTrue() {
        //given
        OrderTable orderTable1 = saveOrderTable(4, true);
        OrderTable orderTable2 = saveOrderTable(2, false);
        OrderTableGroupRequest tableGroup = OrderTableGroupRequest.of(LocalDateTime.now(), Lists.list(
                OrderTableRequest.of(orderTable1.getId(), null, orderTable1.getNumberOfGuests(), orderTable1.isEmpty()),
                OrderTableRequest.of(orderTable2.getId(), null, orderTable2.getNumberOfGuests(), orderTable2.isEmpty())));

        //when
        assertThatThrownBy(() -> orderTableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("다른 주문 테이블 그룹에 속하지 않아야만 한다.")
    @Test
    void createTableGroupExceptionIfTheOtherTableGroupContains() {
        //given
        OrderTableGroupRequest tableGroup = OrderTableGroupRequest.of(LocalDateTime.now(), Lists.list(
                OrderTableRequest.of(1L, 1L, 4, true)));

        //when
        assertThatThrownBy(() -> orderTableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("특정 테이블 그룹을 삭제한다.")
    @Test
    void ungroup() {
        //given
        OrderTable orderTable1 = saveOrderTable(4, false);
        OrderTable orderTable2 = saveOrderTable(2, false);
        OrderTableGroup orderTableGroup = orderTableGroupRepository.save(OrderTableGroup.of(LocalDateTime.now(), Lists.list(orderTable1, orderTable2)));

        //when
        orderTableGroupService.ungroup(orderTableGroup.getId());

        //then
        OrderTable savedOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
        assertThat(savedOrderTable1.getTableGroup()).isNull();
        OrderTable savedOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();
        assertThat(savedOrderTable2.getTableGroup()).isNull();
    }

    @DisplayName("테이블 그룹내 속한 주문 테이블 중 조리중, 식사중 상태이면 삭제할 수 없다.")
    @Test
    void ungroupExceptionIfOrderTableStatusIsCookingOrMeal() {
        //given
        OrderTable orderTable1 = saveOrderTable(4, false);
        OrderTableGroup orderTableGroup = orderTableGroupRepository.save(OrderTableGroup.of(LocalDateTime.now(), Lists.list(orderTable1)));
        orderTable1.setTableGroup(orderTableGroup);
        Order order = Order.of(orderTable1, OrderStatus.COOKING.name(), LocalDateTime.now());
        orderRepository.save(order);
        Long orderTableGroupId = orderTableGroup.getId();

        //when
        assertThatThrownBy(() -> orderTableGroupService.ungroup(orderTableGroupId))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    private OrderTable saveOrderTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = OrderTable.of(numberOfGuests, empty);
        return orderTableRepository.save(orderTable);
    }
}
