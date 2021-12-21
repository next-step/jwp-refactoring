package kitchenpos.domain;

import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class MenuPricePolicyListener {
    @PreUpdate
    void preUpdate(Menu menu) {
        MenuPricePolicy.validate(menu);
    }

    @PrePersist
    void PrePersist(Menu menu) {
        MenuPricePolicy.validate(menu);
    }
}
