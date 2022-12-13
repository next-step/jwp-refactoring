package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import kitchenpos.domain.Product2;
import kitchenpos.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDao productDao;
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    @DisplayName("상품 등록")
    void testCreateProduct() {
        Product2 product = new Product2("스파게티", Money.valueOf(100));

        when(productRepository.save(any()))
                .thenReturn(product);

        Product2 createdProduct = productService.create(product);

        assertThat(product).isEqualTo(createdProduct);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("상품 목록")
    void testGetProductList() {
        // given
        List<Product2> expectedProducts = Lists.newArrayList(createProducts2(3));
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // when
        List<Product2> actualProducts = productService.list();

        // then
        assertThat(actualProducts).containsExactlyInAnyOrderElementsOf(expectedProducts);
        verify(productRepository, times(1)).findAll();
    }

    private List<Product2> createProducts2(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> new Product2("product"+i, Money.valueOf(1000)))
            .collect(Collectors.toList());
    }

    @Deprecated
    public static List<Product> createProducts(int ...prices) {
        return Arrays.stream(prices)
            .mapToObj(price -> {
                Product product = new Product();
                product.setId(ThreadLocalRandom.current().nextLong(1, 100));
                product.setName("상품");
                product.setPrice(BigDecimal.valueOf(price));
                return product;
            }).collect(Collectors.toList());
    }
}
