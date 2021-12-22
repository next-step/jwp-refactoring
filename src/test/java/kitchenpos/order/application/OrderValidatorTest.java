package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.fixture.MenuFixture;
import kitchenpos.menu.fixture.MenuProductFixture;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.fixture.MenuGroupFixture;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.fixture.ProductFixture;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 검증 테스트")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderValidator orderValidator;

    private Menu 더블강정;
    private OrderTable 테이블;
    private OrderTable 빈_테이블;

    @BeforeEach
    void setup() {
        Product 강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        MenuGroup 추천메뉴 = MenuGroupFixture.create(1L, "추천메뉴");
        MenuProduct 메뉴_상품 = MenuProductFixture.create(강정치킨.getId(), 2L);

        더블강정 = MenuFixture.create(1L, "더블강정", BigDecimal.valueOf(32_000), 추천메뉴, 메뉴_상품);
        테이블 = OrderTableFixture.create(1L, 4, false);
        빈_테이블 = OrderTableFixture.create(2L, 4, true);
    }

    @DisplayName("주문 생성 검증")
    @Nested
    class TestValidateCreateOrder {
        @DisplayName("검증 확인")
        @Test
        void 검증_확인() {
            // given
            OrderLineItemRequest 주문_항목 = OrderLineItemRequest.of(더블강정.getId(), 1L);
            OrderRequest 등록_요청_데이터 = OrderRequest.of(테이블.getId(), Collections.singletonList(주문_항목));

            given(orderTableRepository.findById(any())).willReturn(Optional.of(테이블));

            // when
            ThrowableAssert.ThrowingCallable 검증_요청 = () -> orderValidator.validateCreateOrder(등록_요청_데이터);

            // then
            assertThatNoException().isThrownBy(검증_요청);
        }

        @DisplayName("주문 테이블이 존재하지 않음")
        @Test
        void 주문_테이블이_존재하지_않음() {
            // given
            OrderLineItemRequest 주문_항목 = OrderLineItemRequest.of(더블강정.getId(), 1L);
            OrderRequest 등록_요청_데이터 = OrderRequest.of(테이블.getId(), Collections.singletonList(주문_항목));

            given(orderTableRepository.findById(any())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable 검증_요청 = () -> orderValidator.validateCreateOrder(등록_요청_데이터);

            // then
            assertThatThrownBy(검증_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블에 주문 요청")
        @Test
        void 빈_테이블에_주문_요청() {
            // given
            OrderLineItemRequest 주문_항목 = OrderLineItemRequest.of(더블강정.getId(), 1L);
            OrderRequest 등록_요청_데이터 = OrderRequest.of(테이블.getId(), Collections.singletonList(주문_항목));

            given(orderTableRepository.findById(any())).willReturn(Optional.of(빈_테이블));

            // when
            ThrowableAssert.ThrowingCallable 검증_요청 = () -> orderValidator.validateCreateOrder(등록_요청_데이터);

            // then
            assertThatThrownBy(검증_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
