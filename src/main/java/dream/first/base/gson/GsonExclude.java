/**
 * 
 */
package dream.first.base.gson;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD })
/**
 * gson 序列化或者反序列化时排除的的类和字段标注
 * 
 * @author PengFei
 * @date 2020年12月7日下午5:39:08
 */
public @interface GsonExclude {

}
