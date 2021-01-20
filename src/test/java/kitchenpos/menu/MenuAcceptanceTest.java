package kitchenpos.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.menugroup.MenuGroupAcceptanceTestSupport;
import kitchenpos.product.ProductAcceptanceTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

@DisplayName("메뉴 관련 기능")
class MenuAcceptanceTest extends MenuAcceptanceTestSupport {
    private MenuGroup 중화메뉴;
    private Product 짬뽕;
    private Product 짜장면;

    @BeforeEach
    public void beforeEach() {
        중화메뉴 = MenuGroupAcceptanceTestSupport.메뉴_그룹_등록_되어있음("중화메뉴").as(MenuGroup.class);
        짬뽕 = ProductAcceptanceTestSupport.상품_등록되어_있음("짬뽕", 7_000).as(Product.class);
        짜장면 = ProductAcceptanceTestSupport.상품_등록되어_있음("자장면", 5_000).as(Product.class);
    }

    @DisplayName("메뉴 생성")
    @Test
    void createMenu() {
        // Given
        MenuProductRequest 짬뽕_추가 = new MenuProductRequest(짬뽕.getId(), 1L);
        MenuProductRequest 짜장면_추가 = new MenuProductRequest(짜장면.getId(), 2L);
        MenuRequest params = new MenuRequest("짜장짬뽕세트", BigDecimal.valueOf(17_000),
                중화메뉴.getId(), Arrays.asList(짬뽕_추가, 짜장면_추가));

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
