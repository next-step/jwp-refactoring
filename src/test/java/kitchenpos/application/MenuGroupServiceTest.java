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
    private MenuGroupDao dao;
    @InjectMocks
    private MenuGroupService service;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    public void insertMenuGroupTest() {
        given(dao.save(any())).willReturn(new MenuGroup(1L,"그룹1"));

        MenuGroup save = service.create(new MenuGroup(1L, "그룹1"));

        assertThat(save.getId()).isEqualTo(1L);
        assertThat(save.getName()).isEqualTo("그룹1");
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    public void findAllMenuGroupTest() {
        given(dao.findAll()).willReturn(
                Arrays.asList(new MenuGroup(1L,"그룹1"),
                        new MenuGroup(2L,"그룹2")
                )
        );

        List<MenuGroup> results = service.list();

        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0)).isEqualTo(new MenuGroup(1L,"그룹1"));
        assertThat(results.get(1)).isEqualTo(new MenuGroup(2L,"그룹2"));
    }
}