package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 관리 기능")
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroup 메뉴_그룹_A;
    private MenuGroup 메뉴_그룹_B;

    @BeforeEach
    void setUp() {
        메뉴_그룹_A = new MenuGroup(1L, "메뉴_그룹_A");
        메뉴_그룹_B = new MenuGroup(1L, "메뉴_그룹_B");
    }

    @Test
    @DisplayName("메뉴그룹이 정상적으로 생성된다.")
    void createMenuGroup() {
        // given
        when(this.menuGroupDao.save(메뉴_그룹_A)).thenReturn(메뉴_그룹_A);

        // when
        MenuGroup created = this.menuGroupService.create(메뉴_그룹_A);

        // then
        assertThat(created).isEqualTo(new MenuGroup(1L, "메뉴_그룹_A"));
    }

    @Test
    @DisplayName("메뉴그룹을 모두 조회한다.")
    void findAll() {
        // given
        when(this.menuGroupDao.findAll()).thenReturn(Arrays.asList(메뉴_그룹_A, 메뉴_그룹_B));

        // when
        List<MenuGroup> list = this.menuGroupService.list();

        // then
        assertThat(list).containsExactly(메뉴_그룹_A, 메뉴_그룹_B);
    }

}
