package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductFactory;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    public void create() {
        //given
        Product 강정치킨 = ProductFactory.create(1L, "강정치킨", BigDecimal.valueOf(17000));
        given(productRepository.save(any())).willReturn(강정치킨);

        //when
        productService.create(강정치킨);

        //then
        then(productRepository).should().save(any());
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        Product 강정치킨 = ProductFactory.create(1L, "강정치킨", BigDecimal.valueOf(17000));
        Product 양념치킨 = ProductFactory.create(2L, "양념치킨", BigDecimal.valueOf(19000));

        given(productRepository.findAll()).willReturn(Arrays.asList(강정치킨, 양념치킨));

        //when
        List<ProductResponse> result = productService.list();

        //then
        List<Long> actual = result.stream()
                .map(ProductResponse::getId)
                .collect(Collectors.toList());

        assertThat(actual).containsExactly(강정치킨.getId(), 양념치킨.getId());
    }
}
