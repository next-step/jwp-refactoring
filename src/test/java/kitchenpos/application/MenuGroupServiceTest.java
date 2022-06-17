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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
    }

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void create() {
        //given
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        //when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(savedMenuGroup).isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회할 수 있다.")
    void list() {
        //given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup));

        //when
        List<MenuGroup> menuGroupList = menuGroupService.list();

        //then
        assertThat(menuGroupList).containsExactly(menuGroup);
    }
}
