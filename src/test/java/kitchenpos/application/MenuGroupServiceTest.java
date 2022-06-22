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
    MenuGroup 두마리메뉴;
    MenuGroup 한마리메뉴;

    @BeforeEach
    void setUp() {
        두마리메뉴 = MenuGroup.of(1L, "두마리메뉴");
        한마리메뉴 = MenuGroup.of(2L, "한마리메뉴");
    }

    @DisplayName("메뉴 그룹 생성 테스트")
    @Test
    void create() {
        given(menuGroupDao.save(두마리메뉴)).willReturn(두마리메뉴);
        MenuGroup menuGroup = menuGroupService.create(두마리메뉴);
        assertAll(
                () -> assertThat(menuGroup.getName()).isEqualTo("두마리메뉴"),
                () -> assertThat(menuGroup.getId()).isEqualTo(두마리메뉴.getId())
        );
    }

    @DisplayName("메뉴 그룹 목록 조회 테스트")
    @Test
    void list() {
        given(menuGroupDao.findAll()).willReturn(Lists.newArrayList(두마리메뉴, 한마리메뉴));
        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups).containsExactlyElementsOf(Lists.newArrayList(두마리메뉴, 한마리메뉴));
    }
}
