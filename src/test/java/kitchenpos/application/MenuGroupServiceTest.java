package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할수 있다")
    @Test
    void createTest() {
        //given
        MenuGroup expect = new MenuGroup(1L, "groupName");
        given(menuGroupDao.save(expect))
                .willReturn(expect);

        //when
        MenuGroup result = menuGroupService.create(expect);

        //then
        verify(menuGroupDao).save(expect);
        assertThat(result.getId()).isEqualTo(expect.getId());
        assertThat(result.getName()).isEqualTo(expect.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다 ")
    @Test
    void list() {

        //given
        MenuGroup menuGroup = new MenuGroup(1L, "groupName");
        List<MenuGroup> expect = Arrays.asList(menuGroup);
        given(menuGroupDao.findAll())
                .willReturn(expect);

        //when
        List<MenuGroup> result = menuGroupService.list();

        //then
        verify(menuGroupDao).findAll();
        assertThat(result.size()).isEqualTo(expect.size());
        assertThat(result).containsExactly(menuGroup);
    }

}