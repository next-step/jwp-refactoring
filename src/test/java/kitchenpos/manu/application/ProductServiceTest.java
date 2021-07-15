package kitchenpos.manu.application;

import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.wrap.Price;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("상품 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    private ProductRequest 치킨_요청;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 테스트")
    void 상품_등록_테스트() {
        // given
        치킨_요청 = 상품_요청("치킨", 17000);
        Product 치킨 = 상품_생성("치킨", 17000);
        when(productRepository.save(치킨_요청.toProduct())).thenReturn(치킨);

        // when
        // 메뉴 그룹을 등록 요청
        ProductResponse expected = productService.create(치킨_요청);

        // then
        // 메뉴 그릅 등록 됨
        assertThat(expected.getName()).isEqualTo(치킨.getName());
    }

    @Test
    @DisplayName("상품 가격 오류 테스트")
    void 상품_가격_오류_테스트() {
        // given
        // 잘못된 가격을 요청함
        치킨_요청 = 상품_요청("치킨", -17000);

        // than
        // 예외 발생
        assertThatThrownBy(() -> productService.create(치킨_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 리스트 조회 테스트")
    void 상품_리스트_조회_테스트() {
        // when
        // 메뉴 그룹 리스트 조회 요청 함
        Product 치킨 = 상품_생성("치킨", 17000);
        Product 콜라 = 상품_생성("콜라", 1000);
        when(productRepository.findAll()).thenReturn(Arrays.asList(치킨, 콜라));
        List<ProductResponse> expected = productService.list();

        // then
        // 메뉴 그릅 등록 됨
        assertThat(expected.size()).isEqualTo(2);
        assertThat(expected).containsAll(Arrays.asList(ProductResponse.of(치킨), ProductResponse.of(콜라)));
    }

    private ProductRequest 상품_요청(String name, int price) {
        return new ProductRequest(name, BigDecimal.valueOf(price));
    }

    public static Product 상품_생성(String name, int price) {
        return new Product(name, new Price(BigDecimal.valueOf(price)));
    }
}
