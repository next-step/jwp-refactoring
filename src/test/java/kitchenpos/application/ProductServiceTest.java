package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    public void createProduct() {
        //given
        Product given = new Product("메뉴", BigDecimal.valueOf(1000));
        when(productRepository.save(any())).thenReturn(given);
        //when
        Product result = productService.create(given);
        //then
        assertThat(result).isNotNull();
    }


    @DisplayName("상품 가격이 0보다 작으면 에러.")
    @Test
    public void createProductMinusPrice() {
        //given
        Product given = new Product("메뉴", BigDecimal.valueOf(-1));
        //when
        //then
        assertThatThrownBy(() -> productService.create(given)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 조회한다.")
    @Test
    public void getProducts() {
        //given
        Product given = new Product("메뉴", BigDecimal.valueOf(1000));
        Product given2 = new Product("메뉴2", BigDecimal.valueOf(2000));
        when(productRepository.findAll()).thenReturn(Arrays.asList(given, given2));
        //when
        List<Product> result = productService.list();
        //then
        assertThat(result).hasSize(2);
    }
}
