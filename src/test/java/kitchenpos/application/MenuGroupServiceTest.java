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

import static kitchenpos.domain.MenuGroupTest.메뉴_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성 시 정상 생성되어야 한다")
    @Test
    void createMenuGroupTest() {
        // given
        MenuGroup 메뉴_그룹 = 메뉴_그룹_생성("메뉴 그룹");
        when(menuGroupDao.save(메뉴_그룹)).thenReturn(메뉴_그룹);

        // when
        MenuGroup 메뉴_그룹_생성_결과 = menuGroupService.create(메뉴_그룹);

        // then
        메뉴_그룹_생성_성공됨(메뉴_그룹_생성_결과, 메뉴_그룹);
    }

    @DisplayName("메뉴 그룹 전체 조회 시 정상 조회되어야 한다")
    @Test
    void findAllMenuGroupTest() {
        // given
        List<MenuGroup> 메뉴_그룹_리스트 = Arrays.asList(
                메뉴_그룹_생성("메뉴 그룹"),
                메뉴_그룹_생성("메뉴 그룹"),
                메뉴_그룹_생성("메뉴 그룹"),
                메뉴_그룹_생성("메뉴 그룹"),
                메뉴_그룹_생성("메뉴 그룹")
        );
        when(menuGroupDao.findAll()).thenReturn(메뉴_그룹_리스트);

        // when
        List<MenuGroup> 메뉴_그룹_조회_결과 = menuGroupService.list();

        // then
        assertThat(메뉴_그룹_조회_결과.size()).isGreaterThanOrEqualTo(메뉴_그룹_리스트.size());
        assertThat(메뉴_그룹_조회_결과).containsAll(메뉴_그룹_리스트);
    }

    private void 메뉴_그룹_생성_성공됨(MenuGroup source, MenuGroup target) {
        assertThat(source.getId()).isEqualTo(target.getId());
        assertThat(source.getName()).isEqualTo(target.getName());
    }
}
