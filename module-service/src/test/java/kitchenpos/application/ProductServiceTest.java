package kitchenpos.application;

import kitchenpos.AcceptanceTest;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class ProductServiceTest extends AcceptanceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createProduct() {
        // given
        ProductRequest productRequest = new ProductRequest("강정치킨", new BigDecimal(17000));

        // when
        ProductResponse productResponse = productService.create(productRequest);

        // then
        assertThat(productResponse.getId()).isNotNull();
        assertThat(productResponse.getName()).isEqualTo("강정치킨");
        assertThat(productResponse.getPrice()).isEqualByComparingTo(new BigDecimal(17000));
    }

    @DisplayName("상품 생성시 가격은 필수 정보이다.")
    @Test
    void createPriceNull() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            productService.create(new ProductRequest("강정치킨", null));
        }).withMessageMatching("가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("상품 가격은 0원 이상이어야 한다.")
    @Test
    void createPriceZero() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            productService.create(new ProductRequest("강정치킨", BigDecimal.valueOf(-100)));
        }).withMessageMatching("가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void findAllProducts() {
        //when
        List<ProductResponse> results = productService.findAll();

        //then
        assertThat(results).isNotEmpty();
        assertThat(results.stream()
                .map(ProductResponse::getName)
                .collect(Collectors.toList())).containsAll(Arrays.asList("양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨"));
    }
}
