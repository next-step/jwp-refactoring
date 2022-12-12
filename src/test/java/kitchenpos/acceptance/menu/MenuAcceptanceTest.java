package kitchenpos.acceptance.menu;

import static kitchenpos.acceptance.menu.MenuAcceptanceUtils.*;
import static kitchenpos.acceptance.menugroup.MenuGroupAcceptanceUtils.*;
import static kitchenpos.acceptance.product.ProductAcceptanceUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.menugroup.MenuGroupId;
import kitchenpos.acceptance.product.ProductId;

public class MenuAcceptanceTest extends AcceptanceTest {
    /**
     * given 메뉴 그룹 등록되어있음
     * and 상품 등록되어 있음
     * when 메뉴 등록 요청
     * then 메뉴 등록 완료
     * when 메뉴 목록 조회 요청
     * then 메뉴 목록 조회 완료
     */
    @DisplayName("메뉴 관리")
    @Test
    void menu() {
        // given
        String 메뉴_이름 = "후라이드+후라이드";
        Long 가격 = 19000L;
        MenuGroupId 메뉴_그룹_ID = 메뉴_그룹_ID_추출(메뉴_그룹_등록_요청("치킨 그룹"));
        ProductId 상품_ID = 상품_ID_추출(상품_등록_요청("후라이드", 10000));
        List<MenuProductParam> 메뉴_상품_목록 = Arrays.asList(new MenuProductParam(상품_ID, 2));
        // when
        ExtractableResponse<Response> 메뉴_등록_요청 = 메뉴_등록_요청(메뉴_이름, 가격, 메뉴_그룹_ID, 메뉴_상품_목록);
        // then
        메뉴_등록_요청_완료(메뉴_등록_요청);
        // when
        ExtractableResponse<Response> 메뉴_목록_조회_요청 = 메뉴_목록_조회_요청();
        // then
        메뉴_목록_조회_완료(메뉴_목록_조회_요청);
        assertThat(메뉴_목록_이름_추출(메뉴_목록_조회_요청)).contains(메뉴_이름);
        assertThat(메뉴_목록_상품_ID_추출(메뉴_목록_조회_요청)).contains(상품_ID);
    }

    /**
     * given 메뉴 그룹 등록되어있음
     * when 가격이 없이 메뉴 등록 요청
     * then 메뉴 등록 실패
     */
    @DisplayName("메뉴 등록 - 가격이 없이 메뉴 등록 요청")
    @Test
    void menu_create_price_null_failed() {
        // given
        MenuGroupId 메뉴_그룹_ID = 메뉴_그룹_ID_추출(메뉴_그룹_등록_요청("치킨 그룹"));
        ProductId 상품_ID = 상품_ID_추출(상품_등록_요청("후라이드", 10000));
        List<MenuProductParam> 메뉴_상품_목록 = Arrays.asList(new MenuProductParam(상품_ID, 2));

        // when
        Long 빈_가격 = null;
        ExtractableResponse<Response> 가격_없이_메뉴_등록_요청 = 메뉴_등록_요청("후라이드+후라이드", 빈_가격, 메뉴_그룹_ID, 메뉴_상품_목록);

        // then
        메뉴_등록_요청_실패(가격_없이_메뉴_등록_요청);
    }

    /**
     * given 메뉴 그룹 등록되어있음
     * when 가격이 0원 미만 메뉴 등록 요청
     * then 메뉴 등록 실패
     */
    @DisplayName("메뉴 등록 - 가격이 0원 미만 메뉴 등록 요청")
    @Test
    void menu_create_negative_price_failed() {
        // given
        MenuGroupId 메뉴_그룹_ID = 메뉴_그룹_ID_추출(메뉴_그룹_등록_요청("치킨 그룹"));
        ProductId 상품_ID = 상품_ID_추출(상품_등록_요청("후라이드", 10000));
        List<MenuProductParam> 메뉴_상품_목록 = Arrays.asList(new MenuProductParam(상품_ID, 2));

        // when
        Long 가격_0원_미만 = -10000L;
        ExtractableResponse<Response> 가격_0원_미만_메뉴_등록_요청 = 메뉴_등록_요청("후라이드+후라이드", 가격_0원_미만, 메뉴_그룹_ID, 메뉴_상품_목록);

        // then
        메뉴_등록_요청_실패(가격_0원_미만_메뉴_등록_요청);
    }

    /**
     * given 메뉴 그룹 등록되어있지 않음
     * when 잘못된 메뉴 그룹 ID 메뉴 등록 요청
     * then 메뉴 등록 실패
     */
    @DisplayName("메뉴 등록 - 잘못된 메뉴 그룹 ID 메뉴 등록 요청")
    @Test
    void menu_invalid_menu_group_id_failed() {
        // given
        ProductId 상품_ID = 상품_ID_추출(상품_등록_요청("후라이드", 10000));
        List<MenuProductParam> 메뉴_상품_목록 = Arrays.asList(new MenuProductParam(상품_ID, 2));

        // when
        MenuGroupId 잘못된_메뉴_그룹_ID = new MenuGroupId(-1);
        ExtractableResponse<Response> 잘못된_메뉴_그룹_ID_메뉴_등록_요청 = 메뉴_등록_요청("후라이드+후라이드", 19000L, 잘못된_메뉴_그룹_ID, 메뉴_상품_목록);

        // then
        메뉴_등록_요청_실패(잘못된_메뉴_그룹_ID_메뉴_등록_요청);
    }

    /**
     * given 메뉴 그룹 등록되어 있음
     * when 빈 메뉴 상품 목록 메뉴 등록 요청
     * then 메뉴 등록 실패
     */
    @DisplayName("메뉴 등록 - 빈 메뉴 상품 목록 메뉴 등록 요청")
    @Test
    void menu_empty_menu_product_failed() {
        // given
        MenuGroupId 메뉴_그룹_ID = 메뉴_그룹_ID_추출(메뉴_그룹_등록_요청("치킨 그룹"));

        // when
        List<MenuProductParam> 빈_메뉴_상품_목록 = Collections.emptyList();
        ExtractableResponse<Response> 빈_메뉴_상품_목록_메뉴_등록_요청 = 메뉴_등록_요청("후라이드+후라이드", 19000L, 메뉴_그룹_ID, 빈_메뉴_상품_목록);

        // then
        메뉴_등록_요청_실패(빈_메뉴_상품_목록_메뉴_등록_요청);
    }

    /**
     * given 메뉴 그룹 등록되어 있음
     * when 메뉴 상품 목록 금액보다 비싼 메뉴 등록 요청
     * then 메뉴 등록 실패
     */
    @DisplayName("메뉴 등록 - 메뉴 상품 목록 금액보다 비싼 메뉴 등록 요청")
    @Test
    void menu_price_exceed() {
        // given
        MenuGroupId 메뉴_그룹_ID = 메뉴_그룹_ID_추출(메뉴_그룹_등록_요청("치킨 그룹"));
        ProductId 상품_ID = 상품_ID_추출(상품_등록_요청("후라이드", 10000));
        List<MenuProductParam> 메뉴_상품_목록 = Arrays.asList(new MenuProductParam(상품_ID, 2));

        // when
        Long 가격_상품_목록_금액보다_비쌈 = 100000L;
        ExtractableResponse<Response> 상품_목록_금액보다_비싼_메뉴_등록_요청 = 메뉴_등록_요청("후라이드+후라이드", 가격_상품_목록_금액보다_비쌈, 메뉴_그룹_ID,
            메뉴_상품_목록);

        // then
        메뉴_등록_요청_실패(상품_목록_금액보다_비싼_메뉴_등록_요청);
    }
}
