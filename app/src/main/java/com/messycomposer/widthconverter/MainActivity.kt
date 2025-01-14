package com.messycomposer.widthconverter

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import android.content.ClipboardManager
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.messycomposer.widthconverter.ui.theme.WidthConverterTheme

class MainActivity : ComponentActivity() {
    private lateinit var clipboardManager: ClipboardManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val selectedText = if (intent.action == Intent.ACTION_PROCESS_TEXT) {
            intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT) ?: ""
            //            if (!intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true)) {
            //                Log.v("Intent", "Is not read only")
            //                val newIntent = Intent()
            //                newIntent.putExtra(Intent.EXTRA_PROCESS_TEXT, "I am a replacement text")
            //                setResult(RESULT_OK, newIntent)
            //            } else {
            //                Log.v("Intent", "Is read only")
            //            }
        } else {
            ""
        }
        if (selectedText != "") {
            Log.v("Selected text", selectedText)
        }

        setContent {
            WidthConverterTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ConverterUI(
                        onCopy = ::copyTextToClipboard,
                        onPaste = ::pasteTextFromClipboard,
                        selectedText = selectedText,
                    )
                }
            }
        }
    }

    private fun copyTextToClipboard(textToCopy: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("text", textToCopy))
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()
    }
    private fun pasteTextFromClipboard(): String {
        return clipboardManager.primaryClip?.getItemAt(0)?.text.toString()
    }
}

class TextFieldState(defaultValue: String = "", var label: String = ""){
    var text: String by mutableStateOf(defaultValue)
}

@Composable
fun TextInput(state: TextFieldState = remember { TextFieldState() }, onChange: (String) -> Unit = {}) {
    OutlinedTextField(
        modifier=Modifier.fillMaxSize(),
        value=state.text,
        shape=RoundedCornerShape(4.dp),
        onValueChange = {
            state.text = it
            onChange(it)
        },
        label = {
            Text(state.label)
        }
    )
}

@Composable
fun ThreeColumnLayout(
    direction: Boolean = true,
    changeDirection: () -> Unit = {}
) {

    val rotationDegree by animateFloatAsState(
        targetValue = if (direction) 0f else 180f,
        animationSpec = tween(durationMillis = 500),
        label = "convertion-direction"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Text(text = "Half Width")
        }
        Button(
            onClick = changeDirection,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Arrow Icon",
                tint = Color.White,
                modifier = Modifier.rotate(rotationDegree)
            )
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Full Width")
        }
    }
}


val halfToFullWidthKanaMap = mapOf(
    'ﾞ' to '゛', 'ﾟ' to '゜',
    'ｧ' to 'ァ', 'ｱ' to 'ア', 'ｨ' to 'ィ', 'ｲ' to 'イ', 'ｩ' to 'ゥ', 'ｳ' to 'ウ', 'ｪ' to 'ェ', 'ｴ' to 'エ', 'ｫ' to 'ォ', 'ｵ' to 'オ',
    'ｶ' to 'カ', 'ｷ' to 'キ', 'ｸ' to 'ク', 'ｹ' to 'ケ', 'ｺ' to 'コ',
    'ｻ' to 'サ', 'ｼ' to 'シ', 'ｽ' to 'ス', 'ｾ' to 'セ', 'ｿ' to 'ソ',
    'ﾀ' to 'タ', 'ﾁ' to 'チ', 'ｯ' to 'ッ', 'ﾂ' to 'ツ', 'ﾃ' to 'テ', 'ﾄ' to 'ト',
    'ﾅ' to 'ナ', 'ﾆ' to 'ニ', 'ﾇ' to 'ヌ', 'ﾈ' to 'ネ', 'ﾉ' to 'ノ',
    'ﾊ' to 'ハ', 'ﾋ' to 'ヒ', 'ﾌ' to 'フ', 'ﾍ' to 'ヘ', 'ﾎ' to 'ホ',
    'ﾏ' to 'マ', 'ﾐ' to 'ミ', 'ﾑ' to 'ム', 'ﾒ' to 'メ', 'ﾓ' to 'モ',
    'ｬ' to 'ャ', 'ﾔ' to 'ヤ', 'ｭ' to 'ュ', 'ﾕ' to 'ユ', 'ｮ' to 'ョ', 'ﾖ' to 'ヨ',
    'ﾜ' to 'ワ',
    'ﾗ' to 'ラ', 'ﾘ' to 'リ', 'ﾙ' to 'ル', 'ﾚ' to 'レ', 'ﾛ' to 'ロ',
    'ｦ' to 'ヲ',
    'ﾝ' to 'ン',
    'ｰ' to 'ー'
)
val fullToHalfWidthKanaMap = halfToFullWidthKanaMap.entries.associate { it.value to it.key }

