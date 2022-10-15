package com.sirius.library.utils.json_canonical

import com.sirius.library.utils.StringUtils

class JsonCanonicalizer(jsonData: String) {
    var buffer: StringBuilder

    constructor(jsonData: ByteArray) : this(
        StringUtils.bytesToString(
            jsonData,
            StringUtils.CODEC.UTF_8
        )
    ) {
    }

    private fun escape(c: Char) {
        buffer.append('\\').append(c)
    }

    private fun serializeString(value: String) {
        buffer.append('"')

        for (cha in value.toCharArray()) {
            var c = cha
            when (c) {
                '\n' -> escape('n')
                '\b' -> escape('b')
                '\u000C' -> escape('f')
                '\r' -> escape('r')
                '\t' -> escape('t')
                '"', '\\' -> escape(c)
                else -> {
                    if (c.code < 0x20) {
                        escape('u')
                        var i = 0
                        while (i < 4) {
                            val hex = c.code ushr 12
                            buffer.append((if (hex > 9) hex + 'a'.code - 10 else hex + '0'.code).toChar())
                            c = (c.code shl 4).toChar()
                            i++
                        }
                        break
                    }
                    buffer.append(c)
                }
            }
        }
        buffer.append('"')
    }

    @Throws(Exception::class)
    fun serialize(o: Any?) {
        if (o is LinkedHashMap<*, *>) {
            buffer.append('{')
            var next = false
            for (keyValue in (o as LinkedHashMap<String, Any?>).entries) {
                if (next) {
                    buffer.append(',')
                }
                next = true
                serializeString(keyValue.key)
                buffer.append(':')
                serialize(keyValue.value)
            }
            buffer.append('}')
        } else if (o is List<*>) {
            buffer.append('[')
            var next = false
            for (value in (o as List<Any?>).toTypedArray()) {
                if (next) {
                    buffer.append(',')
                }
                next = true
                serialize(value)
            }
            buffer.append(']')
        } else if (o == null) {
            buffer.append("null")
        } else if (o is String) {
            serializeString(o)
        } else if (o is Boolean) {
            buffer.append(o as Boolean?)
        } else if (o is Double) {
            buffer.append(NumberToJSON.serializeNumber(o))
        } else {
            throw Exception("Unknown object: $o")
        }
    }

    val encodedString: String
        get() = buffer.toString()

    @get:Throws(Exception::class)
    val encodedUTF8: ByteArray
        get() = StringUtils.stringToBytes(encodedString, StringUtils.CODEC.UTF_8)

    init {
        buffer = StringBuilder()
        serialize(JsonDecoder(jsonData).root)
    }
}

internal class JsonDecoder(var jsonData: String) {
    var index = 0
    var maxLength: Int
    var root: Any? = null

    @Throws(Exception::class)
    fun parseElement(): Any? {
        return when (scan()) {
            LEFT_CURLY_BRACKET -> parseObject()
            DOUBLE_QUOTE -> parseQuotedString()
            LEFT_BRACKET -> parseArray()
            else -> parseSimpleType()
        }
    }

    @Throws(Exception::class)
    fun parseObject(): Any {
        val dict: LinkedHashMap<String, Any?> = LinkedHashMap<String, Any?>()
        var next = false
        while (testNextNonWhiteSpaceChar() != RIGHT_CURLY_BRACKET) {
            if (next) {
                scanFor(COMMA_CHARACTER)
            }
            next = true
            scanFor(DOUBLE_QUOTE)
            val name = parseQuotedString()
            scanFor(COLON_CHARACTER)
            if (dict.put(name, parseElement()) != null) {
                throw Exception("Duplicate property: $name")
            }
        }
        scan()
        return dict
    }

    @Throws(Exception::class)
    fun parseArray(): Any {
        val array: MutableList<Any> = mutableListOf()
        var next = false
        while (testNextNonWhiteSpaceChar() != RIGHT_BRACKET) {
            if (next) {
                scanFor(COMMA_CHARACTER)
            } else {
                next = true
            }
            val parsed = parseElement()
            parsed?.let {
                array.add(it)
            }
        }
        scan()
        return array
    }

