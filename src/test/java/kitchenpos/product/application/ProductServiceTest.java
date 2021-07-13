package kitchenpos.product.application;

import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 후라이드 = new Product("후라이드", Price.valueOf(16000));
    private Product 양념치킨 = new Product("양념치킨", Price.valueOf(19000));

    @DisplayName("0원 이상의 가격으로 상품을 등록한다")
    @Test
    void 상품_등록() {
        //Given
        when(productRepository.save(any())).thenReturn(후라이드);

        //When
        ProductResponse 생성된_상품 = productService.create(후라이드);

        //Then
        verify(productRepository, times(1)).save(any());
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void 상품_목록_조회() {
        //Given
        List<Product> 입력한_상품_목록 = new ArrayList<>(Arrays.asList(후라이드, 양념치킨));
        when(productRepository.findAll()).thenReturn(입력한_상품_목록);

        //When
        List<ProductResponse> 조회된_상품_목록 = productService.list();

        //Then
        verify(productRepository,times(1)).findAll();
    }
}
