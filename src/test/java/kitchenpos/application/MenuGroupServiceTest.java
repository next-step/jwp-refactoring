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
import static org.mockito.Mockito.when;

@DisplayName("메뉴 그룹 관리 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("추천 메뉴");
    }

    @Test
    void 메뉴_그룹_생성() {
        when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);
        MenuGroup expected = 메뉴_그룹_저장(menuGroup);

        assertThat(expected.getId()).isEqualTo(menuGroup.getId());
        assertThat(expected.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    void 등록된_메뉴_그룹_리스트_조회() {
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup));

        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups.size()).isEqualTo(1);
        MenuGroup expected = menuGroups.get(0);
        assertThat(expected.getId()).isEqualTo(menuGroup.getId());
        assertThat(expected.getName()).isEqualTo(menuGroup.getName());
    }

    private MenuGroup 메뉴_그룹_저장(MenuGroup menuGroup) {
        return menuGroupService.create(menuGroup);
    }
}