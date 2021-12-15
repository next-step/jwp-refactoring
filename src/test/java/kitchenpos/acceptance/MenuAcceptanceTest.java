package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.utils.Http;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관리 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    @DisplayName("메뉴를 관리한다")
    @Test
    void testManagement() {
        // given
        MenuGroup 식사류 = MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음("식사류");
        Product 볶음짜장면 = ProductAcceptanceTest.상품_등록되어_있음("볶음짜장면", 8000);
        Product 삼선짬뽕 = ProductAcceptanceTest.상품_등록되어_있음("삼선짬뽕", 8000);
        MenuProduct 볶음짜장면_하나 = new MenuProduct(볶음짜장면.getId(), 1);
        MenuProduct 삼성짬뽕_하나 = new MenuProduct(삼선짬뽕.getId(), 1);
        Menu 대표메뉴 = new Menu("대표 메뉴", 16000, 식사류.getId(), Arrays.asList(볶음짜장면_하나, 삼성짬뽕_하나));

        // when
        ExtractableResponse<Response> createResponse = 메뉴_생성_요청(대표메뉴);
        // then
        메뉴_생성됨(createResponse);

        // when
        ExtractableResponse<Response> listResponse = 모든_메뉴_조회_요청();
        // then
        모든_메뉴_조회_응답됨(listResponse);
        생성된_메뉴가_포함됨(볶음짜장면_하나, 삼성짬뽕_하나, 대표메뉴, listResponse);
    }

    /**
     * 요청 관련
     */
    private ExtractableResponse<Response> 메뉴_생성_요청(Menu 대표메뉴) {
        return Http.post("/api/menus", 대표메뉴);
    }

    private ExtractableResponse<Response> 모든_메뉴_조회_요청() {
        return Http.get("/api/menus");
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * 응답 관련
     */
    private void 모든_메뉴_조회_응답됨(ExtractableResponse<Response> listResponse) {
        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 생성된_메뉴가_포함됨(MenuProduct 볶음짜장면_하나, MenuProduct 삼성짬뽕_하나, Menu 대표메뉴, ExtractableResponse<Response> listResponse) {
        List<Menu> menus = listResponse.jsonPath().getList(".", Menu.class);
        assertThat(menus).first()
                .extracting(menu -> menu.getName(),
                        menu -> menu.getPrice().longValue())
                .containsExactly(대표메뉴.getName(), 대표메뉴.getPrice().longValue());
        assertThat(menus.get(0).getMenuProducts())
                .extracting("productId", "quantity")
                .contains(Tuple.tuple(볶음짜장면_하나.getProductId(), 볶음짜장면_하나.getQuantity()),
                        Tuple.tuple(삼성짬뽕_하나.getProductId(), 삼성짬뽕_하나.getQuantity()));
    }
}
