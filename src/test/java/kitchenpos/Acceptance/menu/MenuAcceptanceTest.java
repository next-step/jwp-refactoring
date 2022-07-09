package kitchenpos.Acceptance.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.MenuGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceTest extends AcceptanceTest {
    private static final int PRODUCT_PRICE = 1_000;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("메뉴의 가격이 0원 미만인 경우 생성 요청이 실패해야 한다")
    @Test
    void createMenuByMinusPriceTest() {
        // when
        ExtractableResponse<Response> 메뉴_생성_결과 = 메뉴_생성_API_호출("메뉴", -1, 메뉴_그룹_아이디, Collections.emptyList());

        // then
        메뉴_생성_실패됨(메뉴_생성_결과);
    }

    @DisplayName("메뉴 생성 시 메뉴 그룹 정보가 없거나 저장되지 않은 메뉴 그룹으로 생성 요청이 실패해야 한다")
    @Test
    void createMenuByNotSavedMenuGroupTest() {
        // when
        ExtractableResponse<Response> 없는_메뉴_그룹으로_메뉴_생성_결과
                = 메뉴_생성_API_호출("메뉴", 1_000, -1L, Collections.emptyList());

        // then
        메뉴_생성_실패됨(없는_메뉴_그룹으로_메뉴_생성_결과);
    }

    @DisplayName("없는 상품으로 메뉴를 생성하면 생성 요청이 실패해야 한다")
    @Test
    void createMenuByNotSavedProductTest() {
        // given
        Long menuProductQuantity = 2L;
        MenuProductRequest 없는_상품이_포함된_메뉴_상품_요청 = 메뉴_상품_생성_요청(-1L, menuProductQuantity);

        // when
        ExtractableResponse<Response> 없는_상품이_포함된_메뉴_상품_메뉴_생성_결과 =
                메뉴_생성_API_호출("메뉴", 1_000, 메뉴_그룹_아이디, Collections.singletonList(없는_상품이_포함된_메뉴_상품_요청));

        // then
        메뉴_생성_실패됨(없는_상품이_포함된_메뉴_상품_메뉴_생성_결과);
    }

    @DisplayName("메뉴에 포함된 상품의 총 가격과 메뉴의 가격이 다른 메뉴를 생성 요청하면 실패해야 한다")
    @Test
    void createMenuByNotMatchedMenuPriceAndProductTotalPriceTest() {
        // given
        Long menuProductQuantity = 2L;
        MenuProductRequest 메뉴_상품_생성_요청 = 메뉴_상품_생성_요청(상품_아이디, menuProductQuantity);

        // when
        ExtractableResponse<Response> 메뉴_생성_결과 =
                메뉴_생성_API_호출("메뉴", 3_000, 메뉴_그룹_아이디, Collections.singletonList(메뉴_상품_생성_요청));

        // then
        메뉴_생성_실패됨(메뉴_생성_결과);
    }

    @DisplayName("정상 상태의 메뉴를 저장하면 메뉴 생성이 성공되어야 한다")
    @Test
    void createMenuTest() {
        // given
        String 메뉴_이름 = "메뉴";
        Long menuProductQuantity = 2L;
        MenuProductRequest 메뉴_상품_생성_요청 = 메뉴_상품_생성_요청(상품_아이디, menuProductQuantity);

        // when
        ExtractableResponse<Response> 메뉴_생성_결과 =
                메뉴_생성_API_호출("메뉴", PRODUCT_PRICE * menuProductQuantity.intValue(), 메뉴_그룹_아이디, Collections.singletonList(메뉴_상품_생성_요청));

        // then
        메뉴_생성_성공됨(메뉴_생성_결과, 메뉴_이름, PRODUCT_PRICE * menuProductQuantity);
    }

    @DisplayName("메뉴 리스트 조회 시 정상 조회되어야 한다")
    @Test
    void findAllMenuTest() {
        // given
        List<String> 메뉴_이름들 = Arrays.asList("메뉴 1", "메뉴 2", "메뉴 3", "메뉴 4", "메뉴 5");
        MenuProductRequest 메뉴_상품_생성_요청 = 메뉴_상품_생성_요청(상품_아이디, 1L);
        메뉴_이름들.forEach(name -> 메뉴_생성_API_호출(name, PRODUCT_PRICE, 메뉴_그룹_아이디, Collections.singletonList(메뉴_상품_생성_요청)));

        // when
        ExtractableResponse<Response> 상품_생성_결과 = 메뉴_목록_조회_API_호출();

        // then
        메뉴_목록_조회_성공됨(상품_생성_결과, 메뉴_이름들);
    }

    void 메뉴_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    void 메뉴_생성_성공됨(ExtractableResponse<Response> response, String expectedMenuName, Long expectedMenuPrice) {
        MenuResponse menuResponse = response.as(MenuResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(menuResponse.getName()).isEqualTo(expectedMenuName);
        assertThat(menuResponse.getPrice()).isEqualTo(new BigDecimal(expectedMenuPrice));
    }

    void 메뉴_목록_조회_성공됨(ExtractableResponse<Response> response, List<String> expectedMenuNames) {
        List<String> names = response.body().jsonPath().getList("name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(names).containsAll(expectedMenuNames);
    }
}
