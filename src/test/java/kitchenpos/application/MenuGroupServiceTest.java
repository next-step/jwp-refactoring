package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setup() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createMenuGroupTest() {
        // given
        Long menuGroupId = 1L;
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("testMenuGroup");
        MenuGroup savedMenuGroup = new MenuGroup();
        savedMenuGroup.setId(menuGroupId);
        given(menuGroupDao.save(any())).willReturn(savedMenuGroup);

        // when
        MenuGroup saved = menuGroupService.create(menuGroupRequest);

        // then
        assertThat(saved.getId()).isEqualTo(menuGroupId);
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void getMenuGroupsTest() {
        // given
        MenuGroup menuGroup1 = new MenuGroup();
        MenuGroup menuGroup2 = new MenuGroup();
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup1, menuGroup2));

        // when
        List<MenuGroup> foundMenuGroups = menuGroupService.list();

        // then
        assertThat(foundMenuGroups).contains(menuGroup1, menuGroup2);
    }
}