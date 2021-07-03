package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup(5L, "기타안주메뉴");
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        MenuGroup created = menuGroupService.create(menuGroup);

        assertThat(created.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    void list() {
        MenuGroup menuGroup1 = new MenuGroup(5L, "기타안주메뉴");
        MenuGroup menuGroup2 = new MenuGroup(6L, "디저트");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup1, menuGroup2));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
                () -> assertThat(menuGroups).isNotEmpty(),
                () -> assertThat(menuGroups).containsExactly(menuGroup1, menuGroup2)
        );
    }
}
