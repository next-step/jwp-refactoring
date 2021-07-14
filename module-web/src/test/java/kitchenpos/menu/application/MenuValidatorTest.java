package kitchenpos.menu.application;

import static kitchenpos.menu.application.MenuServiceTest.감자튀김;
import static kitchenpos.menu.application.MenuServiceTest.치즈버거;
import static kitchenpos.menu.application.MenuServiceTest.콜라;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.exception.MenuPriceExceedException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 유효성검증 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuValidator menuValidator;

    @DisplayName("메뉴의 가격은 메뉴상품들의 가격의합을 초과할 수 없다.")
    @Test
    void validationMenuProductPrices() {
        // Given
        List<Product> 상품목록 = new ArrayList<>(Arrays.asList(치즈버거, 감자튀김, 콜라));
        Price 상품_가격_합 = 상품목록.stream()
            .map(Product::getPrice)
            .reduce(new Price(0), Price::plus);
        Price 상품_가격을_초과한_가격 = 상품_가격_합.plus(Price.wonOf(1));
        given(productRepository.findAllById(any())).willReturn(상품목록);

        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        menuProductRequests.add(new MenuProductRequest(치즈버거.getId(), 1L));
        menuProductRequests.add(new MenuProductRequest(감자튀김.getId(), 1L));
        menuProductRequests.add(new MenuProductRequest(콜라.getId(), 1L));

        // When & Then
        assertThatThrownBy(() -> menuValidator.validationMenuProductPrices(상품_가격을_초과한_가격, menuProductRequests))
            .isInstanceOf(MenuPriceExceedException.class);

    }
}
