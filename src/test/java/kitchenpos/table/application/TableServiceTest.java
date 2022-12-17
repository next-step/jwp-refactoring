package kitchenpos.table.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.*;
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
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.table.application.TableService.CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE;
import static kitchenpos.table.application.TableService.ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.OrderTable.TABLE_GROUP_NOT_NULL_EXCEPTION_MESSAGE;
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

    private MenuGroup menuGroupA;
    private OrderTable orderTableA;
    private OrderTable orderTableB;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = tableGroupRepository.save(new TableGroup(Arrays.asList(changeEmptyOrder(), changeEmptyOrder())));
        menuGroupA = menuGroupRepository.save(new MenuGroup("a"));
        orderTableA = orderTableRepository.save(new OrderTable());
        orderTableB = orderTableRepository.save(new OrderTable(tableGroup, false));
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
        OrderTable orderTable = orderTableRepository.findById(orderTableA.getId()).get();
        assertThat(orderTable.getNumberOfGuests()).isNotEqualTo(1);
        orderTable.setEmpty(false);
        orderTableRepository.save(orderTable);
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(1);
        assertThat(tableService.changeNumberOfGuests(orderTableA.getId(), changeNumberOfGuestsRequest).getNumberOfGuests()).isEqualTo(1);
    }

    @DisplayName("손님수를 변경한다. / 0명보다 작을 수 없다.")
    @Test
    void changeNumberOfGuests_fail_minimum() {

        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableA.getId(), changeNumberOfGuestsRequest))
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

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableA.getId(), new ChangeNumberOfGuestsRequest(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("공석 상태로 변경한다.")
    @Test
    void empty_success() {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(Collections.singletonList(new OrderLineItem(null, menuGroupA.getId(), 1)));
        Order order = new Order(orderTableA, orderLineItems);
        order.setOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(order);

        assertThat(tableService.changeEmpty(orderTableA.getId()).isEmpty()).isTrue();
    }

    @DisplayName("공석 상태로 변경한다. / 테이블 그룹이 있을 수 없다.")
    @Test
    void changeEmpty_fail_notTableGroup() {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(Collections.singletonList(new OrderLineItem(null, orderTableB.getId(), 1)));
        Order order = new Order(orderTableB, orderLineItems);
        order.setOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableB.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(TABLE_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("공석 상태로 변경한다. / 요리중일 경우 변경할 수 없다.")
    @Test
    void empty_fail_cooking() {
        assertThatThrownBy(() -> tableService.changeEmpty(createOrder().getOrderTable().getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE);
    }

    @DisplayName("공석 상태로 변경한다. / 식사중일 경우 변경할 수 없다.")
    @Test
    void empty_fail_meal() {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(Collections.singletonList(new OrderLineItem(null, menuGroupA.getId(), 1)));
        Order order = new Order(orderTableA, orderLineItems);
        order.setOrderStatus(OrderStatus.MEAL);
        orderRepository.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(order.getOrderTable().getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void list() {
        assertThat(tableService.list()).hasSize(4);
    }

    private OrderTable changeEmptyOrder() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(true));
        return orderTableRepository.save(orderTable1);
    }

    private Order createOrder() {
        Menu menu = menuRepository.save(new Menu(new Name("menu"), new Price(BigDecimal.ONE), menuGroupA, Collections.singletonList(new MenuProduct(null, ProductFixture.product(), 1L))));
        OrderTable orderTable = orderTableRepository.save(new OrderTable());
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.addAll(Collections.singletonList(new OrderLineItem(null, menu.getId(), 1)));
        return orderRepository.save(new Order(orderTable, orderLineItems));
    }

    private void 테이블_공석_상태_확인됨() {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableA.getId())
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.setEmpty(true);
        orderTableRepository.save(savedOrderTable);

        assertThat(savedOrderTable.isEmpty()).isTrue();
    }
}
