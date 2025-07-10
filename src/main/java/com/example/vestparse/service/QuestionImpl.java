package com.example.vestparse.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionImpl implements QuestionService {

    //Is now, we'll developer a boolean method what gonna to identify if a section is a question or not.


    /*  We need to receive a section text
     *
     *
     *  */

    @Override
    public boolean isQuestion(String sectionText) {
        String regex = "^\\d.{199,}[\\r\\n]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sectionText);
        return matcher.find();


    }

    /* let's to divide ind Array */

    @Override
    public String geSectionText(String text)
    {
        if (!text.isEmpty() || text != null) {

        String [] sectionsText = new String[];
        return "";
    }
        }
}



