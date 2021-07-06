package com.celeste.library.spigot.model.menu.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Item {

  int slot() default 0;

}
