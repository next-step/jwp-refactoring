package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
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
@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupTest {
    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setName("짜장면 세트");
        menuGroup.setId(1L);
    }

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void createMenuGroup() {
        // given
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        // given
        MenuGroup createMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(createMenuGroup).isNotNull();
    }

    @Test
    @DisplayName("메뉴 그룹을 조회한다.")
    void getMenuGroup() {
        // given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup));

        // given
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).isNotNull();
    }
}
