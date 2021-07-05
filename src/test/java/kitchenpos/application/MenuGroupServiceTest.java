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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 추천메뉴;
    private MenuGroup 인기메뉴;

    @BeforeEach
    void setUp() {
        추천메뉴 = new MenuGroup(1L, "추천메뉴");
        인기메뉴 = new MenuGroup(2L, "인기메뉴");
    }

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        given(menuGroupDao.save(추천메뉴)).willReturn(추천메뉴);

        // when
        MenuGroup createdMenuGroup = menuGroupService.create(추천메뉴);

        // then
        assertThat(createdMenuGroup.getId()).isEqualTo(추천메뉴.getId());
        assertThat(createdMenuGroup.getName()).isEqualTo(추천메뉴.getName());
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(추천메뉴, 인기메뉴));

        // when
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        // then
        assertThat(menuGroups).containsExactly(추천메뉴, 인기메뉴);
    }
}
