package kitchenpos.menu.application;

import kitchenpos.exception.InvalidPriceException;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        Product 강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        given(productRepository.save(any())).willReturn(강정치킨);

        // when
        ProductResponse createdProduct = productService.create(new ProductRequest("강정치킨", BigDecimal.valueOf(17000)));

        // then
        Assertions.assertThat(createdProduct.getId()).isEqualTo(강정치킨.getId());
        Assertions.assertThat(createdProduct.getName()).isEqualTo(강정치킨.getName());
        Assertions.assertThat(createdProduct.getPrice()).isEqualTo(강정치킨.valueOfPrice());
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다 : 상품의 가격은 0 원 이상이어야 한다.")
    @Test
    void createTest_wrongPrice() {
        // when & then
        assertThatThrownBy(() -> productService.create(new ProductRequest("후라이드", BigDecimal.valueOf(-1000))))
                .isInstanceOf(InvalidPriceException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        Product 강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        Product 후라이드 = new Product(2L, "후라이드", BigDecimal.valueOf(16000));
        given(productRepository.findAll()).willReturn(Arrays.asList(강정치킨, 후라이드));

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertAll(
                () -> Assertions.assertThat(products).hasSize(2),
                () -> Assertions.assertThat(products.get(0).getId()).isEqualTo(강정치킨.getId()),
                () -> Assertions.assertThat(products.get(0).getName()).isEqualTo(강정치킨.getName()),
                () -> Assertions.assertThat(products.get(0).getPrice()).isEqualTo(강정치킨.valueOfPrice()),
                () -> Assertions.assertThat(products.get(1).getId()).isEqualTo(후라이드.getId()),
                () -> Assertions.assertThat(products.get(1).getName()).isEqualTo(후라이드.getName()),
                () -> Assertions.assertThat(products.get(1).getPrice()).isEqualTo(후라이드.valueOfPrice())
        );
    }
}
