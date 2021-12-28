package kitchenpos.product.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.infra.ProductRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품은 이름과 가격을 통해 생성 할 수 있다.")
    @Test
    void create() {
        // given
        ProductRequest request = getCreateRequest("눈내리는치킨", 17_000);
        Product expected = getProduct(1L, "눈내리는치킨", 17_000);
        given(productRepository.save(any(Product.class))).willReturn(expected);
        // when
        ProductResponse actual = productService.create(request);
        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice().intValue()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }


    @DisplayName("상품을 생성할 수 없는 경우")
    @Nested
    class CreateFailTest {

        @DisplayName("가격이 존재 하지 않는 경우 생성할 수 없다.")
        @Test
        void createByEmptyPrice() {
            // given
            ProductRequest request = getCreateRequest("쌀국수", null);
            // when
            ThrowableAssert.ThrowingCallable createCall = () -> productService.create(request);
            // then
            assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
        }


        @DisplayName("가격이 0 미만 일 경우 생성할 수 없다.")
        @Test
        void createByZeroMoreLessPrice() {
            // given
            ProductRequest request = getCreateRequest("쌀국수", -121);
            // when
            ThrowableAssert.ThrowingCallable createCall = () -> productService.create(request);
            // then
            assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Product 눈내리는치킨 = getProduct(1L, "눈내리는치킨", 17_000);
        final Product 쌀국수 = getProduct(2L, "쌀국수", 7_000);
        final List<Product> expected = Arrays.asList(눈내리는치킨, 쌀국수);
        given(productRepository.findAll()).willReturn(expected);
        // when
        List<ProductResponse> list = productService.list();
        // then
        assertThat(list).containsExactlyElementsOf(Arrays.asList(ProductResponse.of(눈내리는치킨), ProductResponse.of(쌀국수)));
    }

    public static Product getProduct(Long id, String name, int price) {
        final Product expected = Product.generate(id, name, price);
        return expected;
    }

    private ProductRequest getCreateRequest(String name, int price) {
        return getCreateRequest(name, BigDecimal.valueOf(price));
    }

    private ProductRequest getCreateRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }
}
