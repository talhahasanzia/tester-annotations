package com.thz.testablebinding;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
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
@SupportedAnnotationTypes("com.thz.testablebinding")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ExposeProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnvironment;
    private Messager messager;
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment=processingEnvironment;
        messager=processingEnvironment.getMessager();
        typeUtils=processingEnvironment.getTypeUtils();
        elementUtils=processingEnvironment.getElementUtils();
        filer=processingEnvironment.getFiler();

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {


        for (Element element : roundEnvironment.getElementsAnnotatedWith(Expose.class)) {

            if (element.getKind() == ElementKind.METHOD) {
                if(element.getModifiers().contains(Modifier.PRIVATE))
                {


                }
                else
                {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Only type of PRIVATE Methods are allowed with @Expose annotations",element);
                }
            }
            else {
                messager.printMessage(Diagnostic.Kind.ERROR, "Only type of Methods are allowed with @Expose annotations",element);
            }
        }

        return true;
    }



}
