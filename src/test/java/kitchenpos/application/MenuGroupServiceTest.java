package kitchenpos.application;

import static kitchenpos.domain.MenuGroupFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

    private MenuGroup 추천메뉴;
    private MenuGroup 인기메뉴;

    @BeforeEach
    void setUp() {
        추천메뉴 = createMenuGroup(1L, "추천메뉴");
        인기메뉴 = createMenuGroup(1L, "인기메뉴");
    }

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        given(menuGroupDao.save(any())).willReturn(추천메뉴);

        MenuGroup savedMenuGroup = menuGroupService.create(추천메뉴);

        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(추천메뉴.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(추천메뉴, 인기메뉴));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(2);
        assertThat(menuGroups).contains(추천메뉴, 인기메뉴);
    }
}
