package com.example.uohih.joowon.base

import android.util.Log
import java.lang.StringBuilder
import java.text.MessageFormat

/**
 * log
 */
object LogUtil {
    private val TAG = "MY_TAG"

//    fun logMethodCalled() {
//        doLog(Log.DEBUG, "called")
//    }

    fun v(vararg text: Any) {
        doLog(Log.VERBOSE, *text)
    }

    fun d(vararg text: Any) {
        doLog(Log.DEBUG, *text)
    }

    fun i(vararg text: Any) {
        doLog(Log.INFO, *text)
    }

    fun w(vararg text: Any) {
        doLog(Log.WARN, *text)
    }

    fun e(vararg text: Any) {
        doLog(Log.ERROR, *text)
    }


    private fun doLog(logLevel: Int, vararg logText: Any) {
        val stb = StringBuilder()
        for(i in logText.indices){
            stb.append(logText[i])
            stb.append(", \t")
        }
        var log = stb.toString()

        val stackTrace = Thread.currentThread()
                .stackTrace

        //take stackTrace element at index 4 because:
        //0: VMStack.getThreadStackTrace(Native Method)
        //1: java.lang.Thread.getStackTrace
        //2: LogUtil -> doLog method (this method)
        //3: LogUtil -> log method
        //4: this is the calling method!
        if (stackTrace != null && stackTrace.size > 4) {

            val element = stackTrace[4]

            val fullClassName = element.className
            val simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)

            //add class and method data to logText
            log = MessageFormat.format("\nT:{0} | {1} {2}() {3}|" +
                    "\n------------------------------\n" +
                    "{4}" +
                    "\n------------------------------", Thread.currentThread()
                    .id, simpleClassName, element.methodName, element.lineNumber, stb.toString())
        }

        Log.println(logLevel, TAG, log)
    }

}