package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.*;
import kitchenpos.exception.NegativePriceException;
import kitchenpos.exception.NoMenuGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(
            final MenuRepository menuRepository
    ) {
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
