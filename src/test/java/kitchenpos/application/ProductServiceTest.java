package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.CommonTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductDao productDao;
    @InjectMocks
    ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create_success() {
        // given
        Product 후라이드싸이순살 = createProduct(1L, "후라이드싸이순살", 20_000);
        given(productDao.save(any(Product.class))).willReturn(후라이드싸이순살);

        // when
        Product saved = productService.create(후라이드싸이순살);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved).isEqualTo(후라이드싸이순살),
                () -> assertThat(saved.getName()).isEqualTo(후라이드싸이순살.getName()),
                () -> assertThat(saved.getPrice()).isEqualTo(후라이드싸이순살.getPrice())
        );
    }

    @DisplayName("상품 등록에 실패한다. (상품 가격이 0 원 미만인 경우)")
    @Test
    void create_fail() {
        // when
        Product 후라이드싸이순살 = createProduct(1L, "후라이드싸이순살", -1);

        // then
        assertThatThrownBy(() -> {
            productService.create(후라이드싸이순살);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        Product 후라이드싸이순살 = createProduct(1L, "후라이드싸이순살", 20_000);
        Product 블랙쏘이치킨 = createProduct(2L, "블랙쏘이치킨", 18_000);
        given(productDao.findAll()).willReturn(Arrays.asList(후라이드싸이순살, 블랙쏘이치킨));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).containsExactly(후라이드싸이순살, 블랙쏘이치킨);
    }
}
