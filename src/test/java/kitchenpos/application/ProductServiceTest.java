package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("create - 가격이 비어있거나, 0보다 적을경우 IllegalArgumentException이 발생한다.")
    void 가격이_비어있거나_0보다_적을경우_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("create - 정상적인 상품 등록")
    void 정상적인_상품_등록() {

    }

    @Test
    @DisplayName("list - 정상적인 상품 전체 조회")
    void 정상적인_상품_전체_조회() {

    }
}