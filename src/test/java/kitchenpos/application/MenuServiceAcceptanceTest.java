package kitchenpos.application;


import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import kitchenpos.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("메뉴 테스트")
class MenuServiceAcceptanceTest extends AcceptanceTest {

    MenuGroupResponse createdMenuGroup;
    ProductResponse createdProduct;
    Menu createdMenu;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createdResponse;

        ProductRequest productRequest = new ProductRequest("후라이드치킨", BigDecimal.valueOf(18000L));

        createdResponse = ProductFactory.상품_생성_요청(productRequest);
        createdProduct = ProductServiceAcceptanceTest.상품이_생성됨(createdResponse);

        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("치킨");
        ExtractableResponse<Response> createResponse = MenuGroupFactory.메뉴그룹_생성_요청(menuGroupRequest);

        createdResponse = MenuGroupFactory.메뉴그룹_생성_요청(menuGroupRequest);
        createdMenuGroup = MenuGroupServiceAcceptanceTest.메뉴그룹이_생성됨(createdResponse);

    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void createTest() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(createdProduct.getId(), 2L);
        MenuRequest menuRequest = new MenuRequest("후라이드치킨", BigDecimal.valueOf(18000L), createdMenuGroup.getId(), Arrays.asList(menuProductRequest));

        ExtractableResponse<Response> createResponse = MenuFactory.메뉴_생성_요청(menuRequest);
        createdMenu = 메뉴가_생성됨(createResponse);
    }

    @DisplayName("메뉴를 조회한다")
    @Test
    void getListTest() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(createdProduct.getId(), 2L);
        MenuRequest menuRequest = new MenuRequest("후라이드치킨", BigDecimal.valueOf(18000L), createdMenuGroup.getId(), Arrays.asList(menuProductRequest));


        ExtractableResponse<Response> createResponse = MenuFactory.메뉴_생성_요청(menuRequest);
        createdMenu = 메뉴가_생성됨(createResponse);

        ExtractableResponse<Response> getResponse = MenuFactory.메뉴_조회_요청();
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static Menu 메뉴가_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(Menu.class);
    }

}
