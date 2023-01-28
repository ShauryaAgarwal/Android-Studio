package com.example.calculatorproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,bDecimal,bEqual,bAdd,bSubtract,bMultiply,bDivide,bClear,bNegative,bAnswer,bSqRt,bLPar,bRPar,bPwr,bDelete;
    TextView numDisplay, inputDisplay;
    String numDisplayText, inputDisplayText, resultString;
    StringTokenizer tokenizer;
    double tempResult, prevAnswer;
    boolean prevAnswerCheck;
    int indexOfLeftParenthesis, indexOfRightParenthesis, leftParenthesisCounter, rightParenthesisCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button6);
        b7 = findViewById(R.id.button7);
        b8 = findViewById(R.id.button8);
        b9 = findViewById(R.id.button9);
        b0 = findViewById(R.id.button0);
        bDecimal = findViewById(R.id.buttonDecimal);
        bEqual = findViewById(R.id.buttonEqual);
        bAdd = findViewById(R.id.buttonAdd);
        bSubtract = findViewById(R.id.buttonSubtract);
        bMultiply = findViewById(R.id.buttonMultiply);
        bDivide = findViewById(R.id.buttonDivide);
        bClear = findViewById(R.id.buttonClear);
        bNegative = findViewById(R.id.buttonNegative);
        bAnswer = findViewById(R.id.buttonAns);
        bSqRt = findViewById(R.id.buttonSqRt);
        bLPar = findViewById(R.id.buttonLeftPar);
        bRPar = findViewById(R.id.buttonRightPar);
        bPwr = findViewById(R.id.buttonCaret);
        bDelete = findViewById(R.id.buttonBackspace);
        numDisplay = findViewById(R.id.textViewDisplay);
        inputDisplay = findViewById(R.id.inputDisplayTextView);
        numDisplayText = "";
        inputDisplayText = "";
        prevAnswerCheck = false;

        bEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDisplayText = numDisplayText + " =";
                inputDisplay.setText(inputDisplayText);
                tempResult = 0;
                String workingString = numDisplayText;
                /*
                if(workingString.length() == 0)
                {
                    return;
                }
                 */
                for(int i = 0; i < workingString.length(); i++)
                {
                    if(workingString.substring(i,i+1).equals("("))
                        leftParenthesisCounter++;
                    else if(workingString.substring(i,i+1).equals(")"))
                        rightParenthesisCounter++;
                }
                if((leftParenthesisCounter > rightParenthesisCounter) || (rightParenthesisCounter > leftParenthesisCounter))
                {
                    displayError();
                    return;
                }
                try
                {
                    while(workingString.indexOf("(") > -1)
                    {
                        indexOfRightParenthesis = workingString.indexOf(")");
                        indexOfLeftParenthesis = workingString.substring(0,indexOfRightParenthesis).lastIndexOf("(");
                        evaluateEquation(workingString.substring(indexOfLeftParenthesis + 1, indexOfRightParenthesis));
                        workingString = workingString.replace(workingString.substring(indexOfLeftParenthesis, indexOfRightParenthesis + 1), resultString);
                    }
                    evaluateEquation(workingString);
                    numDisplayText = resultString;
                    numDisplay.setText(numDisplayText);
                }
                catch (Exception e)
                {
                    Log.d("tag", "ree" + e.toString());
                    displayError();
                    return;
                }
            }
        });

        bClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numDisplayText = "";
                numDisplay.setText(numDisplayText);
                clearInputDisplayText();
            }
        });

        bNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInputDisplayText();
                numDisplayText += "-";
                numDisplay.setText(numDisplayText);
            }
        });

        bAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prevAnswerCheck) {
                    if((prevAnswer*10)%10 == 0)
                        numDisplayText += Integer.toString((int)prevAnswer);
                    else
                        numDisplayText += Double.toString(prevAnswer);
                    numDisplay.setText(numDisplayText);
                }
            }
        });

        bSqRt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numDisplayText += "√(";
                numDisplay.setText(numDisplayText);
            }
        });

        bPwr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numDisplayText += "^(";
                numDisplay.setText(numDisplayText);
            }
        });

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numDisplayText.length() > 0)
                {
                    numDisplayText = numDisplayText.substring(0, numDisplayText.length()-1);
                    numDisplay.setText(numDisplayText);
                }
            }
        });
    }

    public void clickedButton(View v) {
        clearInputDisplayText();
        numDisplayText += ((Button)v).getText();
        numDisplay.setText(numDisplayText);
    }

    public void clickedOperand(View v) {
        numDisplayText += " " + ((Button)v).getText() + " ";
        numDisplay.setText(numDisplayText);
    }

    public void clearInputDisplayText() {
        inputDisplayText = "";
        inputDisplay.setText("");
    }

    public void displayError() {
        numDisplayText = "";
        numDisplay.setText("ERROR");
    }

    public void evaluateEquation(String equation) {
        tokenizer = new StringTokenizer(equation);
        ArrayList<Double> inputtedNumbers = new ArrayList<Double>();
        ArrayList<String> inputtedOperations = new ArrayList<String>();
        ArrayList<String> totalEquationSplit = new ArrayList<String>();
        while(tokenizer.hasMoreTokens())
        {
            String currentString = tokenizer.nextToken();
            totalEquationSplit.add(currentString);
        }
        for(int i = 0; i < totalEquationSplit.size(); i++)
        {
            if(totalEquationSplit.get(i).equals("+") || totalEquationSplit.get(i).equals("−") || totalEquationSplit.get(i).equals("∗") || totalEquationSplit.get(i).equals("÷"))
                inputtedOperations.add(totalEquationSplit.get(i));
            else
            {
                if(totalEquationSplit.get(i).length() == 1 && (totalEquationSplit.get(i).equals("-") || totalEquationSplit.get(i).equals("√") || totalEquationSplit.get(i).equals("^")))
                {
                    displayError();
                    return;
                }
                for(int j = 1; j < (totalEquationSplit.get(i)).length(); j++)
                {
                    if((totalEquationSplit.get(i).substring(j, j+1)).equals("-") || (totalEquationSplit.get(i).substring(j, j+1)).equals("√"))
                    {
                        displayError();
                        return;
                    }
                }
                if((totalEquationSplit.get(i).substring(0,1)).equals("^") || (totalEquationSplit.get(i).substring((totalEquationSplit.get(i)).length()-1)).equals("^"))
                {
                    displayError();
                    return;
                }
                while(totalEquationSplit.get(i).indexOf("^") > -1)
                {
                    int indexOfLastCaret = totalEquationSplit.get(i).lastIndexOf("^");
                    int indexOfFirstCaret = totalEquationSplit.get(i).indexOf("^");
                    if(indexOfFirstCaret != indexOfLastCaret)
                    {
                        int indexOfSecondToLastCaret = (totalEquationSplit.get(i).substring(0, indexOfLastCaret)).indexOf("^");
                        String tempReplacement = Double.toString(Math.pow(Double.parseDouble(totalEquationSplit.get(i).substring(indexOfSecondToLastCaret + 1, indexOfLastCaret)), Double.parseDouble(totalEquationSplit.get(i).substring(indexOfLastCaret + 1))));
                        totalEquationSplit.set(i, totalEquationSplit.get(i).substring(0, indexOfSecondToLastCaret + 1) + tempReplacement);
                    }
                    else
                    {
                        String tempReplacement = Double.toString(Math.pow(Double.parseDouble(totalEquationSplit.get(i).substring(0, indexOfFirstCaret)), Double.parseDouble(totalEquationSplit.get(i).substring(indexOfFirstCaret + 1))));
                        totalEquationSplit.set(i, tempReplacement);
                    }
                }
                if((totalEquationSplit.get(i).substring(0,1)).equals("√"))
                {
                    totalEquationSplit.set(i, Double.toString(Math.sqrt(Double.parseDouble(totalEquationSplit.get(i).substring(1)))));
                    inputtedNumbers.add(Double.parseDouble(totalEquationSplit.get(i)));
                }
                else
                    inputtedNumbers.add(Double.parseDouble(totalEquationSplit.get(i)));
            }
        }
        for(int i = 0; i < inputtedOperations.size(); i++)
        {
            if(inputtedOperations.get(i).equals("∗"))
            {
                double tempFactor1 = inputtedNumbers.get(i);
                double tempFactor2 = inputtedNumbers.get(i+1);
                double tempProduct = tempFactor1*tempFactor2;
                inputtedOperations.remove(i);
                inputtedNumbers.remove(i+1);
                inputtedNumbers.remove(i);
                inputtedNumbers.add(i, tempProduct);
                i--;
            }
            else if(inputtedOperations.get(i).equals("÷"))
            {
                double tempDividend = inputtedNumbers.get(i);
                double tempDivisor = inputtedNumbers.get(i+1);
                if(tempDivisor == 0)
                {
                    return;
                }
                else
                {
                    double tempQuotient = tempDividend/tempDivisor;
                    inputtedOperations.remove(i);
                    inputtedNumbers.remove(i+1);
                    inputtedNumbers.remove(i);
                    inputtedNumbers.add(i, tempQuotient);
                    i--;
                }
            }
        }
        tempResult = inputtedNumbers.get(0);
        for(int i = 0; i < inputtedOperations.size(); i++)
        {
            if(inputtedOperations.get(i).equals("+"))
                tempResult += inputtedNumbers.get(i+1);
            else if(inputtedOperations.get(i).equals("−"))
                tempResult -= inputtedNumbers.get(i+1);
        }
        //numDisplayText = "";
        prevAnswer = tempResult;
        prevAnswerCheck = true;
        if((tempResult*10)%10 > 0)
            resultString = Double.toString(tempResult);
        else
            resultString = Integer.toString((int)tempResult);
    }
}