    @Throws(Exception::class)
    fun parseSimpleType(): Any? {
        index--
        val tempBuffer: StringBuilder = StringBuilder()
        var c: Char
        while (testNextNonWhiteSpaceChar().also {
                c = it
            } != COMMA_CHARACTER && c != RIGHT_BRACKET && c != RIGHT_CURLY_BRACKET) {
            if (isWhiteSpace(nextChar().also { c = it })) {
                break
            }
            tempBuffer.append(c)
        }
        val token: String = tempBuffer.toString()
        if (token.length == 0) {
            throw Exception("Missing argument")
        }
        return if (NUMBER_PATTERN.matches(token)) {
            token.toDouble() // Syntax check...

        } else if (BOOLEAN_PATTERN.matches(token)) {
            token
        } else if (token == "null") {
            null
        } else {
            throw Exception("Unrecognized or malformed JSON token: $token")
        }
    }

    @Throws(Exception::class)
    fun parseQuotedString(): String {
        val result: StringBuilder = StringBuilder()
        while (true) {
            var c = nextChar()
            if (c < ' ') {
                throw Exception(
                    if (c == '\n') "Unterminated string literal" else "Unescaped control character: 0x" + c.code
                )
            }
            if (c == DOUBLE_QUOTE) {
                break
            }
            if (c == BACK_SLASH) {
                when (nextChar().also { c = it }) {
                    '"', '\\', '/' -> {}
                    'b' -> c = '\b'
                    'f' -> c = '\u000C'
                    'n' -> c = '\n'
                    'r' -> c = '\r'
                    't' -> c = '\t'
                    'u' -> {
                        c = 0.toChar()
                        var i = 0
                        while (i < 4) {
                            c = ((c.code shl 4) + hexChar.code).toChar()
                            i++
                        }
                    }
                    else -> throw Exception("Unsupported escape:$c")
                }
            }
            result.append(c)
        }
        return result.toString()
    }

    @get:Throws(Exception::class)
    val hexChar: Char
        get() {
            val c = nextChar()
            when (c) {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> return (c - '0').toChar()
                'a', 'b', 'c', 'd', 'e', 'f' -> return (c - 'a' + 10).toChar()
                'A', 'B', 'C', 'D', 'E', 'F' -> return (c - 'A' + 10).toChar()
            }
            throw Exception("Bad hex in \\u escape: $c")
        }

    @Throws(Exception::class)
    fun testNextNonWhiteSpaceChar(): Char {
        val save = index
        val c = scan()
        index = save
        return c
    }

    @Throws(Exception::class)
    fun scanFor(expected: Char) {
        val c = scan()
        if (c != expected) {
            throw Exception("Expected '$expected' but got '$c'")
        }
    }

    @Throws(Exception::class)
    fun nextChar(): Char {
        if (index < maxLength) {
            return jsonData[index++]
        }
        throw Exception("Unexpected EOF reached")
    }

    fun isWhiteSpace(c: Char): Boolean {
        return c.code == 0x20 || c.code == 0x0A || c.code == 0x0D || c.code == 0x09
    }

    @Throws(Exception::class)
    fun scan(): Char {
        while (true) {
            val c = nextChar()
            if (isWhiteSpace(c)) {
                continue
            }
            return c
        }
    }

    companion object {
        const val LEFT_CURLY_BRACKET = '{'
        const val RIGHT_CURLY_BRACKET = '}'
        const val DOUBLE_QUOTE = '"'
        const val COLON_CHARACTER = ':'
        const val LEFT_BRACKET = '['
        const val RIGHT_BRACKET = ']'
        const val COMMA_CHARACTER = ','
        const val BACK_SLASH = '\\'

        //    Regex()
        val BOOLEAN_PATTERN = Regex("true|false")
        val NUMBER_PATTERN = Regex("-?[0-9]+(\\.[0-9]+)?([eE][-+]?[0-9]+)?")
    }

    init {
        maxLength = jsonData.length
        root = if (testNextNonWhiteSpaceChar() == LEFT_BRACKET) {
            scan()
            parseArray()
        } else {
            scanFor(LEFT_CURLY_BRACKET)
            parseObject()
        }
        while (index < maxLength) {
            if (!isWhiteSpace(jsonData[index++])) {
                throw Exception("Improperly terminated JSON object")
            }
        }
    }
}
