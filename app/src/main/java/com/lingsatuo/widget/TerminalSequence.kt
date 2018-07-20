package com.lingsatuo.widget

interface TerminalSequence {
    /**
     * 获取索引位置的字符
     * @param index 索引的位置
     * @return Char 返回索引位置的字符
     * */
    fun get(index: Int): Char

    /**
     * 替换文本
     * @param start 开始的位置
     * @param end 结束的位置
     * @param charArray 需要替换的文本组
     * */
    fun replace(start: Int, end: Int, charArray: CharArray)

    /**
     * 获取文本长度
     * @return Int 返回文本的长度
     * */
    fun length(): Int

    /**
     * 返回从[startIndex] 到  [endIndex] 的文本
     * @param startIndex 开始的索引位置
     * @param endIndex 结束的位置
     * @return 开始到结束的文本
     * */

    fun subSequence(startIndex: Int, endIndex: Int): String

    /**
     * @param index 插入位置的索引
     * @param charArray 插入的内容
     * */
    fun insert(index: Int, charArray: CharArray)


    /**
     * @param startindex 删除的开始索引
     * @param endindex   删除结束的索引
     */
    fun delete(startIndex: Int, endIndex: Int)

    /**
     * 获取所有文本
     * @return 文本的char组
     * */
    fun get(): CharArray

    /**
     * @param onlineLength 一行最大的字符长度 ，负数为不到换行符不换行
     * @return 行数
     * */
    fun getLines(onlineLength : Int) : Int
}