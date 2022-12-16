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
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;
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

    private Product 하와이안피자;
    private MenuProduct 하와이안피자상품;
    private MenuGroup 피자;
    private Menu 하와이안피자세트;
    private OrderLineItemRequest 하와이안피자세트주문요청;
    private Order 주문;
    private OrderTable 주문테이블;
    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private OrderMenu 주문메뉴;

    @BeforeEach
    void setUp() {
        하와이안피자 = new Product(1L, "하와이안피자", BigDecimal.valueOf(15_000));
        피자 = new MenuGroup(1L, "피자");
        하와이안피자상품 = new MenuProduct(1L, 하와이안피자세트, 하와이안피자, 1L);
        하와이안피자세트 = new Menu(1L, "하와이안피자세트", BigDecimal.valueOf(15_000L), 피자,
            MenuProducts.from(Arrays.asList(하와이안피자상품)));
        주문메뉴 = OrderMenu.from(하와이안피자세트);
        하와이안피자세트주문요청 = OrderLineItemRequest.from(하와이안피자세트.getId(), 1);
        주문테이블 = new OrderTable(1L, null, 0, false);
        주문 = Order.of(주문테이블, OrderLineItems.from(Collections.singletonList(하와이안피자세트주문요청.toOrderLineItem(주문메뉴))));

        주문테이블A = new OrderTable(1L, null, 4, true);
        주문테이블B = new OrderTable(2L, null, 6, true);
    }

    @DisplayName("주문 테이블의 단체를 지정한다.")
    @Test
    void createTableGroup() {
        OrderTable orderTable1 = new OrderTable(3L, null, 4, true);
        OrderTable orderTable2 = new OrderTable(4L, null, 4, true);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());
        TableGroup tableGroup = new TableGroup(OrderTables.from(Arrays.asList(orderTable1, orderTable2)));
        TableGroupRequest request = TableGroupRequest.from(orderTableIds);

        when(orderTableRepository.findById(orderTable1.getId())).thenReturn(Optional.of(주문테이블A));
        when(orderTableRepository.findById(orderTable2.getId())).thenReturn(Optional.of(주문테이블B));
        when(tableGroupRepository.save(tableGroup)).thenReturn(tableGroup);

        // when
        TableGroupResponse result = tableGroupService.create(request);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(tableGroup.getId()),
            () -> assertThat(result.getOrderTables()).hasSize(2)
        );
    }

    @DisplayName("단체 지정 시 주문테이블이 비어있으면 예외가 발생한다.")
    @Test
    void createTableGroupEmptyOrderTableException() {
        // given
        TableGroupRequest request = TableGroupRequest.from(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void createTableGroupUnderTwoSizeOrderTableException() {
        // given
        TableGroupRequest request = TableGroupRequest.from(Collections.singletonList(주문테이블A.getId()));
        when(orderTableRepository.findById(주문테이블A.getId())).thenReturn(Optional.of(주문테이블A));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 비어있지 않은 주문 테이블이 존재하는 경우 예외가 발생한다.")
    @Test
    void notExistOrderTableException() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, null, 4, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 4, true);
        TableGroupRequest request = TableGroupRequest.from(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 다른 단체에 속해 있다면 예외가 발생한다.")
    @Test
    void alreadyTableGroupException() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, null, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 4, true);
        TableGroup tableGroup = new TableGroup(OrderTables.from(Arrays.asList(orderTable1, orderTable2)));
        TableGroupRequest request = TableGroupRequest.from(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroup tableGroup = new TableGroup(OrderTables.from(Arrays.asList(주문테이블, 주문테이블A, 주문테이블B)));
        Order order = 주문.changeOrderStatus(OrderStatus.COMPLETION);
        when(tableGroupRepository.findById(tableGroup.getId())).thenReturn(Optional.of(tableGroup));
        when(orderRepository.findAllByOrderTableIdIn(
            Arrays.asList(주문테이블.getId(), 주문테이블A.getId(), 주문테이블B.getId()))).thenReturn(Collections.singletonList(order));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertAll(
            () -> assertThat(주문테이블A.getTableGroup()).isNull(),
            () -> assertThat(주문테이블B.getTableGroup()).isNull()
        );
    }

    @DisplayName("단체 내 주문 테이블들의 상태가 조리, 식사일 때 단체 지정 해체 시 예외가 발생한다.")
    @Test
    void unGroupStateException() {
        // given
        TableGroup tableGroup = new TableGroup(OrderTables.from(Arrays.asList(주문테이블, 주문테이블A, 주문테이블B)));
        Order order = 주문.changeOrderStatus(OrderStatus.COOKING);
        when(tableGroupRepository.findById(tableGroup.getId())).thenReturn(Optional.of(tableGroup));
        when(orderRepository.findAllByOrderTableIdIn(
            Arrays.asList(주문테이블.getId(), 주문테이블A.getId(), 주문테이블B.getId()))).thenReturn(Collections.singletonList(order));

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
