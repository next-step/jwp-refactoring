package kitchenpos.menu.validator;

import kitchenpos.menu.dto.CreateMenuRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    MenuValidator menuValidator;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("메뉴 상품 validation 정상")
    void validate() {
        // given
        final Product 불고기버거 = new Product(1L, "불고기버거", BigDecimal.valueOf(1_500));
        final Product 새우버거 = new Product(2L, "새우버거", BigDecimal.valueOf(2_000));
        final CreateMenuRequest 햄버거메뉴 = new CreateMenuRequest("햄버거메뉴", BigDecimal.valueOf(1_500), 1L,
                Arrays.asList(new MenuProductRequest(불고기버거.getId(), 1), (new MenuProductRequest(새우버거.getId(), 1))));
        given(productRepository.findAllById(any()))
                .willReturn(Arrays.asList(불고기버거, 새우버거));
        // when
        menuValidator.validate(햄버거메뉴);
    }

    @Test
    @DisplayName("메뉴 상품 개수 상이 오류")
    void exception1() {
        // given
        final Product 불고기버거 = new Product(1L, "불고기버거", BigDecimal.valueOf(1_500));
        final Product 새우버거 = new Product(2L, "새우버거", BigDecimal.valueOf(2_000));
        final CreateMenuRequest 햄버거메뉴 = new CreateMenuRequest("햄버거메뉴", BigDecimal.valueOf(1_500), 1L,
                Arrays.asList(new MenuProductRequest(불고기버거.getId(), 1)));
        given(productRepository.findAllById(any()))
                .willReturn(Arrays.asList(불고기버거, 새우버거));
        // when
        assertThatThrownBy(() -> menuValidator.validate(햄버거메뉴))
                .isInstanceOf(ProductException.class);
    }

    @Test
    @DisplayName("메뉴 상품 개수 금액 초과 오류")
    void exception2() {
        // given
        final Product 불고기버거 = new Product(1L, "불고기버거", BigDecimal.valueOf(1_500));
        final Product 새우버거 = new Product(2L, "새우버거", BigDecimal.valueOf(2_000));
        final CreateMenuRequest 햄버거메뉴 = new CreateMenuRequest("햄버거메뉴", BigDecimal.valueOf(5_500), 1L,
                Arrays.asList(new MenuProductRequest(불고기버거.getId(), 1), (new MenuProductRequest(새우버거.getId(), 1))));
        given(productRepository.findAllById(any()))
                .willReturn(Arrays.asList(불고기버거, 새우버거));
        // when
        assertThatThrownBy(() -> menuValidator.validate(햄버거메뉴))
                .isInstanceOf(MenuException.class);
    }
}
