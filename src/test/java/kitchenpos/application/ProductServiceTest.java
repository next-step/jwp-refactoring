package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 클래스 테스트")
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @Nested
    @DisplayName("create 메서드 테스트")
    public class CreateMethod {
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, Integer.MAX_VALUE})
        @DisplayName("새로운 상품 생성 성공")
        public void createSuccess(int priceValue) {
            // given
            final String name = "test product";
            final Product mockProduct = setupCreate(name, priceValue);

            // given
            final Product product = new Product();
            product.setName(name);
            product.setPrice(BigDecimal.valueOf(priceValue));

            // when
            final Product createdProduct = productService.create(product);

            // then
            assertAll(
                    () -> assertThat(createdProduct.getName()).isEqualTo(mockProduct.getName()),
                    () -> assertThat(createdProduct.getPrice()).isEqualTo(mockProduct.getPrice())
            );
        }

        private Product setupCreate(String name, int price) {
            final Product mockProduct = new Product();
            mockProduct.setName(name);
            mockProduct.setPrice(BigDecimal.valueOf(price));
            Mockito.when(productDao.save(Mockito.any())).thenReturn(mockProduct);
            return mockProduct;
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("가격이 없으면 상품 등록 실패")
        public void createFailure_priceNull(BigDecimal bigDecimal) {
            // given
            final Product product = new Product();
            product.setPrice(bigDecimal);

            // when - then
            assertThrows(IllegalArgumentException.class, () -> productService.create(product));
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -2, -1})
        @DisplayName("가격이 음수이면 상품 등록 실패")
        public void createFailure_priceNegative(int priceValue) {
            // given
            final Product product = new Product();
            product.setPrice(BigDecimal.valueOf(priceValue));

            // when - then
            assertThrows(IllegalArgumentException.class, () -> productService.create(product));
        }
    }


    @Nested
    @DisplayName("list 테스트")
    public class ListMethod {
        private void setup(List<Product> mockProducts) {
            Mockito.when(productDao.findAll()).thenReturn(mockProducts);
        }

        @Test
        @DisplayName("리스트 조회 성공")
        public void listSuccess() {
            // given
            final List<Product> mockProducts = Arrays.asList(new Product(), new Product(), new Product());
            setup(mockProducts);

            // when
            final List<Product> products = productService.list();

            // then
            assertThat(products.size()).isEqualTo(mockProducts.size());
        }

        @Test
        @DisplayName("빈 리스트 조회 성공")
        public void listSuccessEmptyList() {
            // given
            final List<Product> mockProducts = Collections.emptyList();
            setup(mockProducts);

            // when
            final List<Product> products = productService.list();

            // then
            assertThat(products.size()).isEqualTo(0);
        }
    }
}
