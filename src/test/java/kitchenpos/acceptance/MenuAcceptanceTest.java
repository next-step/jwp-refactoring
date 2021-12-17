package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.*;
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
        MenuGroupResponse 식사류 = MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음("식사류");
        ProductResponse 볶음짜장면 = ProductAcceptanceTest.상품_등록되어_있음("볶음짜장면", 8000);
        ProductResponse 삼선짬뽕 = ProductAcceptanceTest.상품_등록되어_있음("삼선짬뽕", 8000);
        MenuProductRequest 볶음짜장면_하나 = new MenuProductRequest(볶음짜장면.getId(), 1);
        MenuProductRequest 삼성짬뽕_하나 = new MenuProductRequest(삼선짬뽕.getId(), 1);
        MenuRequest 대표메뉴 = new MenuRequest("대표 메뉴", 16000L, 식사류.getId(), Arrays.asList(볶음짜장면_하나, 삼성짬뽕_하나));

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
    private static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menu) {
        return Http.post("/api/menus", menu);
    }

    private static ExtractableResponse<Response> 모든_메뉴_조회_요청() {
        return Http.get("/api/menus");
    }

    private static void 메뉴_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * 응답 관련
     */
    private static void 모든_메뉴_조회_응답됨(ExtractableResponse<Response> listResponse) {
        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 생성된_메뉴가_포함됨(MenuProductRequest menuProductRequest1, MenuProductRequest menuProductRequest2, MenuRequest menuRequest, ExtractableResponse<Response> listResponse) {
        List<MenuResponse> menus = listResponse.jsonPath().getList(".", MenuResponse.class);
        assertThat(menus).first()
                .extracting(menu -> menu.getName(),
                        menu -> menu.getPrice().longValue())
                .containsExactly(menuRequest.getName(), menuRequest.getPrice());
        assertThat(menus.get(0).getMenuProducts())
                .extracting("product.id", "quantity")
                .contains(Tuple.tuple(menuProductRequest1.getProductId(), menuProductRequest1.getQuantity()),
                        Tuple.tuple(menuProductRequest2.getProductId(), menuProductRequest2.getQuantity()));
    }

    /**
     * 테스트 픽스처 관련
     */
    public static MenuResponse 대표메뉴_등록되어_있음() {
        MenuGroupResponse 식사류 = MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음("식사류");
        ProductResponse 볶음짜장면 = ProductAcceptanceTest.상품_등록되어_있음("볶음짜장면", 8000);
        ProductResponse 삼선짬뽕 = ProductAcceptanceTest.상품_등록되어_있음("삼선짬뽕", 8000);
        MenuProductRequest 볶음짜장면_하나 = new MenuProductRequest(볶음짜장면.getId(), 1);
        MenuProductRequest 삼성짬뽕_하나 = new MenuProductRequest(삼선짬뽕.getId(), 1);
        MenuRequest 대표메뉴 = new MenuRequest("대표 메뉴", 16000L, 식사류.getId(), Arrays.asList(볶음짜장면_하나, 삼성짬뽕_하나));
        return 메뉴_생성_요청(대표메뉴).as(MenuResponse.class);
    }
}
