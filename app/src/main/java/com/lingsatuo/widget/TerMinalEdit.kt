package com.lingsatuo.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import com.lingsatuo.Typeface
import com.lingsatuo.command.CommandFactory
import com.lingsatuo.terminallauncher.R

class TerMinalEdit : View {
    /**单个字符的高度*/
    private var textheight = 0f

    /**单个字符的宽度*/
    private var textwidth = 0f

    /**字体大小*/
    private var textSize = 30f

    /**画笔*/
    private var textPaint = Paint()

    /**画笔颜色*/
    private var textColor = 0

    /**光标位置*/
    private var index = 0

    /**单行最长字符*/
    private var linemax = 12

    /**行间距*/
    private var lineSpacing = 12

    /**总行数 ， 绘制时会重新记录*/
    private var linenum = 0

    /**总行数 ， 绘制时会不重新记录*/
    private var linenumNotReset = 0


    /**记录字符*/
    private val terminalString = TerminalString()

    /**不能删除的长度*/
    private var cannotdelete = 0

    /**最大能显示的行数*/
    private var maxshowlines = 0

    /**当前绘制的行数*/
    private var nowline = 0

    /**输入法连接器*/
    private var terminalInputConnect: TerminalInputConnect? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    /**
     * @param context
     * @param attributeSet
     * @param def
     * @return Unit
     * <p>初始化画笔，设置字体，初始化编辑器
     * */

    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(context, attributeSet, def) {
        textColor = context.resources.getColor(R.color.button_textColor)
        textPaint.color = textColor
        textPaint.textSize = textSize
        textPaint.typeface = Typeface.getType(context)
        textPaint.style = Paint.Style.FILL
        terminalString.setCharArray("LauncherOne :/ $ ".toCharArray())
        cannotdelete = terminalString.get().size
        initialize()
        isFocusable = true
        isFocusableInTouchMode = true
        CommandFactory.getInstance(context).setMessageListener { back ->
            run {
                write(("\t$back\n${CommandFactory.getInstance(context).getUser()} ").toCharArray())
                commandFinish()
            }
        }

    }

    @Synchronized
    override fun onDraw(canvas: Canvas?) {
        index = 0
        linenum = 0
        linemax = ((measuredWidth - textwidth) / textwidth).toInt()
        if (canvas == null) return
        drawText(canvas, terminalString.get())
    }


    /**
     * 绘制文字
     * <p> 自动分行
     * @param charArray 需要被显示的文本内容
     * */
    @Synchronized
    private fun drawText(canvas: Canvas, charArray: CharArray) {
        var index = 0
        var l = 0 // 当前需要绘制的行
        for (char in charArray) {
            index++
            when {
                index > linemax -> {
                    index = 0
                    linenum++
                    this.index += 1
                    if (linenum in nowline..nowline + maxshowlines)
                        l++
                }
                char == '\n' -> {
                    index = 0
                    linenum++
                    this.index += 1
                    if (linenum in nowline..nowline + maxshowlines)
                        l++
                }
                else -> {
                    this.index += 1
                }

            }
            if (linenum in nowline..nowline + maxshowlines) {
                canvas.drawText(char.toString(), (index) * textwidth, (textheight * 1.2f) * (l + 1), textPaint)
            }
        }
        drawCursor(canvas, index, l)
    }

    /**
     * 绘制光标
     * @param canvas 画布
     * @param index 索引
     * @param line 所在的行数
     * */
    @Synchronized
    private fun drawCursor(canvas: Canvas, index: Int, line: Int) {
        canvas.drawRect(RectF((index + 1) * textwidth, (textheight * 1.2f) * (line + 1) - textheight * 1f,
                (index + 2) * textwidth, (textheight * 1.2f) * (line + 1) + textheight * 0.2f), textPaint)
    }

    /**
     *
     * */
    fun getBefore(): String {
        return ""
    }

    /**
     * 在光标处写入一个字符串
     * */
    @Synchronized
    fun write(charArray: CharArray) {
        terminalString.insert(index, charArray)
        index += charArray.size
        post({
            linenumNotReset = terminalString.getLines(linemax)
            if (linenumNotReset > maxshowlines) setNowLine(linenumNotReset - maxshowlines + 1, linenumNotReset)
            invalidate()
        })
    }

