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
@DisplayName("메뉴 그룹 비즈니스 로직")
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("추천메뉴");
    }

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() {
        // given
        given(menuGroupDao.save(menuGroup)).willReturn(menuGroup);

        // when
        MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertThat(actual.getId()).isEqualTo(menuGroup.getId());
        assertThat(actual.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup));

        // when
        List<MenuGroup> list = menuGroupService.list();

        // then
        assertThat(list.get(0).getId()).isEqualTo(menuGroup.getId());
        assertThat(list.get(0).getName()).isEqualTo(menuGroup.getName());
    }

}
