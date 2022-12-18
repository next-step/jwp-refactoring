package kitchenpos.menu.acceptance;


import static java.util.Collections.singletonList;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestFixture.메뉴_등록되어_있음;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestFixture.메뉴_목록_조회_요청;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestFixture.메뉴_목록_조회됨;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestFixture.메뉴_목록_포함됨;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestFixture.메뉴_생성_요청;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestFixture.메뉴_생성됨;
import static kitchenpos.menu.acceptance.ProductAcceptanceTestFixture.상품_등록_되어_있음;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTestFixture.메뉴_그룹_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuRequest.MenuProductRequest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuGroupResponse 추천메뉴;
    private MenuRequest 두마리치킨;
    private MenuRequest 양념세트;

    @BeforeEach
    void menuSetUp() {
        super.setUp();

        Long 후라이드치킨 = 상품_등록_되어_있음("후라이드치킨", BigDecimal.valueOf(15000));
        Long 양념치킨 = 상품_등록_되어_있음("양념치킨", BigDecimal.valueOf(18000));
        Long 콜라 = 상품_등록_되어_있음("콜라", BigDecimal.valueOf(1000));
        추천메뉴 = 메뉴_그룹_등록되어_있음(new MenuGroupRequest("추천메뉴")).as(MenuGroupResponse.class);
        MenuProductRequest 두마리치킨상품 = new MenuProductRequest(후라이드치킨, 2);
        MenuProductRequest 양념치킨상품 = new MenuProductRequest(양념치킨, 1);
        MenuProductRequest 콜라상품 = new MenuProductRequest(콜라, 1);
        두마리치킨 = new MenuRequest("두마리치킨", new BigDecimal(25000), 추천메뉴.getId(), singletonList(두마리치킨상품));
        양념세트 = new MenuRequest("양념세트", new BigDecimal(17000), 추천메뉴.getId(), Arrays.asList(양념치킨상품, 콜라상품));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void crate() {
        ExtractableResponse<Response> response = 메뉴_생성_요청(두마리치킨);

        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void getMenuList() {
        ExtractableResponse<Response> createResponse1 = 메뉴_등록되어_있음(두마리치킨);
        ExtractableResponse<Response> createResponse2 = 메뉴_등록되어_있음(양념세트);

        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        메뉴_목록_조회됨(response);
        메뉴_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }
}
