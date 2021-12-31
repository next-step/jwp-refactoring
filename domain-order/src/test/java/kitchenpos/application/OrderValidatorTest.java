package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderLineItemFixture;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

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
        MenuProduct 후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨.getId(), 2);
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
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.empty());

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> orderValidator.validate(주문_요청);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_발생_시_주문_테이블이_빈_테이블이면_주문할_수_없다() {
        // given
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(빈_테이블));

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> orderValidator.validate(주문_요청);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

}
