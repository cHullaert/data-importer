package com.covergroup.application.arguments

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ArgumentContainer

@Suppress("UNCHECKED_CAST")
@Component
class ArgumentParser {

    private val logger: Logger = LoggerFactory.getLogger(ArgumentParser::class.java)

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    fun setArguments(arguments: ApplicationArguments?) {
        val beans=applicationContext.getBeansWithAnnotation(ArgumentContainer::class.java)

        beans.values.forEach {  this.parse(arguments, it) }
    }

    private fun updateProperty(initial: Any, optionName: String, action: (Any, KMutableProperty<*>)-> Unit) {
        val list=optionName.split(".")

        var source=initial
        for(propertyName in list.dropLast(1)) {
            val property= source::class.memberProperties.find{ it.name == propertyName } as KProperty1<Any, *>?
                ?: break
            source=property.get(source) as Any
        }

        val property = source::class.members.find{ it.name == list.last() } as KMutableProperty<*>?
        if(property!=null)
            action(source, property)
    }

    fun parse(args: ApplicationArguments?, initial: Any) {
        args?.optionNames?.forEach { optionName ->
            run {
                    this.updateProperty(initial, optionName) { any: Any, kMutableProperty: KMutableProperty<*> ->
                        run {
                            when {
                                kMutableProperty.returnType.isSubtypeOf(String::class.createType()) -> kMutableProperty.setter.call(
                                    any,
                                    args.getOptionValues(optionName)[0]
                                )
                                kMutableProperty.returnType.isSubtypeOf(Boolean::class.createType()) -> kMutableProperty.setter.call(
                                    any,
                                    true
                                )
                                else -> logger.error("cannot map parameters of type: " + kMutableProperty.returnType)
                            }
                        }
                    }
            }
        }
    }
}