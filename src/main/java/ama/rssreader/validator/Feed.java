package ama.rssreader.validator;

import ama.rssreader.cdis.FeedListPage;
import ama.rssreader.util.RssUtil;
import ama.rssreader.validator.Feed.FeedValidator;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * Feedが正常に取得出来そうかを確認する
 * 独自Validator
 * @author amanobu
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {FeedValidator.class})
public @interface Feed {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "URLがまちがってませんか？";

    class FeedValidator implements ConstraintValidator<Feed, String> {

        @Override
        public void initialize(Feed constraintAnnotation) {
            //
        }

        @Override
        public boolean isValid(String url, ConstraintValidatorContext context) {
            if (url == null || url.isEmpty()) {
                return false;
            }
            String title;
            try {
                title = RssUtil.getTitle(url);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            if (title == null || title.isEmpty()) {
                return false;
            }
            Logger.getLogger(FeedListPage.class.getName()).log(Level.SEVERE, "登録URLは問題無さそう");
            return true;
        }
    }
}
