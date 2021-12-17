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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 저장한다")
    @Test
    void testCreate() {
        // given
        MenuGroup menuGroup = new MenuGroup("식사류");
        MenuGroup expectedMenuGroup = new MenuGroup(1L, "식사류");

        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(expectedMenuGroup);

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup).isEqualTo(expectedMenuGroup);
    }

    @DisplayName("모든 메뉴 그룹을 가져온다")
    @Test
    void testList() {
        // given
        List<MenuGroup> expectedMenuGroups = Arrays.asList(new MenuGroup("식사류"), new MenuGroup("요리류"));
        given(menuGroupDao.findAll()).willReturn(expectedMenuGroups);

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).isEqualTo(expectedMenuGroups);
    }
}
