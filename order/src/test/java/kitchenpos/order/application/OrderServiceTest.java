package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.fixture.MenuFixture;
import kitchenpos.menu.fixture.MenuProductFixture;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.fixture.MenuGroupFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.fixture.OrderFixture;
import kitchenpos.order.fixture.OrderLineItemFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.fixture.ProductFixture;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.fixture.OrderTableFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderValidator orderValidator;

    @InjectMocks
    OrderService orderService;

    private Menu 더블강정;
    private OrderTable 테이블;
    private Order 생성된_주문;
    private Order 계산된_주문;

    @BeforeEach
    void setup() {
        Product 강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        MenuGroup 추천메뉴 = MenuGroupFixture.create(1L, "추천메뉴");
        MenuProduct 메뉴_상품 = MenuProductFixture.create(강정치킨.getId(), 2L);

        더블강정 = MenuFixture.create(1L, "더블강정", BigDecimal.valueOf(32_000), 추천메뉴.getId(), 메뉴_상품);
        테이블 = OrderTableFixture.create(1L, 4, false);

        OrderLineItem 생성된_주문_항목 = OrderLineItemFixture.create(1L, 더블강정.getId(), 1L);
        생성된_주문 = OrderFixture.create(1L, 테이블.getId(), OrderStatus.COOKING, 생성된_주문_항목);
        계산된_주문 = OrderFixture.create(2L, 테이블.getId(), OrderStatus.COMPLETION, 생성된_주문_항목);
    }

    @DisplayName("주문 목록 조회 확인")
    @Test
    void 주문_목록_조회_확인() {
        // given
        given(orderRepository.findAll()).willReturn(Collections.singletonList(생성된_주문));

        // when
        List<OrderResponse> 주문_목록 = orderService.list();

        // then
        assertThat(주문_목록).hasSize(1);
    }

    @DisplayName("주문 생성 테스트")
    @Nested
    class TestCreateOrder {
        @DisplayName("주문 생성 확인")
        @Test
        void 주문_생성_확인() {
            // given
            OrderLineItemRequest 주문_항목 = OrderLineItemRequest.of(더블강정.getId(), 1L);
            OrderRequest 등록_요청_데이터 = OrderRequest.of(테이블.getId(), Collections.singletonList(주문_항목));

            given(orderRepository.save(any())).willReturn(생성된_주문);

            // when
            OrderResponse 등록된_주문 = orderService.create(등록_요청_데이터);

            // then
            assertThat(등록된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        }

        @DisplayName("주문 항목이 존재하지 않음")
        @Test
        void 주문_항목이_존재하지_않음() {
            // given
            OrderRequest 등록_요청_데이터 = OrderRequest.of(테이블.getId(), Collections.emptyList());

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> orderService.create(등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 상태 변경 테스트")
    @Nested
    class TestCreateOrderStatus {
        @DisplayName("주문 상태 변경 확인")
        @Test
        void 주문_상태_변경_확인() {
            // given
            ChangeOrderStatusRequest 변경_요청_데이터 = ChangeOrderStatusRequest.of("MEAL");

            given(orderRepository.findById(any())).willReturn(Optional.of(생성된_주문));

            // when
            OrderResponse 변경된_주문 = orderService.changeOrderStatus(1L, 변경_요청_데이터);

            // then
            assertThat(변경된_주문.getOrderStatus()).isEqualTo(변경_요청_데이터.getOrderStatus());
        }

        @DisplayName("존재하지 않는 주문에 변경 요청")
        @Test
        void 존재하지_않는_주문에_변경_요청() {
            // given
            ChangeOrderStatusRequest 변경_요청_데이터 = ChangeOrderStatusRequest.of("MEAL");

            given(orderRepository.findById(any())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable 변경_요청 = () -> orderService.changeOrderStatus(1L, 변경_요청_데이터);

            // then
            assertThatThrownBy(변경_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("계산 완료 상태인 주문에 변경 요청")
        @Test
        void 계산_완료_상태인_주문에_변경_요청() {
            // given
            ChangeOrderStatusRequest 변경_요청_데이터 = ChangeOrderStatusRequest.of("MEAL");

            given(orderRepository.findById(any())).willReturn(Optional.of(계산된_주문));

            // when
            ThrowableAssert.ThrowingCallable 변경_요청 = () -> orderService.changeOrderStatus(1L, 변경_요청_데이터);

            // then
            assertThatThrownBy(변경_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
