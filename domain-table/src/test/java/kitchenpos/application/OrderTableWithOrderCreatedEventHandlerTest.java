package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderCreatedEvent;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderTableWithOrderCreatedEventHandlerTest {

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderTableWithOrderCreatedEventHandler eventHandler;

    Menu 후라이드_후라이드;
    OrderTable 주문_테이블;
    OrderTable 빈_테이블;
    OrderLineItems 주문_항목들;
    @BeforeEach
    void setUp() {
        Product 후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        MenuGroup 두마리치킨 = MenuGroupFixture.from("두마리치킨");
        MenuProduct 후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨.getId(), 2);
        후라이드_후라이드 = MenuFixture.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_메뉴_상품)));
        주문_항목들 = OrderLineItems.from(Collections.singletonList(OrderLineItem.of(후라이드_후라이드, 1L)));
        주문_테이블 = OrderTableFixture.of(4, false);
        빈_테이블 = OrderTableFixture.of(4, true);
    }

    @Test
    void 주문_발생_시_주문_테이블이_존재해야_한다() {
        // given
        Order 주문 = Order.of(1L, 주문_항목들);
        OrderCreatedEvent 주문_이벤트 = new OrderCreatedEvent(주문);
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.empty());

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> eventHandler.handle(주문_이벤트);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_발생_시_주문_테이블이_빈_테이블이면_주문할_수_없다() {
        // given
        Order 주문 = Order.of(1L, 주문_항목들);
        OrderCreatedEvent 주문_이벤트 = new OrderCreatedEvent(주문);
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(빈_테이블));

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> eventHandler.handle(주문_이벤트);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }
}
