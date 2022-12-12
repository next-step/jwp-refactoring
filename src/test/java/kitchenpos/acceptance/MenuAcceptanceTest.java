package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuGroupRestAssured.메뉴_그룹_생성_요청;
import static kitchenpos.acceptance.MenuRestAssured.메뉴_목록_조회_요청;
import static kitchenpos.acceptance.MenuRestAssured.메뉴_생성_요청;
import static kitchenpos.product.acceptance.ProductRestAssured.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.product.domain.Product;
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
    private MenuGroup 피자;
    private Menu 하와이안피자세트;
    private MenuProduct 하와이안피자상품;
    private MenuProduct 콜라상품;
    private MenuProduct 피클상품;

    @BeforeEach
    public void setUp() {
        super.setUp();
        하와이안피자 = 상품_생성_요청(ProductRequest.of("하와이안피자", BigDecimal.valueOf(15_000))).as(ProductResponse.class);
        콜라 = 상품_생성_요청(ProductRequest.of("콜라", BigDecimal.valueOf(2_000))).as(ProductResponse.class);
        피클 = 상품_생성_요청(ProductRequest.of( "피클", BigDecimal.valueOf(1_000))).as(ProductResponse.class);
        피자 = 메뉴_그룹_생성_요청(new MenuGroup(1L, "피자")).as(MenuGroup.class);
        하와이안피자세트 = new Menu(1L, "하와이안피자세트", BigDecimal.valueOf(18_000L), 피자.getId(), new ArrayList<>());
        하와이안피자상품 = new MenuProduct(1L, 하와이안피자세트.getId(), 하와이안피자.getId(), 1L);
        콜라상품 = new MenuProduct(2L, 하와이안피자세트.getId(), 콜라.getId(), 1L);
        피클상품 = new MenuProduct(3L, 하와이안피자세트.getId(), 피클.getId(), 1L);
        하와이안피자세트.setMenuProducts(Arrays.asList(하와이안피자상품, 콜라상품, 피클상품));
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
        하와이안피자세트 = 메뉴_생성_요청(하와이안피자세트).as(Menu.class);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(response);
        메뉴_목록_확인됨(response, Arrays.asList(하와이안피자세트.getId()));
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 메뉴_목록_확인됨(ExtractableResponse<Response> response, List<Long> menuIds) {
        List<Long> resultIds = response.jsonPath().getList(".", Menu.class)
            .stream()
            .map(Menu::getId)
            .collect(Collectors.toList());

        assertThat(resultIds).containsAll(menuIds);
    }
}
