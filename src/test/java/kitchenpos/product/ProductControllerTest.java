package kitchenpos.product;

import kitchenpos.application.ProductService;
import kitchenpos.common.ControllerTest;
import kitchenpos.domain.Product;
import kitchenpos.ui.ProductRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ProductRestController.class)
public class ProductControllerTest extends ControllerTest<Product> {

    private static final String BASE_URI = "/api/products";

    @MockBean
    private ProductService productService;

    @Autowired
    private ProductRestController productRestController;

    private Product 후라이드;
    private Product 양념치킨;

    @Override
    protected Object controller() {
        return productRestController;
    }

    @BeforeEach
    void 사전준비() {
        후라이드 = new Product();
        후라이드.setId(1L);
        후라이드.setName("후라이드");
        후라이드.setPrice(BigDecimal.valueOf(16000));

        양념치킨 = new Product();
        양념치킨.setId(2L);
        양념치킨.setName("양념치킨");
        양념치킨.setPrice(BigDecimal.valueOf(19000));
    }

    @DisplayName("상품 생성요청")
    @Test
    void 상품_생성요청() throws Exception {
        //Given
        when(productService.create(any())).thenReturn(후라이드);

        //When
        ResultActions 결과 = postRequest(BASE_URI, 후라이드);

        //Then
        생성성공(결과, 후라이드);
    }

    @DisplayName("메뉴그룹 목록 조회요청")
    @Test
    void 메뉴그룹_목록_조회요청() throws Exception {
        //Given
        List<Product> 상품_목록 = new ArrayList<>(Arrays.asList(후라이드, 양념치킨));
        when(productService.list()).thenReturn(상품_목록);

        //When
        ResultActions 결과 = getRequest(BASE_URI);

        //Then
        목록_조회성공(결과, 상품_목록);
    }
}
