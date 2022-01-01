package kitchenpos.menu.validate;

import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MenuGroupMustExistValidator implements ConstraintValidator<MenuGroupMustExist, String> {
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return menuGroupRepository.existsById(Long.parseLong(value));
    }
}
