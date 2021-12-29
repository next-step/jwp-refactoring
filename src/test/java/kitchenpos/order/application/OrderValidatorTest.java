package kitchenpos.order.application;

import kitchenpos.common.fixtrue.MenuFixture;
import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.MenuProductFixture;
import kitchenpos.common.fixtrue.OrderLineItemFixture;
import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderValidator orderValidator;

    OrderTable 빈_테이블;
    OrderTable 주문_테이블;
    OrderLineItem 주문_상품;

    Menu 후라이드_후라이드;
    OrderLineItemRequest 주문_항목_요청;
    OrderRequest 주문_요청;

    @BeforeEach
    void setUp() {
        Product 후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        MenuGroup 두마리치킨 = MenuGroupFixture.from("두마리치킨");
        MenuProduct 후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨, 2);
        후라이드_후라이드 = MenuFixture.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_메뉴_상품)));

        주문_테이블 = OrderTableFixture.of(4, false);
        빈_테이블 = OrderTableFixture.of(0, true);
        주문_상품 = OrderLineItemFixture.of(후라이드_후라이드, 1L);

        주문_항목_요청 = OrderLineItemRequest.of(후라이드_후라이드.getId(), 1L);
        주문_요청 = OrderRequest.of(1L, Arrays.asList(주문_항목_요청));
    }

    @Test
    void 주문_발생_시_주문_테이블이_존재해야_한다() {
        // given
        OrderRequest 주문_요청 = OrderRequest.of(1L, Arrays.asList(주문_항목_요청));
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> orderValidator.validate(주문_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_발생_시_주문_테이블이_빈_테이블이면_주문할_수_없다() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.of(빈_테이블));

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> orderValidator.validate(주문_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

}
