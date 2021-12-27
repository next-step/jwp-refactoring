package kitchenpos.menu.domain;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("메뉴 validator 클래스")
@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;


    @Test
    @DisplayName("`메뉴`가 속할 `메뉴그룹`이 필수로 있어야 한다.")
    void 메뉴그룹이_없으면_실패() {
        // given
        Menu menu = Menu.of("메뉴", 15000, 1L, Collections.singletonList(MenuProduct.of(1L, 1L)));
        given(menuGroupRepository.existsById(1L)).willReturn(false);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> menuValidator.registerValidate(menu);

        // then
        assertThatThrownBy(actual).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("`메뉴`의 가격은 상품목록의 가격(상품가격 * 갯수)의 총합보다 클 수 없다.")
    void 메뉴가격이_상품가격_총합보다_크면실패() {
        // given
        Product product = Product.of("치킨", 10000);
        Menu menu = Menu.of("메뉴", 15000, 1L, Collections.singletonList(MenuProduct.of(1L, 1L)));
        given(menuGroupRepository.existsById(1L)).willReturn(true);
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> menuValidator.registerValidate(menu);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }
}
