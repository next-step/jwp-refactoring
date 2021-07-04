package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    void createTest() {

        // when
        when(menuGroupDao.save(any())).thenReturn(new MenuGroup());

        // then
        assertThat(menuGroupService.create(new MenuGroup())).isNotNull();
    }

    @Test
    void listTest() {

        // when
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(new MenuGroup(), new MenuGroup()));

        // then
        assertThat(menuGroupService.list()).hasSize(2);
    }
}