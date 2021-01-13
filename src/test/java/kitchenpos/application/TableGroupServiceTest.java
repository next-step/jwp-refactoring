package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.domain.tablegroup.exceptions.InvalidTableGroupTryException;
import kitchenpos.domain.tablegroup.exceptions.InvalidTableUngroupTryException;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderStatusChangeRequest;
import kitchenpos.ui.dto.ordertable.OrderTableRequest;
import kitchenpos.ui.dto.ordertable.OrderTableResponse;
import kitchenpos.ui.dto.tablegroup.OrderTableInTableGroupRequest;
import kitchenpos.ui.dto.tablegroup.TableGroupRequest;
import kitchenpos.ui.dto.tablegroup.TableGroupResponse;
import kitchenpos.utils.FixtureUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TableGroupServiceTest extends FixtureUtils {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    private OrderTableRequest emptyOrderTableRequest1;
    private OrderTableRequest emptyOrderTableRequest2;
    private OrderTableRequest fullOrderTableRequest1;
    private OrderTableRequest fullOrderTableRequest2;

    @BeforeEach
    void setup() {
        emptyOrderTableRequest1 = new OrderTableRequest(0, true);
        emptyOrderTableRequest2 = new OrderTableRequest(0, true);
        fullOrderTableRequest1 = new OrderTableRequest(500, false);
        fullOrderTableRequest2 = new OrderTableRequest(500, false);
    }

    @DisplayName("2개 미만의 주문테이블로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithEmptyTable() {
        // given
        OrderTableResponse orderTableResponse = orderTableService.create(emptyOrderTableRequest1);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                Collections.singletonList(new OrderTableInTableGroupRequest(orderTableResponse.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.group(tableGroupRequest))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("2개 미만의 주문 테이블로 단체 지정할 수 없다.");
    }

    @DisplayName("존재하지 않는 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithNotExistTableGroups() {
        // given
        OrderTableInTableGroupRequest notExist1 = new OrderTableInTableGroupRequest(10000L);
        OrderTableInTableGroupRequest notExist2 = new OrderTableInTableGroupRequest(10001L);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(notExist1, notExist2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.group(tableGroupRequest))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("존재하지 않는 주문 테이블을 단체 지정할 수 없습니다.");
    }

    @DisplayName("비어있지 않은 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithFullOrderTables() {
        // given
        OrderTableResponse orderTable1Response = orderTableService.create(fullOrderTableRequest1);
        assertThat(orderTable1Response.isEmpty()).isFalse();
        OrderTable orderTable1 = orderTableService.findOrderTable(orderTable1Response.getId());

        OrderTableResponse orderTable2Response = orderTableService.create(fullOrderTableRequest2);
        assertThat(orderTable2Response.isEmpty()).isFalse();
        OrderTable orderTable2 = orderTableService.findOrderTable(orderTable2Response.getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableInTableGroupRequest(orderTable1.getId()),
                new OrderTableInTableGroupRequest(orderTable2.getId())
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupService.group(tableGroupRequest))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("빈 주문 테이블들로만 단체 지정할 수 있습니다.");
    }

    @DisplayName("주문 테이블들을 단체 지정할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        OrderTableResponse orderTable1Response = orderTableService.create(emptyOrderTableRequest1);
        assertThat(orderTable1Response.isEmpty()).isTrue();
        OrderTable orderTable1 = orderTableService.findOrderTable(orderTable1Response.getId());

        OrderTableResponse orderTable2Response = orderTableService.create(emptyOrderTableRequest2);
        assertThat(orderTable2Response.isEmpty()).isTrue();
        OrderTable orderTable2 = orderTableService.findOrderTable(orderTable2Response.getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableInTableGroupRequest(orderTable1.getId()),
                new OrderTableInTableGroupRequest(orderTable2.getId())
        ));

        // when
        TableGroupResponse tableGroupResponse = tableGroupService.group(tableGroupRequest);

        // then
        assertThat(tableGroupResponse.getCreatedDate()).isNotNull();
        tableGroupResponse.getOrderTables().forEach(it -> assertThat(it.getId()).isNotNull());
    }

    @DisplayName("이미 단체 지정된 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithAlreadyGrouped() {
        // given
        OrderTableResponse orderTable1Response = orderTableService.create(emptyOrderTableRequest1);
        assertThat(orderTable1Response.isEmpty()).isTrue();
        OrderTable orderTable1 = orderTableService.findOrderTable(orderTable1Response.getId());

        OrderTableResponse orderTable2Response = orderTableService.create(emptyOrderTableRequest2);
        assertThat(orderTable2Response.isEmpty()).isTrue();
        OrderTable orderTable2 = orderTableService.findOrderTable(orderTable2Response.getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableInTableGroupRequest(orderTable1.getId()),
                new OrderTableInTableGroupRequest(orderTable2.getId())
        ));

        tableGroupService.group(tableGroupRequest);

        // when, then
        assertThatThrownBy(() -> tableGroupService.group(tableGroupRequest))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("이미 단체 지정된 주문 테이블을 단체 지정할 수 없습니다.");
    }

    @DisplayName("주문 상태가 조리나 식사인 단체 지정을 해제할 수 없다.")
    @Test
    void unGroupFailWithInvalidOrderStatus() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("놀라운 메뉴 그룹"));
        Product product = productRepository.save(new Product("놀라운 상품", BigDecimal.ONE));
        Menu menu = menuRepository.save(Menu.of("놀라운 메뉴", BigDecimal.ZERO, menuGroup.getId(),
                Collections.singletonList(MenuProduct.of(product.getId(), 1L))));
        Long menuId = menu.getId();

        OrderTableResponse orderTable1Response = orderTableService.create(emptyOrderTableRequest1);
        OrderTable orderTable1 = orderTableService.findOrderTable(orderTable1Response.getId());

        OrderTableResponse orderTable2Response = orderTableService.create(emptyOrderTableRequest2);
        OrderTable orderTable2 = orderTableService.findOrderTable(orderTable2Response.getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableInTableGroupRequest(orderTable1.getId()),
                new OrderTableInTableGroupRequest(orderTable2.getId())
        ));

        TableGroupResponse tableGroupResponse = tableGroupService.group(tableGroupRequest);

        OrderRequest orderRequest1 = new OrderRequest(
                orderTable1.getId(), Collections.singletonList(new OrderLineItemRequest(menuId, 1L)));
        orderService.create(orderRequest1);
        OrderRequest orderRequest2 = new OrderRequest(
                orderTable2.getId(), Collections.singletonList(new OrderLineItemRequest(menuId, 1L)));
        orderService.create(orderRequest2);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
                .isInstanceOf(InvalidTableUngroupTryException.class)
                .hasMessage("조리 중이거나 식사 중인 단체 지정을 해제할 수 없습니다.");
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void unGroupTest() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("놀라운 메뉴 그룹"));
        Product product = productRepository.save(new Product("놀라운 상품", BigDecimal.ONE));
        Menu menu = menuRepository.save(Menu.of("놀라운 메뉴", BigDecimal.ZERO, menuGroup.getId(),
                Collections.singletonList(MenuProduct.of(product.getId(), 1L))));
        Long menuId = menu.getId();

        OrderTableResponse orderTable1Response = orderTableService.create(emptyOrderTableRequest1);
        OrderTable orderTable1 = orderTableService.findOrderTable(orderTable1Response.getId());

        OrderTableResponse orderTable2Response = orderTableService.create(emptyOrderTableRequest2);
        OrderTable orderTable2 = orderTableService.findOrderTable(orderTable2Response.getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableInTableGroupRequest(orderTable1.getId()),
                new OrderTableInTableGroupRequest(orderTable2.getId())
        ));

        TableGroupResponse tableGroupResponse = tableGroupService.group(tableGroupRequest);
        assertThat(tableGroupRepository.existsById(tableGroupResponse.getId())).isTrue();

        OrderRequest orderRequest1 = new OrderRequest(
                orderTable1.getId(), Collections.singletonList(new OrderLineItemRequest(menuId, 1L)));
        OrderResponse orderResponse1 = orderService.create(orderRequest1);
        OrderRequest orderRequest2 = new OrderRequest(
                orderTable2.getId(), Collections.singletonList(new OrderLineItemRequest(menuId, 1L)));
        OrderResponse orderResponse2 = orderService.create(orderRequest2);

        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(orderResponse1.getId(), orderStatusChangeRequest);
        orderService.changeOrderStatus(orderResponse2.getId(), orderStatusChangeRequest);

        // when
        tableGroupService.ungroup(tableGroupResponse.getId());

        // then
        assertThat(tableGroupRepository.existsById(tableGroupResponse.getId())).isFalse();
    }
}
