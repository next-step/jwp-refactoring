package kitchenpos.table.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.ProductFixture;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.table.application.TableService.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("테이블 서비스")
class TableServiceTest extends ServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    private Long orderTableId;

    @BeforeEach
    void setUp() {
        orderTableId = orderTableRepository.save(new OrderTable()).getId();
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        OrderTable orderTable = tableService.create(new OrderTable());
        assertAll(
                () -> assertThat(orderTable.getTableGroup()).isNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isZero(),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("손님수를 변경한다.")
    @Test
    void changeNumberOfGuests_success() {
        OrderTable orderTable = orderTableRepository.findById(orderTableId).get();
        assertThat(orderTable.getNumberOfGuests()).isNotEqualTo(1);
        orderTable.setEmpty(false);
        orderTableRepository.save(orderTable);
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(1);
        assertThat(tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestsRequest).getNumberOfGuests()).isEqualTo(1);
    }

    @DisplayName("손님수를 변경한다. / 0명보다 작을 수 없다.")
    @Test
    void changeNumberOfGuests_fail_minimum() {

        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE);
    }

    @DisplayName("손님수를 변경한다. / 주문테이블이 없을 수 없다.")
    @Test
    void changeNumberOfGuests_fail_notExistOrderTable() {
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님수를 변경한다. / 테이블이 공석 상태면 손님수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuest_fail_notEmptyTable() {

        테이블_공석_상태_확인됨();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, new ChangeNumberOfGuestsRequest(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공석 상태로 변경한다.")
    @Test
    void empty_success() {

        OrderTable orderTable = createOrderTable();
        orderTable.setTableGroup(null);
        orderTableRepository.save(orderTable);

        Order order = createOrder();
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        orderRepository.save(order);

        assertThat(tableService.changeEmpty(orderTable.getId()).isEmpty()).isTrue();
    }

    @DisplayName("공석 상태로 변경한다. / 테이블 그룹이 있을 수 없다.")
    @Test
    void changeEmpty_fail_notTableGroup() {
        assertThatThrownBy(() -> tableService.changeEmpty(createOrderTable().getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(TABLE_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("공석 상태로 변경한다. / 요리중일 경우 변경할 수 없다.")
    @Test
    void empty_fail_cooking() {
        assertThatThrownBy(() -> tableService.changeEmpty(createOrder().getOrderTableId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE);
    }

    @DisplayName("공석 상태로 변경한다. / 식사중일 경우 변경할 수 없다.")
    @Test
    void empty_fail_meal() {

        Order order = createOrder();
        order.setOrderStatus(OrderStatus.MEAL.name());
        orderRepository.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(order.getOrderTableId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void list() {
        assertThat(tableService.list()).hasSize(1);
    }

    private OrderTable createOrderTable() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable());
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable());

        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable1, orderTable2));

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables));

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.setTableGroup(tableGroup);

        return orderTableRepository.save(savedOrderTable);
    }

    private Order createOrder() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("a"));
        Menu menu = menuRepository.save(new Menu(new Name("menu"), new Price(BigDecimal.ONE), menuGroup.getId(), Arrays.asList(new MenuProduct(null, ProductFixture.product(), 1L))));
        OrderTable orderTable = orderTableRepository.save(new OrderTable());
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(null, menu.getId(), 1));
        return orderRepository.save(new Order(orderTable.getId(), orderLineItems));
    }

    private void 테이블_공석_상태_확인됨() {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.setEmpty(true);
        orderTableRepository.save(savedOrderTable);

        assertThat(savedOrderTable.isEmpty()).isTrue();
    }
}
