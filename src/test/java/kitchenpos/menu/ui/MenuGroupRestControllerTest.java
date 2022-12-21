package kitchenpos.menu.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.BaseTest;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MenuGroupRestControllerTest extends BaseTest {
    @Test
    void 생성() {
        MenuGroup menuGroup = new MenuGroup(1L, "한마리메뉴");

        ResponseEntity<MenuGroup> response = 메뉴_그룹_생성_요청(menuGroup);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("한마리메뉴");
    }

    @Test
    void 조회() {
        MenuGroup menuGroup = new MenuGroup(1L, "한마리메뉴");
        메뉴_그룹_생성_요청(menuGroup);
        menuGroup = new MenuGroup(2L, "두마리메뉴");
        메뉴_그룹_생성_요청(menuGroup);

        ResponseEntity<List<MenuGroup>> response = 메뉴_그룹_조회_요청();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    public static ResponseEntity<MenuGroup> 메뉴_그룹_생성_요청(MenuGroup menuGroup) {
        return testRestTemplate.postForEntity(basePath + "/api/menu-groups", menuGroup, MenuGroup.class);
    }

    private ResponseEntity<List<MenuGroup>> 메뉴_그룹_조회_요청() {
        return testRestTemplate.exchange(
                basePath + "/api/menu-groups",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MenuGroup>>() {});
    }
}
