/*
 * Copyright (C) 2020 Nesprasit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nesprasit.library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Parcelable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View

/**
 * Created by Nesprasit (https://Maew.dev) on 15/1/2019 AD.
 */
class BaseProgressbar: View {
    private val tag = this::class.java.simpleName

    private var onUpdateView:(()->Unit)? = null
    private var progressDrawable:ShapeDrawable? = null
    private var progressBackgroundDrawable:GradientDrawable? = null
    private var isSaveInstance:Boolean = false

    var max:Float = 100F
    private var maxWidth:Int = 0
    private var radius:Int = 1
        set(value) {
            field = value.toFloat().dpToPx().toInt()
            onUpdateView?.let { callback->callback() } }

    private var padding:Int = 0
        set(value) {
            field = value.toFloat().dpToPx().toInt()
            onUpdateView?.let { callback->callback() } }

    var progressColor:Int = Color.BLUE
        set(value) {
            field = value
            progressDrawable?.paint?.color = field
        }

    var progressBackgroundColor:Int = Color.GRAY
        set(value) {
            field = value
            progressBackgroundDrawable?.setColor(field)
        }

    var progressStrokeWidth = 0
        set(value) {
            field = value.toFloat().dpToPx().toInt()
            progressBackgroundDrawable?.setStroke(field,progressStrokeColor)
        }

    var progressStrokeColor:Int = Color.WHITE
        set(value) {
            field = value
            progressBackgroundDrawable?.setStroke(progressStrokeWidth,field)
        }

    var progress = 0.0F
        set(value) {
            field = when {
                value > max -> max
                value < 0.0 -> 0.0F
                else -> value
            }

            invalidate()
        }

    constructor(context: Context):super(context){
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet):
            super(context,attributeSet){
        init()
        initStyleable(context,attributeSet,0,0)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr:Int):
            super(context,attributeSet,defStyleAttr){
        init()
        initStyleable(context,attributeSet,defStyleAttr,0)
    }

    @SuppressLint("NewApi")
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int, defStyleRes: Int) :
            super(context,attributeSet,defStyleAttr,defStyleRes){
        init()
        initStyleable(context,attributeSet,defStyleAttr,defStyleRes)
    }

    /**
     * Initialize
     * */

    private fun init(){

        // setUp progress
        progressDrawable = ShapeDrawable()
        progressDrawable?.paint?.color = progressColor

        // setUp progress background
        progressBackgroundDrawable = GradientDrawable()
        progressBackgroundDrawable?.setColor(progressBackgroundColor)
        progressBackgroundDrawable?.setStroke(progressStrokeWidth,progressStrokeColor)

        // view set background
        this.background = progressBackgroundDrawable

        onUpdateView = {
            val roundRectShape = calRoundRectShape()
            progressDrawable?.shape = roundRectShape.first
            progressBackgroundDrawable?.cornerRadii = roundRectShape.second
        }
    }

    private fun initStyleable(context: Context, attributeSet : AttributeSet, defStyleAttr: Int, defStyleRes: Int){
        val a = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.BaseProgressbar,
            defStyleAttr,
            defStyleRes)

        try {
            padding = a.getDimension(R.styleable.BaseProgressbar_padding,padding.toFloat()).toInt()
            radius = a.getDimension(R.styleable.BaseProgressbar_radius,radius.toFloat()).toInt()

            max = a.getFloat(R.styleable.BaseProgressbar_max,max)
            progress = a.getFloat(R.styleable.BaseProgressbar_progress,progress)

            progressColor = a.getColor(R.styleable.BaseProgressbar_progressColor,progressColor)
            progressBackgroundColor = a.getColor(R.styleable.BaseProgressbar_progressBackgroundColor,progressBackgroundColor)

            progressStrokeWidth = a.getDimension(R.styleable.BaseProgressbar_progressStrokeWidth,progressStrokeWidth.toFloat()).toInt()
            progressStrokeColor= a.getColor(R.styleable.BaseProgressbar_progressStrokeColor,progressStrokeColor)
        }finally {
            a.recycle()
        }

        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.maxWidth = w
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val width = (progress() + padding)
            val height = this.height - padding

            progressDrawable?.setBounds(
                padding,
                padding,
                if(width <= 0) 0 else width,
                if(height <= 0) 0 else height)

            progressDrawable?.draw(canvas)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        isSaveInstance = true

        val parcel =   super.onSaveInstanceState()
        if(parcel != null){
            val save = SaveState(parcel)
            save.isSaveInstance = isSaveInstance
            return save
        }

        return null
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if(state !is SaveState){
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)

        isSaveInstance = state.isSaveInstance
    }

    /**
     * Data class
     * */

    data class SaveState(
        var parcel:Parcelable,
        var isSaveInstance:Boolean = false
    ): BaseSavedState(parcel)

    /**
     * Functional
     * */

    private fun progress(): Int {
        val ratio = (max / progress)
        return ((maxWidth - (padding * 2)) / ratio).toInt()
    }

    private fun Float.dpToPx():Float{
        val displayMetrics = context.resources.displayMetrics
        return (this * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    private fun calRoundRectShape():Pair<RoundRectShape,FloatArray>{
        val newRadius = (radius - (padding / 2)).toFloat()
        val outerRadii = floatArrayOf(
            newRadius,newRadius, // radius left top
            newRadius,newRadius, // radius right top
            newRadius,newRadius, // radius right bottom
            newRadius,newRadius  // radius left bottom
        )

        // initialize shape
        val progressShape = RoundRectShape(outerRadii,null,null)

        return Pair(progressShape,outerRadii)
    }

    fun reset(){
        progress = 0.0F
        this.invalidate()
    }

}