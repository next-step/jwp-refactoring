package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    ProductRepository productRepository;

    MenuValidator menuValidator;

    @DisplayName("메뉴 가격은 제품 가격의 합보다 비쌀 수 없다.")
    @Test
    void validate() {
        // given
        Product product = new Product(1L, "칠리 스테이크", new BigDecimal("10000"));
        MenuProduct menuProduct = new MenuProduct(1L, null, product.getId(), 1);
        Menu menu = new Menu(1L, "스테이크", new BigDecimal("15000"), new MenuGroup("고기류"), Collections.singletonList(menuProduct));

        when(productRepository.findAllById(any())).thenReturn(Collections.singletonList(product));
        menuValidator = new MenuValidator(productRepository);

        // when. then
        assertThatThrownBy(() -> {
            menuValidator.validate(menu);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
