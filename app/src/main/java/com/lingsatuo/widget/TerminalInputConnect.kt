package com.lingsatuo.widget

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.inputmethod.*


open class TerminalInputConnect(private var terMinalEdit: TerMinalEdit) : BaseInputConnection(terMinalEdit,false) {

    override fun commitText(p0: CharSequence?, p1: Int): Boolean {
        if ((p0?:"").toString() == "\n"){
            sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER))
            return true
        }
        terMinalEdit.write((p0?:"").toString().toCharArray())
        return true
    }

    override fun closeConnection() {
        super.closeConnection()
    }

    override fun commitCompletion(p0: CompletionInfo?): Boolean {
        return super.commitCompletion(p0)
    }

    override fun setComposingRegion(p0: Int, p1: Int): Boolean {
        return super.setComposingRegion(p0, p1)
    }

    override fun performContextMenuAction(p0: Int): Boolean {
        return super.performContextMenuAction(p0)
    }

    override fun setSelection(p0: Int, p1: Int): Boolean {
        return super.setSelection(p0, p1)
    }

    override fun requestCursorUpdates(p0: Int): Boolean {
        return super.requestCursorUpdates(p0)
    }

    override fun getTextBeforeCursor(p0: Int, p1: Int): CharSequence? {
        return null
    }

    override fun getHandler(): Handler? {
        return super.getHandler()
    }

    override fun deleteSurroundingTextInCodePoints(p0: Int, p1: Int): Boolean {
        return super.deleteSurroundingTextInCodePoints(p0, p1)
    }

    override fun getExtractedText(p0: ExtractedTextRequest?, p1: Int): ExtractedText? {
        return super.getExtractedText(p0, p1)
    }

    override fun beginBatchEdit(): Boolean {
        return super.beginBatchEdit()
    }

    override fun setComposingText(p0: CharSequence?, p1: Int): Boolean {
        return super.setComposingText(p0, p1)
    }

    override fun clearMetaKeyStates(p0: Int): Boolean {
        return super.clearMetaKeyStates(p0)
    }

    override fun endBatchEdit(): Boolean {
        return super.endBatchEdit()
    }

    override fun getSelectedText(p0: Int): CharSequence {
        return ""
    }

    override fun reportFullscreenMode(p0: Boolean): Boolean {
        return super.reportFullscreenMode(p0)
    }

    override fun deleteSurroundingText(p0: Int, p1: Int): Boolean {
        terMinalEdit.delete()
        return super.deleteSurroundingText(p0, p1)
    }

    override fun getCursorCapsMode(p0: Int): Int {
        return super.getCursorCapsMode(p0)
    }

    override fun getTextAfterCursor(p0: Int, p1: Int): CharSequence {
        return terMinalEdit.getBefore()
    }

    override fun performPrivateCommand(p0: String?, p1: Bundle?): Boolean {
        return super.performPrivateCommand(p0, p1)
    }

    override fun sendKeyEvent(p0: KeyEvent?): Boolean {
        if (p0?.action == KeyEvent.ACTION_DOWN){
            if (p0.keyCode == KeyEvent.KEYCODE_ENTER){
                terMinalEdit.write("\n".toCharArray())
                terMinalEdit.enterCommand()
            }else if (p0.keyCode == KeyEvent.KEYCODE_DEL){
                terMinalEdit.delete()
            }else{
                terMinalEdit.write(KeyCode2String.getString(p0).toCharArray())
            }
        }
        return true
    }

    override fun finishComposingText(): Boolean {
        return super.finishComposingText()
    }

    override fun commitCorrection(p0: CorrectionInfo?): Boolean {
        return super.commitCorrection(p0)
    }

    override fun commitContent(p0: InputContentInfo?, p1: Int, p2: Bundle?): Boolean {
        return super.commitContent(p0, p1, p2)
    }

    override fun performEditorAction(p0: Int): Boolean {
        return super.performEditorAction(p0)
    }
    fun getCharCode(code:Int) : String{
        return "-version"
    }
}