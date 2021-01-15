package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관리")
public class MenuAcceptanceTest extends AcceptanceTest {
    @DisplayName("메뉴를 관리한다")
    @Test
    void manage() {
        //given
        MenuGroup menuGroup = MenuGroupAcceptanceTest.생성_요청(MenuGroupAcceptanceTest.createRequest())
                .as(MenuGroup.class);
        Product product = ProductAcceptanceTest.생성_요청(ProductAcceptanceTest.createRequest())
                .as(Product.class);

        //when
        Menu request = createRequest(menuGroup, product);
        ExtractableResponse<Response> createdResponse = 생성_요청(request);
        //then
        생성됨(createdResponse, request);
        //when
        ExtractableResponse<Response> selectedResponse = 조회_요청();
        //then
        조회됨(selectedResponse);
    }

    public static Menu createRequest(MenuGroup menuGroup, Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);

        Menu request = new Menu();
        request.setName("후라이드+후라이드");
        request.setPrice(new BigDecimal(19_000));
        request.setMenuGroupId(menuGroup.getId());
        request.setMenuProducts(menuProducts);

        return request;
    }

    public static ExtractableResponse<Response> 생성_요청(Menu request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 생성됨(ExtractableResponse<Response> response, Menu request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Menu menu = response.as(Menu.class);
        assertThat(menu.getName()).isEqualTo(request.getName());
        assertThat(menu.getPrice().intValue()).isEqualTo(request.getPrice().intValue());
        assertThat(menu.getMenuGroupId()).isEqualTo(request.getMenuGroupId());
    }

    public static ExtractableResponse<Response> 조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Menu> menu = Arrays.asList(response.as(Menu[].class));
        assertThat(menu.size()).isEqualTo(1);
    }
}
