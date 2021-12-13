package org.example

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

class Provider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Preprocessor(environment.codeGenerator)
    }
}

class Preprocessor(val generator: CodeGenerator) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver
            .getSymbolsWithAnnotation("org.example.MyAnnotation")
            .filterIsInstance<KSClassDeclaration>()
            .forEach {
                val ksFile = it.containingFile ?: return@forEach
                val name = it.simpleName.asString()
                val packageName = ksFile.packageName.asString()
                val file = generator.createNewFile(Dependencies(false, ksFile), packageName, "Test")
                file.bufferedWriter().use {
                    it.write("""
                        package $packageName
                        
                        val $name.extension get() = 42
                    """.trimIndent())
                }
            }

        return emptyList()
    }
}