package kitchenpos.order.application;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.application.fixture.MenuDtoFixtureFactory;
import kitchenpos.menu.application.fixture.MenuProductDtoFixtureFactory;
import kitchenpos.menu.application.util.MenuContextServiceBehavior;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.order.application.util.OrderContextServiceBehavior;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.CannotChangeOrderStatusException;
import kitchenpos.order.exception.CannotMakeOrderException;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import kitchenpos.order.fixture.OrderLineItemFixtureFactory;
import kitchenpos.product.application.util.ProductContextServiceBehavior;
import kitchenpos.product.domain.Product;
import kitchenpos.table.application.util.TableContextServiceBehavior;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.NotExistTableException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {
    @Autowired
    private TableContextServiceBehavior tableContextServiceBehavior;

    @Autowired
    private MenuContextServiceBehavior menuContextServiceBehavior;

    @Autowired
    private ProductContextServiceBehavior productContextServiceBehavior;

    @Autowired
    private OrderContextServiceBehavior orderContextServiceBehavior;

    @Autowired
    private OrderService orderService;

    private MenuGroup menuGroup;
    private Product product1;
    private Product product2;
    private Product product3;
    private MenuDto menu1;
    private MenuDto menu2;
    private OrderTableResponse orderTable;

    @BeforeEach
    void setUp() {
        menuGroup = menuContextServiceBehavior.메뉴그룹_생성됨("메뉴그룹1");
        product1 = productContextServiceBehavior.상품_생성됨("상품1", 1000);
        product2 = productContextServiceBehavior.상품_생성됨("상품2", 2000);
        product3 = productContextServiceBehavior.상품_생성됨("상품3", 3000);
        MenuProductDto menuProduct1 = MenuProductDtoFixtureFactory.createMenuProduct(product1.getId(), 4);
        MenuProductDto menuProduct2 = MenuProductDtoFixtureFactory.createMenuProduct(product2.getId(), 2);
        menu1 = menuContextServiceBehavior.메뉴_생성됨(menuGroup, "메뉴1", 4000, Lists.newArrayList(menuProduct1));
        menu2 = menuContextServiceBehavior.메뉴_생성됨(menuGroup, "메뉴2", 4000, Lists.newArrayList(menuProduct2));
        orderTable = tableContextServiceBehavior.비어있지않은테이블_생성됨(3);
    }

    @Test
    @DisplayName("주문 등록")
    void 주문등록() {
        OrderLineItemRequest orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItemRequest orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);

        OrderResponse savedOrder = orderContextServiceBehavior.주문_생성됨(orderTable.getId(),
                Lists.newArrayList(orderLineItem1, orderLineItem2));
        Assertions.assertAll("등록된 주문을 확인한다"
                , () -> assertThat(savedOrder.getId()).isNotNull()
                , () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString())
                , () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTable.getId())
                , () -> assertThat(savedOrder.getOrderLineItemResponses()).hasSize(2)
        );
    }

    @Test
    @DisplayName("인원수가 0명이라도 주문 등록 가능")
    void 주문등록_인원수가0일때도_주문등록가능() {
        OrderLineItemRequest orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItemRequest orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);
        OrderTableResponse zeroGuestTable = tableContextServiceBehavior.비어있지않은테이블_생성됨(0);
        OrderResponse savedOrder = orderContextServiceBehavior.주문_생성됨(zeroGuestTable.getId(),
                Lists.newArrayList(orderLineItem1, orderLineItem2));
        Assertions.assertAll("등록된 주문을 확인한다"
                , () -> assertThat(savedOrder.getId()).isNotNull()
                , () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString())
                , () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(zeroGuestTable.getId())
                , () -> assertThat(savedOrder.getOrderLineItemResponses()).hasSize(2)
        );
    }

    @Test
    @DisplayName("주문항목이 없는경우 주문 등록 실패")
    void 주문등록_주문항목이_없는경우() {
        Long orderTableId = orderTable.getId();
        List<OrderLineItemRequest> orderLineItemRequests = emptyList();
        assertThatThrownBy(() -> orderContextServiceBehavior.주문_생성됨(orderTableId, orderLineItemRequests))
                .isInstanceOf(EmptyOrderLineItemsException.class);
    }

    @Test
    @DisplayName("주문항목에 표시된 메뉴가 존재하지 않는 경우 주문 등록 실패")
    void 주문등록_주문항목에_표시된_메뉴가_저장되지않은경우() {
        MenuProductDto menuProduct = MenuProductDtoFixtureFactory.createMenuProduct(product1.getId(), 1);
        MenuDto notSavedMenu = MenuDtoFixtureFactory.createMenu(menuGroup, "메뉴", 4000, Lists.newArrayList(menuProduct));
        OrderLineItemRequest orderLineItem = OrderLineItemFixtureFactory.createOrderLine(notSavedMenu.getId(), 3);

        Long orderTableId = orderTable.getId();
        List<OrderLineItemRequest> orderLineItemRequests = Lists.newArrayList(orderLineItem);
        assertThatThrownBy(() -> orderContextServiceBehavior.주문_생성됨(orderTableId, orderLineItemRequests))
                .isInstanceOf(CannotMakeOrderException.class);
    }

    @Test
    @DisplayName("주문테이블이 존재하지 않는 경우 주문 등록 실패")
    void 주문등록_주문테이블이_없는경우() {
        Long notExistTableId = -1L;
        OrderLineItemRequest orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItemRequest orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);
        List<OrderLineItemRequest> orderLineItemRequests = Lists.newArrayList(orderLineItem1, orderLineItem2);
        assertThatThrownBy(() -> orderContextServiceBehavior.주문_생성됨(notExistTableId, orderLineItemRequests))
                .isInstanceOf(NotExistTableException.class);
    }

    @Test
    @DisplayName("주문테이블이 빈 테이블인 경우 주문 등록 실패")
    void 주문등록_주문테이블이_빈테이블인_경우() {
        OrderTableResponse orderTable = tableContextServiceBehavior.빈테이블_생성됨();
        OrderLineItemRequest orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItemRequest orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);

        Long orderTableId = orderTable.getId();
        List<OrderLineItemRequest> orderLineItemRequests = Lists.newArrayList(orderLineItem1, orderLineItem2);
        assertThatThrownBy(() -> orderContextServiceBehavior.주문_생성됨(orderTableId, orderLineItemRequests))
                .isInstanceOf(CannotMakeOrderException.class);
    }

    @Test
    @DisplayName("주문 목록 조회")
    void 주문목록_조회() {
        OrderLineItemRequest orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItemRequest orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);
        OrderLineItemRequest orderLineItem3 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 2);

        orderContextServiceBehavior.주문_생성됨(orderTable.getId(), Lists.newArrayList(orderLineItem1, orderLineItem2));
        orderContextServiceBehavior.주문_생성됨(orderTable.getId(), Lists.newArrayList(orderLineItem3));

        List<OrderResponse> orders = orderService.list();
        assertThat(orders).hasSize(2);
    }

    @Test
    @DisplayName("주문 상태 변경")
    void 주문상태_변경() {
        OrderLineItemRequest orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItemRequest orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);
        OrderResponse order = orderContextServiceBehavior.주문_생성됨(orderTable.getId(),
                Lists.newArrayList(orderLineItem1, orderLineItem2));

        OrderResponse updatedOrder = orderContextServiceBehavior.주문상태_변경(order.getId(), OrderStatus.MEAL);
        Assertions.assertAll("주문상태의 변경여부를 확인한다"
                , () -> assertThat(updatedOrder.getId()).isEqualTo(order.getId())
                , () -> assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
        );


    }

    @Test
    @DisplayName("주문 상태가 계산완료인 경우 주문 상태 변경 불가")
    void 주문상태_변경_이미_계산완료상태인경우() {
        OrderLineItemRequest orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItemRequest orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);

        OrderResponse order = orderContextServiceBehavior.주문_생성됨(orderTable.getId(),
                Lists.newArrayList(orderLineItem1, orderLineItem2));
        orderContextServiceBehavior.주문상태_변경(order.getId(), OrderStatus.COMPLETION);
        Long orderId = order.getId();
        assertThatThrownBy(() -> orderContextServiceBehavior.주문상태_변경(orderId, OrderStatus.MEAL))
                .isInstanceOf(CannotChangeOrderStatusException.class);
    }
}
