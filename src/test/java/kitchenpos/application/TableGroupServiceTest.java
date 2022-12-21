package kitchenpos.application;

import static kitchenpos.application.OrderBuilder.anOrder;
import static kitchenpos.application.OrderTableBuilder.emptyOrderTableWithGuestNo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 단체지정시_주문테이블목록이_비어있는경우_예외발생() {
        final TableGroup tableGroup = tableGroup(Collections.emptyList());
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체지정시_주문테이블수가_2개보다작은경우_예외발생() {
        final TableGroup tableGroup = tableGroup(Collections.singletonList(persist(emptyOrderTableWithGuestNo(2))));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체지정시_주문테이블수가_저장된주문테이블수와일치하지않는경우_예외발생() {
        persist(emptyOrderTableWithGuestNo(2));

        final TableGroup tableGroup = tableGroup(Arrays.asList(
            emptyOrderTableWithGuestNo(2),
            emptyOrderTableWithGuestNo(4)
        ));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체지정시_주문테이블목록중_단체지정된주문테이블이포함되지않은경우_생성된테이블그룹반환() {
        final TableGroup tableGroup = tableGroup(Arrays.asList(
            persist(emptyOrderTableWithGuestNo(2)),
            persist(emptyOrderTableWithGuestNo(4))
        ));
        assertThat(tableGroupService.create(tableGroup).getOrderTables()).hasSize(2);
    }

    @Test
    void 단체지정시_주문테이블목록중_이미단체지정된주문테이블이포함된경우_예외발생() {
        // 단체지정
        final TableGroup tableGroup = tableGroupService.create(tableGroup(Arrays.asList(
            persist(emptyOrderTableWithGuestNo(2)),
            persist(emptyOrderTableWithGuestNo(4))
        )));
        // 다시 단체지정
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    void 단체지정해제시_주문테이블목록중_완료되지않은주문의테이블이포함되어있는경우_예외발생() {
        // given
        final OrderTable savedOrderTableWithCookingStatusOrder = persist(emptyOrderTableWithGuestNo(2));
        persist(anOrder()
            .withStatus(OrderStatus.COOKING)
            .withOrderTable(savedOrderTableWithCookingStatusOrder)
            .build());
        // when
        final TableGroup tableGroup = tableGroupService.create(tableGroup(Arrays.asList(
            savedOrderTableWithCookingStatusOrder,
            persist(emptyOrderTableWithGuestNo(4))
        )));
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단치지정해제시_모든주문테이블이_완료된주문의테이블일경우_해제성공() {
        // given
        final OrderTable savedOrderTable1 = persist(emptyOrderTableWithGuestNo(2));
        final OrderTable savedOrderTable2 = persist(emptyOrderTableWithGuestNo(4));
        persist(anOrder()
            .withStatus(OrderStatus.COMPLETION)
            .withOrderTable(savedOrderTable1)
            .build());
        persist(anOrder()
            .withStatus(OrderStatus.COMPLETION)
            .withOrderTable(savedOrderTable2)
            .build());
        // when
        final TableGroup tableGroup = tableGroupService.create(tableGroup(Arrays.asList(
            savedOrderTable1,
            savedOrderTable2
        )));
        // then
        tableGroupService.ungroup(tableGroup.getId());

        final Optional<OrderTable> foundOrderTable1 = find(savedOrderTable1);
        final Optional<OrderTable> foundOrderTable2= find(savedOrderTable2);

        assertAll(
            () -> assertThat(foundOrderTable1.isPresent()).isTrue(),
            () -> assertThat(foundOrderTable1.get().getTableGroupId()).isNull(),
            () -> assertThat(foundOrderTable2.isPresent()).isTrue(),
            () -> assertThat(foundOrderTable2.get().getTableGroupId()).isNull()
        );
    }

    private Optional<OrderTable> find(OrderTable orderTable) {
        return orderTableDao.findById(orderTable.getId());
    }

    private Order persist(Order order) {
        return orderDao.save(order);
    }

    private OrderTable persist(OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    private TableGroup tableGroup(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }
}
