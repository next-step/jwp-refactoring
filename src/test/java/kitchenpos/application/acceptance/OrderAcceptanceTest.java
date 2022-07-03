package kitchenpos.application.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends BaseAcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * Given 메뉴 그룹명을 이용하여
     * When 메뉴 그룹을 생성하면
     * Then 생성된 메뉴 그룹 데이터가 리턴된다
     */
    @DisplayName("주문 생성")
    @Test
    void createMenuGroup() {
        // given
        String name = "신메뉴";

        // when
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_그룹_생성_요청(name);

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), 메뉴_그룹_생성_요청_응답.statusCode()),
                () -> assertNotNull(메뉴_그룹_생성_요청_응답.as(Product.class).getId()),
                () -> assertEquals(name, 메뉴_그룹_생성_요청_응답.as(Product.class).getName())
        );
    }


    /**
     * Given 메뉴 그룹 2건을 등록한 후
     * When 메뉴 그룹 목록을 조회하면
     * Then 2건이 조회된다
     */
    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        // given
        메뉴_그룹_생성_요청("신메뉴");
        메뉴_그룹_생성_요청("두마리메뉴");

        // when
        ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청_응답 = 메뉴_그룹_목록_조회_요청();

        // then
        assertThat(메뉴_그룹_목록_조회_요청_응답.jsonPath().getList("")).hasSize(2);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return RestAssured
                .given().log().all()
                .body(menuGroup)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }
}
