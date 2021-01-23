package kitchenpos.ui;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class MenuGroupRestControllerTest extends ControllerTest {

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    void create() throws Exception {
        String body = objectMapper.writeValueAsString(new MenuGroup("세마리메뉴"));
        컨트롤러_생성_요청_및_검증(MENU_GROUP_URI, body);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void list() throws Exception {
        컨트롤러_조회_요청_및_검증(MENU_GROUP_URI);
    }

}