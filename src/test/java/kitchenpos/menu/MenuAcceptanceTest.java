package kitchenpos.menu;

import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.*;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.menu.MenuGroupAcceptanceTest.메뉴_그룹_생성됨;
import static kitchenpos.product.ProductAcceptanceTest.상품_생성됨;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // given
        MenuGroupResponse 한마리메뉴 = 메뉴_그룹_생성됨(MenuGroupRequest.of("한마리메뉴"));
        ProductResponse 후라이드치킨 = 상품_생성됨(ProductRequest.of("후라이드치킨", new BigDecimal(160000)));
        MenuRequest 치킨세트 = MenuRequest.of("치킨세트",
                BigDecimal.valueOf(35000),
                한마리메뉴.getId(),
                Collections.singletonList(MenuProductRequest.of(후라이드치킨.getId(), 2)));

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(치킨세트);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("상품을 목록을 조회한다.")
    @Test
    void findAllProduct() {
        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회됨();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
        return ofRequest(Method.POST, "/api/menus", menuRequest);
    }

    private ExtractableResponse<Response> 메뉴_목록_조회됨() {
        return ofRequest(Method.GET, "/api/menus");
    }
}

