package com.celeste.library.spigot.model.menu.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Menu {

  String title();

  int size();

}
