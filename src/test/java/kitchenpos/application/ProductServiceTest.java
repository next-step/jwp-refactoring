package kitchenpos.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품을 등록")
    @Test
    void create1() {
        //given
        ProductRequest newProduct = new ProductRequest(null, "김치찌개", new BigDecimal(6000));
        // TODO: 임시로 any() 로 돌려놓음.
//        Product product = new Product(null, "김치찌개", new BigDecimal(6000));
        given(productRepository.save(any()))
                .willReturn(new Product(2L, "김치찌개", new BigDecimal(6000)));

        //when
        ProductResponse createProduct = productService.create(newProduct);

        //then
        assertThat(createProduct.getId()).isEqualTo(2L);
        assertThat(createProduct.getName()).isEqualTo("김치찌개");
        assertThat(createProduct.getPrice()).isEqualTo(new BigDecimal(6000));
    }

    @DisplayName("상품을 등록 - 상품의 가격은 0 원 이상이어야 한다.")
    @Test
    void create2() {
        //given
        ProductRequest newProduct = new ProductRequest(null, "김치찌개", new BigDecimal(-1));

        //when
        //then
        assertThatThrownBy(() -> productService.create(newProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품의 가격은 0 원 이상이어야 합니다.");
    }

    @DisplayName("상품을 등록 - 상품의 가격이 지정되어 있어야 한다.")
    @Test
    void create3() {
        //given
        ProductRequest newProduct = new ProductRequest(null, "김치찌개", null);

        //when
        //then
        assertThatThrownBy(() -> productService.create(newProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품의 가격은 0 원 이상이어야 합니다.");
    }

    @DisplayName("상품의 목록을 조회할 수 있다")
    @Test
    void list() {
        //given
        given(productRepository.findAll())
                .willReturn(
                        Arrays.asList(
                                new Product(1L, "볶음밥", new BigDecimal(7000)),
                                new Product(2L, "김치찌개", new BigDecimal(6000))
                        )
                );

        //when
        List<ProductResponse> products = productService.list();

        //then
        assertThat(products.size()).isEqualTo(2);
        assertThat(products.get(0).getName()).isEqualTo("볶음밥");
        assertThat(products.get(1).getName()).isEqualTo("김치찌개");
    }
}
