package task;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import org.reflections.*;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

public class FindAnnotated {

    private static String annotationClassName = "com.example.documentation.ClassDocumentation";
    private static String annotationMethodName = "com.example.documentation.MethodDocumentation";



    public static void main(String[] args) throws IOException {

        String packageName = "com.example.documentation";

        Reflections reflections2 = new org.reflections.Reflections(packageName, new MethodAnnotationsScanner());


        Reflections reflections = new org.reflections.Reflections(packageName);

        Set<Class<?>> annotationClasses = reflections.getTypesAnnotatedWith(getClassForName(annotationClassName), true);
        System.out.println("Classes with @" + annotationClassName + ":");
        for (Class<?> annotatedClass : annotationClasses) {
            System.out.println(annotatedClass.getName());
        }
        System.out.println();

        Set<Method> annotatedMethods = reflections2.getMethodsAnnotatedWith(getClassForName(annotationMethodName));
        System.out.println("Methods with @" + annotationMethodName + ":");
        for (Method annotationMethod : annotatedMethods) {
            System.out.println(annotationMethod);
        }
        System.out.println();
//
//        Reflections reflections3 = new Reflections(packageName, new SubTypesScanner());
//        Set<Class<?>> classes = reflections3.getSubTypesOf(Object.class);

        System.out.println("Classes with javadoc:");
        for (Class<?> clazz : annotationClasses) {
            String classPath = clazz.getName().replace(".", "/") + ".java";
//            System.out.println(classPath);
            try {
                String sourceCode = new String(Files.readAllBytes(Paths.get("src/main/java", classPath)));
                CompilationUnit cu = StaticJavaParser.parse(sourceCode);

                cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classDeclaration -> {
                    if (classDeclaration.getComment().isPresent() && classDeclaration.getComment().get() instanceof JavadocComment) {
                        String javadoc = ((JavadocComment) classDeclaration.getComment().get()).getContent();
                        System.out.println("Class: " + clazz.getName());
                        System.out.println("Javadoc:\n" + javadoc);
                        System.out.println("-------------------------------");
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println();

        System.out.println("Methods with javadoc:");
        for (Class<?> clazz : annotationClasses) {
            String classPath = clazz.getName().replace(".", "/") + ".java";
            try {
                String sourceCode = new String(Files.readAllBytes(Paths.get("src/main/java", classPath)));
                CompilationUnit cu = StaticJavaParser.parse(sourceCode);

                cu.findAll(MethodDeclaration.class).forEach(method -> {
                    if (method.getComment().isPresent() && method.getComment().get() instanceof JavadocComment) {
                        String javadoc = ((JavadocComment) method.getComment().get()).getContent();
                        System.out.println("Method: " + method.getDeclarationAsString());
                        System.out.println("Javadoc:\n" + javadoc);
                        System.out.println("-------------------------------");
                    }
                });
            } catch (IOException | ParseProblemException e) {
                e.printStackTrace();
            }
        }

    }

    private static Class<? extends Annotation> getClassForName(String className) {
        try {
            return (Class<? extends Annotation>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load class: " + className, e);
        }
    }

}
