package kitchenpos.menugroup.application;

import static kitchenpos.menugroup.application.MenuGroupServiceTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = 메뉴_그룹_정보(1L, "제육볶음");
    }

    @DisplayName("메뉴그룹을 등록한다.")
    @Test
    void create() {
        // given
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(menuGroup);
        MenuGroup expected = menuGroupService.create(menuGroup);

        //then
        assertThat(expected).isNotNull();
        assertThat(expected.getName()).isEqualTo("제육볶음");
    }

    @DisplayName("메뉴그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup));
        List<MenuGroup> expected = menuGroupService.list();

        // then
        assertThat(expected).isNotNull();
        assertThat(expected.size()).isEqualTo(1);
    }

}
