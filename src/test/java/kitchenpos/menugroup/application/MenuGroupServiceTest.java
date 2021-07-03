package kitchenpos.menugroup.application;

import static kitchenpos.util.TestDataSet.추천_메뉴_그륩;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("그룹을 생성 정상 성공 케이스")
    void create() {
        //given
        given(menuGroupDao.save(any())).willReturn(추천_메뉴_그륩);

        //when
        MenuGroup result = menuGroupService.create(추천_메뉴_그륩);
        // then

        assertThat(result.getName()).isEqualTo(추천_메뉴_그륩.getName());
        verify(menuGroupDao, times(1)).save(result);
    }

}
