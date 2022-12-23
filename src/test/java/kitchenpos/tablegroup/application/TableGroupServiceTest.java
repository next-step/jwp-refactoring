package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupTest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    TableGroup tableGroup;
    private OrderTable firstTable;
    private OrderTable secondTable;
    private OrderTables orderTables;
    private Order order;
    private OrderLineItemRequest 후라이드치킨주문요청;
    private MenuGroup 치킨단품;
    private MenuProduct 치킨상품;
    private Menu 후라이드치킨;
    private Product 치킨;

    @BeforeEach
    void setUp() {
        tableGroup = TableGroup.of(1L);
        firstTable = new OrderTable(1L, new NumberOfGuests(0), true);
        secondTable = new OrderTable(2L, new NumberOfGuests(0), true);
        orderTables = OrderTables.of(Arrays.asList(firstTable, secondTable));
        orderTables.group(tableGroup.getId());
        치킨 = Product.of(1L, "치킨", 15_000L);
        치킨상품 = MenuProduct.of(치킨, 1L);
        치킨단품 = MenuGroup.of(1L, "치킨단품");
        후라이드치킨 = Menu.of(1L, "후라이드치킨", BigDecimal.valueOf(15_000L), 치킨단품.getId(), Collections.singletonList(치킨상품));
        후라이드치킨주문요청 = new OrderLineItemRequest(후라이드치킨.getId(), 2L);
        order = Order.of(secondTable.getId(), OrderLineItems.of(Collections.singletonList(후라이드치킨주문요청.toOrderLineItem(OrderMenu.of(후라이드치킨)))));
    }

    @Test
    void 테이블_그룹을_등록할_수_있다() {
        OrderTable orderTable3 = new OrderTable(3L, new NumberOfGuests(0), true);
        OrderTable orderTable4 = new OrderTable(4L, new NumberOfGuests(0), true);
        when(orderTableRepository.findById(orderTable3.getId())).thenReturn(Optional.of(orderTable3));
        when(orderTableRepository.findById(orderTable4.getId())).thenReturn(Optional.of(orderTable4));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(tableGroup);
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(orderTable3.getId(), orderTable4.getId()));

        TableGroupResponse savedTableGroup = tableGroupService.create(request);

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(savedTableGroup.getOrderTables()).hasSize(orderTables.getOrderTableIds().size())
        );

    }

    @Test
    void 주문_테이블이_비어있다면_테이블_그룹을_등록할_수_없다() {
        List<Long> orderTableIds = Collections.emptyList();

        TableGroupRequest request = TableGroupRequest.of(orderTableIds);

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_EXISTS_ORDER_TABLE_LIST.message());
    }

    @Test
    void 주문_테이블이_2개_이하일_경우_테이블_그룹을_등록할_수_없다() {
        List<Long> orderTableIds = Arrays.asList(firstTable.getId());

        when(orderTableRepository.findById(firstTable.getId())).thenReturn(Optional.of(firstTable));
        TableGroupRequest request = TableGroupRequest.of(orderTableIds);

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.ORDER_TABLE_TWO_OVER.message());
    }

    @Test
    void 주문_테이블이_등록되지_않은_경우_테이블_그룹을_등록할_수_없다() {
        TableGroupRequest request = TableGroupRequest.of(Arrays.asList(firstTable.getId(), secondTable.getId()));

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_EXISTS_ORDER_TABLE.message());
    }

    @Test
    void 주문_테이블이_이미_테이블_그룹으로_등록되어_있는_경우_테이블_그룹을_등록할_수_없다() {
        when(orderTableRepository.findById(firstTable.getId())).thenReturn(Optional.of(firstTable));
        when(orderTableRepository.findById(secondTable.getId())).thenReturn(Optional.of(secondTable));
        when(tableGroupRepository.save(any())).thenReturn(TableGroup.of(1L));

        assertThatThrownBy(() -> tableGroupService.create(
                TableGroupRequest.of(Arrays.asList(firstTable.getId(), secondTable.getId()))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.EXISTS_NOT_EMPTY_ORDER_TABLE.message());
    }

    @Test
    void 테이블_그룹을_해제할_수_있다() {
        when(tableGroupRepository.findById(tableGroup.getId())).thenReturn(Optional.of(tableGroup));
        when(orderTableRepository.findAllByTableGroupId(any(Long.class))).thenReturn(orderTables.getOrderTables());

        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertAll(
                () -> assertThat(firstTable.getTableGroupId()).isNull(),
                () -> assertThat(secondTable.getTableGroupId()).isNull()
        );
    }

    @Test
    void 주문_상태가_조리_또는_식사중이면_테이블_그룹을_해제할_수_없다() {
        TableGroup 단체 = TableGroup.of(1L);
        order.setOrderStatus(OrderStatus.MEAL);
        when(tableGroupRepository.findById(단체.getId())).thenReturn(Optional.of(단체));
        when(orderTableRepository.findAllByTableGroupId(단체.getId())).thenReturn(Arrays.asList(firstTable, secondTable));
        when(orderRepository.findAllByOrderTableIdIn(Arrays.asList(firstTable.getId(), secondTable.getId()))).thenReturn(Collections.singletonList(order));

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.NOT_PAYMENT_ORDER.message());
    }
}
