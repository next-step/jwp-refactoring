package kitchenpos.menu;

import static kitchenpos.menu.MenuGroupAcceptanceTest.메뉴그룹_등록_요청;
import static kitchenpos.menu.application.MenuServiceTest.감자튀김_ID;
import static kitchenpos.menu.application.MenuServiceTest.치즈버거_ID;
import static kitchenpos.menu.application.MenuServiceTest.콜라_ID;
import static kitchenpos.menu.ui.MenuRestControllerTest.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptancePerMethodTest {

    @DisplayName("메뉴 관리")
    @Test
    void manage() {
        // When & Then
        메뉴_등록됨();

        // When
        ExtractableResponse<Response> 메뉴_목록_조회_응답 = 메뉴_목록_조회_요청();
        // Then
        메뉴_목록_조회됨(메뉴_목록_조회_응답);
    }

    public static Long 메뉴_등록됨() {
        MenuGroupResponse 패스트푸드 = 메뉴그룹_등록_요청("패스트푸드").as(MenuGroupResponse.class);
        MenuProductRequest 치즈버거세트_치즈버거 = new MenuProductRequest(치즈버거_ID, 1L);
        MenuProductRequest 치즈버거세트_감자튀김 = new MenuProductRequest(감자튀김_ID, 1L);
        MenuProductRequest 치즈버거세트_콜라 = new MenuProductRequest(콜라_ID, 1L);
        List<MenuProductRequest> 메뉴상품목록 = new ArrayList<>(Arrays.asList(치즈버거세트_치즈버거, 치즈버거세트_감자튀김, 치즈버거세트_콜라));
        MenuRequest 치즈버거세트 = new MenuRequest("치즈버거세트", new BigDecimal(6000), 패스트푸드.getId(), 메뉴상품목록);

        // When
        ExtractableResponse<Response> 메뉴_등록_응답 = 메뉴_등록_요청(치즈버거세트);
        메뉴가_등록됨(메뉴_등록_응답);

        return Long.parseLong(메뉴_등록_응답.header("Location").split(BASE_URL + "/")[1]);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return get(BASE_URL);
    }

    private List<MenuResponse> 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<MenuResponse> 목록_조회_응답 = new ArrayList<>(response.jsonPath().getList(".", MenuResponse.class));
        assertThat(목록_조회_응답).hasSize(1);
        return 목록_조회_응답;
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest menuRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", menuRequest.getName());
        params.put("price", menuRequest.getPrice());
        params.put("menuGroupId", menuRequest.getMenuGroupId());
        params.put("menuProducts", menuRequest.getMenuProducts());

        return post(params, BASE_URL);
    }

    public static void 메뉴가_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
