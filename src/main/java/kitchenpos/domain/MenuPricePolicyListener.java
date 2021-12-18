package kitchenpos.domain;

import org.springframework.stereotype.Component;

import javax.persistence.PreUpdate;

@Component
public class MenuPricePolicyListener {
    @PreUpdate
    void preUpdate(Menu menu) {
        MenuPricePolicy.validate(menu);
    }
}
