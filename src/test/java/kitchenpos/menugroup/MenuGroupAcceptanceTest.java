package kitchenpos.menugroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void createMenuGroup() {
        // given
        MenuGroup 추천메뉴 = new MenuGroup("추천메뉴");

        // when
        ExtractableResponse<Response> 메뉴_그룹_등록_요청_응답 = 메뉴_그룹_등록을_요청(추천메뉴);

        // then
        메뉴_그룹_등록됨(메뉴_그룹_등록_요청_응답);
    }

    private void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
        MenuGroup 등록된_메뉴_그룹 = response.as(MenuGroup.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(등록된_메뉴_그룹.getId()).isNotNull(),
                () -> assertThat(등록된_메뉴_그룹.getName()).isEqualTo("추천메뉴")
        );
    }

    public ExtractableResponse<Response> 메뉴_그룹_등록을_요청(MenuGroup menuGroup) {
        return post("/api/menu-groups", menuGroup);
    }
}
