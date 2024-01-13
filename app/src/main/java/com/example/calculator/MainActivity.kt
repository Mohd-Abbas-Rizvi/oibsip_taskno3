package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var lastNumeric=false
    var stateError=false
    var lastDot=false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onAllClearClick(view:View){
        binding.datatv.text=""
        binding.resulttv.text=""
        stateError=false
        lastDot=false
        lastNumeric=false
        binding.resulttv.visibility=View.GONE
    }
    fun onEqualClick(view:View){
        onEqual()
        binding.datatv.text=binding.resulttv.text.toString().drop(1)
    }
    fun onDigitClick(view:View){
        if(stateError){
            binding.datatv.text=(view as Button).text
            stateError=false
        }
        else
        {
            binding.datatv.append((view as Button).text)
        }
        lastNumeric=true
        onEqual()
    }
    fun onOperatorClick(view:View) {
        if (!stateError && lastNumeric) {
            binding.datatv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }
    fun onBackClick(view:View){
        binding.datatv.text=binding.datatv.text.toString().dropLast(1)
        try {
            val lasttChar= binding.datatv.text.toString().last()
            if(lasttChar.isDigit()){
                onEqual()
            }
        }
        catch (e:Exception){
            binding.resulttv.text=""
            binding.resulttv.visibility=View.GONE
            Log.e("Last char error",e.toString())
        }
    }
    fun onClearClick(view:View){
        binding.datatv.text=""
        lastNumeric=false
    }

    fun onEqual(){
        if(lastNumeric && !stateError){
            val text=binding.datatv.text.toString()
            expression= ExpressionBuilder(text).build()
            try {
                val result=expression.evaluate()
                binding.resulttv.visibility=View.VISIBLE
                binding.resulttv.text="="+result.toString()
            }
            catch (ex:ArithmeticException){
                Log.e("evaluate error",ex.toString())
                binding.resulttv.text="Error"
                stateError=true
                lastNumeric=false
            }
        }
    }


}