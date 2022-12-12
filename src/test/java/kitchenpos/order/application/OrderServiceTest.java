package kitchenpos.order.application;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static kitchenpos.menu.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.order.domain.OrderLineItemTestFixture.generateOrderLineItemRequest;
import static kitchenpos.order.domain.OrderMenuTestFixture.generateOrderMenu;
import static kitchenpos.order.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.order.domain.OrderTestFixture.generateOrder;
import static kitchenpos.order.domain.OrderTestFixture.generateOrderRequest;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private Product 감자튀김;
    private Product 불고기버거;
    private Product 치킨버거;
    private Product 콜라;
    private MenuGroup 햄버거세트;
    private MenuProduct 감자튀김상품;
    private MenuProduct 불고기버거상품;
    private MenuProduct 치킨버거상품;
    private MenuProduct 콜라상품;
    private Menu 불고기버거세트;
    private Menu 치킨버거세트;
    private OrderMenu 불고기버거세트주문메뉴;
    private OrderMenu 치킨버거세트주문메뉴;
    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private OrderLineItemRequest 불고기버거세트주문요청;
    private OrderLineItemRequest 치킨버거세트주문요청;
    private Order 주문A;
    private Order 주문B;

    @BeforeEach
    void setUp() {
        감자튀김 = generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = generateProduct(2L, "콜라", BigDecimal.valueOf(1500L));
        불고기버거 = generateProduct(3L, "불고기버거", BigDecimal.valueOf(4000L));
        치킨버거 = generateProduct(4L, "치킨버거", BigDecimal.valueOf(4500L));
        햄버거세트 = generateMenuGroup(1L, "햄버거세트");
        감자튀김상품 = generateMenuProduct(1L, null, 감자튀김, 1L);
        콜라상품 = generateMenuProduct(2L, null, 콜라, 1L);
        불고기버거상품 = generateMenuProduct(3L, null, 불고기버거, 1L);
        치킨버거상품 = generateMenuProduct(3L, null, 치킨버거, 1L);
        불고기버거세트 = generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트,
                Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));
        치킨버거세트 = generateMenu(2L, "치킨버거세트", BigDecimal.valueOf(9000L), 햄버거세트,
                Arrays.asList(감자튀김상품, 콜라상품, 치킨버거상품));
        불고기버거세트주문메뉴 = generateOrderMenu(불고기버거세트);
        치킨버거세트주문메뉴 = generateOrderMenu(치킨버거세트);
        주문테이블A = generateOrderTable(1L, null, 5, false);
        주문테이블B = generateOrderTable(2L, null, 7, false);
        불고기버거세트주문요청 = generateOrderLineItemRequest(불고기버거세트.getId(), 2);
        치킨버거세트주문요청 = generateOrderLineItemRequest(치킨버거세트.getId(), 1);
        주문A = generateOrder(주문테이블A, Arrays.asList(불고기버거세트주문요청.toOrderLineItem(불고기버거세트주문메뉴), 치킨버거세트주문요청.toOrderLineItem(치킨버거세트주문메뉴)));
        주문B = generateOrder(주문테이블B, singletonList(불고기버거세트주문요청.toOrderLineItem(불고기버거세트주문메뉴)));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        OrderRequest orderRequest = generateOrderRequest(주문테이블A.getId(), OrderStatus.COOKING, singletonList(불고기버거세트주문요청));
        Order 주문 = Order.of(주문테이블A, OrderLineItems.from(singletonList(불고기버거세트주문요청.toOrderLineItem(불고기버거세트주문메뉴))));
        given(menuRepository.findById(불고기버거세트.getId())).willReturn(Optional.of(불고기버거세트));
        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.of(주문테이블A));
        given(orderRepository.save(주문)).willReturn(주문);

        // when
        OrderResponse orderResponse = orderService.create(orderRequest);

        // then
        assertAll(
                () -> assertThat(orderResponse.getOrderedTime()).isNotNull(),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(orderResponse.getId()).isEqualTo(주문.getId())
        );
    }

    @DisplayName("주문 항목이 비어있으면 주문 생성 시 예외가 발생한다.")
    @Test
    void createOrderThrowErrorWhenOrderLineItemIsEmpty() {
        // given
        OrderRequest orderRequest = generateOrderRequest(주문테이블A.getId(), OrderStatus.COOKING, emptyList());
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderRequest))
                .withMessage(ErrorCode.주문_항목은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("주문 생성 시, 주문 항목 내에 등록되지 않은 메뉴가 있다면 예외가 발생한다.")
    @Test
    void createOrderThrowErrorWhenOrderLineItemHasUnregisteredMenu() {
        // given
        OrderRequest orderRequest = generateOrderRequest(주문테이블A.getId(), OrderStatus.COOKING, singletonList(불고기버거세트주문요청));
        given(menuRepository.findById(불고기버거세트주문요청.getMenuId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderRequest))
                .withMessage(ErrorCode.존재하지_않는_메뉴.getErrorMessage());
    }

    @DisplayName("등록되어 있지않은 주문 테이블을 가진 주문은 생성될 수 없다.")
    @Test
    void createOrderThrowErrorWhenOrderTableIsNotExists() {
        // given
        OrderRequest orderRequest = generateOrderRequest(10L, OrderStatus.COOKING, singletonList(불고기버거세트주문요청));
        given(menuRepository.findById(불고기버거세트.getId())).willReturn(Optional.of(불고기버거세트));
        given(orderTableRepository.findById(10L)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderRequest))
                .withMessage(ErrorCode.존재하지_않는_주문_테이블.getErrorMessage());
    }

    @DisplayName("주문 테이블이 비어있을 경우 주문은 생성될 수 없다.")
    @Test
    void createOrderThrowErrorWhenOrderTableIsEmpty() {
        // given
        OrderTable orderTable = generateOrderTable(4L, null, 6, true);
        OrderRequest orderRequest = generateOrderRequest(orderTable.getId(), OrderStatus.COOKING, singletonList(불고기버거세트주문요청));
        given(menuRepository.findById(불고기버거세트.getId())).willReturn(Optional.of(불고기버거세트));
        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderRequest))
                .withMessage(ErrorCode.주문_테이블은_비어있으면_안됨.getErrorMessage());
    }

    @DisplayName("주문 전체 목록을 조회한다.")
    @Test
    void findAllOrders() {
        // given
        List<Order> orders = Arrays.asList(주문A, 주문B);
        given(orderRepository.findAll()).willReturn(orders);

        // when
        List<OrderResponse> findOrders = orderService.list();

        // then
        assertAll(
                () -> assertThat(findOrders).hasSize(orders.size()),
                () -> assertThat(findOrders.stream().map(OrderResponse::getId)).containsExactly(주문A.getId(), 주문B.getId())
        );
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderStatus expectOrderStatus = OrderStatus.MEAL;
        OrderRequest changeOrderRequest = generateOrderRequest(주문테이블B.getId(), expectOrderStatus, singletonList(불고기버거세트주문요청));
        given(orderRepository.findById(주문B.getId())).willReturn(Optional.of(주문B));

        // when
        OrderResponse resultOrder = orderService.changeOrderStatus(주문B.getId(), changeOrderRequest);

        // then
        assertThat(resultOrder.getOrderStatus()).isEqualTo(expectOrderStatus);
    }

    @DisplayName("등록되지 않은 주문에 대해 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusThrowErrorWhenOrderIsNotExists() {
        // given
        OrderRequest orderRequest = generateOrderRequest(주문테이블B.getId(), OrderStatus.COOKING, singletonList(불고기버거세트주문요청));
        Long notExistsOrderId = 5L;
        given(orderRepository.findById(notExistsOrderId)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(notExistsOrderId, orderRequest))
                .withMessage(ErrorCode.존재하지_않는_주문.getErrorMessage());
    }

    @DisplayName("이미 완료된 주문이면 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusThrowErrorWhenOrderStatusAlreadyComplete() {
        // given
        Order order = generateOrder(주문테이블B, singletonList(불고기버거세트주문요청.toOrderLineItem(불고기버거세트주문메뉴)));
        order.changeOrderStatus(OrderStatus.COMPLETION);
        OrderRequest changeOrderRequest = generateOrderRequest(주문테이블B.getId(), OrderStatus.MEAL, singletonList(불고기버거세트주문요청));
        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrderRequest))
                .withMessage(ErrorCode.이미_완료된_주문.getErrorMessage());
    }
}
