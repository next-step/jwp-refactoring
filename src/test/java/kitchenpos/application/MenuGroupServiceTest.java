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
class MenuGroupServiceTest {

    private MenuGroup 세마리메뉴;
    private MenuGroup 패밀리메뉴;

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        세마리메뉴 = new MenuGroup();
        세마리메뉴.setId(1L);
        세마리메뉴.setName("세마리메뉴");

        패밀리메뉴 = new MenuGroup();
        패밀리메뉴.setId(2L);
        패밀리메뉴.setName("패밀리메뉴");
    }

    @DisplayName("그룹명을 입력해 메뉴그룹을 등록한다")
    @Test
    void createMenuGroup() {
        // given
        given(menuGroupDao.save(세마리메뉴)).willReturn(세마리메뉴);

        // when
        MenuGroup actual = menuGroupService.create(세마리메뉴);

        // then
        assertThat(actual).isEqualTo(세마리메뉴);
    }

    @DisplayName("메뉴그룹 목록을 조회한다")
    @Test
    void findAll() {
        // given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(세마리메뉴, 패밀리메뉴));

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // when
        assertThat(actual).isEqualTo(Arrays.asList(세마리메뉴, 패밀리메뉴));
    }
}