package com.hoau.virgo;

import javax.script.*;
import java.util.Date;

/**
 * @author 刘德云
 * @version V1.0
 * @title: ValidationDemo
 * @package com.hoau.virgo
 * @description
 * @date 2017/9/18
 */
public class ValidationDemo {

    static String[] rules = {
            "^100$|^(\\d|[1-9]\\d)(\\.\\d+)*$",//数字小于等于100
            "(DDU)+|(DDP)+",//枚举
            "(ACT)+",//Australian Capital Territory
            "(^02+\\d{2})|(^26[0-1]\\d{1})|(^29([0-1][0-9]|20))",//邮编范围在0200-0299或者2600-2619或2900-2920
            "(VIC)+",//Victoria
            "^(3|8)[0-9]{3}",//邮编范围在3000-3999或8000-8999
            "\\w+"
    };


    static String[] fields = {
            "packageInfoList.packageTotalWeight",
            "incoterm",
            "consigneeState",
            "consigneeZipCode",
            "consigneeAddr1"
    };

    static String[] fieldValues = {
            "10.33",
            "DDU",
            "VIC",
            "3120",
            "中国上海"
    };

    static String r = "R1 && R2 &&( ( R3 && R4) ||  ( R5 && R6))";

    boolean validate(String fieldValue , String regex){
        //long begin = System.currentTimeMillis();
        boolean c = fieldValue.matches(regex);
        //System.out.println("cost"+(System.currentTimeMillis()-begin));
        return c ;
    }

    public static void main(String[] args){

        ValidationDemo demo = new ValidationDemo();
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        long begin = System.currentTimeMillis();

        //for(int i = 0 ; i <100000 ; i++){

            engine.put("R1",demo.validate(fieldValues[0],rules[0]));
            engine.put("R2",demo.validate(fieldValues[1],rules[1]));
            engine.put("R3",demo.validate(fieldValues[2],rules[2]));
            engine.put("R4",demo.validate(fieldValues[3],rules[3]));
            engine.put("R5",demo.validate(fieldValues[2],rules[4]));
            engine.put("R6",demo.validate(fieldValues[3],rules[5]));
            try {
                //engine.eval(r);
                System.out.println(engine.eval(r));
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        //}
        System.out.println(System.currentTimeMillis()-begin);


        //System.out.println(demo.validate("7999","^(3|8)[0-9]{3}"));

    }
}
