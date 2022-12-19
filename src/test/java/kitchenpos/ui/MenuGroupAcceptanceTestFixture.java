package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceTestFixture extends AcceptanceTest {

    public MenuGroupRequest 세트;

    @BeforeEach
    public void setUp() {
        super.setUp();

        세트 = new MenuGroupRequest("세트");
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static MenuGroupResponse 메뉴_그룹_생성되어있음(MenuGroupRequest menuGroupRequest) {
        return 메뉴_그룹(메뉴_그룹_생성_요청(menuGroupRequest));
    }

    public static MenuGroupResponse 메뉴_그룹(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", MenuGroupResponse.class);
    }

    public static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static List<MenuGroupResponse> 메뉴_그룹_목록(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("", MenuGroupResponse.class);
    }

    public static void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
