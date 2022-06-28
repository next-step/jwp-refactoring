package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 그룹")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

/*
- 메뉴그룹 등록 관리
      given 등록할 메뉴그룹 생성
      when 메뉴 그룹 등록
      then 메뉴 그룹이 등록됨
      when 등록된_메뉴그룹들을_조회
      then 등록한_메뉴그룹이_조회됨
*/
    @Test
    @DisplayName("메뉴그룹 등록 관리")
    void menuGroupManage() {
        //given
        MenuGroup 일식 = new MenuGroup("일식");

        //when
        ExtractableResponse<Response> createResponse = 메뉴그룹_등록을_요청(일식);
        //then
        메뉴_그룹이_등록됨(일식, createResponse);

        //when
        ExtractableResponse<Response> retrievedResponse = 등록된_메뉴그룹들을_조회();
        //then
        등록한_메뉴그룹이_조회됨(일식, retrievedResponse);
    }


    public static ExtractableResponse<Response> 메뉴그룹_등록을_요청(MenuGroup menuGroup) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post("/api/menu-groups")
                .then()
                .log().all()
                .extract();
    }

    private void 메뉴_그룹이_등록됨(MenuGroup menuGroup, ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotEmpty();
        assertThat(response.jsonPath().getString("name")).isEqualTo(menuGroup.getName());
    }

    private static ExtractableResponse<Response> 등록된_메뉴그룹들을_조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then()
                .log().all()
                .extract();
    }

    private void 등록한_메뉴그룹이_조회됨(MenuGroup menuGroup, ExtractableResponse<Response> searchProducts) {
        assertThat(searchProducts.jsonPath().getList(".", MenuGroup.class))
                .extracting("name")
                .contains(menuGroup.getName());
    }




}
