package kitchenpos.menu.acceptance;

import static kitchenpos.menugroup.acceptance.MenuGroupRestAssured.메뉴_그룹_등록되어_있음;
import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_등록되어_있음;
import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_목록_조회_요청;
import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_생성_요청;
import static kitchenpos.product.acceptance.ProductRestAssured.상품_등록되어_있음;
import static kitchenpos.menugroup.domain.MenuGroupTestFixture.generateMenuGroupRequest;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProductRequest;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenuRequest;
import static kitchenpos.product.domain.ProductTestFixture.generateProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.acceptance.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.acceptance.ProductRestAssured;
import kitchenpos.product.domain.ProductTestFixture;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 관련 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private ProductResponse 감자튀김;
    private ProductResponse 불고기버거;
    private ProductResponse 치킨버거;
    private ProductResponse 콜라;
    private MenuGroupResponse 햄버거세트;
    private List<MenuProductRequest> 불고기버거상품요청 = new ArrayList<>();
    private List<MenuProductRequest> 치킨버거상품요청 = new ArrayList<>();
    private MenuRequest 불고기버거세트요청;
    private MenuRequest 치킨버거세트요청;

    @BeforeEach
    public void setUp() {
        super.setUp();
        햄버거세트 = 메뉴_그룹_등록되어_있음(generateMenuGroupRequest("햄버거세트")).as(MenuGroupResponse.class);
        감자튀김 = ProductRestAssured.상품_등록되어_있음(ProductTestFixture.generateProductRequest("감자튀김", BigDecimal.valueOf(3000L))).as(ProductResponse.class);
        콜라 = ProductRestAssured.상품_등록되어_있음(ProductTestFixture.generateProductRequest("콜라", BigDecimal.valueOf(1500L))).as(ProductResponse.class);
        불고기버거 = ProductRestAssured.상품_등록되어_있음(ProductTestFixture.generateProductRequest("불고기버거", BigDecimal.valueOf(4000L))).as(ProductResponse.class);
        치킨버거 = ProductRestAssured.상품_등록되어_있음(ProductTestFixture.generateProductRequest("치킨버거", BigDecimal.valueOf(4500L))).as(ProductResponse.class);
        불고기버거상품요청.add(generateMenuProductRequest(감자튀김.getId(), 1L));
        불고기버거상품요청.add(generateMenuProductRequest(콜라.getId(), 1L));
        불고기버거상품요청.add(generateMenuProductRequest(불고기버거.getId(), 1L));
        치킨버거상품요청.add(generateMenuProductRequest(감자튀김.getId(), 1L));
        치킨버거상품요청.add(generateMenuProductRequest(콜라.getId(), 1L));
        치킨버거상품요청.add(generateMenuProductRequest(치킨버거.getId(), 1L));
        불고기버거세트요청 = generateMenuRequest("불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트.getId(), 불고기버거상품요청);
        치킨버거세트요청 = generateMenuRequest("치킨버거세트", BigDecimal.valueOf(9000L), 햄버거세트.getId(), 치킨버거상품요청);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(불고기버거세트요청);

        // then
        메뉴_생성됨(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void findAllProducts() {
        // given
        ExtractableResponse<Response> 불고기버거_생성_응답 = 메뉴_등록되어_있음(불고기버거세트요청);
        ExtractableResponse<Response> 치킨버거_생성_응답 = 메뉴_등록되어_있음(치킨버거세트요청);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(response);
        메뉴_목록_포함됨(response, Arrays.asList(불고기버거_생성_응답, 치킨버거_생성_응답));
    }

    private static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    private static void 메뉴_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 메뉴_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedMenuIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultMenuIds = response.jsonPath().getList(".", MenuResponse.class).stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultMenuIds).containsAll(expectedMenuIds);
    }
}
