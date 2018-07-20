package com.lingsatuo.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class InputMethodHelper @SuppressLint("ServiceCast") private constructor(context: Context) {

    /**初始化输入法服务*/
    var inputMethodManager: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    companion object {

        /**预留管理  InputMethodHelper*/
        private var inputMethodHelper: InputMethodHelper? = null

        /**
         * 获取管理实例
         * @param context 上下文
         * @return 管理对象
         * */
        fun getInstance(context: Context): InputMethodHelper {
            synchronized(InputMethodHelper::class) {
                return if (inputMethodHelper == null) {
                    inputMethodHelper = InputMethodHelper(context)
                    inputMethodHelper!!
                } else {
                    inputMethodHelper!!
                }
            }
        }
    }

    /**
     * 显示软键盘
     * @param view 显示在哪个View上
     * */
    fun show(view: View) {
        inputMethodManager.showSoftInput(view, 0)
    }

    /**
     * 隐藏软键盘
     * @param view 把哪个View的软键盘断开
     * */
    fun dismiss(view: View) {
        if (inputMethodManager.isActive) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}