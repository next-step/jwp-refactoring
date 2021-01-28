package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        // given
        MenuGroupRequest menuGroup = new MenuGroupRequest("음료수");

        // when
        MenuGroupResponse response = menuGroupService.create(menuGroup);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("음료수");
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        MenuGroupRequest menuGroup = new MenuGroupRequest("음료수");
        MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroup);

        // when
        List<MenuGroupResponse> list = menuGroupService.list();

        // then
        assertThat(list).extracting("id").contains(savedMenuGroup.getId());
    }


}
