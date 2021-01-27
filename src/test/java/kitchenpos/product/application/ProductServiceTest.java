package kitchenpos.product.application;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductService service;

    @DisplayName("상품 등록하기")
    @Test
    void addProductTest() {
        Product expected = new Product("상품", Price.of(1000));
        given(productRepository.save(any())).willReturn(expected);

        Product saved = service.create(expected);

        assertThat(saved).isEqualTo(expected);
    }
    
    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void findAllProductTest() {
        Product expected1 = new Product("상품1", Price.of(10));
        Product expected2 = new Product("상품2", Price.of(10));
        given(productRepository.findAll()).willReturn(
                Arrays.asList(expected1, expected2)
        );

        List<Product> results = service.list();

        assertThat(results).contains(expected1, expected2);
    }
}