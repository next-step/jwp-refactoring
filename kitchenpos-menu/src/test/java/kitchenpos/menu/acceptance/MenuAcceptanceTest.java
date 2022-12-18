package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.acceptance.ProductAcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    private ProductResponse 불고기;
    private ProductResponse 김치;
    private ProductResponse 공기밥;
    private MenuGroupResponse 한식;
    private MenuProductRequest 불고기상품;
    private MenuProductRequest 김치상품;
    private MenuProductRequest 공기밥상품;
    private MenuRequest 불고기정식;

    @BeforeEach
    public void setUp() {
        super.setUp();
        불고기 = ProductAcceptanceTest.상품_생성_요청(ProductRequest.of("불고기", BigDecimal.valueOf(10_000))).as(ProductResponse.class);
        김치 = ProductAcceptanceTest.상품_생성_요청(ProductRequest.of("김치", BigDecimal.valueOf(10_000))).as(ProductResponse.class);
        공기밥 = ProductAcceptanceTest.상품_생성_요청(ProductRequest.of("공기밥", BigDecimal.valueOf(10_000))).as(ProductResponse.class);
        한식 = 메뉴그룹_생성_요청(MenuGroupRequest.of("한식")).as(MenuGroupResponse.class);


        불고기상품 = MenuProductRequest.of(불고기.getId(), 1L);
        김치상품 = MenuProductRequest.of(김치.getId(), 1L);
        공기밥상품 = MenuProductRequest.of(공기밥.getId(), 1L);
        불고기정식 = MenuRequest.of(
                "불고기정식",
                BigDecimal.valueOf(12_000L),
                한식.getId(),
                Arrays.asList(불고기상품, 김치상품, 공기밥상품)
        );
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(불고기정식);

        // then
        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void findAllMenu() {
        // given
        MenuResponse 불고기정식응답 = 메뉴_생성_요청(불고기정식).as(MenuResponse.class);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(response, Arrays.asList(불고기정식응답.getId()));
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_목록_응답됨(ExtractableResponse<Response> response, List<Long> menuIds) {
        List<Long> ids = response.jsonPath().getList(".", MenuResponse.class)
                        .stream()
                        .map(MenuResponse::getId)
                        .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(ids).containsAll(menuIds)
        );
    }
}