// Converts a string of full-width characters to half-width
fun convertToHalf(e: String): String {
    return e.flatMap { char ->
        when (char) {
            // Convert full-width punctuation to half-width
            in '！'..'～' -> listOf((char.code - 0xfee0).toChar())
            // Convert full-width kana to half-width
            in fullToHalfWidthKanaMap.keys -> listOf(fullToHalfWidthKanaMap[char])
            // Special handling for voiced kana
            in 'カ' .. 'ド' -> listOf(fullToHalfWidthKanaMap[(char.code - 1).toChar()], 'ﾞ')
            in 'ハ' .. 'ポ' -> {
                val codeMod3 = char.code % 3
                val voicedMark = if (codeMod3 - 1 == 0) 'ﾞ' else 'ﾟ'
                listOf(fullToHalfWidthKanaMap[(char.code - codeMod3).toChar()], voicedMark)
            }
            'ヴ' -> listOf('ｳ', 'ﾞ')
            // Convert full-width space to half-width
            '　' -> listOf(' ') // Half width space 0x0020 -> Full width 0x3000
            // Leave other characters as they are
            else -> listOf(char)
        }
    }.joinToString("")
}

// Converts a string of half-width characters to full-width
fun convertToFull(e: String): String {
    return mergeVoicedMarks(e.map { char ->
        when (char) {
            // Convert half-width punctuation to full-width
            in '!'..'~' -> (char.code + 0xfee0).toChar()
            // Convert half-width kana to full-width
            in halfToFullWidthKanaMap.keys -> halfToFullWidthKanaMap[char]
            // Convert half-width space to full-width
            ' ' -> '　'
            // Leave other characters as they are
            else -> char
        }
    }.joinToString(""))
}

// Merges a composite characters with a separate voiced mark character to a single character
// Half width characters like ﾄﾞ contains two bytes, one for the base character ﾄ and another for
// the voiced character ﾞ. When converting to full width, it would become ト゛ instead of ド
const val DAKUTEN = 0x309B // Stand-alone voiced sound mark
const val HAN_DAKUTEN = 0x309C // Stand-alone semi-voiced mark
fun mergeVoicedMarks(value: String): String {
    val charArray = mutableListOf<Int>()
    var i = 0
    while (i < value.length) {
        val charCode = value[i].code
        val nextChar = value.getOrNull(i + 1)?.code ?: 0
        var newCharCode = charCode
        when {
            nextChar == DAKUTEN && ( // う and ウ
                0x3046 == charCode || 0x30a6 == charCode
            ) -> newCharCode += 78
            nextChar == DAKUTEN && ( // if odd, か ... ち and カ ... チ    if even, つ ... と and ツ ... ト
                charCode % 2 == 1 && charCode in listOf(0x304B..0x3061, 0x30AB..0x30C2).flatten() ||
                charCode % 2 == 0 && charCode in listOf(0x3064..0x3068, 0x30C4..0x30C9).flatten()
            ) -> newCharCode += 1
            (nextChar == DAKUTEN || nextChar == HAN_DAKUTEN) && ( // は ... ほ and ハ ... ホ
                charCode % 3 == 0 && charCode in listOf(0x306F..0x307B, 0x30CF..0x30DB).flatten()
            ) -> newCharCode += if (nextChar == DAKUTEN) 1 else 2
            else -> newCharCode = charCode
        }
        charArray.add(newCharCode)
        if (newCharCode != charCode) i++ // Merged, increment index by one extra to skip the voiced mark
        i++
    }
    return String(charArray.map { it.toChar() }.toCharArray())
}

@Preview
@Composable
fun ConverterUI(
    onCopy: (String) -> Unit = {},
    onPaste: () -> String = { "" },
    selectedText: String = ""
) {
    var h2f by remember { mutableStateOf(true)}
    val convertFrom = remember { TextFieldState(label="Input Text", defaultValue=selectedText) }

    fun converter(e: String): String {
        return if (h2f) {
            convertToFull(e)
        } else {
            convertToHalf(e)
        }
    }

    var converted by remember { mutableStateOf(converter(selectedText)) }

    fun swapDirection() {
        h2f = !h2f
        converted = converter(convertFrom.text)
    }
    val activity = (LocalContext.current as? Activity)

    Box(
        modifier = Modifier.padding(10.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(Modifier.weight(1f)) {
                TextInput(convertFrom, onChange= { converted = converter(it) })
                if (convertFrom.text == "") IconButton(
                    onClick = {
                        val text = onPaste()
                        convertFrom.text = text
                        converted = converter(text)
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        painterResource(R.drawable.content_paste),
                        contentDescription = "Copy"
                    )
                }
            }
            ThreeColumnLayout(
                h2f,
                changeDirection={ swapDirection() }
            )
            Box(Modifier.weight(1f)) {
                SelectionContainer {
                    Text(
                        converted,
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                            .padding(10.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
                IconButton(
                    onClick = {
                        onCopy(converted)
                        if (selectedText != "") {
                            activity?.finish()
                        }
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        painterResource(R.drawable.content_copy),
                        contentDescription = "Copy"
                    )
                }
            }
        }
    }
}