package menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import menu.domain.MenuGroup;
import menu.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    private ProductResponse 소고기한우;
    private MenuGroupResponse 추천메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();
        추천메뉴 = 메뉴그룹_등록되어있음(MenuGroupRequest.of("추천메뉴"));
        소고기한우 = 상품_등록되어있음(ProductRequest.of("소고기한우", BigDecimal.valueOf(30000)));
    }

    @DisplayName("메뉴 관리")
    @Test
    void handleMenu() {
        MenuRequest menuRequest = MenuRequest.of(
                "소고기+소고기",
                50000,
                추천메뉴.toEntity(),
                Arrays.asList(MenuProductRequest.of(소고기한우.toEntity(), 2L))
        );

        ExtractableResponse<Response> createResponse = 메뉴_생성_요청(menuRequest);
        MenuResponse savedMenu = 메뉴_생성_확인(createResponse);

        ExtractableResponse<Response> findResponse = 모든_메뉴_조회_요청();
        모든_메뉴_조회_확인(findResponse, savedMenu);

    }

    private void 모든_메뉴_조회_확인(ExtractableResponse<Response> findResponse, MenuResponse expected) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<MenuResponse> menuResponses = findResponse.jsonPath().getList(".", MenuResponse.class);
        List<MenuProductResponse> menuProductResponseList = menuResponses.stream()
                .map(menu -> menu.getMenuProductResponses())
                .flatMap(menuProductResponses -> menuProductResponses.stream())
                .collect(Collectors.toList());
        assertAll(
                () -> {
                    List<Long> ids = menuResponses.stream()
                            .map(menuResponse -> menuResponse.getId())
                            .collect(Collectors.toList());
                    assertThat(ids).contains(expected.getId());
                },
                () -> {
                    List<Long> ids = menuProductResponseList.stream()
                            .map(menuProductResponse -> menuProductResponse.getSeq())
                            .collect(Collectors.toList());
                    assertThat(ids).containsAll(expected.createSeqList());
                }
        );
    }

    private ExtractableResponse<Response> 모든_메뉴_조회_요청() {
        return TestApiClient.get("/api/menus");
    }

    private MenuResponse 메뉴_생성_확인(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = createResponse.header("Location");
        assertThat(location).isEqualTo("/api/menus/" + createResponse.as(MenuResponse.class).getId());
        return createResponse.as(MenuResponse.class);
    }

    public static MenuGroupResponse 메뉴그룹_등록되어있음(MenuGroupRequest menuGroup) {
        return TestApiClient.create(menuGroup, "/api/menu-groups").as(MenuGroupResponse.class);
    }

    public static ProductResponse 상품_등록되어있음(ProductRequest product) {
        return TestApiClient.create(product, "/api/products").as(ProductResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
        return TestApiClient.create(menuRequest, "/api/menus");
    }

    public static MenuResponse 메뉴_등록되어있음(String name, int price, MenuGroup menuGroup, List<MenuProductRequest> menuProducts) {
        MenuRequest menu = MenuRequest.of(name, price, menuGroup, menuProducts);
        return 메뉴_생성_요청(menu).as(MenuResponse.class);
    }
}
