package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 그룹 관련 기능")
class MenuGroupAcceptanceTest extends AcceptanceTest {
    private MenuGroup 한식;
    private MenuGroup 양식;

    @BeforeEach
    public void setUp() {
        super.setUp();
        한식 = new MenuGroup(1L, "한식");
        양식 = new MenuGroup(2L, "양식");
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹_생성_요청(한식);

        // then
        메뉴_그룹_생성됨(response);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void findAllMenuGroup() {
        // given
        한식 = 메뉴그룹_생성_요청(한식).as(MenuGroup.class);
        양식 = 메뉴그룹_생성_요청(양식).as(MenuGroup.class);

        // when
        ExtractableResponse<Response> response = 메뉴그룹_목록_조회_요청();

        // then
        메뉴그룹_목록_응답됨(response, Arrays.asList(한식.getId(), 양식.getId()));

    }

    public static ExtractableResponse<Response> 메뉴그룹_생성_요청(MenuGroup menuGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    private void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴그룹_목록_응답됨(ExtractableResponse<Response> response, List<Long> menuGroupIds) {
        List<Long> ids = response.jsonPath().getList(".", MenuGroup.class)
                        .stream()
                        .map(MenuGroup::getId)
                        .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(ids).containsAll(menuGroupIds)
        );
    }
}
