package kitchenpos.menu.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.ui.MenuGroupAcceptanceTestSupport;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.ui.ProductAcceptanceTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.math.BigDecimal;
import java.util.Arrays;

@DisplayName("메뉴 관련 기능")
class MenuAcceptanceTest extends MenuAcceptanceTestSupport {
    private MenuGroupResponse 중화메뉴;
    private ProductResponse 짬뽕;
    private ProductResponse 짜장면;

    @BeforeEach
    public void beforeEach() {
        중화메뉴 = MenuGroupAcceptanceTestSupport.메뉴_그룹_등록_되어있음("중화메뉴").as(MenuGroupResponse.class);
        짬뽕 = ProductAcceptanceTestSupport.상품_등록되어_있음("짬뽕", 7_000).as(ProductResponse.class);
        짜장면 = ProductAcceptanceTestSupport.상품_등록되어_있음("자장면", 5_000).as(ProductResponse.class);
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

    @DisplayName("메뉴 생성시, 가격이 전달되지 않았을때 예외 발생한다.")
    @ParameterizedTest
    @NullSource
    void exceptionToCreateMenu(BigDecimal price) {
        // Given
        MenuProductRequest 짬뽕_추가 = new MenuProductRequest(짬뽕.getId(), 1L);
        MenuProductRequest 짜장면_추가 = new MenuProductRequest(짜장면.getId(), 2L);
        MenuRequest params = new MenuRequest("짜장짬뽕세트", price,
                중화메뉴.getId(), Arrays.asList(짬뽕_추가, 짜장면_추가));

        // When
        ExtractableResponse<Response> createResponse = 메뉴_등록_요청(params);

        // Then
        잘못된_메뉴_가격_응답(createResponse);
    }
}
