package kitchenpos.product.application;

import static kitchenpos.product.domain.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        Product given = 양념치킨;
        given(productRepository.save(any(Product.class))).willReturn(given);

        ProductResponse actual = productService.create(new ProductRequest(given.getName(), given.getPrice()));

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(given.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(given.getPrice())
        );
    }

    @DisplayName("상품명이 비어있거나 공백인 상품은 등록할 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createWithNameIsNullOrEmpty(String name) {
        ProductRequest given = new ProductRequest(name, BigDecimal.valueOf(18000));

        assertThatThrownBy(() -> productService.create(given))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품명은 비어있거나 공백일 수 없습니다.");
    }

    @DisplayName("가격이 존재하지 않는 상품은 등록할 수 없다.")
    @Test
    void createWithPriceIsNull() {
        ProductRequest given = new ProductRequest("가격_미존재_상품", null);

        assertThatThrownBy(() -> productService.create(given))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 필수 값 입니다.");
    }

    @DisplayName("가격이 0원 이하인 상품은 등록할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-1000, -2000})
    void createWithPriceNegative(int price) {
        ProductRequest given = new ProductRequest("가격이_음수인_상품", BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(given))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이하 일 수 없습니다.");
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        List<Product> expectedProducts = Arrays.asList(양념치킨, 후라이드치킨);
        given(productRepository.findAll()).willReturn(expectedProducts);

        List<ProductResponse> actual = productService.list();

        assertThat(actual).hasSize(2);
        assertThat(actual.stream().map(ProductResponse::getName)).contains(양념치킨.getName(), 후라이드치킨.getName());
    }
}
