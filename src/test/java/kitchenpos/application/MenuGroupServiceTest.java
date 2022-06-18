package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 Service 기능 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = new MenuGroup(null, "인기 메뉴");
        when(menuGroupDao.save(any())).thenReturn(menuGroup);

        //when
        MenuGroup result = menuGroupService.create(menuGroup);

        //then
        assertThat(result).isEqualTo(menuGroup);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //given
        MenuGroup menuGroup1 = new MenuGroup(null, "인기 메뉴");
        MenuGroup menuGroup2 = new MenuGroup(null, "단품 메뉴");
        MenuGroup menuGroup3 = new MenuGroup(null, "세트 메뉴");
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup1, menuGroup2, menuGroup3));

        //when
        List<MenuGroup> results = menuGroupService.list();

        //then
        assertThat(results)
                .containsExactlyInAnyOrderElementsOf(Arrays.asList(menuGroup1, menuGroup2, menuGroup3));
    }
}
