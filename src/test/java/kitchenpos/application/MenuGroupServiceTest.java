package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("점심 특선 메뉴"));
        assertThat(menuGroup.getName()).isEqualTo("점심 특선 메뉴");
    }

    @DisplayName("메뉴 그룹 조회")
    @Test
    void list() {
        assertThat(menuGroupService.list()).isNotEmpty();
    }
}
