package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.moduledomain.menu.Menu;
import kitchenpos.moduledomain.menu.MenuDao;
import kitchenpos.moduledomain.menu.MenuValidation;
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
        menu.addMenuProducts(menuRequest.toMenuProducts(), menuValidation);
        return MenuResponse.of(menuDao.save(menu));
    }

    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuDao.findAll());
    }

}
