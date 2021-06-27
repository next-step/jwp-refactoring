package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 클래스")
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {

        @Nested
        @DisplayName("가격이 포함된 상품이 주어지면")
        class Context_with_product_with_price {
            Product givenProduct = new Product();
            BigDecimal givenPrice = BigDecimal.valueOf(1000);

            @BeforeEach
            void setUp() {
                givenProduct.setPrice(givenPrice);
                when(productDao.save(any(Product.class)))
                        .thenReturn(givenProduct);
            }

            @Test
            @DisplayName("주어진 상품을 저장하고, 저장된 객체를 리턴한다.")
            void it_returns_saved_product() {
                Product actual = productService.create(givenProduct);

                assertThat(actual).isEqualTo(givenProduct);
            }
        }

        @Nested
        @DisplayName("가격이 없는 상품이 주어지면")
        class Context_with_product_with_no_price {
            Product givenProduct = new Product();

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> productService.create(givenProduct))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("0보다 작은 가격의 상품이 주어지면")
        class Context_with_product_with_negative_price {
            Product givenProduct = new Product();
            BigDecimal givenPrice = BigDecimal.valueOf(-1000);

            @BeforeEach
            void setUp() {
                givenProduct.setPrice(givenPrice);
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> productService.create(givenProduct))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
    
    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {

        @Nested
        @DisplayName("저장된 상품 목록이 주어지면")
        class Context_with_products {
            final Product givenProduct1 = new Product();
            final Product givenProduct2 = new Product();

            @BeforeEach
            void setUp() {
                when(productDao.findAll())
                        .thenReturn(Arrays.asList(givenProduct1, givenProduct2));
            }

            @Test
            @DisplayName("상품 목록을 리턴한다.")
            void it_returns_products() {
                List<Product> actual = productService.list();
                assertThat(actual).containsExactly(givenProduct1, givenProduct2);
            }
        }
    }
}
