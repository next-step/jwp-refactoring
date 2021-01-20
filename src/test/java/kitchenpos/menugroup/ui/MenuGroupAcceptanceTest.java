package kitchenpos.menugroup.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createMenuGroups() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_등록_요청("추천메뉴");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> 메뉴_그룹_등록_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return RestAssured.given().log().all().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/api/menu-groups").
                then().
                log().all().
                extract();
    }

    @DisplayName("메뉴 그룹 목록을 가져올수 있다.")
    @Test
    void listMenuGroup() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_리스트_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findMenuGroupNames(response))
                .containsAnyOf("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴");
    }

    private ExtractableResponse<Response> 메뉴_그룹_리스트_조회_요청() {
        return RestAssured.given().log().all().
                when().
                get("/api/menu-groups").
                then().
                log().all().
                extract();
    }

    private List<String> findMenuGroupNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", MenuGroupResponse.class).stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toList());
    }
}
