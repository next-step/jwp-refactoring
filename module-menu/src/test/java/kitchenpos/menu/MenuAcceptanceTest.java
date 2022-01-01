package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menuGroup.MenuGroupAcceptanceTest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import kitchenpos.product.ProductAcceptanceTest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    @Test
    @DisplayName("메뉴를 관리한다.")
    void createMenu() {
        ProductResponse product = ProductAcceptanceTest.상품_등록_요청("짜장면", new BigDecimal(5000)).as(ProductResponse.class);
        MenuGroupResponse menuGroup = MenuGroupAcceptanceTest.메뉴_그룹_등록_요청("중국음식").as(MenuGroupResponse.class);
        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);

        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청("짜장면", new BigDecimal(5000), menuGroup.getId(), Arrays.asList(menuProductRequest));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> getResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all().extract();

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.jsonPath().getList(".")).hasSize(1);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        MenuRequest menuRequest = new MenuRequest(name, price, menuGroupId, menuProductRequests);

        return RestAssured
                .given().log().all()
                .body(menuRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all().extract();
    }
}
