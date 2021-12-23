package kitchenpos.product.menu;


import static kitchenpos.product.group.step.MenuGroupAcceptanceStep.메뉴_그룹_등록_되어_있음;
import static kitchenpos.product.menu.step.MenuAcceptanceStep.메뉴_등록_되어_있음;
import static kitchenpos.product.menu.step.MenuAcceptanceStep.메뉴_등록_됨;
import static kitchenpos.product.menu.step.MenuAcceptanceStep.메뉴_등록_요청;
import static kitchenpos.product.menu.step.MenuAcceptanceStep.메뉴_목록_조회_됨;
import static kitchenpos.product.menu.step.MenuAcceptanceStep.메뉴_목록_조회_요청;
import static kitchenpos.product.product.step.ProductAcceptanceStep.상품_등록_되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.product.AcceptanceTest;
import kitchenpos.product.group.ui.response.MenuGroupResponse;
import kitchenpos.product.menu.ui.response.MenuResponse;
import kitchenpos.product.product.ui.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관련 기능")
class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 두마리메뉴;
    private ProductResponse 후라이드치킨;

    @BeforeEach
    void setUp() {
        두마리메뉴 = 메뉴_그룹_등록_되어_있음("두마리메뉴");
        후라이드치킨 = 상품_등록_되어_있음("후라이드치킨", BigDecimal.TEN);
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void create() {
        //given
        String name = "후라이드치킨세트";
        BigDecimal price = BigDecimal.TEN;
        int quantity = 2;

        //when
        ExtractableResponse<Response> response = 메뉴_등록_요청(name, price,
            두마리메뉴.getId(), 후라이드치킨.getId(), quantity);

        //then
        메뉴_등록_됨(response, name, price, quantity, 두마리메뉴, 후라이드치킨);
    }

    @Test
    @DisplayName("메뉴들을 조회할 수 있다.")
    void list() {
        //given
        MenuResponse menu = 메뉴_등록_되어_있음("후라이드치킨세트", BigDecimal.TEN,
            두마리메뉴.getId(), 후라이드치킨.getId(), 2L);

        //when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        //then
        메뉴_목록_조회_됨(response, Collections.singletonList(menu));
    }

    @Test
    @DisplayName("id 목록으로 원하는 메뉴들을 조회할 수 있다.")
    void list_ids() {
        //given
        MenuResponse firstMenu = 메뉴_등록_되어_있음("첫번째메뉴", BigDecimal.TEN,
            두마리메뉴.getId(), 후라이드치킨.getId(), 3L);
        MenuResponse secondMenu = 메뉴_등록_되어_있음("두번째메뉴", BigDecimal.TEN,
            두마리메뉴.getId(), 후라이드치킨.getId(), 3L);

        메뉴_등록_되어_있음("세번째메뉴", BigDecimal.TEN,
            두마리메뉴.getId(), 후라이드치킨.getId(), 3L);

        //when
        ExtractableResponse<Response> response =
            메뉴_목록_조회_요청(Arrays.asList(firstMenu.getId(), secondMenu.getId()));

        //then
        메뉴_목록_조회_됨(response, Arrays.asList(firstMenu, secondMenu));
    }
}