    /**
     * 删除光标前的一个字符
     * */
    @Synchronized
    fun delete() {
        try {
            if (index <= cannotdelete) return
            terminalString.delete(--index)
            invalidate()
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    /**
     * 初始化
     * <p> 初始化画笔
     * 初始化文字高度宽度，宽高用于绘制光标
     * @return Unit
     * */
    private fun initialize() {
        val rect = Rect()
        textPaint.getTextBounds("S", 0, 1, rect)
        textheight = rect.height() * 1.2f
        textwidth = textPaint.measureText("S")
    }

    /**
     * 可编辑状态
     * @return boolean true为可编辑
     * */
    override fun onCheckIsTextEditor(): Boolean {
        return true
    }

    /**
     * 建立连接时指定输入类型
     * */
    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        if (outAttrs == null) return super.onCreateInputConnection(outAttrs)
        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_FULLSCREEN
        outAttrs.inputType = InputType.TYPE_NULL
        return if (terminalInputConnect == null) {
            terminalInputConnect = TerminalInputConnect(this)
            terminalInputConnect!!
        } else {
            terminalInputConnect!!
        }
    }

    var yy = 0f //下落点的y
    var downy = 0f //滑动的y
    /**弹出软键盘*/
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                yy = event.y
                downy = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val my = event.y - downy
                if (my > textheight) {
                    scroll(true, Math.abs(my))
                    downy = event.y
                } else if (my < textheight * -1) {
                    scroll(false, Math.abs(my))
                    downy = event.y
                }
            }
            MotionEvent.ACTION_UP -> {
                if (Math.abs(downy - yy) < 20) {
                    InputMethodHelper.getInstance(this.context).show(this)
                    if (linenumNotReset > maxshowlines) setNowLine(linenumNotReset - maxshowlines)
                } else;
            }
        }
        return true
    }


    /**
     * 输入完成
     * */
    fun commandFinish() {
        cannotdelete = terminalString.get().size
    }

    /**
     * 确定运行指令
     * */

    @Synchronized
    fun enterCommand() {
        val command = getCommand()
        if (command.isEmpty()) {
            write("${CommandFactory.getInstance(context).getUser()} ".toCharArray())
            commandFinish()
            return
        }
        Thread({
            CommandFactory.getInstance(context).commandRuned(command)
        }).start()
    }

    /**
     * 获取将要执行的的命令字符串
     * @return 将要执行的字符串
     * */
    fun getCommand(): String {
        return terminalString.subSequence(cannotdelete, terminalString.get().size - 2)
    }


    /**动画*/
    private var ani: ValueAnimator? = null

    /**
     * 自动滚动
     * */
    private fun scroll() {
        if ((linenum + 1) * textheight * 1.2f < measuredHeight) return
        if (ani?.isRunning == true) ani?.end()
        //总高度
        val max = (linenum + 1) * textheight * 1.2f

        //需要滚动的高度
        val needtoscroll = max - measuredHeight - scrollY + textheight
        ani = ValueAnimator.ofFloat(needtoscroll).setDuration(500)
        ani!!.addUpdateListener { an ->
            if (an.animatedValue as Float != 0f)
                scrollY = (scrollY + (an.animatedValue as Float)).toInt()
        }
        ani!!.start()
    }

    /**
     * 手势滚动
     * */
    private fun scroll(downScroll: Boolean, down: Float) {
        if (ani?.isRunning == true) ani?.end()
        val lines = (down / textheight).toInt()
        if (downScroll) setNowLine(nowline - lines) else setNowLine(nowline + lines)
        if (nowline < 0) {
            nowline = 0
        }
        if (nowline > linenumNotReset) {
            nowline = linenumNotReset
        }
        invalidate()
    }


    /**
     * 计算View宽高
     * */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(useAllDimensions(widthMeasureSpec),
                useAllDimensions(heightMeasureSpec))
    }


    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (changed) {
            val rect = Rect()
            getWindowVisibleDisplayFrame(rect)
            invalidate()
        }
        super.onLayout(changed, left, top, right, bottom)
    }


    /**
     * 当布局发生变化，监听软键盘弹起或关闭
     * */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        maxshowlines = (h / textheight / 1.2f).toInt() - 2
        setNowLine(linenumNotReset - maxshowlines + 1)
    }


    fun setNowLine(line: Int) {
        setNowLine(line, linenumNotReset)
    }

    /**设置当前应该绘制的行数*/
    fun setNowLine(line: Int, max: Int) {
        nowline = line
        if (line < 0) nowline = 0
        if (line > max) nowline = max
    }

    /**
     * 计算布局宽高
     * */
    private fun useAllDimensions(measureSpec: Int): Int {
        val specMode = View.MeasureSpec.getMode(measureSpec)
        var result = View.MeasureSpec.getSize(measureSpec)

        if (specMode != View.MeasureSpec.EXACTLY && specMode != View.MeasureSpec.AT_MOST) {
            result = Integer.MAX_VALUE
        }

        return result
    }
}