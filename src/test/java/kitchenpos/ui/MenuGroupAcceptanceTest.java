package kitchenpos.ui;

import static kitchenpos.utils.AcceptanceTestUtil.get;
import static kitchenpos.utils.AcceptanceTestUtil.post;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.MenuGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void createMenuGroup() {
        // when
        ExtractableResponse<Response> 메뉴그룹_응답 = 메뉴그룹_생성(MenuGroup.of("추천메뉴"));

        // then
        메뉴그룹_생성됨(메뉴그룹_응답);
    }

    @DisplayName("메뉴 그룹을 조회한다")
    @Test
    void readMenuGroups() {
        // when
        ExtractableResponse<Response> 메뉴그룹_조회_응답 = 메뉴그룹_조회();

        // then
        메뉴그룹_목록_조회됨(메뉴그룹_조회_응답);
    }

    private ExtractableResponse<Response> 메뉴그룹_생성(MenuGroup menuGroup) {
        return post("/api/menu-groups", menuGroup);
    }

    private void 메뉴그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.as(MenuGroup.class).getName()).isEqualTo("추천메뉴");
    }

    private ExtractableResponse<Response> 메뉴그룹_조회() {
        return get("/api/menu-groups");
    }

    private void 메뉴그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<MenuGroup> menuGroups = Lists.newArrayList(response.as(MenuGroup[].class));
        assertThat(menuGroups).hasSize(4);
        assertThat(menuGroups).extracting(MenuGroup::getName)
            .contains("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴");
    }

}
