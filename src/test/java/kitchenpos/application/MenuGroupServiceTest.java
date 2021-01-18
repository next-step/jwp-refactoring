package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    public void createMenuGroup() {
        MenuGroup menuGroup = new MenuGroup(1L, "두마리메뉴");

        MenuGroup result = menuGroupService.create(menuGroup);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("두마리메뉴");
    }
}
