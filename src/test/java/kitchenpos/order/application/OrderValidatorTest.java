package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.fixture.MenuFixture;
import kitchenpos.menu.fixture.MenuProductFixture;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.fixture.MenuGroupFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
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
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 검증 테스트")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    ApplicationEventPublisher applicationEventPublisher;
    @Mock
    MenuRepository menuRepository;

    @InjectMocks
    OrderValidator orderValidator;

    private Menu 더블강정;
    private OrderTable 테이블;

    @BeforeEach
    void setup() {
        Product 강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        MenuGroup 추천메뉴 = MenuGroupFixture.create(1L, "추천메뉴");
        MenuProduct 메뉴_상품 = MenuProductFixture.create(강정치킨.getId(), 2L);

        더블강정 = MenuFixture.create(1L, "더블강정", BigDecimal.valueOf(32_000), 추천메뉴.getId(), 메뉴_상품);
        테이블 = OrderTableFixture.create(1L, 4, false);
    }

    @DisplayName("주문 생성 검증")
    @Nested
    class TestValidateCreateOrder {
        @DisplayName("검증 확인")
        @Test
        void 검증_확인() {
            // given
            OrderLineItem 주문_항목 = OrderLineItemFixture.create(1L, 더블강정.getId(), 1L);
            Order 주문 = OrderFixture.create(1L, 테이블.getId(), OrderStatus.COOKING, 주문_항목);

            given(menuRepository.findAllById(any())).willReturn(Collections.singletonList(더블강정));

            // when
            ThrowableAssert.ThrowingCallable 검증_요청 = () -> orderValidator.validateCreateOrder(주문);

            // then
            assertThatNoException().isThrownBy(검증_요청);
        }

        @DisplayName("주문 메뉴가 존재하지 않음")
        @Test
        void 주문_메뉴가_존재하지_않음() {
            // given
            OrderLineItem 주문_항목 = OrderLineItemFixture.create(1L, 더블강정.getId(), 1L);
            Order 주문 = OrderFixture.create(1L, 테이블.getId(), OrderStatus.COOKING, 주문_항목);

            given(menuRepository.findAllById(any())).willReturn(Collections.emptyList());

            // when
            ThrowableAssert.ThrowingCallable 검증_요청 = () -> orderValidator.validateCreateOrder(주문);

            // then
            assertThatThrownBy(검증_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
