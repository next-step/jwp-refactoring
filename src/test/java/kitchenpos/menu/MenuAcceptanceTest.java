package kitchenpos.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.menugroup.MenuGroupAcceptanceTestSupport;
import kitchenpos.product.ProductAcceptanceTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@DisplayName("메뉴 관련 기능")
class MenuAcceptanceTest extends MenuAcceptanceTestSupport {
    private MenuGroup 중화메뉴;
    private Product 짬뽕;
    private Product 짜장면;

    @BeforeEach
    public void beforeEach() {
        중화메뉴 = MenuGroupAcceptanceTestSupport.메뉴_그룹_등록됨("중화메뉴").as(MenuGroup.class);
        짬뽕 = ProductAcceptanceTestSupport.상품_등록되어_있음("짬뽕", 7_000).as(Product.class);
        짜장면 = ProductAcceptanceTestSupport.상품_등록되어_있음("자장면", 5_000).as(Product.class);
    }

    @DisplayName("메뉴 생성")
    @Test
    void createMenu() {
        // Given
        Map<String, Object> 짬뽕_추가 = new HashMap<>();
        짬뽕_추가.put("productId", 짬뽕.getId());
        짬뽕_추가.put("quantity", 1);
        Map<String, Object> 짜장면_추가 = new HashMap<>();
        짜장면_추가.put("productId", 짜장면.getId());
        짜장면_추가.put("quantity", 2);
        Map<String, Object> params = new HashMap<>();
        params.put("name", "짜장짬뽕세트");
        params.put("price", 17_000);
        params.put("menuGroupId", 중화메뉴.getId());
        params.put("menuProducts", Arrays.asList(짬뽕_추가, 짜장면_추가));

        // When
        ExtractableResponse<Response> createResponse = 메뉴_등록_요청(params);

        // Then
        메뉴_생성_완료(createResponse);
    }

    @DisplayName("모든 메뉴 목록 조회")
    @Test
    void findMenus() {
        // When
        ExtractableResponse<Response> findResponse = 메뉴_목록_조회_요청();

        // Then
        메뉴_목록_응답(findResponse);
    }
}
