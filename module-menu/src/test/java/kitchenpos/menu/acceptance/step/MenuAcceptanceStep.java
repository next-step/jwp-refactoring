package kitchenpos.menu.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.util.HttpUtil;
import org.assertj.core.api.Assertions;

public class MenuAcceptanceStep {

    private static final String API_URL = "/api/menus";

    private MenuAcceptanceStep() {
    }

    public static Long 메뉴_등록됨(MenuRequest menu) {
        return 메뉴_등록_검증(메뉴_등록_요청(menu), menu);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest menu) {
        return HttpUtil.post(API_URL, menu);
    }

    public static ExtractableResponse<Response> 메뉴_목록조회_요청() {
        return HttpUtil.get(API_URL);
    }

    public static Long 메뉴_등록_검증(ExtractableResponse<Response> response,
        MenuRequest expected) {
        MenuResponse 등록된_메뉴 = response.as(MenuResponse.class);
        assertAll(
            () -> assertThat(등록된_메뉴.getId()).isNotNull(),
            () -> assertThat(등록된_메뉴.getPrice()).isEqualByComparingTo(
                BigDecimal.valueOf(expected.getPrice())),
            () -> assertThat(등록된_메뉴.getMenuGroupId()).isEqualTo(expected.getMenuGroupId())
        );

        return 등록된_메뉴.getId();
    }

    public static List<MenuResponse> 메뉴_목록조회_검증(ExtractableResponse<Response> response,
        Long expected) {
        List<MenuResponse> 조회된_메뉴_목록 = response.as(new TypeRef<List<MenuResponse>>() {
        });

        assertThat(조회된_메뉴_목록).extracting("id").contains(expected);
        return 조회된_메뉴_목록;
    }
}
