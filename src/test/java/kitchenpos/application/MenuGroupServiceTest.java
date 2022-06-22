package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.util.Lists;
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
    MenuGroup 피자메뉴그룹;
    MenuGroup 스파게티메뉴그룹;

    @BeforeEach
    void setUp() {
        피자메뉴그룹 = MenuGroup.of(1L, "피자메뉴그룹");
        스파게티메뉴그룹 = MenuGroup.of(2L, "스파게티메뉴그룹");
    }

    @DisplayName("메뉴 그룹 생성 테스트")
    @Test
    void create() {
        given(menuGroupDao.save(피자메뉴그룹)).willReturn(피자메뉴그룹);
        MenuGroup menuGroup = menuGroupService.create(피자메뉴그룹);
        assertAll(
                () -> assertThat(menuGroup.getName()).isEqualTo("피자메뉴그룹"),
                () -> assertThat(menuGroup.getId()).isEqualTo(피자메뉴그룹.getId())
        );
    }

    @DisplayName("메뉴 그룹 목록 조회 테스트")
    @Test
    void list() {
        given(menuGroupDao.findAll()).willReturn(Lists.newArrayList(피자메뉴그룹, 스파게티메뉴그룹));
        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups).containsExactlyElementsOf(Lists.newArrayList(피자메뉴그룹, 스파게티메뉴그룹));
    }
}
