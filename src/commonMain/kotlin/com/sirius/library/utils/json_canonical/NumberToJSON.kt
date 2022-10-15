package com.sirius.library.utils.json_canonical

/**
 * Number serialization support.
 */
object NumberToJSON {
    /**
     * Formats a number according to ES6.
     *
     *
     * This code is emulating 7.1.12.1 of the EcmaScript V6 specification.
     * @param value Value to be formatted
     * @return String representation
     * @throws IOException &nbsp;
     */
    @Throws(Exception::class)
    fun serializeNumber(value: Double): String {
        // 1. Check for JSON compatibility.

        if (value.isNaN() || value.isInfinite()) {
            throw Exception("NaN/Infinity are not permitted in JSON")
        }

        // 2.Deal with zero separately.  Note that this test takes "-0.0" as well
        if (value == 0.0) {
            return "0"
        }

        // 3. Call the DtoA algorithm crunchers
        // V8 FastDtoa can't convert all numbers, so try it first but
        // fall back to old DToA in case it fails
        val result = NumberFastDtoa.numberToString(value)
        if (result != null) {
            return result
        }

        val buffer: StringBuilder = StringBuilder()
        NumberDToA.JS_dtostr(buffer, NumberDToA.DTOSTR_STANDARD, 0, value)
        return buffer.toString()
    }
}
