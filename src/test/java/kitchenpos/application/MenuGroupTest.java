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

@DisplayName("메뉴 그룹 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 한마리메뉴_메뉴그룹;
    private MenuGroup 두마리메뉴_메뉴그룹;

    @BeforeEach
    void setUp() {
        한마리메뉴_메뉴그룹 = MenuGroup.of(1L, "한마리메뉴");
        두마리메뉴_메뉴그룹 = MenuGroup.of(1L, "두마리메뉴");
    }

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void 메뉴_그룹을_생성할_수_있다() {
        // given
        when(menuGroupDao.save(한마리메뉴_메뉴그룹)).thenReturn(한마리메뉴_메뉴그룹);

        // when
        MenuGroup 저장된_메뉴그룹 = menuGroupService.create(한마리메뉴_메뉴그룹);

        // then
        assertThat(저장된_메뉴그룹.getId()).isEqualTo(한마리메뉴_메뉴그룹.getId());
        assertThat(저장된_메뉴그룹.getName()).isEqualTo(한마리메뉴_메뉴그룹.getName());
    }

    @DisplayName("메뉴 그룹을 조회할 수 있다.")
    @Test
    void 메뉴_그룹을_조회할_수_있다() {
        // given
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(한마리메뉴_메뉴그룹, 두마리메뉴_메뉴그룹));

        // when
        List<MenuGroup> 메뉴그룹_목록 = menuGroupService.list();

        // then
        assertThat(메뉴그룹_목록).hasSize(2);
        assertThat(메뉴그룹_목록).containsExactly(한마리메뉴_메뉴그룹, 두마리메뉴_메뉴그룹);
    }
}
