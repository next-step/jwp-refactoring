package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuValidation;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuValidation menuValidation;

    public MenuService(
        final MenuDao menuDao,
        final MenuValidation menuValidation
    ) {
        this.menuDao = menuDao;
        this.menuValidation = menuValidation;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = menuRequest.toMenu();
        menu.addMenuProducts(menuRequest.toMenuProducts());

        menuValidation.valid(menu);

        return MenuResponse.of(menuDao.save(menu));
    }

    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuDao.findAll());
    }

    public Long findById(Long id) {
        Menu menu = menuDao.findById(id)
            .orElseThrow(NoResultDataException::new);
        return menu.getId();
    }

}
