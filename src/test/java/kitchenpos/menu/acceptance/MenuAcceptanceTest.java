package kitchenpos.menu.acceptance;

import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest.*;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 기능 통합 테스트")
    @TestFactory
    Stream<DynamicNode> menu() {
        Long 중식_ID = 메뉴그룹_등록됨("중식");
        Long 짜장면_ID = 상품_등록됨("짜장면", 9000);
        Long 탕수육_ID = 상품_등록됨("탕수육", 12000);
        return Stream.of(
            dynamicTest("메뉴를 등록한다.", () -> {
                // given
                MenuRequest 짜장_탕수_세트 = 메뉴_생성("짜장_탕수_세트", BigDecimal.valueOf(15000), 중식_ID, 짜장면_ID, 탕수육_ID);
                // when
                ExtractableResponse<Response> response = 메뉴_생성_요청(짜장_탕수_세트);
                // then
                메뉴_정상_생성됨(response);
            }),
            dynamicTest("가격이 없는 메뉴를 등록할 수 없다.", () -> {
                // given
                MenuRequest 가격_없는_세트 = 메뉴_생성("짜장_탕수_세트", null, 중식_ID, 짜장면_ID, 탕수육_ID);
                // when
                ExtractableResponse<Response> response = 메뉴_생성_요청(가격_없는_세트);
                // then
                메뉴_생성_실패됨(response);
            }),
            dynamicTest("가격이 0 미만인 메뉴를 등록할 수 없다.", () -> {
                // given
                MenuRequest 가격_음수_세트 = 메뉴_생성("짜장_탕수_세트", BigDecimal.valueOf(-1), 중식_ID, 짜장면_ID, 탕수육_ID);
                // when
                ExtractableResponse<Response> response = 메뉴_생성_요청(가격_음수_세트);
                // then
                메뉴_생성_실패됨(response);
            }),
            dynamicTest("메뉴그룹이 없이 메뉴를 등록 할 수 없다.", () -> {
                // given
                MenuRequest 메뉴그룹_없는_세트 = 메뉴_생성("짜장_탕수_세트", BigDecimal.valueOf(15000), null, 짜장면_ID, 탕수육_ID);
                // when
                ExtractableResponse<Response> response = 메뉴_생성_요청(메뉴그룹_없는_세트);
                // then
                메뉴_생성_실패됨(response);

            }),
            dynamicTest("등록하려는 메뉴의 메뉴상품이 등록되지 않으면 등록할 수 없다.", () -> {
                // given
                Product 미등록_짬뽕 = Product.of("짬뽕", BigDecimal.valueOf(7000));
                Product 미등록_양장피 = Product.of("양장피", BigDecimal.valueOf(12000));
                MenuRequest 신규_메뉴 = 메뉴_생성("미등록_메뉴", BigDecimal.valueOf(15000), 중식_ID, 미등록_짬뽕.getId(), 미등록_양장피.getId());
                // when
                ExtractableResponse<Response> response = 메뉴_생성_요청(신규_메뉴);
                // then
                메뉴_생성_실패됨(response);
            }),
            dynamicTest("메뉴의 가격을 메뉴상품의 총금액보다 높게 책정할 수 없다.", () -> {
                // given
                MenuRequest 비싼_세트 = 메뉴_생성("짜장_탕수_세트", BigDecimal.valueOf(30000), 중식_ID, 짜장면_ID, 탕수육_ID);

                // when
                ExtractableResponse<Response> response = 메뉴_생성_요청(비싼_세트);
                // then
                메뉴_생성_실패됨(response);
            }),
            dynamicTest("메뉴의 목록을 조회할 수 있다.", () -> {
                // given
                Long id = 짜장_탕수_세트_생성됨();
                // when
                ExtractableResponse<Response> 메뉴_목록 = 메뉴_목록_조회_요청();
                // then
                메뉴_목록_정상_응답됨(메뉴_목록, id);
            })
        );
    }

    public static Long 짜장_탕수_세트_생성됨() {
        Long 중식_ID = 메뉴그룹_등록됨("중식");
        Long 짜장면_ID = 상품_등록됨("짜장면", 9000);
        Long 탕수육_ID = 상품_등록됨("탕수육", 12000);
        MenuRequest menuRequest = 메뉴_생성("짜장_탕수_세트", BigDecimal.valueOf(15000), 중식_ID, 짜장면_ID, 탕수육_ID);
        return 메뉴_생성_요청(menuRequest).as(MenuResponse.class).getId();
    }

    private static MenuProductRequest 메뉴상품_생성(Long productId) {
        return MenuProductRequest.of(productId, 1);
    }

    private static MenuRequest 메뉴_생성(String name, BigDecimal price, Long menuGroupId, Long... productIds) {
        List<MenuProductRequest> menuProductRequests = Arrays.asList(productIds)
            .stream()
            .map(it -> 메뉴상품_생성(it))
            .collect(Collectors.toList());
        return MenuRequest.of(name, price, menuGroupId, menuProductRequests);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menuRequest)
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

    private void 메뉴_정상_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 메뉴_목록_정상_응답됨(ExtractableResponse<Response> response, Long... 메뉴_목록) {
        List<Long> 메뉴_조회_결과_목록 = response.jsonPath()
            .getList(".", MenuResponse.class)
            .stream()
            .map(MenuResponse::getId)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(메뉴_조회_결과_목록).containsAll(Arrays.asList(메뉴_목록))
        );
    }

}
