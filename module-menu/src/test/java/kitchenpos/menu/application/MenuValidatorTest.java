package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 유효성 관련 테스트")
@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    MenuValidator menuValidator;

    private MenuProductRequest requestMenuProductSeq1;
    private MenuProductRequest requestMenuProductSeq2;
    private List<MenuProductRequest> requestMenuProducts;
    private MenuProduct menuProductSeq1;
    private MenuProduct menuProductSeq2;
    private List<MenuProduct> menuProducts;
    private Product 초밥;
    private Product 우동;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        초밥 = new Product(1L, "초밥", BigDecimal.valueOf(10000));
        우동 = new Product(2L, "우동", BigDecimal.valueOf(3000));
        menuGroup = new MenuGroup(1L, "런치메뉴");

        requestMenuProductSeq1 = new MenuProductRequest(null, 1L, 2);
        requestMenuProductSeq2 = new MenuProductRequest(null, 2L, 2);
        requestMenuProducts = Arrays.asList(requestMenuProductSeq1, requestMenuProductSeq2);

        menuProductSeq1 = new MenuProduct(1L, null, 1L, 2);
        menuProductSeq2 = new MenuProduct(2L, null, 2L, 2);
        menuProducts = Arrays.asList(menuProductSeq1, menuProductSeq2);
    }

    @DisplayName("존재하는 상품이 아닐 경우 예외 발생")
    @Test
    void validateProducts_exception() {
        // given
        given(productRepository.countAllByIdIn(anyList())).willReturn(1);

        // when && then
        assertThatThrownBy(() -> menuValidator.validateProducts(requestMenuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 포함된 상품들의 금액에 합보다 작거나 같아야 한다")
    @Test
    void validateMenuProductsAmount_exception() {
        // given
        Menu menu = new Menu("메뉴명", BigDecimal.valueOf(26001), menuGroup, new MenuProducts(menuProducts));
        given(productRepository.findById(1L)).willReturn(Optional.ofNullable(초밥));
        given(productRepository.findById(2L)).willReturn(Optional.ofNullable(우동));

        // when && then
        assertThatThrownBy(() -> menuValidator.validateMenuProductsAmount(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
