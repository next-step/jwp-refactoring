package kitchenpos.menu.domain;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Optional;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    @DisplayName("메뉴 그룹은 등록되어 있어야 한다.")
    @Test
    void validateMenuGroup() {
        Menu menu = new Menu("두마리치킨", BigDecimal.valueOf(15000), 1000L);
        given(menuGroupRepository.existsById(1000L)).willReturn(false);

        assertThatThrownBy(() -> menuValidator.validate(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    @DisplayName("메뉴 가격은 상품 가격의 총합보다 작아야 한다.")
    @Test
    void validatePrice() {
        Menu menu = new Menu("두마리치킨", BigDecimal.valueOf(35000), 1L);
        menu.addMenuProduct(new MenuProduct(1L, 2));
        given(menuGroupRepository.existsById(1L)).willReturn(true);
        given(productRepository.findById(1L)).willReturn(Optional.of(new Product("후라이드치킨", BigDecimal.valueOf(15000))));

        assertThatThrownBy(() -> menuValidator.validate(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 메뉴 상품 가격의 총합보다 클 수 없습니다.");
    }

    @DisplayName("상품은 등록되어 있어야 한다.")
    @Test
    void validateProduct() {
        Menu menu = new Menu("두마리치킨", BigDecimal.valueOf(25000), 1L);
        menu.addMenuProduct(new MenuProduct(1000L, 2));
        given(menuGroupRepository.existsById(1L)).willReturn(true);
        given(productRepository.findById(1000L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> menuValidator.validate(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }
}
