package kitchenpos.menu.ui;

import io.restassured.RestAssured;
import kitchenpos.menu.MenuUiTest;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static kitchenpos.menu.domain.MenuGroupTestFixture.메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 ui 테스트")
class MenuGroupRestControllerTest extends MenuUiTest {

    private MenuGroup 메뉴_그룹;

    @BeforeEach
    void setup() {
        super.setUp();
        메뉴_그룹 = 메뉴_그룹("group");
    }

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        //when:
        final MenuGroup 저장된_메뉴_그룹 = 메뉴_그룹_생성(메뉴_그룹);
        //then:
        assertThat(저장된_메뉴_그룹).isEqualTo(메뉴_그룹);
    }

    @DisplayName("목록 조회 성공")
    @Test
    void 목록_조회_성공() throws Exception {
        //given:
        메뉴_그룹_생성(메뉴_그룹);
        //when:
        List<MenuGroup> 메뉴_그룹_목록 = 메뉴_그룹_조회();
        //then:
        assertThat(메뉴_그룹_목록).isNotEmpty();
    }

    MenuGroup 메뉴_그룹_생성(MenuGroup menuGroup) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().body().as(MenuGroup.class);
    }

    List<MenuGroup> 메뉴_그룹_조회() {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", MenuGroup.class);
    }
}
