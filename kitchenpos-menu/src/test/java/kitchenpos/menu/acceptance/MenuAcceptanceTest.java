package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_목록_조회_요청;
import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_생성_요청;
import static kitchenpos.menugroup.acceptance.MenuGroupRestAssured.메뉴_그룹_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.acceptance.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.acceptance.ProductRestAssured;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 관련 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private ProductResponse 하와이안피자;
    private ProductResponse 콜라;
    private ProductResponse 피클;
    private MenuGroupResponse 피자;
    private MenuRequest 하와이안피자세트;
    private MenuProductRequest 하와이안피자상품;
    private MenuProductRequest 콜라상품;
    private MenuProductRequest 피클상품;

    @BeforeEach
    public void setUp() {
        super.setUp();

        하와이안피자 = ProductRestAssured.상품_생성_요청(ProductRequest.of("하와이안피자", BigDecimal.valueOf(15_000))).as(ProductResponse.class);
        콜라 = ProductRestAssured.상품_생성_요청(ProductRequest.of("콜라", BigDecimal.valueOf(2_000))).as(ProductResponse.class);
        피클 = ProductRestAssured.상품_생성_요청(ProductRequest.of( "피클", BigDecimal.valueOf(1_000))).as(ProductResponse.class);

        피자 = 메뉴_그룹_생성_요청(MenuGroupRequest.from("피자")).as(MenuGroupResponse.class);

        하와이안피자상품 = MenuProductRequest.of(하와이안피자.getId(), 1L);
        콜라상품 = MenuProductRequest.of(콜라.getId(), 1L);
        피클상품 = MenuProductRequest.of(피클.getId(), 1L);

        하와이안피자세트 = MenuRequest.of(
            "불고기정식",
            BigDecimal.valueOf(18_000L),
            피자.getId(),
            Arrays.asList(하와이안피자상품, 콜라상품, 피클상품)
        );
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(하와이안피자세트);

        // then
        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void findAllMenu() {
        // given
        MenuResponse menuResponse = 메뉴_생성_요청(하와이안피자세트).as(MenuResponse.class);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(response);
        메뉴_목록_확인됨(response, Arrays.asList(menuResponse.getId()));
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 메뉴_목록_확인됨(ExtractableResponse<Response> response, List<Long> menuIds) {
        List<Long> resultIds = response.jsonPath().getList(".", MenuResponse.class)
            .stream()
            .map(MenuResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultIds).containsAll(menuIds);
    }
}
