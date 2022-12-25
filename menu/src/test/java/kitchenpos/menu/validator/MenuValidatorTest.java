package kitchenpos.menu.validator;

import static org.mockito.ArgumentMatchers.eq;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    Product 개손해치킨;
    Product 강정치킨;
    MenuProduct 더블강정치킨상품;
    MenuProduct 더블개손해치킨상품;
    MenuGroup 추천메뉴;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuValidator menuValidator;

    @BeforeEach
    void setup() {
        개손해치킨 = new Product(2L, "개손해치킨", new BigDecimal(1));
        강정치킨 = new Product(1L, "강정치킨", new BigDecimal(17_000));
        더블강정치킨상품 = new MenuProduct(1L, null, 강정치킨.getId(), 2L);
        더블개손해치킨상품 = new MenuProduct(1L, null, 개손해치킨.getId(), 2L);
        추천메뉴 = new MenuGroup(1L, "추천메뉴");
    }

    @Test
    @DisplayName("메뉴의 가격이 상품들의 가격 합보다 크면 안된다")
    void validateSumOfMenuPrice() {
        //given
        BDDMockito.when(productRepository.findById(eq(개손해치킨.getId())))
            .thenReturn(Optional.of(개손해치킨));
        BDDMockito.when(productRepository.findById(eq(강정치킨.getId())))
            .thenReturn(Optional.of(강정치킨));
        Menu 개손해상품 = new Menu(1L, "더블강정치킨", new BigDecimal(199_000), 추천메뉴,
            Arrays.asList(더블강정치킨상품, 더블개손해치킨상품));

        //when & then
        Assertions
            .assertThatThrownBy(() -> menuValidator.validate(개손해상품))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴의 가격이 상품들의 가격 합보다 크면 안된다");

    }

}