package kitchenpos.product.ui;

import kitchenpos.product.application.ProductService;
import kitchenpos.common.ui.ControllerTest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.product.ProductTestFixture.에그맥머핀_응답;
import static kitchenpos.product.ProductTestFixture.아이스아메리카노_응답;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ProductRestController.class)
public class ProductControllerTest extends ControllerTest<ProductResponse> {

    private static final String BASE_URI = "/api/products";

    @MockBean
    private ProductService productService;

    @Autowired
    private ProductRestController productRestController;

    @Override
    protected Object controller() {
        return productRestController;
    }

    @DisplayName("상품 생성요청")
    @Test
    void 상품_생성요청() throws Exception {
        //Given
        when(productService.create(any())).thenReturn(아이스아메리카노_응답);

        //When
        ResultActions 결과 = postRequest(BASE_URI, 아이스아메리카노_응답);

        //Then
        생성성공(결과);
    }

    @DisplayName("메뉴그룹 목록 조회요청")
    @Test
    void 메뉴그룹_목록_조회요청() throws Exception {
        //Given
        List<ProductResponse> 상품_목록 = new ArrayList<>(Arrays.asList(아이스아메리카노_응답, 에그맥머핀_응답));
        when(productService.list()).thenReturn(상품_목록);

        //When
        ResultActions 결과 = getRequest(BASE_URI);

        //Then
        조회성공(결과);
    }
}
