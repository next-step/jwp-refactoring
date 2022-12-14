package kitchenpos.acceptence;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupRestControllerTest extends AcceptanceSupport {

    private MenuGroup 크리스마스메뉴;
    private MenuGroup 겨울메뉴;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        크리스마스메뉴 = new MenuGroup(1L, "크리스마스메뉴");
        겨울메뉴 = new MenuGroup(2L, "겨울메뉴");
    }

    @Test
    @DisplayName("메뉴 그륩에 등록한다")
    void createMenuGroup() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹을_생성한다(크리스마스메뉴);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("메뉴 그륩 리스트를 불러온다")
    void getMenuGroupList() {
        // when
        MenuGroup 메뉴A = 메뉴그룹을_생성한다(크리스마스메뉴).as(MenuGroup.class);
        MenuGroup 메뉴B = 메뉴그룹을_생성한다(겨울메뉴).as(MenuGroup.class);

        ExtractableResponse<Response> response = 메뉴그룹_리스트를_조회해온다();

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        메뉴그륩_리스트를_비교한다(response, Arrays.asList(메뉴A.getId(), 메뉴B.getId()));
    }

    private ExtractableResponse<Response> 메뉴그룹을_생성한다(MenuGroup menuGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    private void 상태값을_비교한다(int statusCode, HttpStatus created) {
        assertThat(statusCode).isEqualTo(created.value());
    }

    private ExtractableResponse<Response> 메뉴그룹_리스트를_조회해온다() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    private void 메뉴그륩_리스트를_비교한다(ExtractableResponse<Response> response, List<Long> createId) {
        List<MenuGroup> result = response.jsonPath().getList(".", MenuGroup.class);
        List<Long> responseId = result.stream().map(MenuGroup::getId).collect(Collectors.toList());
        assertThat(responseId).containsAll(createId);
    }
}
