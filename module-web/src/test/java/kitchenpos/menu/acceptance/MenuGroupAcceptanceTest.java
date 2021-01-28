package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupAcceptanceTest extends AcceptanceTest {
    private MenuGroupRequest menuGroupRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        menuGroupRequest = new MenuGroupRequest("디저트메뉴");
    }

    @DisplayName("메뉴그룹을 관리한다")
    @Test
    void manageMenuGroup() {
        ExtractableResponse<Response> createResponse = 메뉴그룹_등록_요청(menuGroupRequest);
        메뉴그룹_등록됨(createResponse);

        ExtractableResponse<Response> findResponse = 메뉴그룹_조회_요청();
        메뉴그룹_조회됨(findResponse);
    }

    private ExtractableResponse<Response> 메뉴그룹_등록_요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured.given().log().all().
                body(menuGroupRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/menu-groups").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 메뉴그룹_조회_요청() {
        return RestAssured.given().log().all().
                when().get("/api/menu-groups").
                then().log().all().
                extract();
    }

    private void 메뉴그룹_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<MenuGroupResponse> menuGroups = response.jsonPath().getList(".", MenuGroupResponse.class);
        List<String> menuGroupNames = menuGroups.stream().map(menuGroup -> menuGroup.getName()).collect(Collectors.toList());

        assertThat(menuGroupNames).contains("디저트메뉴");
    }

    private void 메뉴그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(MenuGroupResponse.class).getName()).isEqualTo("디저트메뉴");
    }
}
