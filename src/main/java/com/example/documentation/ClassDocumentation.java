package com.example.documentation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClassDocumentation {
    String value() default "Class documentation";
}