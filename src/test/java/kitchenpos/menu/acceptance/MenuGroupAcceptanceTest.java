package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void createMenuGroup() {
        ExtractableResponse<Response> response = 메뉴그룹_생성을_요청("치킨");

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    @DisplayName("빈 값의 이름을 입력하여 메뉴 그룹을 생성하면 실패")
    @Test
    void createMenuWithNullName() {
        ExtractableResponse<Response> response = 메뉴그룹_생성을_요청(null);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void getMenuGroupList() {
        메뉴그룹_생성을_요청("치킨");
        메뉴그룹_생성을_요청("피자");

        ExtractableResponse<Response> response = 메뉴그룹_목록을_요청();

        assertThat(response.jsonPath().getList(".", MenuGroupResponse.class)).hasSize(2);
    }

    public static ExtractableResponse<Response> 메뉴그룹_생성을_요청(String name) {
        MenuGroupRequest request = new MenuGroupRequest(name);
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_목록을_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }
}
