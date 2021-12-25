package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.fixture.OrderFixture;
import kitchenpos.order.fixture.OrderLineItemFixture;
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
    MenuClient menuClient;

    @InjectMocks
    OrderValidator orderValidator;

    private OrderTable 테이블;

    @BeforeEach
    void setup() {
        테이블 = OrderTableFixture.create(1L, 4, false);
    }

    @DisplayName("주문 생성 검증")
    @Nested
    class TestValidateCreateOrder {
        @DisplayName("검증 확인")
        @Test
        void 검증_확인() {
            // given
            Long 메뉴_ID = 1L;
            OrderLineItem 주문_항목 = OrderLineItemFixture.create(1L, 메뉴_ID, 1L);
            Order 주문 = OrderFixture.create(1L, 테이블.getId(), OrderStatus.COOKING, 주문_항목);

            given(menuClient.isExistMenuByIds(any())).willReturn(true);

            // when
            ThrowableAssert.ThrowingCallable 검증_요청 = () -> orderValidator.validateCreateOrder(주문);

            // then
            assertThatNoException().isThrownBy(검증_요청);
        }

        @DisplayName("주문 메뉴가 존재하지 않음")
        @Test
        void 주문_메뉴가_존재하지_않음() {
            // given
            Long 메뉴_ID = 1L;
            OrderLineItem 주문_항목 = OrderLineItemFixture.create(1L, 메뉴_ID, 1L);
            Order 주문 = OrderFixture.create(1L, 테이블.getId(), OrderStatus.COOKING, 주문_항목);

            given(menuClient.isExistMenuByIds(any())).willReturn(false);

            // when
            ThrowableAssert.ThrowingCallable 검증_요청 = () -> orderValidator.validateCreateOrder(주문);

            // then
            assertThatThrownBy(검증_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
