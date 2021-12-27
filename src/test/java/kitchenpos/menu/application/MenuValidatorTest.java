package kitchenpos.menu.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 유효성 검증 테스트")
class MenuValidatorTest {

    @InjectMocks
    private MenuValidator menuValidator;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    private Long productId;
    private Long menuGroupId;

    @BeforeEach
    public void setUp() {
        productId = 1L;
        menuGroupId = 1L;
    }

    @DisplayName("메뉴 생성 유효성 검증 성공 테스트")
    @Test
    void validateCreate_success() {
        // given
        Product product = Product.of(productId, "강정치킨", BigDecimal.valueOf(17_000));
        MenuProducts menuProducts = MenuProducts.of(Arrays.asList(MenuProduct.of(productId, Quantity.of(2))));
        Menu menu = Menu.of(Name.of("강정치킨_두마리_세트_메뉴"), Price.of(BigDecimal.valueOf(30_000)), menuGroupId, menuProducts);

        given(menuGroupRepository.existsById(menuGroupId)).willReturn(Boolean.TRUE);
        given(productRepository.findByIdIn(Arrays.asList(productId))).willReturn(Arrays.asList(product));

        // when & then
        assertThatNoException()
                .isThrownBy(() -> menuValidator.validateCreate(menu));
    }

    @DisplayName("메뉴 생성 유효성 검증 실패 테스트 - 메뉴 그룹 존재하지 않음")
    @Test
    void validateCreate_failure_validateMenuGroup() {
        // given
        MenuProducts menuProducts = MenuProducts.of(Arrays.asList(MenuProduct.of(productId, Quantity.of(2))));
        Menu menu = Menu.of(Name.of("강정치킨_두마리_세트_메뉴"), Price.of(BigDecimal.valueOf(30_000)), menuGroupId, menuProducts);

        given(menuGroupRepository.existsById(menuGroupId)).willReturn(Boolean.FALSE);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.validateCreate(menu));
    }

    @DisplayName("메뉴 생성 유효성 검증 실패 테스트 - 메뉴 상품 없음")
    @Test
    void validateCreate_failure_validateMenuProducts_empty() {
        // given
        MenuProducts menuProducts = MenuProducts.of(Collections.emptyList());
        Menu menu = Menu.of(Name.of("강정치킨_두마리_세트_메뉴"), Price.of(BigDecimal.valueOf(51_000)), menuGroupId, menuProducts);

        given(menuGroupRepository.existsById(menuGroupId)).willReturn(Boolean.TRUE);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.validateCreate(menu));
    }

    @DisplayName("메뉴 생성 유효성 검증 실패 테스트 - 메뉴가 메뉴 상품들의 합계보다 비쌈")
    @Test
    void validateCreate_failure_validateMenuProducts_expensive() {
        // given
        Product product = Product.of(productId, "강정치킨", BigDecimal.valueOf(17_000));
        MenuProducts menuProducts = MenuProducts.of(Arrays.asList(MenuProduct.of(productId, Quantity.of(2))));
        Menu menu = Menu.of(Name.of("강정치킨_두마리_세트_메뉴"), Price.of(BigDecimal.valueOf(51_000)), menuGroupId, menuProducts);

        given(menuGroupRepository.existsById(menuGroupId)).willReturn(Boolean.TRUE);
        given(productRepository.findByIdIn(Arrays.asList(productId))).willReturn(Arrays.asList(product));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.validateCreate(menu));
    }

    @DisplayName("메뉴 생성 유효성 검증 실패 테스트 - 상품 수 일치하지 않음")
    @Test
    void validateCreate_failure_invalidProductSize() {
        // given
        MenuProducts menuProducts = MenuProducts.of(Arrays.asList(MenuProduct.of(productId, Quantity.of(2))));
        Menu menu = Menu.of(Name.of("강정치킨_두마리_세트_메뉴"), Price.of(BigDecimal.valueOf(30_000)), menuGroupId, menuProducts);

        given(menuGroupRepository.existsById(menuGroupId)).willReturn(Boolean.TRUE);
        given(productRepository.findByIdIn(Arrays.asList(productId))).willReturn(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuValidator.validateCreate(menu));
    }
}
