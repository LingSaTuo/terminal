package com.lingsatuo.widget;

import org.jetbrains.annotations.NotNull;

public class TerminalString implements TerminalSequence {
    public TerminalString() {
        setCharArray(" ".toCharArray());
    }

    private char[] charArray = new char[0];

    public synchronized void setCharArray(char[] array) {
        this.charArray = array;
    }

    @Override
    public synchronized char get(int index) {
        return charArray[index];
    }

    @Override
    public synchronized void replace(int start, int end, @NotNull char[] charArray) {
        int length = end - start + 1;
        char[] newchar;

        if (charArray.length == 0) {
            delete(start, end);
            return;
        }

        if (charArray.length > length) {
            newchar = new char[this.charArray.length + charArray.length - length];
            for (int a = 0; a < newchar.length; a++) {
                if (a < start) {
                    newchar[a] = this.charArray[a];
                } else if (a >= start && a <= start + charArray.length - 1) {
                    newchar[a] = charArray[a - start];
                } else {
                    newchar[a] = this.charArray[a - length];
                }
            }
        } else {
            newchar = new char[this.charArray.length - charArray.length + length];
            int idx = 0;
            for (int a = 0; a < this.charArray.length; a++) {
                if (a < start) {
                    newchar[idx++] = this.charArray[a];
                } else if (a >= start && a <= end) {
                    if (a - start <= length) {
                        newchar[idx++] = charArray[a - start];
                    }
                } else {
                    newchar[idx++] = this.charArray[a];
                }
            }
        }
        this.charArray = newchar;
    }

    /**
     * 追加内容
     *
     * @param charArray 需要被追加的内容
     */
    public synchronized void append(char[] charArray) {
        insert(this.charArray.length, charArray);
    }


    /**
     * 删除最后一个字符
     */
    public synchronized void delete() {
        delete(this.charArray.length);
    }

    @Override
    public synchronized int length() {
        return charArray.length;
    }

    @NotNull
    @Override
    public synchronized String subSequence(int startIndex, int endIndex) {
        if (endIndex >= this.charArray.length) endIndex = this.charArray.length - 1;
        char[] newchararray = new char[endIndex - startIndex + 1];
        for (int a = 0; a < this.charArray.length; a++) {
            if (a >= startIndex && a <= endIndex) {
                newchararray[a - startIndex] = this.charArray[a];
            }
        }
        if (startIndex >= this.charArray.length) return "";
        return new String(newchararray);
    }

    public synchronized String toString() {
        return new String(charArray);
    }

    @Override
    public synchronized void insert(int index, @NotNull char[] charArray) {
        if (index < 0) index = 0;
        char[] newchararray = new char[this.charArray.length + charArray.length];
        if (index >= this.charArray.length) {
            for (int a = 0; a < newchararray.length; a++) {
                if (a < this.charArray.length) {
                    newchararray[a] = this.charArray[a];
                } else {
                    newchararray[a] = charArray[a - this.charArray.length];
                }
            }
        } else if (index == 0) {
            for (int a = 0; a < newchararray.length; a++) {
                if (a < charArray.length) {
                    newchararray[a] = charArray[a];
                } else {
                    newchararray[a] = this.charArray[a - charArray.length];
                }
            }
        } else {
            int idx = 0;
            for (int a = 0; a < newchararray.length; a++) {
                if (a < index) {
                    newchararray[a] = this.charArray[a];
                } else if (a >= index && a < index + charArray.length) {
                    newchararray[a] = charArray[idx++];
                } else {
                    newchararray[a] = this.charArray[a - charArray.length];
                }
            }
        }
        this.charArray = newchararray;
    }

    public synchronized void delete(int index) {
        delete(index, index);
    }

    @Override
    public synchronized void delete(int startindex, int endindex) {
        if (startindex < 0 || endindex < 0) return;
        int length = endindex - startindex + 1;
        char[] newchararray = new char[this.charArray.length - length];
        for (int a = 0; a < this.charArray.length; a++) {
            if (a < startindex) {
                newchararray[a] = this.charArray[a];
            } else if (a > endindex) {
                newchararray[a - length] = this.charArray[a];
            }
        }
        this.charArray = newchararray;
    }

    @NotNull
    @Override
    public synchronized char[] get() {
        return this.charArray;
    }

    @Override
    public synchronized int getLines(int onlineLength) {
        int lines = 0;
        int index = 0;
        for (char c : this.charArray) {
            index++;
            if (index > onlineLength) {
                lines++;
                index = 0;
            }
            else if (c == '\n') {
                index = 0;
                lines++;
            }
        }
        return lines;
    }
}
