//package kitchenpos.menu.acceptance;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import io.restassured.response.ExtractableResponse;
//import io.restassured.response.Response;
//import kitchenpos.AcceptanceTest;
//import kitchenpos.menu.dto.MenuGroupRequest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//
//@DisplayName("메뉴 그룹을 관리한다.")
//public class MenuGroupAcceptanceTest extends AcceptanceTest {
//    @Test
//    @DisplayName("메뉴 그룹을 생성한다.")
//    void createMenuGroup() {
//        // when
//        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청("추천메뉴");
//
//        // then
//        메뉴_그룹_생성_요청됨(response);
//    }
//
//    @Test
//    @DisplayName("메뉴 그룹 목록을 조회한다.")
//    void findAll() {
//        // when
//        ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();
//
//        // then
//        메뉴_그룹_목록_조회_응답됨(response);
//    }
//
//    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
//        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name);
//
//        return RestAssured.given().log().all()
//            .contentType(MediaType.APPLICATION_JSON_VALUE)
//            .body(menuGroupRequest)
//            .when().post("/api/menu-groups")
//            .then().log().all().extract();
//    }
//
//    private static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
//        return RestAssured.given().log().all()
//            .contentType(MediaType.APPLICATION_JSON_VALUE)
//            .when().get("/api/menu-groups")
//            .then().log().all().extract();
//    }
//
//    public static void 메뉴_그룹_생성_요청됨(ExtractableResponse<Response> response) {
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
//        assertThat(response.header("Location")).isNotBlank();
//    }
//
//    public static void 메뉴_그룹_목록_조회_응답됨(ExtractableResponse response) {
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//    }
//}
