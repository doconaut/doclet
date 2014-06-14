package com.doconaut.doclet;

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
     * The empty constructor
     */
    public SampleClass(){

    }

    /**
     * Creates a new SampleClass with an initial value
     * @param v The initial value
     */
    public SampleClass(int v){

    }


    private int fooMethod(){
        return 0;
    }

    public void doNothing(){

    }

}
