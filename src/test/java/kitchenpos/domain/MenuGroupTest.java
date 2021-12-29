package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 도메인 테스트")
class MenuGroupTest {
    private MenuGroup 메뉴그룹;

    @BeforeEach
    void setUp() {
        메뉴그룹 = MenuGroup.from("메뉴그룹");
    }

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void createMenuGroup() {
        assertThat(메뉴그룹.getName()).isEqualTo("메뉴그룹");
    }
}
