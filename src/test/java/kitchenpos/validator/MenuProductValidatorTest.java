package kitchenpos.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.exception.PriceNotAcceptableException;
import kitchenpos.common.vo.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.testfixtures.ProductTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuProductValidatorTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuProductValidator menuProductValidator;


    @DisplayName("메뉴에 속한 상품리스트가 없을 경우 메뉴 가격은 0원 이어야 한다.")
    @Test
    void validateMenuPriceIsLessThanMenuProductsSum1() {
        //given
        Price menuPrice = Price.valueOf(BigDecimal.valueOf(1));
        List<MenuProductRequest> menuProductRequests = new ArrayList<>();

        //when, then
        assertThatThrownBy(
            () -> menuProductValidator.validateMenuPriceIsLessThanMenuProductsSum(menuPrice,
                menuProductRequests))
            .isInstanceOf(PriceNotAcceptableException.class);
    }

    @DisplayName("메뉴 가격은 상품 리스트의 합보다 작거나 같아야 한다.")
    @Test
    void validateMenuPriceIsLessThanMenuProductsSum2() {
        //given
        Product 타코야끼 = new Product(1L, "타코야끼", Price.valueOf(BigDecimal.valueOf(12000)));
        Product 뿌링클 = new Product(2L, "뿌링클", Price.valueOf(BigDecimal.valueOf(15000)));
        List<MenuProductRequest> menuProductRequests = Arrays.asList(
            new MenuProductRequest(타코야끼.getId(), 3),
            new MenuProductRequest(뿌링클.getId(), 1));
        Price menuPrice = Price.valueOf(BigDecimal.valueOf(51001));
        ProductTestFixtures.상품_조회시_응답_모킹(productRepository, 타코야끼);
        ProductTestFixtures.상품_조회시_응답_모킹(productRepository, 뿌링클);

        //when, then
        assertThatThrownBy(
            () -> menuProductValidator.validateMenuPriceIsLessThanMenuProductsSum(menuPrice,
                menuProductRequests))
            .isInstanceOf(PriceNotAcceptableException.class);
    }
}
