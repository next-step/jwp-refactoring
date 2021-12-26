package kitchenpos.menugroup.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("메뉴 그룹 도메인 테스트")
class MenuGroupTest {
    private MenuGroup 중국집_메뉴;

    @BeforeEach
    void setUp() {
        중국집_메뉴 = new MenuGroup("중국집_메뉴");
    }

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void createMenuGroupTest() {
        assertThat(중국집_메뉴.getName()).isEqualTo("중국집_메뉴");
    }
}