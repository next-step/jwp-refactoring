package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴그룹 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴그룹 생성에 성공한다.")
    @Test
    void createMenuGroup() {
        //when
        final ExtractableResponse<Response> 결과 = 메뉴_그룹_생성_요청("두마리메뉴");

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(결과.jsonPath().getString("name")).isEqualTo("두마리메뉴");
    }

    @DisplayName("메뉴그룹을 조회할 수 있다.")
    @Test
    void findMenuGroups() {
        //given
        메뉴_그룹_생성_요청("한마리메뉴");
        메뉴_그룹_생성_요청("두마리메뉴");

        //when
        final ExtractableResponse<Response> 메뉴_그룹_목록 = 메뉴_그룹_목록_조회();

        //then
        assertThat(메뉴_그룹_목록.jsonPath().getList(".").size()).isEqualTo(2);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(final String name) {
        final MenuGroup menuGroup = new MenuGroup(name);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(menuGroup)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }
}
