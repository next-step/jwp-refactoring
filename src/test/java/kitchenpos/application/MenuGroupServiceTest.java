package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private MenuGroup 후라이드치킨 = new MenuGroup(1L, "후라이드치킨");
    private MenuGroup 양념치킨 = new MenuGroup(2L, "양념치킨");
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    void 메뉴그룹을_등록할_수_있다() {
        given(menuGroupDao.save(후라이드치킨)).willReturn(후라이드치킨);

        MenuGroup 저장된_후라이드치킨 = menuGroupService.create(후라이드치킨);

        assertAll(
                () -> assertThat(저장된_후라이드치킨.getId()).isEqualTo(1L),
                () -> assertThat(저장된_후라이드치킨.getName()).isEqualTo("후라이드치킨")
        );
    }

    @Test
    void 메뉴그룹_목록을_조회할_수_있다() {
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(후라이드치킨, 양념치킨));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups).containsExactly(후라이드치킨, 양념치킨)
        );
    }
}
