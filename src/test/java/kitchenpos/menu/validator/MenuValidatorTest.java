package kitchenpos.menu.validator;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menuGroup.dao.MenuGroupRepository;
import kitchenpos.product.dao.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static kitchenpos.common.domain.PriceTest.가격_생성;
import static kitchenpos.menu.MenuGenerator.*;
import static kitchenpos.menu.domain.QuantityTest.수량_생성;
import static kitchenpos.product.ProductGenerator.상품_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    @DisplayName("메뉴 유효성 검사시 없는 메뉴 그룹 정보로 메뉴를 생성하면 예외가 발생해야 한다")
    @Test
    void createMenuByNotAssociateMenuGroup() {
        // when
        Menu 메뉴 = 메뉴_생성("메뉴 상품이 null 인 메뉴", 1_000, 0L, 메뉴_상품_목록_생성(Collections.emptyList()));

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->  menuValidator.validateMenu(메뉴));
    }

    @DisplayName("메뉴 유효성 검사시 메뉴 상품이 포함되어 있지 않으면 예외가 발생해야 한다")
    @Test
    void createMenuByNotIncludeMenuProducts() {
        // given
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(메뉴_그룹_생성("메뉴 그룹")));

        // when
        Menu 메뉴 = 메뉴_생성("메뉴 상품이 null 인 메뉴", 1_000, 0L, 메뉴_상품_목록_생성(Collections.emptyList()));

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->  menuValidator.validateMenu(메뉴));
    }

    @DisplayName("메뉴 유효성 검사시 메뉴 상품 목록의 전체 가격과 메뉴의 가격이 일치하지 않으면 예외가 발생해야 한다")
    @Test
    void createMenuByNotMatchedTotalMenuProductPriceAndMenuPrice() {
        // given
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(메뉴_그룹_생성("메뉴 그룹")));
        when(productRepository.findById(any())).thenReturn(Optional.of(상품_생성("상품", 가격_생성(1_000))));
        MenuProduct 메뉴_상품 = 메뉴_상품_생성(0, 수량_생성(2L));
        MenuProducts 메뉴_상품_목록 = 메뉴_상품_목록_생성(Collections.singletonList(메뉴_상품));

        // when
        Menu 메뉴 = 메뉴_생성("메뉴", 1_000, 0, 메뉴_상품_목록);

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->  menuValidator.validateMenu(메뉴));
    }

    @DisplayName("정상 상태의 메뉴 생성 시 메뉴 객체가 생성되어야 한다")
    @Test
    void createMenuTest() {
        // given
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(메뉴_그룹_생성("메뉴 그룹")));
        when(productRepository.findById(any())).thenReturn(Optional.of(상품_생성("상품", 가격_생성(1_000))));
        MenuProduct 메뉴_상품 = 메뉴_상품_생성(0, 수량_생성(1L));
        MenuProducts 메뉴_상품_목록 = 메뉴_상품_목록_생성(Collections.singletonList(메뉴_상품));

        // when
        Menu 메뉴 = 메뉴_생성("메뉴", 1_000, 0, 메뉴_상품_목록);

        // then
        assertThatNoException().isThrownBy(() ->  menuValidator.validateMenu(메뉴));
    }
}