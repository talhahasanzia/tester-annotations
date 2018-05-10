package com.thz.exposeprocessor;

import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.thz.testablebinding.Expose;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by Talha Hasan Zia on 16-Mar-18.
 * <p></p><b>Description:</b><p></p> Why class was created?
 * <p></p>
 * <b>Public Methods:</b><p></p> Only listing to public methods usage.
 */
@SupportedAnnotationTypes("com.thz.expose.annotation.Expose")
public class ExposeProcessor extends AbstractProcessor {

    private static final String METHOD_PREFIX = "start";
    private static final ClassName classIntent = ClassName.get("android.content", "Intent");
    private static final ClassName classContext = ClassName.get("android.content", "Context");
    private ProcessingEnvironment processingEnvironment;
    private Messager messager;
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Map<String, String> activitiesWithPackage;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnv;
        messager = processingEnvironment.getMessager();
        typeUtils = processingEnvironment.getTypeUtils();
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        activitiesWithPackage = new HashMap<>();
        System.out.println("Init called");

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("Process called");
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Expose.class)) {

            if (element.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Can be only applied to class.");
            }

            TypeElement typeElement = (TypeElement) element;
            PackageElement pkg = elementUtils.getPackageOf(element);

            activitiesWithPackage.put(
                    typeElement.getSimpleName().toString(),
                    pkg.getQualifiedName().toString());


        }

        TypeSpec.Builder navigatorClass = generateClass();

        try {
            JavaFile.builder("com.thz.annotationdemo", navigatorClass.build()).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }


    private TypeSpec.Builder generateClass() {
        /*TypeSpec.Builder navigatorClass = TypeSpec
                .classBuilder("NavigatorNew")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
*/

        TypeSpec.Builder navigatorClass=null;

        for (Map.Entry<String, String> element : activitiesWithPackage.entrySet()) {

            String activityName = element.getKey();
            String packageName = element.getValue();
             navigatorClass = TypeSpec
                    .classBuilder(activityName+"Exposed")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);


            ClassName activityClass = ClassName.get(packageName, activityName);
            MethodSpec intentMethod = MethodSpec
                    .methodBuilder(METHOD_PREFIX + activityName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(classIntent)
                    .addParameter(classContext, "context")
                    .addStatement("return new $T($L, $L)", classIntent, "context", activityClass + ".class")
                    .build();
            navigatorClass.addMethod(intentMethod);
        }

        System.out.println("class created");
        return navigatorClass;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(Expose.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

}
