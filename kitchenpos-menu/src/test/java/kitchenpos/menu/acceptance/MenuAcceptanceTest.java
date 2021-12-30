package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import kitchenpos.menugroup.MenuGroupAcceptanceFixtures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import kitchenpos.menu.MenuAcceptanceFixtures;
import kitchenpos.product.ProductAcceptanceFixtures;

public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 추천메뉴;
    private ProductResponse 양념치킨;
    private ProductResponse 후라이드치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // background
        추천메뉴 = MenuGroupAcceptanceFixtures.메뉴그룹_생성_요청(
            MenuGroupAcceptanceFixtures.메뉴그룹_정의("추천메뉴")).getBody();
        양념치킨 = ProductAcceptanceFixtures.상품_생성_요청(
            ProductAcceptanceFixtures.상품_정의("양념치킨", BigDecimal.valueOf(10000))).getBody();
        후라이드치킨 = ProductAcceptanceFixtures.상품_생성_요청(
            ProductAcceptanceFixtures.상품_정의("후라이드", BigDecimal.valueOf(9000))).getBody();
    }

    @DisplayName("메뉴 등록")
    @Test
    void create() {
        //given
        MenuProductRequest 양념치킨_하나 = MenuAcceptanceFixtures.메뉴상품_정의(양념치킨.getId(), 1);
        MenuProductRequest 후라이드치킨_둘 = MenuAcceptanceFixtures.메뉴상품_정의(후라이드치킨.getId(), 2);
        MenuRequest 후라이드_앤드_양념 = MenuAcceptanceFixtures.메뉴_정의("후라이드와 양념", BigDecimal.valueOf(27000),
            추천메뉴.getId(), Arrays.asList(양념치킨_하나, 후라이드치킨_둘));

        //when
        ResponseEntity<MenuResponse> 메뉴_생성_결과 = MenuAcceptanceFixtures.메뉴_생성_요청(
            후라이드_앤드_양념);

        //then
        메뉴_생성_정상_확인(메뉴_생성_결과);
        메뉴_생성_세부내용_확인(메뉴_생성_결과, 후라이드_앤드_양념);
    }

    @DisplayName("메뉴 조회")
    @Test
    void list() {
        //given
        MenuProductRequest 양념치킨_하나 = MenuAcceptanceFixtures.메뉴상품_정의(양념치킨.getId(), 1);
        MenuProductRequest 후라이드치킨_둘 = MenuAcceptanceFixtures.메뉴상품_정의(후라이드치킨.getId(), 2);
        MenuResponse 후라이드_앤드_양념_생성됨 = MenuAcceptanceFixtures.메뉴_생성_요청(
            MenuAcceptanceFixtures.메뉴_정의("후라이드와 양념", BigDecimal.valueOf(27000),
                추천메뉴.getId(), Arrays.asList(양념치킨_하나, 후라이드치킨_둘))).getBody();

        //when
        ResponseEntity<List<MenuResponse>> 조회_결과 = MenuAcceptanceFixtures.메뉴_전체_조회_요청();

        //then
        조회_정상(조회_결과);
        조회목록_정상(조회_결과, Arrays.asList(후라이드_앤드_양념_생성됨));
    }

    private void 조회_정상(ResponseEntity<List<MenuResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void 메뉴_생성_정상_확인(ResponseEntity<MenuResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private void 메뉴_생성_세부내용_확인(ResponseEntity<MenuResponse> response,
        MenuRequest menuRequest) {
        MenuResponse createdMenuResponse = response.getBody();
        assertThat(createdMenuResponse.getName()).isEqualTo(menuRequest.getName());
    }

    private void 조회목록_정상(ResponseEntity<List<MenuResponse>> response,
        List<MenuResponse> expectedMenus) {
        List<MenuResponse> menuResponses = response.getBody();
        assertThat(menuResponses).containsAll(expectedMenus);
    }
}
