package kitchenpos.table.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.CreateTableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import static java.util.Collections.singletonList;
import static kitchenpos.common.fixture.NameFixture.*;
import static kitchenpos.common.fixture.PriceFixture.priceMenuA;
import static kitchenpos.common.fixture.PriceFixture.priceProductA;
import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;
import static kitchenpos.table.application.TableGroupService.ORDER_STATUS_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.OrderTables.ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE;
import static kitchenpos.table.domain.fixture.NumberOfGuestsFixture.initNumberOfGuests;
import static kitchenpos.table.domain.fixture.OrderTableFixture.emptyOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TableGroupService")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private TableGroup tableGroupA;
    private TableGroup tableGroupB;
    private Orders order;
    private Menu menu;
    private OrderLineItems orderLineItemsA;

    @BeforeEach
    public void setUp() {
        super.setUp();
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(nameMenuGroupA()));
        Product product = productRepository.save(new Product(1L, nameProductA(), priceProductA()));
        menu = menuRepository.save(new Menu(nameMenuA(), priceMenuA(), menuGroup, new MenuProducts(singletonList(new MenuProduct(product.getId(), new Quantity(1))))));
        orderLineItemsA = orderLineItemsA(OrderMenu.of(menu.getId(), new Name("a"), new Price(BigDecimal.ONE)));
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        OrderTable orderTableA = emptyTableEmptyNotEmptyTableGroup(tableGroupB);
        OrderTable orderTableB = emptyTableEmptyNotEmptyTableGroup(tableGroupB);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(Arrays.asList(orderTableA, orderTableB))));
        TableGroupResponse saveTableGroup = tableGroupService.create(new CreateTableGroupRequest(tableGroup.getOrderTableIds()));
        assertThat(saveTableGroup.getCreatedDate()).isNotNull();
    }

    @DisplayName("테이블 그룹을 생성한다. / 주문 테이블의 갯수가 2보다 작을 수 없다.")
    @Test
    void create_fail_minimumSize() {

        assertThatThrownBy(() -> {
            TableGroup tableGroupC = tableGroupRepository.save(new TableGroup(new OrderTables(Collections.singletonList(emptyTableEmptyTableGroup()))));
            tableGroupService.create(new CreateTableGroupRequest(tableGroupC.getOrderTableIds()));
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void unGroup_success() {

        OrderTable orderTableA = emptyTableEmptyTableGroup();

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(Arrays.asList(orderTableA, emptyTableEmptyTableGroup()))));

        테이블_그룹_존재_검증(tableGroup);
        orderTableA.setEmpty(false);

        Orders order = new Orders(orderTableA.getId(), new OrderLineItems(Collections.singletonList(new OrderLineItem(null, OrderMenu.of(menu.getId(), menu.getName(), menu.getPrice()), new Quantity(1)))));
        order.setOrderStatus(OrderStatus.COMPLETION);
        orderRepository.save(order);

        tableGroupService.ungroup(tableGroup.getId());

        테이블_그룹_해제_검증됨(tableGroup);
    }

    @DisplayName("테이블 그룹을 해제한다. / 요리중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_cooking() {
        OrderTable orderTableA = emptyTableEmptyTableGroup();

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(Arrays.asList(orderTableA, emptyTableEmptyTableGroup()))));

        테이블_그룹_존재_검증(tableGroup);

        orderTableA.setEmpty(false);

        Orders order = new Orders(orderTableA.getId(), orderLineItemsA);
        order.setOrderStatus(OrderStatus.COOKING);
        orderRepository.save(order);

        주문_요리중_상태_검증(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    @DisplayName("테이블 그룹을 해제한다. / 식사중일 경우 해제할 수 없다.")
    @Test
    void unGroup_fail_meal() {

        OrderTable orderTableA = emptyTableEmptyTableGroup();

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(new OrderTables(Arrays.asList(orderTableA, emptyTableEmptyTableGroup()))));

        테이블_그룹_존재_검증(tableGroup);

        orderTableA.setEmpty(false);

        Orders order = new Orders(orderTableA.getId(), orderLineItemsA);
        order.setOrderStatus(OrderStatus.MEAL);
        orderRepository.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_STATUS_EXCEPTION_MESSAGE);
    }

    private OrderTable emptyTableEmptyTableGroup() {
        return orderTableRepository.save(emptyOrderTable());
    }

    private OrderTable emptyTableEmptyNotEmptyTableGroup(TableGroup tableGroup) {
        return orderTableRepository.save(new OrderTable(tableGroup, initNumberOfGuests(), true));
    }

    private void 주문_요리중_상태_검증(Orders order) {
        Orders order1 = orderRepository.findById(order.getId()).get();
        assertThat(order1.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    private void 테이블_그룹_존재_검증(TableGroup tableGroup) {
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableRepository.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroup()).isNotNull();
        }
    }

    private void 테이블_그룹_해제_검증됨(TableGroup tableGroup) {
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable find = orderTableRepository.findById(orderTable.getId()).orElseThrow(NoSuchElementException::new);
            assertThat(find.getTableGroup()).isNull();
        }
    }
}
