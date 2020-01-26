package com.covergroup.application.arguments

import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.boot.ApplicationArguments


data class Foo(var value: String="", var bool: Boolean=false)
data class Bar(var foo: Foo = Foo())

class TestArguments(private val arguments: Map<String, List<String>>): ApplicationArguments {
    override fun getSourceArgs(): Array<String> {
        return arrayOf()
    }

    override fun getOptionNames(): MutableSet<String> {
        return arguments.keys.toMutableSet()
    }

    override fun containsOption(name: String?): Boolean {
        return arguments.containsKey(name)
    }

    override fun getNonOptionArgs(): MutableList<String> {
        return mutableListOf()
    }

    override fun getOptionValues(name: String?): MutableList<String> {
        if(name!=null)
            return arguments.getValue(name).toMutableList()

        return mutableListOf()
    }

}

internal class ArgumentParserTest {

    @Test
    fun parseOneStringParameter() {
        val argumentParser = ArgumentParser()
        val foo = Foo()
        val args=
            TestArguments(mapOf("value" to listOf("12345")))
        argumentParser.parse(args, foo)

        assertEquals( "12345", foo.value)
    }

    @Test
    fun parseOneBooleanParameter() {
        val argumentParser = ArgumentParser()
        val foo = Foo()
        val args= TestArguments(mapOf("bool" to listOf()))
        argumentParser.parse(args, foo)

        assertEquals( true, foo.bool)
    }

    @Test
    fun parseOneObjectParameter() {
        val argumentParser = ArgumentParser()
        val bar = Bar()
        val args=
            TestArguments(mapOf("foo.value" to listOf("12345")))
        argumentParser.parse(args, bar)

        assertEquals( "12345", bar.foo.value)
    }

    @Test
    fun unknownArgumentsAreIgnored() {
        val argumentParser = ArgumentParser()
        val bar = Bar()
        val args=
            TestArguments(mapOf("barfoo" to listOf("12345")))
        argumentParser.parse(args, bar)

        assertEquals( "", bar.foo.value)
    }

    @Test
    fun unknownObjectArgumentsAreIgnoredAlso() {
        val argumentParser = ArgumentParser()
        val bar = Bar()
        val args=
            TestArguments(mapOf("value.foo" to listOf("12345")))
        argumentParser.parse(args, bar)

        assertEquals( "", bar.foo.value)
    }

    @Test
    fun unknownSubArgumentsAreIgnoredAlso() {
        val argumentParser = ArgumentParser()
        val bar = Bar()
        val args=
            TestArguments(mapOf("foo.values" to listOf("12345")))
        argumentParser.parse(args, bar)

        assertEquals( "", bar.foo.value)
    }
}