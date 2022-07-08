package kitchenpos.menu.acceptance;

import acceptance.ProductAcceptanceMethods;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static acceptance.ProductAcceptanceMethods.*;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_등록되어_있음;
import static kitchenpos.utils.RestAssuredMethods.get;
import static kitchenpos.utils.RestAssuredMethods.post;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능 인수테스트")
public class MenuAcceptanceTest extends AcceptanceTest {
    private ProductResponse 김치찌개_product;
    private ProductResponse 공기밥_product;
    private MenuGroupResponse 한식_menuGroup;

    private MenuRequest 김치찌개1인세트_menu;
    private MenuRequest 김치찌개2인세트_menu;
    private MenuProductRequest 김치찌개_menuProduct_1인;
    private MenuProductRequest 공기밥_menuProduct_1인;
    private MenuProductRequest 김치찌개_menuProduct_2인;
    private MenuProductRequest 공기밥_menuProduct_2인;

    @BeforeEach
    public void setUp() {
        super.setUp();
        한식_menuGroup = 메뉴그룹_등록되어_있음("한식").as(MenuGroupResponse.class);
        김치찌개_product = 상품_등록되어_있음("김치찌개", 8000).as(ProductResponse.class);
        공기밥_product = 상품_등록되어_있음("공기밥", 1000).as(ProductResponse.class);

        김치찌개_menuProduct_1인 = MenuProductRequest.of(김치찌개_product.getId(), 1);
        공기밥_menuProduct_1인 = MenuProductRequest.of(공기밥_product.getId(), 1);
        김치찌개_menuProduct_2인 = MenuProductRequest.of(김치찌개_product.getId(), 2);
        공기밥_menuProduct_2인 = MenuProductRequest.of(공기밥_product.getId(), 2);

        김치찌개1인세트_menu = MenuRequest.of("김치찌개1인세트", 8500, 한식_menuGroup.getId(),
                Arrays.asList(김치찌개_menuProduct_1인, 공기밥_menuProduct_1인));
        김치찌개2인세트_menu = MenuRequest.of("김치찌개2인세트", 15000, 한식_menuGroup.getId(),
                Arrays.asList(김치찌개_menuProduct_2인, 공기밥_menuProduct_2인));
    }

    /**
     * Feature: 메뉴 관련 기능
     *
     *   Scenario: 메뉴를 관리
     *     When 김치찌개 1인세트 메뉴 등록 요청
     *     Then 김치찌개 1인세트 메뉴 등록됨
     *     When 김치찌개 2인세트 메뉴 등록 요청
     *     Then 김치찌개 2인세트 메뉴 등록됨
     *     When 메뉴 조회 요청
     *     Then 김치찌개 1인세트, 김치찌개 2인세트 메뉴 조회됨
     */
    @DisplayName("메뉴를 관리한다")
    @Test
    void 메뉴_관리_정상_시나리오() {
        ExtractableResponse<Response> 김치찌개_1인세트_등록 = 메뉴_등록_요청(김치찌개1인세트_menu);
        메뉴_등록됨(김치찌개_1인세트_등록);

        ExtractableResponse<Response> 김치찌개_2인세트_등록 = 메뉴_등록_요청(김치찌개2인세트_menu);
        메뉴_등록됨(김치찌개_2인세트_등록);

        ExtractableResponse<Response> 메뉴_목록_조회 = 메뉴_목록_조회_요청();
        메뉴_목록_응답됨(메뉴_목록_조회);
        메뉴_목록_포함됨(메뉴_목록_조회, Arrays.asList(김치찌개_1인세트_등록, 김치찌개_2인세트_등록));
    }

    /**
     * Feature: 메뉴 관련 기능
     *
     *   Scenario: 메뉴를 관리 실패
     *     When 가격 0 미만 메뉴 등록 요청
     *     Then 메뉴 등록 실패함
     *     When 존재하지 않는 메뉴그룹에 메뉴 등록 요청
     *     Then 메뉴 등록 실패함
     *     When 존재하지 않는 상품으로 메뉴 등록 요청
     *     Then 메뉴 등록 실패함
     *     When 상품의 정가의 합보다 비싼 가격으로 메뉴 등록 요청
     *     Then 메뉴 등록 실패함
     */
    @DisplayName("메뉴 관리에 실패한다")
    @Test
    void 상품_관리_비정상_시나리오() {
        MenuRequest 가격_0미만_menuRequest = MenuRequest.of("가격 0미만 메뉴", -8000, 한식_menuGroup.getId(),
                Arrays.asList(김치찌개_menuProduct_1인, 공기밥_menuProduct_1인));
        ExtractableResponse<Response> 가격_0미만_등록 = 메뉴_등록_요청(가격_0미만_menuRequest);
        메뉴_등록_실패됨(가격_0미만_등록);

        MenuRequest 없는_메뉴그룹_menuRequest = MenuRequest.of("없는 메뉴그룹", 8500, 0L,
                Arrays.asList(김치찌개_menuProduct_1인, 공기밥_menuProduct_1인));
        ExtractableResponse<Response> 없는_메뉴그룹_등록 = 메뉴_등록_요청(없는_메뉴그룹_menuRequest);
        메뉴_등록_실패됨(없는_메뉴그룹_등록);

        MenuProductRequest 없는_상품 = MenuProductRequest.of(0L, 10000);
        MenuRequest 없는_상품_menuRequest = MenuRequest.of("없는 상품", 8500, 0L,
                Arrays.asList(없는_상품));
        ExtractableResponse<Response> 없는_상품_등록 = 메뉴_등록_요청(없는_상품_menuRequest);
        메뉴_등록_실패됨(없는_상품_등록);

        MenuRequest 정가보다_비싼_메뉴_menuRequest = MenuRequest.of("정가보다 비싼 메뉴", 20000, 0L,
                Arrays.asList(김치찌개_menuProduct_1인, 공기밥_menuProduct_1인));
        ExtractableResponse<Response> 정가보다_비싼_메뉴_등록 = 메뉴_등록_요청(정가보다_비싼_메뉴_menuRequest);
        메뉴_등록_실패됨(정가보다_비싼_메뉴_등록);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest params) {
        return post("/api/menus", params);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return get("/api/menus");
    }

    public static void 메뉴_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 메뉴_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedMenuIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultMenuIds = response.jsonPath().getList(".", MenuResponse.class).stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultMenuIds).containsAll(expectedMenuIds);
    }

    public static void 메뉴_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 메뉴_등록되어_있음(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return 메뉴_등록_요청(MenuRequest.of(name, price, menuGroupId, menuProducts));
    }
}
