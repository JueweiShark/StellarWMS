package com.example.wmsspringbootproject.common.Annotation;

import java.lang.annotation.*;

/**
 * MP数据权限注解
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataPermission {

    /**
     * 数据权限 {@link com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor}
     */
    boolean value() default true;
    String warehouseAlias() default "";

    String warehouseIdColumnName() default "warehouse_id";

    String userAlias() default "";

    String userIdColumnName() default "id";

}

