package com.messycomposer.widthconverter

import org.junit.Test

import org.junit.Assert.*


const val HALF_WIDTH_CHARS = (
    "ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜｦﾝ" +
    "ｶﾞｷﾞｸﾞｹﾞｺﾞｻﾞｼﾞｽﾞｾﾞｿﾞﾀﾞﾁﾞﾂﾞﾃﾞﾄﾞﾊﾞﾋﾞﾌﾞﾍﾞﾎ" +
    "ﾞﾊﾟﾋﾟﾌﾟﾍﾟﾎﾟｳﾞ" +
    "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん" +
    "がぎぐげござじずぜぞだぢづでどばびぶべぼ" +
    "ぱぴぷぺぽ" +
    " !?~ｰ"
)
const val FULL_WIDTH_CHARS = (
    "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲン" +
    "ガギグゲゴザジズゼゾダヂヅデドバビブベボ" +
    "パピプペポヴ" +
    "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをん" +
    "がぎぐげござじずぜぞだぢづでどばびぶべぼ" +
    "ぱぴぷぺぽ" +
    "　！？～ー"
)
class ConverterUnitTest {
    @Test
    fun testConvertFullToHalf() {
        assertEquals(convertToHalf(FULL_WIDTH_CHARS), HALF_WIDTH_CHARS)
    }

    @Test
    fun testConvertHalfToFull() {
        assertEquals(convertToFull(HALF_WIDTH_CHARS), FULL_WIDTH_CHARS)
    }
    @Test
    fun testMergeVoicedMarks() {
        val input = "カ゛キ゛ク゛ケ゛コ゛サ゛シ゛ス゛セ゛ソ゛タ゛チ゛ツ゛テ゛ト゛ハ゛ヒ゛フ゛ヘ゛ホ゛ハ゜ヒ゜フ゜ヘ゜ホ゜ウ゛" +
                "か゛き゛く゛け゛こ゛さ゛し゛す゛せ゛そ゛た゛ち゛つ゛て゛と゛は゛ひ゛ふ゛へ゛ほ゛は゜ひ゜ふ゜へ゜ほ゜"
        val expected = "ガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポヴ" +
                "がぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽ"
        val actual = mergeVoicedMarks(input)
        assertEquals(expected, actual)
    }
}