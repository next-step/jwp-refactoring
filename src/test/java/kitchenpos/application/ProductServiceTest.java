package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
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
import static org.junit.jupiter.api.Assertions.*;
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
        Product expected = new Product("상품", new BigDecimal(1000));
        given(productRepository.save(any())).willReturn(expected);

        Product saved = service.create(expected);

        assertThat(saved).isEqualTo(expected);
    }

    @DisplayName("상품의 가격이 0 이하 이면 Exception")
    @Test
    void addProductPriceExceptionTest() {
        Product expected = new Product("상품", new BigDecimal(-10));

        assertThrows(IllegalArgumentException.class,
                () -> service.create(expected));
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void findAllProductTest() {
        Product expected1 = new Product("상품1", new BigDecimal(10));
        Product expected2 = new Product("상품2", new BigDecimal(10));
        given(productRepository.findAll()).willReturn(
                Arrays.asList(expected1, expected2)
        );

        List<Product> results = service.list();

        assertThat(results).contains(expected1, expected2);
    }
}