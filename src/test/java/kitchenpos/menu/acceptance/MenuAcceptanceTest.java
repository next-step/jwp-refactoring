package kitchenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest.*;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private ProductResponse 후라이드치킨;
    private MenuGroupResponse 한마리메뉴그룹;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        후라이드치킨 = 상품_등록되어_있음(new ProductRequest("후라이드치킨", new BigDecimal(16_000)));
        한마리메뉴그룹 = 메뉴_그룹_등록되어_있음(new MenuGroupRequest("한마리메뉴"));
    }

    @Test
    @DisplayName("메뉴를 관리한다.")
    void manageMenu() {
        // given
        MenuRequest menuRequest = new MenuRequest(
                후라이드치킨.getName(),
                후라이드치킨.getPrice(),
                한마리메뉴그룹.getId(),
                Collections.singletonList(new MenuProductRequest(후라이드치킨.getId(), 1))
        );

        // when
        ExtractableResponse<Response> createResponse = 메뉴_등록_요청(menuRequest);

        // then
        메뉴_등록됨(createResponse);

        // when
        ExtractableResponse<Response> listResponse = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회됨(listResponse);
    }

    public static MenuResponse 메뉴_등록되어_있음(MenuRequest menuRequest) {
        return 메뉴_등록_요청(menuRequest).as(MenuResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest menuRequest) {
        return post("/api/menus", menuRequest);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return get("/api/menus");
    }

    private void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", MenuResponse.class).size()).isOne();
    }
}
