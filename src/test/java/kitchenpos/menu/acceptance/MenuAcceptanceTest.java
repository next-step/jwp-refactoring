package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupTestFixture.메뉴_그룹_생성_요청함;
import static kitchenpos.menu.acceptance.MenuTestFixture.메뉴_생성_요청함;
import static kitchenpos.menu.acceptance.MenuTestFixture.메뉴_생성됨;
import static kitchenpos.menu.acceptance.MenuTestFixture.메뉴_조회_요청_응답됨;
import static kitchenpos.menu.acceptance.MenuTestFixture.메뉴_조회_요청함;
import static kitchenpos.menu.acceptance.MenuTestFixture.메뉴_조회_포함됨;
import static kitchenpos.product.acceptance.ProductTestFixture.상품_생성_요청함;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuRequest 후라이드세트;

    @BeforeEach
    public void setUp() {
        super.setUp();
        MenuGroupResponse 메뉴분류세트 = 메뉴_그룹_생성_요청함("메뉴분류세트").as(MenuGroupResponse.class);
        ProductResponse 후라이드 = 상품_생성_요청함("후라이드", BigDecimal.valueOf(15000)).as(ProductResponse.class);
        ProductResponse 콜라 = 상품_생성_요청함("콜라", BigDecimal.valueOf(1000)).as(ProductResponse.class);
        MenuProductRequest 훌라이드메뉴상품요청 = new MenuProductRequest(후라이드.getId(), 1L);
        MenuProductRequest 콜라메뉴상품요청 = new MenuProductRequest(콜라.getId(), 1L);
        후라이드세트 = new MenuRequest("후라이드세트", BigDecimal.valueOf(16000), 메뉴분류세트.getId(),
                Arrays.asList(훌라이드메뉴상품요청, 콜라메뉴상품요청));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        //when
        ExtractableResponse<Response> response = 메뉴_생성_요청함(후라이드세트);
        //then
        메뉴_생성됨(response);
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void list() {
        //given
        ExtractableResponse<Response> 후라이드한마리_response = 메뉴_생성_요청함(후라이드세트);
        //when
        ExtractableResponse<Response> response = 메뉴_조회_요청함();
        //then
        메뉴_조회_요청_응답됨(response);
        메뉴_조회_포함됨(response, Collections.singletonList(후라이드한마리_response));
    }
}
