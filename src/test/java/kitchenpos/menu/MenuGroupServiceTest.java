package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    MenuGroupRepository menuGroupRepository;
    @Autowired
    MenuGroupService menuGroupService;

    MenuGroup 양식;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
    }

    @AfterEach
    void tearDown() {
        menuGroupRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("메뉴 그룹 정상 등록 확인")
    public void create() {
        MenuGroup savedMenuGroup = menuGroupService.create(양식);
        assertThat(savedMenuGroup.getName()).isEqualTo(양식.getName());
    }

    @Test
    @DisplayName("전체 조회")
    public void list(){
        MenuGroup 분식 = new MenuGroup("분식");
        menuGroupService.create(양식);
        menuGroupService.create(분식);

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isEqualTo(2);
    }
}