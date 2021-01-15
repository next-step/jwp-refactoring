package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 서비스")
public class MenuGroupServiceTest extends ServiceTestBase {
    private final MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        setUpMenuGroup();
    }

    @Autowired
    public MenuGroupServiceTest(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void createProduct() {
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("모든 메뉴 그룹을 조회한다")
    @Test
    void findAllProduct() {
        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups.size()).isEqualTo(1);
        assertThat(menuGroups.get(0)).isEqualTo(menuGroup);
    }
}
