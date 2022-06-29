package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 양식;
    private MenuGroup 한식;

    @BeforeEach
    void init() {
        양식 = new MenuGroup(1L, "양식");
        한식 = new MenuGroup(2L, "한식");
    }

    @Test
    @DisplayName("메뉴그룹을 생성한다.")
    void createMenuGroup() {
        // given
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(양식);

        // when
        MenuGroup menuGroup = menuGroupService.create(양식);

        // then
        assertAll(
            () -> assertThat(menuGroup).isNotNull(),
            () -> assertThat(menuGroup.getName()).isEqualTo(양식.getName())
        );
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회한다.")
    void findAll() {
        // given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(양식, 한식));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).containsExactly(양식, 한식);
    }
}
