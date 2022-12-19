package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptance.MenuGroupRestAssured.메뉴_그룹_생성_요청;
import static kitchenpos.acceptance.MenuRestAssured.메뉴_목록_조회_요청;
import static kitchenpos.acceptance.MenuRestAssured.메뉴_생성_요청;
import static kitchenpos.acceptance.ProductRestAssured.상품_생성_요청;
import static kitchenpos.domain.MenuGroupTestFixture.createMenuGroup;
import static kitchenpos.domain.MenuTestFixture.createMenuProduct;
import static kitchenpos.domain.ProductTestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 관련 인수 테스트")
public class MenuAcceptanceTest extends AbstractAcceptanceTest {
    private Product 불고기버거;
    private Product 치즈버거;
    private Product 콜라;
    private MenuGroup 햄버거세트;
    private MenuProduct 불고기버거상품;
    private MenuProduct 콜라상품;

    private MenuProduct 치즈버거상품;
    private Menu 불고기버거세트;

    private Menu 치즈버거세트;

    @BeforeEach
    public void setUp() {
        super.setUp();

        햄버거세트 = 메뉴_그룹_생성_요청(createMenuGroup("햄버거세트")).as(MenuGroup.class);
        치즈버거 = 상품_생성_요청(createProduct(null, "치즈버거", BigDecimal.valueOf(6000))).as(Product.class);
        불고기버거 = 상품_생성_요청(createProduct(null, "불고기버거", BigDecimal.valueOf(5000))).as(Product.class);
        콜라 = 상품_생성_요청(createProduct(null, "콜라", BigDecimal.valueOf(3000))).as(Product.class);
        치즈버거상품 = createMenuProduct(1L, null, 치즈버거.getId(), 1L);
        콜라상품 = createMenuProduct(2L, null, 콜라.getId(), 1L);
        불고기버거상품 = createMenuProduct(3L, null, 불고기버거.getId(), 1L);

    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(1L, "불고기버거세트", BigDecimal.valueOf(7500), 햄버거세트.getId(), Arrays.asList(불고기버거상품, 콜라상품));
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("상품 전체 목록을 조회한다.")
    @Test
    void 상품_전체_목록_조회() {
        // given
        ExtractableResponse<Response> response1 = 메뉴_생성_요청(1L, "불고기버거세트", BigDecimal.valueOf(7500), 햄버거세트.getId(), Arrays.asList(불고기버거상품, 콜라상품));
        ExtractableResponse<Response> response2 = 메뉴_생성_요청(2L, "치즈버거세트", BigDecimal.valueOf(8000), 햄버거세트.getId(), Arrays.asList(치즈버거상품, 콜라상품));
        // when
        ExtractableResponse<Response> menuResponse = 메뉴_목록_조회_요청();
        // then
        assertAll(
                () -> assertThat(menuResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> 메뉴_목록_포함_확인(menuResponse, Arrays.asList(response1, response2))
        );
    }

    private static void 메뉴_목록_포함_확인(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> responses) {
        List<Long> expectedMenuIds = responses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());
        List<Long> resultMenuIds = response.jsonPath().getList(".", Menu.class).stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        assertThat(resultMenuIds).containsAll(expectedMenuIds);
    }


}
