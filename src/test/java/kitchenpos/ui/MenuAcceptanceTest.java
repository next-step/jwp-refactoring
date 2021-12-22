package kitchenpos.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

class MenuAcceptanceTest extends AcceptanceTest {
    @DisplayName("메뉴 관리")
    @Test
    void manageMenu() {
        // given
        Map<String, Integer> menuProduct = new HashMap<>();
        menuProduct.put("productId", 1);
        menuProduct.put("quantity", 2);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "후라이드+후라이드");
        params.put("price", "19000");
        params.put("menuGroupId", "1");
        params.put("menuProducts", Arrays.asList(menuProduct));

        // when
        ExtractableResponse<Response> createResponse = MenuAcceptanceTestHelper.메뉴_생성_요청(params);

        // then
        MenuAcceptanceTestHelper.메뉴_생성됨(createResponse);

        // given
        int expected = 7;
        // when
        ExtractableResponse<Response> getResponse = MenuAcceptanceTestHelper.메뉴_조회_요청();

        // then
        MenuAcceptanceTestHelper.메뉴_조회됨(getResponse);
        MenuAcceptanceTestHelper.메뉴_갯수_예상과_일치(getResponse, expected);
    }

    @DisplayName("메뉴 상품 가격의 합과 입력받은 가격 비교하여 입력받은 가격이 더 크면 에러가 발생한다.")
    @Test
    void createMenuFailWhenPriceIsBiggerThanSum() {
        // given
        Map<String, Integer> menuProduct = new HashMap<>();
        menuProduct.put("productId", 1);
        menuProduct.put("quantity", 2);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "후라이드+후라이드");
        params.put("price", "32001");
        params.put("menuGroupId", "1");
        params.put("menuProducts", Arrays.asList(menuProduct));

        // when
        ExtractableResponse<Response> createResponse = MenuAcceptanceTestHelper.메뉴_생성_요청(params);

        // then
        MenuAcceptanceTestHelper.메뉴_생성_실패(createResponse);
    }

    @DisplayName("메뉴 상품 가격이 0 이하이면 에러가 발생한다.")
    @Test
    void createMenuFailWhenPriceIsLowerThanZero() {
        // given
        Map<String, Integer> menuProduct = new HashMap<>();
        menuProduct.put("productId", 1);
        menuProduct.put("quantity", 2);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "후라이드+후라이드");
        params.put("price", "-1");
        params.put("menuGroupId", "1");
        params.put("menuProducts", Arrays.asList(menuProduct));

        // when
        ExtractableResponse<Response> createResponse = MenuAcceptanceTestHelper.메뉴_생성_요청(params);

        // then
        MenuAcceptanceTestHelper.메뉴_생성_실패(createResponse);
    }

    @DisplayName("메뉴 상품 가격이 null 이면 에러가 발샌한다.")
    @Test
    void createMenuFailWhenPriceIsNull() {
        // given
        Map<String, Integer> menuProduct = new HashMap<>();
        menuProduct.put("productId", 1);
        menuProduct.put("quantity", 2);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "후라이드+후라이드");
        params.put("menuGroupId", "1");
        params.put("menuProducts", Arrays.asList(menuProduct));

        // when
        ExtractableResponse<Response> createResponse = MenuAcceptanceTestHelper.메뉴_생성_요청(params);

        // then
        MenuAcceptanceTestHelper.메뉴_생성_실패(createResponse);
    }
}