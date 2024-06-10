package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class ItemValidator implements Validator {

    @Override // 지원을 하냐?
    public boolean supports(Class<?> aClass) {
        return Item.class.isAssignableFrom(aClass);
        // item == clazz
        // item == subItem
    }

    @Override // 검증 로직
    public void validate(Object o, Errors errors) {

        Item item = (Item) o;
        // 검증 로직
        // ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required"); 아래 if문을 대신 써주는 ValidationUtils라는 거임 단, 공백같은 단순 기능만 제공
        if (!StringUtils.hasText(item.getItemName())) { // 텍스트가 없으면
            errors.rejectValue("itemName", "required");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{10000, 1000000}, null);

        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                // 글로벌 오류의 경우는 ObjectError를 사용한다.
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
    }
}

