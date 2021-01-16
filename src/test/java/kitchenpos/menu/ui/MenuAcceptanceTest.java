package kitchenpos.menu.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.util.MenuRequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴_등록되어_있음(new MenuRequestBuilder()
                .withName("후라이드+양념")
                .withPrice(3000)
                .withGroupId(1L)
                .addMenuProduct(1L, 1)
                .addMenuProduct(2L, 1)
                .build());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("메뉴의 가격이 메뉴의 상품 가격 * 수량보다 작아야 한다.")
    @Test
    void menuPriceLessThanProductAllPrice() {
        // when
        ExtractableResponse<Response> response = 메뉴_등록되어_있음(new MenuRequestBuilder()
                .withName("후라이드+양념")
                .withPrice(40000)
                .withGroupId(1L)
                .addMenuProduct(1L, 1)
                .addMenuProduct(2L, 1)
                .build());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void isNotCollectMenuPrice() {
        // when
        ExtractableResponse<Response> response = 메뉴_등록되어_있음(new MenuRequestBuilder()
                .withName("후라이드+양념")
                .withPrice(-1)
                .withGroupId(1L)
                .addMenuProduct(1L, 1)
                .addMenuProduct(2L, 1)
                .build());
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("메뉴의 상품이 추가 되어있지 않으면 등록할 수 없다.")
    @Test
    void notAddedProductInMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴_등록되어_있음(new MenuRequestBuilder()
                .withName("후라이드+양념")
                .withPrice(10000)
                .withGroupId(1L)
                .build());
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("")
    @Test
    void notFoundMenuGroup() {
        // when
        ExtractableResponse<Response> response = 메뉴_등록되어_있음(new MenuRequestBuilder()
                .withName("후라이드+양념")
                .withPrice(-1)
                .withGroupId(5L)
                .addMenuProduct(1L, 1)
                .addMenuProduct(2L, 1)
                .build());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("매뉴의 목록을 조회할 수 있다.")
    @Test
    void listMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴목록_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findMenuNames(response))
                .containsExactly("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");

    }

    private ExtractableResponse<Response> 메뉴목록_조회_요청() {
        return RestAssured.given().log().all().
                when().
                get("/api/menus").
                then().
                log().all().
                extract();
    }

    private List<String> findMenuNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", MenuResponse.class).stream()
                .map(MenuResponse::getName)
                .collect(Collectors.toList());
    }

    private ExtractableResponse<Response> 메뉴_등록되어_있음(MenuRequest menuRequest) {
        return RestAssured.given().log().all().
                body(menuRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/api/menus").
                then().
                log().all().
                extract();
    }


}
