package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("상품을 관리한다.")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void createMenuGroup() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("추천메뉴");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(menuGroupRequest)
            .when().post("/api/menu-groups")
            .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
