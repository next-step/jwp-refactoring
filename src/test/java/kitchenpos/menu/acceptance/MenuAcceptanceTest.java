package kitchenpos.menu.acceptance;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.BaseAcceptanceTest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.product.fixture.ProductFixture;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.rest.MenuRestAssured;
import kitchenpos.rest.MenuGroupRestAssured;
import kitchenpos.product.rest.ProductRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuAcceptanceTest extends BaseAcceptanceTest {

    private ProductResponse product;
    private MenuGroupResponse menuGroup;
    private List<MenuProductRequest> menuProductRequests;

    @BeforeEach
    public void setUp() {
        super.setUp();

        this.product = ProductRestAssured.상품_등록됨(ProductFixture.후라이드);
        this.menuGroup = MenuGroupRestAssured.메뉴_그룹_등록됨(MenuGroupFixture.한마리메뉴);
        this.menuProductRequests = Arrays.asList(new MenuProductRequest(product.getId(), 2L));
    }

    @Test
    @DisplayName("신규 메뉴 정보가 주어진 경우 메뉴 등록 요청시 요청에 성공한다")
    void createMenuThenReturnMenuInfoResponse() {
        // given
        MenuCreateRequest request = new MenuCreateRequest(
                "후라이드치킨",
                product.getPrice(),
                menuGroup.getId(),
                menuProductRequests
        );

        // when
        ExtractableResponse<Response> response = MenuRestAssured.메뉴_등록_요청(request);

        // then
        MenuResponse menuResponse = response.as(MenuResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(menuResponse.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("메뉴 목록 조회 요청시 요청에 성공한다")
    void findAllMenusThenReturnMenuInfoResponses() {
        // given
        MenuResponse menu = MenuRestAssured.메뉴_등록됨("후라이드치킨", product.getPrice(), menuGroup.getId(), menuProductRequests).as(MenuResponse.class);

        // when
        ExtractableResponse<Response> response = MenuRestAssured.메뉴_목록_조회();

        // then
        List<MenuResponse> menuResponses = response.as(new TypeRef<List<MenuResponse>>() {});
        List<String> menuNames = menuResponses.stream()
                .map(MenuResponse::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(menuResponses).hasSize(1),
                () -> assertThat(menuNames).containsExactly(menu.getName())
        );
    }
}
