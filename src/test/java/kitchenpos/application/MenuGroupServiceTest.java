package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.creator.MenuGroupHelper;
import kitchenpos.dto.MenuGroupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-12
 */
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성 테스트")
    @Test
    void menuGroupCreateTest() {
        MenuGroupDto menuGroup = menuGroupService.create(MenuGroupHelper.create("메뉴 그룹"));
        assertThat(menuGroup.getId()).isNotNull();
        assertThat(menuGroup.getName()).isEqualTo("메뉴 그룹");
    }

    @DisplayName("메뉴 그룹 조회 테스트")
    @Test
    void menuGroupListTest() {
        menuGroupService.create(MenuGroupHelper.create("메뉴 그룹 01"));
        menuGroupService.create(MenuGroupHelper.create("메뉴 그룹 02"));
        menuGroupService.create(MenuGroupHelper.create("메뉴 그룹 03"));

        List<MenuGroupDto> list = menuGroupService.list();
        assertThat(list.size()).isGreaterThan(2);
    }

}
