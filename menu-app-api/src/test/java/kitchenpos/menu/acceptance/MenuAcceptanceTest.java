package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.*;
import static kitchenpos.menu.acceptance.ProductAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    private ProductResponse 후라이드;
    private MenuGroupResponse 두마리메뉴;
    private MenuGroupResponse 한마리메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        후라이드 = 상품_생성_요청(new ProductRequest("후라이드", new BigDecimal(16000)))
            .as(ProductResponse.class);
        두마리메뉴 = 메뉴_그룹_생성_요청("두마리 메뉴").as(MenuGroupResponse.class);
        한마리메뉴 = 메뉴_그룹_생성_요청("한마리 메뉴").as(MenuGroupResponse.class);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void createMenu() {
        // given
        MenuRequest menuRequest = new MenuRequest("후라이드+후라이드", new BigDecimal(19000), 두마리메뉴.getId(),
            Collections.singletonList(new MenuProductRequest(후라이드.getId(), 2)));

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);

        // then
        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void findAllMenu() {
        // given
        MenuRequest 후라이드한마리 = new MenuRequest("후라이드", new BigDecimal(16000), 한마리메뉴.getId(),
            Collections.singletonList(new MenuProductRequest(후라이드.getId(), 2)));
        MenuRequest 후라이드두마리 = new MenuRequest("후라이드+후라이드", new BigDecimal(19000), 두마리메뉴.getId(),
            Collections.singletonList(new MenuProductRequest(후라이드.getId(), 2)));

        List<MenuResponse> menuResponses = Arrays.asList(
            메뉴_생성_요청(후라이드한마리).as(MenuResponse.class),
            메뉴_생성_요청(후라이드두마리).as(MenuResponse.class)
        );

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회됨(response, menuResponses);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
        return RestAssured
            .given().log().all()
            .body(menuRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/menus")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/api/menus")
            .then().log().all().extract();
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response,
        List<MenuResponse> responses) {
        List<MenuResponse> menuResponses = response.as(new TypeRef<List<MenuResponse>>() {
        });
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(menuResponses)
            .extracting("id")
            .containsExactlyElementsOf(responses.stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList()));
    }
}
