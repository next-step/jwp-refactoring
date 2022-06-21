package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.application.MenuGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스에 대한 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 치킨_그룹;
    private MenuGroup 피자_그룹;

    @BeforeEach
    void setUp() {
        치킨_그룹 = MenuGroup.of(1L, "치킨 메뉴 그룹");
        피자_그룹 = MenuGroup.of(2L, "피자 메뉴 그룹");
    }

    @DisplayName("메뉴그룹을 등록하면 정상적으로 등록되어야 한다")
    @Test
    void create_test() {
        // given
        when(menuGroupDao.save(치킨_그룹))
            .thenReturn(치킨_그룹);

        // when
        MenuGroup result = menuGroupService.create(치킨_그룹);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(치킨_그룹.getId()),
            () -> assertThat(result.getName()).isEqualTo(치킨_그룹.getName())
        );
    }

    @DisplayName("메뉴그룹의 목록을 조회한다")
    @Test
    void findAll_test() {
        // given
        when(menuGroupDao.findAll())
            .thenReturn(Arrays.asList(치킨_그룹, 피자_그룹));

        // when
        List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
