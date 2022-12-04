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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@DisplayName("MenuGroupService 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 양식;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup(1L, "양식");
    }

    @Test
    void 메뉴_그룹_등록() {
        given(menuGroupDao.save(양식)).willReturn(양식);

        MenuGroup savedMenuGroup = menuGroupService.create(양식);

        assertThat(savedMenuGroup).satisfies(menuGroup -> {
           assertEquals(양식.getId(), menuGroup.getId());
           assertEquals(양식.getName(), menuGroup.getName());
        });
    }

    @Test
    void 메뉴_그룹_목록_조회() {
        MenuGroup 중식 = new MenuGroup(2L, "중식");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(양식, 중식));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups).containsExactly(양식, 중식)
        );
    }
}
