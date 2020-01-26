package com.example.coolkulator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    //text view to display input and output
    lateinit var textInput: TextView

    // to represent whether the last pressed key is a numeric value
    var lastNumeric: Boolean = false

    //to represent whether the current state is in error or not
    var stateError:Boolean = false

    //If true, do not allow the add another DOT
    var lastDot:Boolean = false

    //if true, a total has been calculated
    var isTotal:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textInput = findViewById(R.id.txtInput)
    }


    /**
     * This appends the Button.text to the textview
     */
    fun onDigit(view: View) {
        if (stateError) {
            textInput.text = (view as Button).text
            stateError = false
        } else {
            // If not, already there is a valid expression so append to it
            //this maintains the previous number and adds to the position next to it
            //todo check if the output is not the total before appending, if it is then override with new value to calculate
            if (isTotal && lastNumeric) {
                textInput.text = (view as Button).text
                isTotal = false
            } else {
                textInput.append((view as Button).text)
            }
        }
        //set the flag
        lastNumeric = true

    }

    /**
     Append . to the TextView
     */
    fun onDecimalPoint(view: View) {
        if (lastNumeric && !stateError && !lastDot) {
            textInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    /**
     * Append +,-,*,/ operators to the text view
     */
    fun onOperator(view: View) {
        if(lastNumeric && !stateError) {
            textInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false //reset the DOT flag
        }
    }
    /**
     * Clear the textview
     */
    fun onClear(view: View) {
        this.textInput.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
        isTotal = false
    }

    /**
     * Calculate the outout using Wxp4j
     */
    fun onEqual(view: View) {
        //if the current state is error, nothing to do
        //if the last input is a number only, solution can be found
        if (lastNumeric && !stateError) {
            // Read the expression
            val txt = textInput.text.toString()
            //create an expression (A class from exp4j library)
            val expression = ExpressionBuilder(txt).build()
            try {
                //calculate the result and display
                val result = expression.evaluate()
                textInput.text = result.toString()
                lastDot = true //results contain a dot
                isTotal = true
            } catch (e: ArithmeticException) {
                //display an error message
                textInput.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}
