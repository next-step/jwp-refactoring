package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static kitchenpos.domain.MenuGroupTest.와퍼_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 관리 테스트")
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹 생성")
    void createTest() {
        // given
        given(menuGroupDao.save(any())).willReturn(와퍼_메뉴);
        // when
        MenuGroup actual = menuGroupService.create(와퍼_메뉴);
        // then
        assertThat(actual).isEqualTo(와퍼_메뉴);
    }

    @Test
    @DisplayName("메뉴 그룹 리스트 조회")
    void listTest() {
        // given
        given(menuGroupDao.findAll())
                .willReturn(Collections.singletonList(와퍼_메뉴));
        // when
        List<MenuGroup> actual = menuGroupService.list();
        // then
        assertThat(actual).hasSize(1);
        assertThat(actual).containsExactly(와퍼_메뉴);
    }

}

