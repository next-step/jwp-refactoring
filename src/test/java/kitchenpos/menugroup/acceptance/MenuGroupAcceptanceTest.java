package kitchenpos.menugroup.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    private MenuGroup 요리;
    private MenuGroup 안주;

    @BeforeEach
    public void setUp() {
        super.setUp();

        요리 = 메뉴그룹_등록되어_있음("요리").as(MenuGroup.class);
        안주 = 메뉴그룹_등록되어_있음("안주").as(MenuGroup.class);
    }

    @Test
    @DisplayName("메뉴그룹을 등록할 수 있다.")
    void create() {
        // given
        String name = "식사";

        // when
        ExtractableResponse<Response> response = 메뉴그룹_등록_요청(name);

        // then
        메뉴그룹_등록됨(response);
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회할 수 있다.")
    void list() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹_목록_조회_요청();

        // then
        메뉴그룹_목록_조회됨(response);
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록되어_있음(String name) {
        return 메뉴그룹_등록_요청(name);
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록_요청(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 메뉴그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
