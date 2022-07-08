package kitchenpos.menu.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 검증")
@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    private Menu 새_메뉴;
    private MenuGroup 메뉴_그룹;

    @BeforeEach
    void setUp() {
        메뉴_그룹 = new MenuGroup(1L, "메뉴 그룹");

        List<MenuProduct> menuProducts = Arrays.asList(MenuProduct.of(1L, 1L),
                MenuProduct.of(2L, 1L),
                MenuProduct.of(3L, 2L));

        새_메뉴 = new Menu.Builder()
                .menuGroupId(1L)
                .name("새 메뉴")
                .price(Price.from(5600))
                .menuProducts(new MenuProducts(menuProducts))
                .build();
    }

    @DisplayName("존재하지 않는 메뉴 그룹으로 메뉴를 생성할 수 없다.")
    @Test
    void 존재하지_않는_메뉴_그룹으로_생성하면_예외처리() {
        // given
        given(menuGroupRepository.findById(eq(새_메뉴.getMenuGroupId()))).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> menuValidator.validate(새_메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상품으로 구성한 메뉴 상품이 있으면 메뉴를 생성할 수 없다.")
    @Test
    void 존재하지_않는_상품으로_구성한_메뉴_상품이_있으면_예외처리() {
        // given
        Product 상품1 = Product.of(1L, "상품1", new BigDecimal(1000));
        Product 상품2 = Product.of(2L, "상품2", new BigDecimal(500));

        given(menuGroupRepository.findById(eq(새_메뉴.getMenuGroupId()))).willReturn(Optional.of(메뉴_그룹));
        given(productRepository.findById(eq(1L))).willReturn(Optional.of(상품1));
        given(productRepository.findById(eq(2L))).willReturn(Optional.of(상품2));
        given(productRepository.findById(eq(3L))).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> menuValidator.validate(새_메뉴)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 메뉴 상품의 전체 금액보다 큰 경우 메뉴를 생성할 수 없다.")
    @Test
    void 메뉴에_포함된_상품의_전체_금액보다_메뉴의_가격이_큰_경우_예외처리() {
        // given
        Product 상품1 = Product.of(1L, "상품1", new BigDecimal(1000));
        Product 상품2 = Product.of(2L, "상품2", new BigDecimal(500));
        Product 상품3 = Product.of(3L, "상품3", new BigDecimal(2000));

        given(menuGroupRepository.findById(eq(새_메뉴.getMenuGroupId()))).willReturn(Optional.of(메뉴_그룹));

        given(productRepository.findById(eq(1L))).willReturn(Optional.of(상품1));
        given(productRepository.findById(eq(2L))).willReturn(Optional.of(상품2));
        given(productRepository.findById(eq(3L))).willReturn(Optional.of(상품3));

        // when / then
        assertThatThrownBy(() -> menuValidator.validate(새_메뉴)).isInstanceOf(IllegalArgumentException.class);
    }
}
