package com.lingsatuo.command

import android.annotation.SuppressLint
import android.content.Context
import com.lingsatuo.terminallauncher.R
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class CommandFactory private constructor(private var context: Context) {
    private var lis: (String) -> Unit = { _ -> }
    private var breakAll = false
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: CommandFactory? = null

        fun getInstance(context: Context): CommandFactory {
            return if (instance == null) {
                instance = CommandFactory(context)
                instance!!
            } else {
                instance!!
            }
        }
    }

    fun getUser(): String {
        return ">_ "
    }

    fun breakAll(breakall: Boolean){
        breakAll = breakall
    }

    fun commandRuned(command: String) {
        breakAll = false
        when (command[0]) {
            '-' -> {
                doAppCommand(command)
            }
            else -> {
                execBin(command)
            }
        }
    }

    /**
     * 设置信息刷新监听
     * */
    fun setMessageListener(lis: (String) -> Unit) {
        this.lis = lis
    }

    /**
     * 执行App的命令行
     * @param command 命令
     * @return 返回运行结果
     * */
    private fun doAppCommand(command: String) {
        lis.invoke(when (command.toLowerCase()) {
            "-version" -> {
                context.resources.getString(R.string.version)
            }
            else -> {
                context.resources.getString(R.string.command_not_found)
            }
        })
    }

    /**
     * 执行系统bin
     * @param name 文件的名字 脚本 参数
     * @return 返回运行结果
     * */
    private fun execBin(name: String) {
        val filename = name.split(" ")[0]
        if (!File("/system/bin/$filename").exists()) {
            lis.invoke(context.resources.getString(R.string.command_not_found))
            return
        }
        try {
            val process = Runtime.getRuntime().exec("/system/bin/$name")
            val error = BufferedReader(InputStreamReader(process.errorStream))
            Thread({
                while (true) {
                    if (breakAll){
                        error.close()
                        break
                    }
                    val s = error.readLine() ?: break
                    lis.invoke(s)
                }
            }).start()
            val inreader = BufferedReader(InputStreamReader(process.inputStream))
            while (true) {
                if (breakAll){
                    inreader.close()
                    break
                }
                val s = inreader.readLine() ?: break
                lis.invoke(s)
            }
        } catch (e: Throwable) {
            val mess = e.message ?: ""
            val array = mess.split(",")
            if (array.size > 1) {
                lis.invoke(mess.substring(array[0].length + 1, mess.length))
                return
            }
            lis.invoke(mess)
        }
    }
}