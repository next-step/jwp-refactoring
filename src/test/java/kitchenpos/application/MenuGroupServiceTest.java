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

    private MenuGroup 분식류;
    private MenuGroup 양식류;

    @BeforeEach
    void setUp() {
        분식류 = new MenuGroup();
        분식류.setName("분식류");
        양식류 = new MenuGroup();
        양식류.setName("양식류");
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다")
    void create() {
        //given
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(분식류);

        //when
        MenuGroup menuGroup = menuGroupService.create(분식류);

        //then
        assertThat(menuGroup).isEqualTo(분식류);
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회할 수 있다")
    void list() {
        //given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(분식류, 양식류));

        //when
        List<MenuGroup> results = menuGroupService.list();

        //then
        assertThat(results).containsExactly(분식류, 양식류);
    }
}
