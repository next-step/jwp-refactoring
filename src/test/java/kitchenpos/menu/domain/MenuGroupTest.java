package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupTest {

    @Test
    @DisplayName("메뉴그룹이 잘 생성되는지 확인")
    void 메뉴그룹_생성() {
        // given, when
        MenuGroup 메뉴그룹 = MenuGroup.from("메뉴그룹");
        
        // then
        assertThat(메뉴그룹).isEqualTo(MenuGroup.from("메뉴그룹"));
    }

}
