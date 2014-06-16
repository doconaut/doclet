package com.doconaut.doclet;

import java.io.IOException;
import java.io.Serializable;

/**
 * Sample class with simple description
 * @param <V> Sample generic type description
 */
public class SampleClass<V> extends SampleSuperClass implements Serializable, Cloneable{

    public static final int DISPLAY_FOO = 1;

    /**
     * Boolean sample
     */
    public static final boolean DISPLY_BOOLEAN = true;

    /**
     * The constant for storing usernames
     */
    public static final String DISPLAY_USERNAME="username";

    public int foo;

    /**
     * Used to store aaa letters
     */
    public String aaa;

    /**
     * Creates a new SampleClass with an initial value
     * @param v The initial value
     */
    public SampleClass(int v){

    }


    /**
     * The empty constructor
     */
    public SampleClass(){

    }


    /**
     * A method with some parameters and exceptions. Just to demonstrate the use of doconaut.
     * Just do a simple method call like this: <code>this.getFoo();</code>
     *
     *
     * @param f the integer paramter
     * @param a The float Parameter
     * @param sup The sup
     * @param utils The utils that are <b>very useful</b>
     * @return Always returns null
     * @throws IOException Thrown if not readable or writeable
     * @throws IllegalArgumentException Thrown if utils is null bla bla bla bal qweqweib ak uwbaroqbw kqweuqbwoqbws ,qiowboqwneoiqw qw oebsafoniasnd qwt oqbetwoinqtow obqwneionqwoeb boabsd obqwneiqwneo ioqnweionqwe
     */
    public String getFoo(int f, float a, SampleSuperClass sup, StringUtils utils) throws IOException, IllegalArgumentException{
        return null;
    }


    /**
     * A simple foo method. Does nothing spectacular. Just a simple method
     * @return Always returns zero
     */
    private int fooMethod(){
        return 0;
    }

    public void doNothing(){

    }


}
