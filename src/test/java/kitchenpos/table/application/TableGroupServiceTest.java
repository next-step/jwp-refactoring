package kitchenpos.table.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.table.dto.TableGroupCreateRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Test
    @DisplayName("단체를 지정한다.")
    void create() {
        // given
        List<Long> orderTableIds = new ArrayList<>(Arrays.asList(1L, 2L));
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(OrderTable.of(4, true));
        orderTables.add(OrderTable.of(3, true));

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        List<OrderTable> savedOrderTables = new ArrayList<>();
        savedOrderTables.add(OrderTable.of(4, true));
        savedOrderTables.add(OrderTable.of(3, true));
        TableGroup tableGroup = TableGroup.of(savedOrderTables);

        given(tableGroupDao.save(any())).willReturn(tableGroup);

        // when
        TableGroupResponse result = tableGroupService.create(tableGroupCreateRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getCreatedDate()).isNotNull();
    }

    @Test
    @DisplayName("단체 지정할 주문 테이블들이 존재하는 주문 테이블이 아니면 단체 지정을 할 수 없다.")
    void create_not_exist_order_tables() {
        // given
        List<Long> orderTableIds = new ArrayList<>(Arrays.asList(1L, 2L));
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(OrderTable.of(4, true));

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문 테이블이 포함되어 있습니다.");
    }

    @Test
    @DisplayName("주문 테이블이 없으면 단체 지정을 할 수 없다.")
    void create_order_table_is_empty() {
        // given
        List<Long> orderTableIds = new ArrayList<>();
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

        List<OrderTable> orderTables = new ArrayList<>();

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단체 지정할 주문 테이블은 2개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("주문 테이블이 1개이면 단체 지정을 할 수 없다.")
    void create_order_table_only_one() {
        // given
        List<Long> orderTableIds = new ArrayList<>(Arrays.asList(1L));
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(OrderTable.of(4, true));

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단체 지정할 주문 테이블은 2개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("주문 테이블들이 빈 테이블이 아니면 단체 지정을 할 수 없다.")
    void create_not_empty() {
        // given
        List<Long> orderTableIds = new ArrayList<>(Arrays.asList(1L, 2L));
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(OrderTable.of(4, true));
        orderTables.add(OrderTable.of(3, false));

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 빈 테이블이 아니거나 이미 단체 지정이 되어있습니다.");
    }

    @Test
    @DisplayName("주문 테이블들이 이미 단체로 지정되어 있으면 단체 지정을 할 수 없다.")
    void create_already_table_group() {
        // given
        List<Long> orderTableIds = new ArrayList<>(Arrays.asList(1L, 2L));
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(OrderTable.of(4, true));
        orderTables.add(OrderTable.of(3, true));

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        TableGroup.of(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 빈 테이블이 아니거나 이미 단체 지정이 되어있습니다.");
    }

    @Test
    @DisplayName("단체 지정을 해제한다.")
    void ungroup() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(4, true);
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);
        TableGroup tableGroup = TableGroup.of(orderTables);

        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(Product.of("후라이드", new BigDecimal(16_000)), 1));
        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), MenuGroup.of("두마리메뉴"), menuProducts);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));
        Order.of(orderLineItems, orderTable1, OrderStatus.COMPLETION);
        Order.of(orderLineItems, orderTable2, OrderStatus.COMPLETION);

        given(tableGroupDao.findById(any())).willReturn(Optional.ofNullable(tableGroup));

        // when
        tableGroupService.ungroup(1L);

        // then
        assertThatNoException();
    }

    @Test
    @DisplayName("해제할 주문 테이블들 중에 조리 또는 식사 중인 주문 테이블이 존재하면 단체 지정 해제를 할 수 없다.")
    void ungroup_order_status_cooking_or_meal() {
        // given
        List<OrderTable> orderTables = new ArrayList<>();
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(4, true);
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);
        TableGroup tableGroup = TableGroup.of(orderTables);

        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(Product.of("후라이드", new BigDecimal(16_000)), 1));
        Menu menu = Menu.of("후라이드치킨", new BigDecimal(16_000), MenuGroup.of("두마리메뉴"), menuProducts);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(OrderLineItem.of(menu, 1));
        Order.of(orderLineItems, orderTable1);
        Order.of(orderLineItems, orderTable2);

        given(tableGroupDao.findById(any())).willReturn(Optional.ofNullable(tableGroup));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("조리 또는 식사 중인 주문 테이블이 존재합니다.");
    }
}
