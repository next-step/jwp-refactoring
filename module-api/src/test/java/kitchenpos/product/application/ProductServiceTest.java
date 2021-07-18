package kitchenpos.product.application;

import kitchenpos.product.ProductTestFixture;
import kitchenpos.product.ProductRepository;
import kitchenpos.product.Product;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("0원 이상의 가격으로 상품을 등록한다")
    @Test
    void 상품_등록() {
        //Given
        when(productRepository.save(any())).thenReturn(ProductTestFixture.아이스아메리카노);

        //When
        ProductResponse 생성된_상품 = productService.create(ProductTestFixture.아이스아메리카노_요청);

        //Then
        verify(productRepository, times(1)).save(any());
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void 상품_목록_조회() {
        //Given
        List<Product> 입력한_상품_목록 = new ArrayList<>(Arrays.asList(ProductTestFixture.아이스아메리카노, ProductTestFixture.에그맥머핀));
        when(productRepository.findAll()).thenReturn(입력한_상품_목록);

        //When
        List<ProductResponse> 조회된_상품_목록 = productService.list();

        //Then
        verify(productRepository,times(1)).findAll();
    }
}
