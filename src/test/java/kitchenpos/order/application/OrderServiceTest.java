package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 서비스")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private MenuService menuService;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("전체 주문을 조회할 수 있다.")
    @Test
    void 전체_주문_조회() {
        // given
        Product 후라이드 = Product.of("후라이드", 16000L);
        MenuProduct 후라이드_하나 = MenuProduct.of(후라이드, 1L);
        MenuProducts 메뉴_상품 = new MenuProducts(Collections.singletonList(후라이드_하나));
        Menu 메뉴 = new Menu("후라이드치킨", new BigDecimal(16000), new MenuGroup("한마리메뉴"), 메뉴_상품);

        OrderTable 주문_테이블 = new OrderTable(1L, 5);
        Order 주문 = Order.of(주문_테이블, new OrderLineItems(Arrays.asList(OrderLineItem.of(메뉴, 2L))));

        given(orderRepository.findAll()).willReturn(Collections.singletonList(주문));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
    }

    @DisplayName("주문 생성")
    @Nested
    class 주문_생성 {
        @DisplayName("주문을 생성할 수 있고 생성된 주문의 주문 상태는 조리이다.")
        @Test
        void 주문_생성_성공() {
            // given
            Product 후라이드 = Product.of("후라이드", 16000L);
            MenuProduct 후라이드_하나 = MenuProduct.of(후라이드, 1L);
            MenuProducts 메뉴_상품 = new MenuProducts(Collections.singletonList(후라이드_하나));
            Menu 메뉴 = new Menu("후라이드치킨", new BigDecimal(16000), new MenuGroup("한마리메뉴"), 메뉴_상품);
            OrderLineItem 주문_항목 = new OrderLineItem(메뉴, 1);
            OrderLineItems 주문_항목들 = new OrderLineItems(Collections.singletonList(주문_항목));

            OrderTable 주문_테이블 = new OrderTable(1L, 5);

            given(orderTableRepository.findById(eq(주문_테이블.getId()))).willReturn(Optional.ofNullable(주문_테이블));
            given(orderRepository.save(any(Order.class))).willReturn(
                    new Order(1L, 주문_테이블, OrderStatus.COOKING, LocalDateTime.now(), 주문_항목들));

            OrderLineItemRequest 주문_항목_요청 = new OrderLineItemRequest(1L, 3);
            OrderRequest 주문_요청 = OrderRequest.of(주문_테이블.getId(), Collections.singletonList(주문_항목_요청));

            // when
            OrderResponse savedOrder = orderService.create(주문_요청);

            // then
            assertAll(() -> assertThat(savedOrder).isNotNull(), () -> assertThat(savedOrder.getId()).isNotNull(),
                    () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()));
        }

        @DisplayName("주문 생성 실패")
        @Nested
        class 주문_생성_실패 {
            private OrderRequest 주문_요청;

            @BeforeEach
            void setUp() {
                OrderLineItemRequest 주문_항목_요청 = new OrderLineItemRequest(1L, 2);
                주문_요청 = OrderRequest.of(1L, Collections.singletonList(주문_항목_요청));
            }

            @DisplayName("주문 항목이 없는 주문은 생성할 수 없다.")
            @Test
            void 주문_항목이_없는_주문_생성() {
                // given
                OrderRequest 주문_항목이_없는_주문_요청 = OrderRequest.of(1L, Collections.emptyList());

                // when / then
                assertThatThrownBy(() -> orderService.create(주문_항목이_없는_주문_요청)).isInstanceOf(
                        IllegalArgumentException.class);
            }

            @DisplayName("중복된 메뉴의 주문 항목을 가진 주문은 생성할 수 없다.")
            @Test
            void 중복된_메뉴의_주문_항목을_가진_주문_생성() {
                // given
                Product 후라이드 = Product.of("후라이드", 16000L);
                MenuProduct 후라이드_하나 = MenuProduct.of(후라이드, 1L);
                MenuProducts 메뉴_상품 = new MenuProducts(Collections.singletonList(후라이드_하나));
                Menu 메뉴 = new Menu("메뉴", new BigDecimal(16000L), new MenuGroup("메뉴그룹"), 메뉴_상품);
                given(menuService.findMenuById(eq(메뉴.getId()))).willReturn(메뉴);

                OrderTable 주문_테이블 = new OrderTable(1L, 5);

                OrderLineItem 주문_항목1 = new OrderLineItem(메뉴, 1);
                OrderLineItem 주문_항목2 = new OrderLineItem(메뉴, 3);

                OrderLineItemRequest 요청의_주문_항목1 = new OrderLineItemRequest(주문_항목1.getMenuId(), 주문_항목1.getQuantity());
                OrderLineItemRequest 요청의_주문_항목2 = new OrderLineItemRequest(주문_항목2.getMenuId(), 주문_항목2.getQuantity());
                OrderRequest 주문_요청 = OrderRequest.of(주문_테이블.getId(), Arrays.asList(요청의_주문_항목1, 요청의_주문_항목2));

                // when / then
                assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("존재하지 않는 주문 테이블로 주문을 생성할 수 없다.")
            @Test
            void 존재하지_않는_주문_테이블을_포함한_주문_생성() {
                // given
                OrderLineItemRequest 주문_항목_요청 = new OrderLineItemRequest(1L, 1);

                Long 존재하지_않는_주문_테이블_아이디 = 99999L;
                OrderRequest 주문_요청 = OrderRequest.of(존재하지_않는_주문_테이블_아이디, Collections.singletonList(주문_항목_요청));

                given(orderTableRepository.findById(eq(존재하지_않는_주문_테이블_아이디))).willReturn(Optional.empty());

                // when / then
                assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("빈 주문 테이블로 주문을 생성할 수 없다.")
            @Test
            void 빈_주문_테이블로_주문_생성() {
                // given
                OrderTable 빈_테이블 = new OrderTable(1L, 0, Boolean.TRUE);
                given(orderTableRepository.findById(eq(빈_테이블.getId()))).willReturn(Optional.ofNullable(빈_테이블));

                OrderLineItemRequest 주문_항목_요청 = new OrderLineItemRequest(1L, 2);
                OrderRequest 주문_요청 = OrderRequest.of(1L, Collections.singletonList(주문_항목_요청));

                // when / then
                assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @DisplayName("주문 상태 변경")
    @Nested
    class 주문_상태_변경 {
        @DisplayName("생성된 주문의 상태를 변경할 수 있다.")
        @Test
        void 주문_상태_변경_성공() {
            OrderLineItemRequest 주문_항목_요청 = new OrderLineItemRequest(1L, 3);
            OrderRequest 주문_요청 = OrderRequest.of(1L, Collections.singletonList(주문_항목_요청), OrderStatus.COOKING.name());

            Product 후라이드 = Product.of("후라이드", 16000L);
            MenuProduct 후라이드_하나 = MenuProduct.of(후라이드, 1L);
            MenuProducts 메뉴_상품 = new MenuProducts(Collections.singletonList(후라이드_하나));
            Menu 메뉴 = new Menu("메뉴", new BigDecimal(16000L), new MenuGroup("메뉴그룹"), 메뉴_상품);

            OrderLineItem 주문_항목 = new OrderLineItem(메뉴, 1);
            OrderLineItems 주문_항목들 = new OrderLineItems(Collections.singletonList(주문_항목));

            OrderTable 주문_테이블 = new OrderTable(1L, 5);

            Order 주문 = new Order(1L, 주문_테이블, OrderStatus.MEAL, LocalDateTime.now(), 주문_항목들);

            given(orderRepository.findById(eq(주문.getId()))).willReturn(Optional.of(주문));

            // when
            OrderResponse 변경된_주문 = orderService.changeOrderStatus(주문.getId(), 주문_요청);

            // then
            assertThat(변경된_주문.getOrderStatus()).isEqualTo(주문_요청.getOrderStatus());
        }

        @DisplayName("생성되지 않은 주문의 주문 상태를 변경할 수 없다.")
        @Test
        void 생성되지_않은_주문의_주문_상태_변경() {
            Long 존재하지_않는_주문_아이디 = 9999L;
            OrderRequest 주문_요청 = new OrderRequest(존재하지_않는_주문_아이디, Collections.emptyList(), OrderStatus.COOKING.name());

            given(orderRepository.findById(eq(존재하지_않는_주문_아이디))).willReturn(Optional.empty());

            // when / then
            assertThatThrownBy(() -> orderService.changeOrderStatus(존재하지_않는_주문_아이디, 주문_요청)).isInstanceOf(
                    IllegalArgumentException.class);
        }

        @DisplayName("계산 완료된 주문은 변경할 수 없다.")
        @Test
        void 계산_완료_주문_변경() {
            // given
            Product 후라이드 = Product.of("후라이드", 16000L);
            MenuProduct 후라이드_하나 = MenuProduct.of(후라이드, 1L);
            MenuProducts 메뉴_상품 = new MenuProducts(Collections.singletonList(후라이드_하나));
            Menu 메뉴 = new Menu("후라이드치킨", new BigDecimal(16000), new MenuGroup("한마리메뉴"), 메뉴_상품);
            OrderLineItem 주문_항목 = new OrderLineItem(메뉴, 1);
            OrderLineItems 주문_항목들 = new OrderLineItems(Collections.singletonList(주문_항목));

            OrderTable 주문_테이블 = new OrderTable(1L, 5);

            Order 주문 = new Order(1L, 주문_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), 주문_항목들);

            OrderRequest 주문_변경_요청 = OrderRequest.of(주문_테이블.getId(),
                    Collections.singletonList(new OrderLineItemRequest(주문_항목.getMenuId(), 주문_항목.getQuantity())),
                    OrderStatus.COOKING.name());

            given(orderRepository.findById(eq(주문.getId()))).willReturn(Optional.of(주문));

            // when / then
            assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문_변경_요청)).isInstanceOf(
                    IllegalArgumentException.class);
        }
    }
}
