package com.blamejared.crafttweaker_annotation_processors.processors.document.conversion.converter.member.header;

import com.blamejared.crafttweaker_annotation_processors.processors.document.conversion.converter.comment.*;
import com.blamejared.crafttweaker_annotation_processors.processors.document.conversion.converter.type.*;
import com.blamejared.crafttweaker_annotation_processors.processors.document.page.comment.*;
import com.blamejared.crafttweaker_annotation_processors.processors.document.page.member.header.*;
import com.blamejared.crafttweaker_annotation_processors.processors.document.page.type.*;

import javax.lang.model.element.*;

public class ParameterConverter {
    
    private final CommentConverter commentConverter;
    private final TypeConverter typeConverter;
    private final OptionalParameterConverter optionalParameterConverter;
    
    public ParameterConverter(CommentConverter commentConverter, TypeConverter typeConverter, OptionalParameterConverter optionalParameterConverter) {
        this.commentConverter = commentConverter;
        this.typeConverter = typeConverter;
        this.optionalParameterConverter = optionalParameterConverter;
    }
    
    public DocumentedParameter convertParameter(TypeElement parentElement, VariableElement variableElement) {
        if(isOptional(variableElement)) {
            return convertOptionalParameter(parentElement,variableElement);
        } else {
            return convertNonOptionalParameter(parentElement,variableElement);
        }
    }
    
    private boolean isOptional(VariableElement variableElement) {
        return optionalParameterConverter.isOptional(variableElement);
    }
    
    private String convertDefaultValue(VariableElement variableElement) {
        return optionalParameterConverter.convertDefaultValue(variableElement);
    }
    
    private DocumentedParameter convertOptionalParameter(TypeElement parentElement, VariableElement variableElement) {
        final String name = convertName(variableElement);
        final AbstractTypeInfo type = convertType(variableElement);
        final DocumentationComment comment = convertComment(parentElement,variableElement);
        final String defaultValue = convertDefaultValue(variableElement);
        
        return new DocumentedOptionalParameter(name, type, comment, defaultValue);
    }
    
    private DocumentedParameter convertNonOptionalParameter(TypeElement parentElement, VariableElement variableElement) {
        final String name = convertName(variableElement);
        final AbstractTypeInfo type = convertType(variableElement);
        final DocumentationComment comment = convertComment(parentElement,variableElement);
        return new DocumentedParameter(name, type, comment);
    }
    
    private String convertName(VariableElement variableElement) {
        return variableElement.getSimpleName().toString();
    }
    
    private AbstractTypeInfo convertType(VariableElement variableElement) {
        return typeConverter.convertType(variableElement.asType());
    }
    
    private DocumentationComment convertComment(TypeElement parentElement, VariableElement variableElement) {
        return commentConverter.convertForParameter(parentElement, variableElement);
    }
}
