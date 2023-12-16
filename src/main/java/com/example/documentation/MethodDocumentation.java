package com.example.documentation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodDocumentation {
    String value() default "Method documentation";
}