package kitchenpos.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    Product 후라이드;
    Product 양념치킨;
    MenuRequest 상품;

    @BeforeEach
    void setUp() {
        createProduct();
    }

    void createProduct() {
        후라이드 = new Product("후라이드", BigDecimal.valueOf(15000));
        양념치킨 = new Product("양념치킨", BigDecimal.valueOf(15000));

        상품 = new MenuRequest("후라이드치킨", BigDecimal.valueOf(15000), 1L,
                Collections.singletonList(new MenuProductRequest(1L, 1L)));
    }

    @Test
    @DisplayName("상품을 저장한다")
    void create() {
        // given
        given(productRepository.save(any())).willReturn(후라이드);
        ProductRequest 상품 = new ProductRequest("후라이드", BigDecimal.valueOf(15000));

        // when
        ProductResponse actual = productService.create(상품);

        // then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo("후라이드"),
                () -> assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(15000))
        );
    }

    @Test
    @DisplayName("상품 정보 저장시 상품의 금액은 0원 이상이다")
    void create_priceError() {
        // given
        ProductRequest 상품 = new ProductRequest("후라이드", BigDecimal.valueOf(-1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> productService.create(상품)
        );
    }

    @Test
    @DisplayName("상품 리스트를 조회한다")
    void list() {
        // given
        given(productRepository.findAll()).willReturn(Arrays.asList(후라이드, 양념치킨));

        // when
        List<ProductResponse> actual = productService.list();

        // then
        assertThat(actual).hasSize(2);
    }


    @Test
    @DisplayName("메뉴 저장시 메뉴는 존재하는 상품 정보를 가져야 한다")
    void create_nonProductInfoError() {
        // given
        given(productRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> productService.validPriceCheck(상품)
        ).withMessageContaining("존재하지 않는 상품입니다.");
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴상품에 속한 상품들의 금액 합보다 메뉴 가격이 작아야 한다")
    void create_totalPriceError() {
        // given
        상품 = new MenuRequest("후라이드치킨", BigDecimal.valueOf(20000), 1L,
                Collections.singletonList(new MenuProductRequest(1L, 1L)));
        given(productRepository.findById(any())).willReturn(Optional.ofNullable(후라이드));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> productService.validPriceCheck(상품)
        ).withMessageContaining("메뉴의 금액은 상품의 합 보다 작아야합니다.");
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴의 금액은 0원 이상이다")
    void create_priceException() {
        // given
        상품 = new MenuRequest("후라이드치킨", BigDecimal.valueOf(-1), 1L,
                Collections.singletonList(new MenuProductRequest(1L, 1L)));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> productService.validPriceCheck(상품)
        );
    }
}
