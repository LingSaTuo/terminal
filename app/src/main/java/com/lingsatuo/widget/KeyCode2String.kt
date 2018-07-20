@file:Suppress("UNUSED_EXPRESSION")

package com.lingsatuo.widget

import android.view.KeyEvent


/**
 * 把KeyCode转成String
 * */
object KeyCode2String {
    /**
     * 转成String
     * @param keyEvent 发送的keyevent
     * @return 返回按下的按钮的char
     * */
    fun getString(keyEvent: KeyEvent): String {
        val code = keyEvent.keyCode
        val isshift = keyEvent.isShiftPressed
        if (code in 7..16 && !isshift) return getNumber(code)
        else if (code in 29..54) return getEng(code, isshift)
        else return getSymbol(code,isshift)
    }

    /**
     * 获取数字
     * @param code 按下的按键
     * @return char
     * */
    private fun getNumber(code: Int): String {
        return "${code - 7}"
    }

    /**
     * 获取字母
     * @param code 按下的按键的keycode
     * @param isshift 是否按了shift [true] 返回大写 [false] 返回小写
     * @return 按下的字符转chr
     * */
    private fun getEng(code: Int, isshift: Boolean): String {
        val offset = if (isshift) 36 else 68
        return String(charArrayOf((offset + code).toChar()))
    }

    /**
     * 获取英文字符
     * @param code 按下的按键
     * @param isshift 是否按下了shift
     * @return 返回按下的符号
     * */
    private fun getSymbol(code: Int, isshift: Boolean): String {
       return when(code){
            77->{"@"}
            18->{"#"}
            11->{"$"}
            69->{if (isshift) "_" else "-"}
            14->{"&"}
            81->{"+"}
            16->{"("}
            7->{")"}
           76->{if (isshift) "?" else "/"}
           17->{"*"}
           75->{if (isshift)"\"" else "\'"}
           74->{if (isshift)":" else ";"}
           8->{"!"}
           68->{if (isshift) "~" else "`"}
           73->{if(isshift) "|" else "\\"}
           13->{"^"}
           70->{if (!isshift) "=" else "+"}
           71->{if (isshift) "{" else "["}
           72 ->{if (isshift) "}" else "]"}
           12 ->{"%"}
           56->{if (isshift) ">" else "."}
           55->{if (isshift) "<" else ","}
            else->{""}
        }
    }
}